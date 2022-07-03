package org.kamenkov.java_kanban.managers;

import org.junit.jupiter.api.BeforeEach;

class FileBackedTasksManagerTest extends TaskManagerTest<FileBackedTasksManager> {

    @BeforeEach
    void beforeEach() {
        taskManager = new FileBackedTasksManager();
    }

}