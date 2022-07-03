package org.kamenkov.java_kanban.managers;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.kamenkov.java_kanban.task.TaskImpl;

abstract class TaskManagerTest<T extends TaskManager> {

    T taskManager;

    @Test
    void createTask() {
        Long id = taskManager.createTask(new TaskImpl("summary", "description"));
        Assertions.assertEquals(1, id);
    }
}