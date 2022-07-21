package org.kamenkov.java_kanban.task;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.kamenkov.java_kanban.Status;
import org.kamenkov.java_kanban.managers.TaskManager;

abstract class EpicTest<T extends TaskManager> {

    T taskManager;
    Epic epic;

    @Test
    void epicWithoutSubtasks() {
        Assertions.assertEquals(Status.NEW, epic.getStatus());
    }

    @Test
    void epicWithSubtasksInStatusNew() {
        createSubtaskWithStatus(Status.NEW);
        Assertions.assertEquals(Status.NEW, epic.getStatus());
    }

    @Test
    void epicWithSubtasksInStatusInProgress() {
        createSubtaskWithStatus(Status.IN_PROGRESS);
        Assertions.assertEquals(Status.IN_PROGRESS, epic.getStatus());
    }

    @Test
    void epicWithSubtasksInStatusDone() {
        createSubtaskWithStatus(Status.DONE);
        Assertions.assertEquals(Status.DONE, epic.getStatus());
    }

    @Test
    void epicWithSubtasksInStatusesNewAndDone() {
        createSubtaskWithStatus(Status.DONE);
        createSubtaskWithStatus(Status.NEW);
        Assertions.assertEquals(Status.IN_PROGRESS, epic.getStatus());
    }

    Subtask createSubtaskWithStatus(Status status) {
        Long id = taskManager.createSubtask(new Subtask("summary", "description", epic.getId()));
        Subtask subtask = taskManager.getSubtaskObjectById(id);
        subtask.setStatus(status);
        taskManager.updateSubtask(subtask, subtask.getId());
        return taskManager.getSubtaskObjectById(id);
    }

}