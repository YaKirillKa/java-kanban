package org.kamenkov.java_kanban.managers;

import org.kamenkov.java_kanban.task.Epic;
import org.kamenkov.java_kanban.task.Subtask;
import org.kamenkov.java_kanban.task.Task;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

public class InMemoryTaskManager implements TaskManager {

    /* Error messages */
    private static final String PARENT_CANNOT_BE_NULL = "Parent cannot be null";
    private static final String MAP_CANNOT_BE_NULL = "Map cannot be null";
    private static final String OBJECT_CANNOT_BE_NULL = "Object cannot be null";
    private static final String ID_CANNOT_BE_NULL = "ID cannot be null";
    private static final String TIME_INTERSECTION_FOUND = "Time intersection found with task. ID: ";

    /* Managers */
    IdManager idManager;
    HistoryManager historyManager;
    /* Different maps for different types */
    final Map<Long, Task> tasks;
    final Map<Long, Epic> epics;
    final Map<Long, Subtask> subtasks;
    final Set<Task> prioritizedTasks = new TreeSet<>(new StartDateComparator());

    public InMemoryTaskManager() {
        idManager = Managers.getDefaultIdManager();
        historyManager = Managers.getDefaultHistoryManager();
        tasks = new HashMap<>();
        epics = new HashMap<>();
        subtasks = new HashMap<>();
    }

    @Override
    public Collection<Task> getAllTaskObjects() {
        return getValuesFromMap(tasks);
    }

    @Override
    public Collection<Epic> getAllEpicObjects() {
        return getValuesFromMap(epics);
    }

    @Override
    public Collection<Subtask> getAllSubtaskObjects() {
        return getValuesFromMap(subtasks);
    }

    @Override
    public Task getTaskObjectById(Long id) {
        Task task = tasks.get(id);
        if (task == null) {
            return null;
        }
        historyManager.add(task);
        return task;
    }

    @Override
    public Epic getEpicObjectById(Long id) {
        Epic task = epics.get(id);
        if (task == null) {
            return null;
        }
        historyManager.add(task);
        return task;
    }

    @Override
    public Subtask getSubtaskObjectById(Long id) {
        Subtask task = subtasks.get(id);
        if (task == null) {
            return null;
        }
        historyManager.add(task);
        return task;
    }

    @Override
    public void removeAllTaskObjects() {
        for (Map.Entry<Long, Task> entry : tasks.entrySet()) {
            historyManager.remove(entry.getKey());
            prioritizedTasks.removeIf(t ->  t.equals(entry.getValue()));
        }
        tasks.clear();
    }

    @Override
    public void removeAllEpicObjects() {
        removeAllSubtaskObjects();
        for (Map.Entry<Long, Epic> entry : epics.entrySet()) {
            historyManager.remove(entry.getKey());
            prioritizedTasks.removeIf(t ->  t.equals(entry.getValue()));
        }
        epics.clear();
    }

    @Override
    public void removeAllSubtaskObjects() {
        Set<Long> parentIds = subtasks.values().stream().map(Subtask::getParentId).collect(Collectors.toSet());
        for (Map.Entry<Long, Subtask> entry : subtasks.entrySet()) {
            historyManager.remove(entry.getKey());
            prioritizedTasks.removeIf(t ->  t.equals(entry.getValue()));
        }
        subtasks.clear();
        for (Long id : parentIds) {
            getEpicObjectById(id).recalculateStatus();
            getEpicObjectById(id).recalculateDates();
        }
    }

    @Override
    public Long createTask(Task task) {
        Objects.requireNonNull(task, OBJECT_CANNOT_BE_NULL);
        final Optional<Task> intersectionTask = getFirstIntersection(task);
        if (intersectionTask.isPresent()) {
            throw new IllegalArgumentException(TIME_INTERSECTION_FOUND + intersectionTask.get().getId());
        }
        return createTask(task, tasks);
    }

    @Override
    public Long createEpic(Epic epic) {
        return createTask(epic, epics);
    }

    @Override
    public Long createSubtask(Subtask subtask) {
        Epic parentObject = getParentObject(subtask);
        Objects.requireNonNull(parentObject, PARENT_CANNOT_BE_NULL);
        final Optional<Task> intersectionTask = getFirstIntersection(subtask);
        if (intersectionTask.isPresent()) {
            throw new IllegalArgumentException(TIME_INTERSECTION_FOUND + intersectionTask.get().getId());
        }
        parentObject.addSubtask(subtask);
        return createTask(subtask, subtasks);
    }

    @Override
    public void updateTask(Task task, Long id) {
        final Optional<Task> intersectionTask = getFirstIntersection(task);
        if (intersectionTask.isPresent()) {
            throw new IllegalArgumentException(TIME_INTERSECTION_FOUND + intersectionTask.get().getId());
        }
        updateTask(task, tasks, id);
    }

    @Override
    public void updateEpic(Epic epic, Long id) {
        updateTask(epic, epics, id);
    }

    @Override
    public void updateSubtask(Subtask subtask, Long id) {
        Epic parentObject = getParentObject(subtask);
        Objects.requireNonNull(parentObject, PARENT_CANNOT_BE_NULL);
        final Optional<Task> intersectionTask = getFirstIntersection(subtask);
        if (intersectionTask.isPresent()) {
            throw new IllegalArgumentException(TIME_INTERSECTION_FOUND + intersectionTask.get().getId());
        }
        parentObject.recalculateStatus();
        parentObject.recalculateDates();
        updateTask(subtask, subtasks, id);
    }

    @Override
    public void removeTask(Long id) {
        removeEntryFromMap(tasks, id);
    }

    @Override
    public void removeEpic(Long id) {
        Epic epic = epics.get(id);
        Objects.requireNonNull(epic, OBJECT_CANNOT_BE_NULL);
        for (Subtask subtask : epic.getSubtaskObjects()) {
            removeEntryFromMap(subtasks, subtask.getId());
        }
        removeEntryFromMap(epics, id);
    }

    @Override
    public void removeSubtask(Long id) {
        Subtask subtask = getSubtaskObjectById(id);
        Epic parentObject = getParentObject(subtask);
        Objects.requireNonNull(parentObject, PARENT_CANNOT_BE_NULL);
        parentObject.removeSubtask(subtask);
        removeEntryFromMap(subtasks, id);
    }

    @Override
    public Collection<Subtask> getSubtaskObjectsByParent(Epic epic) {
        return epic == null ? null : epic.getSubtaskObjects();
    }

    @Override
    public Set<Task> getPrioritizedTasks() {
        return prioritizedTasks;
    }

    @Override
    public List<Task> getHistory() {
        return historyManager.getHistory();
    }

    /**
     * Returns {@link Epic} object by given {@link Subtask}.
     *
     * @param subtask whose parent should be returned.
     * @return {@link Epic} object.
     */
    private Epic getParentObject(Subtask subtask) {
        return epics.get(subtask.getParentId());
    }

    <T extends Task> Long createTask(T taskObject, Map<Long, T> map) {
        Objects.requireNonNull(map, MAP_CANNOT_BE_NULL);
        if (taskObject == null) {
            return null;
        }
        final Long id = idManager.getLastId();
        taskObject.setId(id);
        map.put(id, taskObject);
        prioritizedTasks.add(taskObject);
        return taskObject.getId();
    }

    /**
     * Updates task in given map.
     *
     * @param taskObject object that should be updated in memory.
     * @param map        in which map object should be saved.
     * @param id         unique id of task.
     * @throws IllegalArgumentException if any of the arguments is null.
     */
    <T extends Task> void updateTask(T taskObject, Map<Long, T> map, Long id) {
        Objects.requireNonNull(taskObject, OBJECT_CANNOT_BE_NULL);
        Objects.requireNonNull(map, MAP_CANNOT_BE_NULL);
        Objects.requireNonNull(id, ID_CANNOT_BE_NULL);
        if (map.containsKey(id)) {
            map.put(id, taskObject);
            prioritizedTasks.add(taskObject);
        }
    }

    /**
     * Returns all values of the given map.
     *
     * @param map from which map values should be returned.
     * @return {@link Collection} of the {@link Task} or its child classes
     */
    private <T extends Task> Collection<T> getValuesFromMap(Map<Long, T> map) {
        return map.values();
    }

    /**
     * Removes task from the given map.
     *
     * @param map from which map values should be removed.
     */
    <T extends Task> void removeEntryFromMap(Map<Long, T> map, Long id) {
        historyManager.remove(id);
        prioritizedTasks.removeIf(t ->  t.equals(map.get(id)));
        map.remove(id);
    }

    /**
     * Checks whether the given {@link Task} intersects with other created {@link Task}s.
     *
     * @param task new {@link Task} which dates should be checked.
     * @return {@link Optional} with the first found {@link Task} that intersects with or an empty {@link Optional}.
     */
    private Optional<Task> getFirstIntersection(Task task) {
        if (task.getStartDate() == null) {
            return Optional.empty();
        }
        return prioritizedTasks.stream()
                .filter(t -> !t.equals(task))
                .filter(t -> !(t instanceof Epic))
                .filter(t -> t.getStartDate() != null)
                .filter(t -> t.getEndDate().isAfter(task.getStartDate())
                        && t.getStartDate().isBefore(task.getEndDate()))
                .findFirst();
    }

    /**
     * Comparator to compare tasks by their start date.
     */
    private static class StartDateComparator implements Comparator<Task> {
        @Override
        public int compare(Task o1, Task o2) {
            LocalDateTime o1sd = o1.getStartDate();
            LocalDateTime o2sd = o2.getStartDate();
            if (o1sd != null && o2sd != null && !o1sd.equals(o2sd)) {
                return o1sd.compareTo(o2sd);
            }
            return 1;
        }
    }
}
