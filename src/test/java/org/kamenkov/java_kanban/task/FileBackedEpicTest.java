package org.kamenkov.java_kanban.task;

import org.junit.jupiter.api.BeforeEach;
import org.kamenkov.java_kanban.managers.FileBackedTasksManager;

class FileBackedEpicTest extends EpicTest<FileBackedTasksManager> {

    @BeforeEach
    public void beforeEach() {
        taskManager = new FileBackedTasksManager();
        Long id = taskManager.createEpic(new Epic("summary", "description"));
        epic = taskManager.getEpicObjectById(id);
    }

}