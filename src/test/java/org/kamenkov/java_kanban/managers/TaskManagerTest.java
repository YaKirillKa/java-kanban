package org.kamenkov.java_kanban.managers;

import org.junit.jupiter.api.Test;
import org.kamenkov.java_kanban.Status;
import org.kamenkov.java_kanban.task.Epic;
import org.kamenkov.java_kanban.task.Subtask;
import org.kamenkov.java_kanban.task.Task;

import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

abstract class TaskManagerTest<T extends TaskManager> {

    T taskManager;

    @Test
    void getAllTaskObjects() {
        Task task1 = new Task("Summary1", "Description1");
        Task task2 = new Task("Summary2", "Description2");
        Task task3 = new Task("Summary3", "Description3");
        final List<Task> taskList = List.of(task1, task2, task3);
        for (Task task : taskList) {
            taskManager.createTask(task);
        }
        List<Task> savedTasks = new ArrayList<>(taskManager.getAllTaskObjects());
        assertFalse(savedTasks.isEmpty(), "Tasks are not returned.");
        assertEquals(3, savedTasks.size(), "Wrong amount of tasks.");
        assertEquals(taskList, savedTasks, "Tasks are not equals.");
    }

    @Test
    void getAllTaskObjectsIfEmpty() {
        assertTrue(taskManager.getAllTaskObjects().isEmpty());
    }

    @Test
    void getAllEpicObjects() {
        Epic epic1 = new Epic("Summary1", "Description1");
        Epic epic2 = new Epic("Summary2", "Description2");
        Epic epic3 = new Epic("Summary3", "Description3");
        final List<Epic> epicList = List.of(epic1, epic2, epic3);
        for (Epic epic : epicList) {
            taskManager.createEpic(epic);
        }
        List<Epic> savedEpics = new ArrayList<>(taskManager.getAllEpicObjects());
        assertFalse(savedEpics.isEmpty(), "Epics are not returned.");
        assertEquals(3, savedEpics.size(), "Wrong amount of epics.");
        assertEquals(epicList, savedEpics, "Epics are not equals.");
    }

    @Test
    void getAllEpicObjectsIfEmpty() {
        assertTrue(taskManager.getAllEpicObjects().isEmpty());
    }

    @Test
    void getAllSubtaskObjects() {
        Epic epic = new Epic("Summary0", "Description0");
        Long parentId = taskManager.createEpic(epic);
        Subtask subtask1 = new Subtask("Summary1", "Description1", parentId);
        Subtask subtask2 = new Subtask("Summary2", "Description2", parentId);
        Subtask subtask3 = new Subtask("Summary3", "Description3", parentId);
        final List<Subtask> subtaskList = List.of(subtask1, subtask2, subtask3);
        for (Subtask subtask : subtaskList) {
            taskManager.createSubtask(subtask);
        }
        List<Subtask> savedSubtasks = new ArrayList<>(taskManager.getAllSubtaskObjects());
        assertFalse(savedSubtasks.isEmpty(), "Subtasks are not returned.");
        assertEquals(3, savedSubtasks.size(), "Wrong amount of subtasks.");
        assertEquals(subtaskList, savedSubtasks, "Subtasks are not equals.");
    }

    @Test
    void getAllSubtaskObjectsIfEmpty() {
        assertTrue(taskManager.getAllSubtaskObjects().isEmpty());
    }

    @Test
    void getTaskObjectById() {
        Task task = new Task("Summary", "Description");
        final Long taskId = taskManager.createTask(task);
        final Task savedTask = taskManager.getTaskObjectById(taskId);

        assertNotNull(savedTask, "Task not found.");
        assertEquals(task, savedTask, "Tasks are not equal.");
    }

    @Test
    void getTaskObjectByIdIfEmpty() {
        assertNull(taskManager.getTaskObjectById(1L));
        assertNull(taskManager.getTaskObjectById(null));
    }

    @Test
    void getEpicObjectById() {
        Epic epic = new Epic("Summary", "Description");
        final Long epicId = taskManager.createEpic(epic);
        final Epic savedEpic = taskManager.getEpicObjectById(epicId);

        assertNotNull(savedEpic, "Epic not found.");
        assertEquals(epic, savedEpic, "Epics are not equal.");
    }

    @Test
    void getEpicObjectByIdIfEmpty() {
        assertNull(taskManager.getEpicObjectById(1L));
        assertNull(taskManager.getEpicObjectById(null));
    }

    @Test
    void getSubtaskObject() {
        Epic epic = new Epic("Summary0", "Description0");
        Long parentId = taskManager.createEpic(epic);
        Subtask subtask = new Subtask("Summary1", "Description1", parentId);
        Long subtaskId = taskManager.createSubtask(subtask);
        Subtask savedSubtask = taskManager.getSubtaskObjectById(subtaskId);

        assertNotNull(savedSubtask, "Subtask are not returned.");
        assertEquals(subtask, savedSubtask, "Returned subtask is not equal.");
    }

    @Test
    void getSubtaskObjectByIdIfEmpty() {
        assertNull(taskManager.getSubtaskObjectById(1L));
        assertNull(taskManager.getSubtaskObjectById(null));
    }

    @Test
    void getPrioritizedTasks() {
        Task task1 = new Task("Summary1", "Description1");
        task1.setStartDate(LocalDateTime.of(2022, 1, 1, 10, 0));
        task1.setDurationInMinutes(30);
        Task task2 = new Task("Summary2", "Description2");
        task2.setStartDate(LocalDateTime.of(2022, 1, 2, 10, 0));
        task2.setDurationInMinutes(30);
        Task task3 = new Task("Summary3", "Description3");
        task3.setStartDate(LocalDateTime.of(2022, 1, 1, 9, 0));
        task3.setDurationInMinutes(30);
        final List<Task> taskList = List.of(task1, task2, task3);
        for (Task task : taskList) {
            taskManager.createTask(task);
        }
        TreeSet<Task> orderedTasks = new TreeSet<>(Comparator.comparing(Task::getStartDate));
        orderedTasks.addAll(taskList);
        Collection<Task> prioritizedTasks = taskManager.getPrioritizedTasks();
        assertFalse(prioritizedTasks.isEmpty(), "Tasks are not returned.");
        assertEquals(3, prioritizedTasks.size(), "Wrong amount of tasks.");
        assertEquals(orderedTasks, prioritizedTasks, "Tasks are not equals.");
    }

    @Test
    void getPrioritizedTasksIfEmpty() {
        assertTrue(taskManager.getPrioritizedTasks().isEmpty());
    }
    @Test
    void removeAllTaskObjects() {
        Task task1 = new Task("Summary1", "Description1");
        Task task2 = new Task("Summary2", "Description2");
        Task task3 = new Task("Summary3", "Description3");
        final List<Task> taskList = List.of(task1, task2, task3);
        for (Task task : taskList) {
            taskManager.createTask(task);
        }
        List<Task> savedTasks = new ArrayList<>(taskManager.getAllTaskObjects());
        assertFalse(savedTasks.isEmpty(), "Tasks are not returned.");
        assertEquals(3, savedTasks.size(), "Wrong amount of tasks.");
        assertEquals(3, taskManager.getPrioritizedTasks().size(), "Wrong amount of tasks.");
        taskManager.removeAllTaskObjects();
        assertTrue(taskManager.getAllTaskObjects().isEmpty());
        assertTrue(taskManager.getPrioritizedTasks().isEmpty());
    }

    @Test
    void removeAllEpicObjects() {
        Epic epic1 = new Epic("Summary1", "Description1");
        Epic epic2 = new Epic("Summary2", "Description2");
        Epic epic3 = new Epic("Summary3", "Description3");
        final List<Epic> epicList = List.of(epic1, epic2, epic3);
        for (Epic epic : epicList) {
            taskManager.createEpic(epic);
        }
        Subtask subtask1 = new Subtask("Summary4", "Description4", 1L);
        taskManager.createSubtask(subtask1);
        List<Epic> savedEpics = new ArrayList<>(taskManager.getAllEpicObjects());
        assertFalse(savedEpics.isEmpty(), "Epics are not returned.");
        assertEquals(3, savedEpics.size(), "Wrong amount of epics.");
        assertEquals(4, taskManager.getPrioritizedTasks().size(), "Wrong amount of tasks.");
        taskManager.removeAllEpicObjects();
        assertTrue(taskManager.getAllEpicObjects().isEmpty());
        assertTrue(taskManager.getAllSubtaskObjects().isEmpty());
        assertTrue(taskManager.getPrioritizedTasks().isEmpty());
    }

    @Test
    void removeAllSubtaskObjects() {
        Epic epic = new Epic("Summary0", "Description0");
        Long parentId = taskManager.createEpic(epic);
        Subtask subtask1 = new Subtask("Summary1", "Description1", parentId);
        Subtask subtask2 = new Subtask("Summary2", "Description2", parentId);
        Subtask subtask3 = new Subtask("Summary3", "Description3", parentId);
        final List<Subtask> subtaskList = List.of(subtask1, subtask2, subtask3);
        for (Subtask subtask : subtaskList) {
            taskManager.createSubtask(subtask);
        }
        List<Subtask> savedSubtasks = new ArrayList<>(taskManager.getAllSubtaskObjects());
        assertFalse(savedSubtasks.isEmpty(), "Subtasks are not returned.");
        assertEquals(3, savedSubtasks.size(), "Wrong amount of subtasks.");
        assertEquals(4, taskManager.getPrioritizedTasks().size(), "Wrong amount of tasks.");
        taskManager.removeAllSubtaskObjects();
        assertTrue(taskManager.getAllSubtaskObjects().isEmpty());
        assertEquals(1, taskManager.getPrioritizedTasks().size(), "Wrong amount of tasks.");

    }

    @Test
    void createTask() {
        Task task = new Task("Test createTask", "Test createTask description");
        final Long taskId = taskManager.createTask(task);
        final Task savedTask = taskManager.getTaskObjectById(taskId);

        assertNotNull(savedTask, "Task not found.");
        assertEquals(task, savedTask, "Tasks are not equal.");

        final Collection<Task> tasks = taskManager.getAllTaskObjects();

        assertNotNull(tasks, "Tasks are not returned.");
        assertEquals(1, tasks.size(), "Wrong amount of tasks.");
        assertEquals(task, tasks.iterator().next(), "Returned task is not equal.");
    }

    @Test
    void createTaskIfTaskIsNull() {
        assertThrows(NullPointerException.class, () -> taskManager.createTask(null));
    }

    @Test
    void createEpic() {
        Epic epic = new Epic("Test createEpic", "Test createEpic description");
        final Long epicId = taskManager.createEpic(epic);
        final Epic savedEpic = taskManager.getEpicObjectById(epicId);

        assertNotNull(savedEpic, "Epic not found.");
        assertEquals(epic, savedEpic, "Epics are not equal.");

        final Collection<Epic> epics = taskManager.getAllEpicObjects();

        assertNotNull(epics, "Epics are not returned.");
        assertEquals(1, epics.size(), "Wrong amount of epics.");
        assertEquals(epic, epics.iterator().next(), "Returned epic is not equal.");
    }

    @Test
    void createEpicIfEpicIsNull() {
        assertNull(taskManager.createEpic(null));
    }

    @Test
    void createSubtask() {
        Epic epic = new Epic("Test createSubtask", "Test createSubtask description");
        final Long epicId = taskManager.createEpic(epic);
        Subtask subtask = new Subtask("Test createSubtask", "Test createSubtask description", epicId);
        final Long subtaskId = taskManager.createSubtask(subtask);
        final Subtask savedSubtask = taskManager.getSubtaskObjectById(subtaskId);

        assertNotNull(savedSubtask, "Subtask not found.");
        assertEquals(subtask, savedSubtask, "Subtasks are not equal.");

        final Collection<Subtask> subtasks = taskManager.getAllSubtaskObjects();

        assertNotNull(subtasks, "Subtasks are not returned.");
        assertEquals(1, subtasks.size(), "Wrong amount of subtasks.");
        assertEquals(subtask, subtasks.iterator().next(), "Returned subtask is not equal.");
    }

    @Test
    void createSubtaskIfSubtaskIsNull() {
        assertThrows(NullPointerException.class, () -> taskManager.createSubtask(null));
    }

    @Test
    void updateTask() {
        Task createdTask = new Task("Summary", "Description");
        final Long createdTaskId = taskManager.createTask(createdTask);
        Task updatedTask = new Task("Summary1", "Description1");
        updatedTask.setId(createdTaskId);
        updatedTask.setStatus(Status.DONE);
        taskManager.updateTask(updatedTask, createdTaskId);
        final Task savedTask = taskManager.getTaskObjectById(createdTaskId);
        assertNotEquals(createdTask.getDescription(), savedTask.getDescription());
        assertNotEquals(createdTask.getSummary(), savedTask.getSummary());
        assertEquals(updatedTask.getDescription(), savedTask.getDescription());
        assertEquals(updatedTask.getSummary(), savedTask.getSummary());
    }

    @Test
    void updateTaskIfNull() {
        Task createdTask = new Task("Summary", "Description");
        final Long createdTaskId = taskManager.createTask(createdTask);
        assertThrows(NullPointerException.class, () -> taskManager.updateTask(null, createdTaskId));
    }

    @Test
    void updateEpic() {
        Epic createdEpic = new Epic("Summary", "Description");
        final Long createdEpicId = taskManager.createEpic(createdEpic);
        Epic updatedEpic = new Epic("Summary1", "Description1");
        updatedEpic.setId(createdEpicId);
        updatedEpic.setDescription("Description2");
        taskManager.updateEpic(updatedEpic, createdEpicId);
        final Epic savedEpic = taskManager.getEpicObjectById(createdEpicId);
        assertNotEquals(createdEpic.getDescription(), savedEpic.getDescription());
        assertNotEquals(createdEpic.getSummary(), savedEpic.getSummary());
        assertEquals(updatedEpic.getDescription(), savedEpic.getDescription());
        assertEquals(updatedEpic.getSummary(), savedEpic.getSummary());
    }

    @Test
    void updateEpicIfNull() {
        Epic createdEpic = new Epic("Summary", "Description");
        final Long createdEpicId = taskManager.createEpic(createdEpic);
        assertThrows(NullPointerException.class, () -> taskManager.updateEpic(null, createdEpicId));
    }

    @Test
    void updateSubtask() {
        Epic createdEpic = new Epic("Summary", "Description");
        final Long createdEpicId = taskManager.createEpic(createdEpic);
        Subtask createdSubtask = new Subtask("Summary", "Description", createdEpicId);
        final Long createdSubtaskId = taskManager.createSubtask(createdSubtask);
        Subtask updatedSubtask = new Subtask("Summary1", "Description1", createdEpicId);
        updatedSubtask.setId(createdSubtaskId);
        updatedSubtask.setDescription("Description1");
        taskManager.updateSubtask(updatedSubtask, createdSubtaskId);
        final Subtask savedSubtask = taskManager.getSubtaskObjectById(createdSubtaskId);
        assertNotEquals(createdSubtask.getDescription(), savedSubtask.getDescription());
        assertNotEquals(createdSubtask.getSummary(), savedSubtask.getSummary());
        assertEquals(updatedSubtask.getDescription(), savedSubtask.getDescription());
        assertEquals(updatedSubtask.getSummary(), savedSubtask.getSummary());
    }

    @Test
    void updateSubtaskIfNull() {
        Epic createdEpic = new Epic("Summary", "Description");
        final Long createdEpicId = taskManager.createEpic(createdEpic);
        Subtask createdSubtask = new Subtask("Summary", "Description", createdEpicId);
        final Long createdSubtaskId = taskManager.createSubtask(createdSubtask);
        assertThrows(NullPointerException.class, () -> taskManager.updateSubtask(null, createdSubtaskId));
    }

    @Test
    void removeTask() {
        Task task1 = new Task("Summary1", "Description1");
        Task task2 = new Task("Summary2", "Description2");
        Task task3 = new Task("Summary3", "Description3");
        final List<Task> taskList = List.of(task1, task2, task3);
        for (Task task : taskList) {
            taskManager.createTask(task);
        }
        assertEquals(3, taskManager.getAllTaskObjects().size());
        taskManager.removeTask(1L);
        assertEquals(2, taskManager.getAllTaskObjects().size());
        assertNull(taskManager.getTaskObjectById(1L));
    }

    @Test
    void removeTaskIfEmpty() {
        taskManager.removeTask(1L);
        taskManager.removeTask(null);
    }

    @Test
    void removeEpic() {
        Epic epic1 = new Epic("Summary1", "Description1");
        Epic epic2 = new Epic("Summary2", "Description2");
        Epic epic3 = new Epic("Summary3", "Description3");
        final List<Epic> epicList = List.of(epic1, epic2, epic3);
        for (Epic epic : epicList) {
            taskManager.createEpic(epic);
        }
        Subtask subtask = new Subtask("Summary", "Description", 1L);
        taskManager.createSubtask(subtask);
        assertEquals(3, taskManager.getAllEpicObjects().size());
        assertEquals(1, taskManager.getAllSubtaskObjects().size());
        taskManager.removeEpic(1L);
        assertEquals(2, taskManager.getAllEpicObjects().size());
        assertTrue(taskManager.getAllSubtaskObjects().isEmpty());
        assertNull(taskManager.getEpicObjectById(1L));
    }

    @Test
    void removeEpicIfEmpty() {
        assertThrows(NullPointerException.class, () -> taskManager.removeEpic(1L));
        assertThrows(NullPointerException.class, () -> taskManager.removeEpic(null));
    }

    @Test
    void removeSubtask() {
        Epic epic = new Epic("Summary0", "Description0");
        Long parentId = taskManager.createEpic(epic);
        Subtask subtask1 = new Subtask("Summary1", "Description1", parentId);
        Subtask subtask2 = new Subtask("Summary2", "Description2", parentId);
        Subtask subtask3 = new Subtask("Summary3", "Description3", parentId);
        final List<Subtask> subtaskList = List.of(subtask1, subtask2, subtask3);
        for (Subtask subtask : subtaskList) {
            taskManager.createSubtask(subtask);
        }
        assertEquals(3, taskManager.getAllSubtaskObjects().size());
        taskManager.removeSubtask(2L);
        assertEquals(2, taskManager.getAllSubtaskObjects().size());
        assertNull(taskManager.getSubtaskObjectById(2L));
    }

    @Test
    void removeSubtaskIfEmpty() {
        assertThrows(NullPointerException.class, () -> taskManager.removeSubtask(1L));
        assertThrows(NullPointerException.class, () -> taskManager.removeSubtask(null));
    }

    @Test
    void getSubtaskObjectsByParentId() {
        Epic epic = new Epic("Summary0", "Description0");
        Long parentId = taskManager.createEpic(epic);
        Subtask subtask1 = new Subtask("Summary1", "Description1", parentId);
        Subtask subtask2 = new Subtask("Summary2", "Description2", parentId);
        Subtask subtask3 = new Subtask("Summary3", "Description3", parentId);
        final List<Subtask> subtaskList = List.of(subtask1, subtask2, subtask3);
        for (Subtask subtask : subtaskList) {
            taskManager.createSubtask(subtask);
        }
        final Epic savedEpic = taskManager.getEpicObjectById(parentId);
        List<Subtask> savedSubtasks = new ArrayList<>(taskManager.getSubtaskObjectsByParentId(savedEpic.getId()));
        assertFalse(savedSubtasks.isEmpty());
        assertEquals(subtaskList, savedSubtasks);
    }

    @Test
    void getSubtaskObjectsByParentIfParentIdIsNull() {
        assertNull(taskManager.getSubtaskObjectsByParentId(null));
    }

    @Test
    void getHistory() {
        Task task1 = new Task("Summary1", "Description1");
        Task task2 = new Task("Summary2", "Description2");
        Task task3 = new Task("Summary3", "Description3");
        final List<Task> taskList = List.of(task1, task2, task3);
        final List<Long> taskIds = new ArrayList<>();
        for (Task task : taskList) {
            taskIds.add(taskManager.createTask(task));
        }
        Collections.shuffle(taskIds);
        List<Task> historyTasks = new ArrayList<>();
        for (Long taskId : taskIds) {
            historyTasks.add(taskManager.getTaskObjectById(taskId));
        }
        final Collection<Task> history = taskManager.getHistory();
        assertFalse(history.isEmpty());
        assertEquals(historyTasks, new ArrayList<>(history));
    }

    @Test
    void getHistoryIfEmpty() {
        assertTrue(taskManager.getHistory().isEmpty());
    }

    @Test
    void testIntersection() {
        Task task1 = new Task("Summary1", "Description1");
        task1.setStartDate(LocalDateTime.of(2022, 1, 1, 10, 0));
        task1.setDurationInMinutes(50);
        Task task2 = new Task("Summary2", "Description2");
        task2.setStartDate(LocalDateTime.of(2022, 1, 1, 10, 0));
        task2.setDurationInMinutes(50);
        taskManager.createTask(task1);
        assertThrows(IllegalArgumentException.class, () -> taskManager.createTask(task2));
    }
}