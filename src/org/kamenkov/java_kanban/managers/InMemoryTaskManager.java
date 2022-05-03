package org.kamenkov.java_kanban.managers;

import org.kamenkov.java_kanban.task.Epic;
import org.kamenkov.java_kanban.task.Subtask;
import org.kamenkov.java_kanban.task.Task;

import java.util.*;
import java.util.stream.Collectors;

public class InMemoryTaskManager implements TaskManager {

    /* Error messages */
    private static final String PARENT_CANNOT_BE_NULL = "Parent cannot be null";
    private static final String MAP_CANNOT_BE_NULL = "Map cannot be null";
    private static final String OBJECT_CANNOT_BE_NULL = "Object cannot be null";
    private static final String ID_CANNOT_BE_NULL = "ID cannot be null";

    /* Variable to keep ID unique */
    private Long lastId;
    /* Different maps for different types */
    private final Map<Long, Task> tasks;
    private final Map<Long, Epic> epics;
    private final Map<Long, Subtask> subtasks;

    public InMemoryTaskManager() {
        this.lastId = 0L;
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
        return tasks.get(id);
    }

    @Override
    public Epic getEpicObjectById(Long id) {
        return epics.get(id);
    }

    @Override
    public Subtask getSubtaskObject(Long id) {
        return subtasks.get(id);
    }

    @Override
    public void removeAllTaskObjects() {
        tasks.clear();
    }

    @Override
    public void removeAllEpicObjects() {
        removeAllSubtaskObjects();
        epics.clear();
    }

    @Override
    public void removeAllSubtaskObjects() {
        Set<Long> parentIds = subtasks.values().stream().map(Subtask::getParentId).collect(Collectors.toSet());
        subtasks.clear();
        for (Long id : parentIds) {
            getEpicObjectById(id).recalculateStatus();
        }
    }

    @Override
    public Long createTask(Task task) {
        return createTask(task, tasks);
    }

    @Override
    public Long createEpic(Epic epic) {
        return createTask(epic, epics);
    }

    @Override
    public Long createSubtask(Subtask subtask) {
        Epic parentObject = getEpicObjectById(subtask.getParentId());
        if (parentObject == null) {
            throw new IllegalArgumentException(PARENT_CANNOT_BE_NULL);
        }
        parentObject.addSubtask(subtask);
        return createTask(subtask, subtasks);
    }

    @Override
    public void updateTask(Task task, Long id) {
        updateTask(task, tasks, id);
    }

    @Override
    public void updateEpic(Epic epic, Long id) {
        updateTask(epic, epics, id);
    }

    @Override
    public void updateSubtask(Subtask subtask, Long id) {
        Epic parentObject = getEpicObjectById(subtask.getParentId());
        if (parentObject == null) {
            throw new IllegalArgumentException(PARENT_CANNOT_BE_NULL);
        }
        parentObject.recalculateStatus();
        updateTask(subtask, subtasks, id);
    }

    @Override
    public void removeTask(Long id) {
        removeEntryFromMap(tasks, id);
    }

    @Override
    public void removeEpic(Long id) {
        Epic parent = getEpicObjectById(id);
        if (parent == null) {
            throw new IllegalArgumentException(PARENT_CANNOT_BE_NULL);
        }
        for (Subtask subtask : parent.getSubtaskObjects()) {
            removeEntryFromMap(subtasks, subtask.getId());
        }
        removeEntryFromMap(epics, id);
    }

    @Override
    public void removeSubtask(Long id) {
        Subtask subtask = getSubtaskObject(id);
        Epic parentObject = getEpicObjectById(subtask.getParentId());
        if (parentObject == null) {
            throw new IllegalArgumentException(PARENT_CANNOT_BE_NULL);
        }
        parentObject.removeSubtask(subtask);
        removeEntryFromMap(subtasks, id);
    }

    @Override
    public Collection<Subtask> getSubtaskObjectsByParent(Epic epic) {
        return epic == null ? null : epic.getSubtaskObjects();
    }

    private <T extends Task> Long createTask(T taskObject, Map<Long, T> map) {
        if (map == null) {
            throw new IllegalArgumentException(MAP_CANNOT_BE_NULL);
        }
        if (taskObject == null) {
            return null;
        }
        taskObject.setId(++lastId);
        map.put(lastId, taskObject);
        return lastId;
    }

    /**
     * Updates task in given map.
     * @param taskObject object that should be updated in memory.
     * @param map in which map object should be saved.
     * @param id unique id of task.
     * @throws IllegalArgumentException if any of the arguments is null.
     */
    private <T extends Task> void updateTask(T taskObject, Map<Long, T> map, Long id) {
        if (taskObject == null) {
            throw new IllegalArgumentException(OBJECT_CANNOT_BE_NULL);
        }
        if (map == null) {
            throw new IllegalArgumentException(MAP_CANNOT_BE_NULL);
        }
        if (id == null) {
            throw new IllegalArgumentException(ID_CANNOT_BE_NULL);
        }
        if (map.containsKey(id)) {
            map.put(id, taskObject);
        }
    }

    /**
     * Gets all values of the given map.
     * @param map from which map values should be returned.
     * @return {@link Collection} of the {@link Task} or its child classes
     */
    private <T extends Task> Collection<T> getValuesFromMap(Map<Long, T> map) {
        return map.values();
    }

    /**
     * Removes task from the given map.
     * @param map from which map values should be removed.
     */
    private <T extends Task> void removeEntryFromMap(Map<Long, T> map, Long id) {
        map.remove(id);
    }
}
