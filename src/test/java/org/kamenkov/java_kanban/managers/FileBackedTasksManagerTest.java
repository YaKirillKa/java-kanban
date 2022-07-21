package org.kamenkov.java_kanban.managers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.kamenkov.java_kanban.task.Epic;
import org.kamenkov.java_kanban.task.Subtask;
import org.kamenkov.java_kanban.task.Task;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

class FileBackedTasksManagerTest extends TaskManagerTest<FileBackedTasksManager> {

    private static final String HEADER = "id,type,name,status,description,start_date,duration,end_date,epic";

    @BeforeEach
    void beforeEach() {
        taskManager = new FileBackedTasksManager();
    }

    @Test
    void loadFromFile() {
        File file = new File("src/test/resources/TestLoadFromFile.csv");
        TaskManager loadedTaskManager = FileBackedTasksManager.loadFromFile(file);
        assertEquals(2, loadedTaskManager.getAllSubtaskObjects().size());
        assertEquals(2, loadedTaskManager.getAllTaskObjects().size());
        assertEquals(2, loadedTaskManager.getAllEpicObjects().size());
        assertEquals(2, loadedTaskManager.getHistory().size());
        assertEquals("1,EPIC,Первый эпик,IN_PROGRESS,Описание первого эпика,null,0,null",
                loadedTaskManager.getEpicObjectById(1L).toString());
        assertEquals("3,SUBTASK,Вторая сабтаска первого эпика,IN_PROGRESS," +
                        "Описание второй сабтаски первого эпика,null,0,null,1",
                loadedTaskManager.getSubtaskObjectById(3L).toString());
        assertEquals("4,EPIC,Второй эпик,DONE,Описание второго эпика,2022-07-03T10:10,250,2022-07-03T14:20",
                loadedTaskManager.getEpicObjectById(4L).toString());
        assertEquals("5,SUBTASK,Первая сабтаска второго эпика,DONE," +
                        "Описание первой сабтаски второго эпика,2022-07-03T10:10,250,2022-07-03T14:20,4",
                loadedTaskManager.getSubtaskObjectById(5L).toString());
        assertEquals("6,TASK,First Task,NEW,desc1,2022-07-04T10:00,11,2022-07-04T10:11",
                loadedTaskManager.getTaskObjectById(6L).toString());
        assertEquals("7,TASK,Second Task,NEW,desc2,2022-07-02T10:00,11,2022-07-02T10:11",
                loadedTaskManager.getTaskObjectById(7L).toString());
    }

    @Test
    void loadFromFileWithOneEpicWithoutHistory() {
        File file = new File("src/test/resources/TestLoadFromFileWithOneEpicWithoutHistory.csv");
        TaskManager loadedTaskManager = FileBackedTasksManager.loadFromFile(file);
        assertEquals(0, loadedTaskManager.getAllSubtaskObjects().size());
        assertEquals(0, loadedTaskManager.getAllTaskObjects().size());
        assertEquals(1, loadedTaskManager.getAllEpicObjects().size());
        assertEquals(0, loadedTaskManager.getHistory().size());
    }

    @Test
    void loadFromEmptyFile() {
        File file = new File("src/test/resources/TestLoadFromFileEmptyFile.csv");
        TaskManager loadedTaskManager = FileBackedTasksManager.loadFromFile(file);
        assertEquals(0, loadedTaskManager.getAllSubtaskObjects().size());
        assertEquals(0, loadedTaskManager.getAllTaskObjects().size());
        assertEquals(0, loadedTaskManager.getAllEpicObjects().size());
        assertEquals(0, loadedTaskManager.getHistory().size());
    }

    @Test
    void save(@TempDir File tempDir) throws IOException {
        String path = tempDir + "TestOutput.csv";
        Task task = new Task("Summary1", "Description1");
        task.setStartDate(LocalDateTime.of(2022, 1, 1, 10, 0));
        task.setDurationInMinutes(30);
        Long taskId = taskManager.createTask(task);
        Epic epic = new Epic("Summary2", "Description2");
        Long epicId = taskManager.createEpic(epic);
        Subtask subtask = new Subtask("Summary3", "Description3", epicId);
        subtask.setStartDate(LocalDateTime.of(2022, 1, 1, 11, 0));
        subtask.setDurationInMinutes(30);
        Long subtaskId = taskManager.createSubtask(subtask);
        subtask = taskManager.getSubtaskObjectById(subtaskId);
        epic = taskManager.getEpicObjectById(epicId);
        task = taskManager.getTaskObjectById(taskId);
        taskManager.save(path);
        String history = taskManager.getHistory().stream()
                .map(Task::getId)
                .map(String::valueOf)
                .collect(Collectors.joining(","));
        File output = new File(path);
        List<String> lines = List.of(HEADER, task.toString(), epic.toString(), subtask.toString(), "", history);
        assertTrue(output.exists());
        assertLinesMatch(lines, Files.readAllLines(output.toPath()));
    }

    @Test
    void saveEmptyTaskManager(@TempDir File tempDir) throws IOException {
        String path = tempDir + "TestOutput.csv";
        File file = new File(path);
        taskManager.save(path);
        assertTrue(file.exists());
        assertLinesMatch(List.of(HEADER, ""),
                Files.readAllLines(file.toPath()));
    }

    @Test
    void saveOneEpicWithoutHistory(@TempDir File tempDir) throws IOException {
        String path = tempDir + "TestOutput.csv";
        File file = new File(path);
        Epic epic = new Epic("Summary", "Description");
        taskManager.createEpic(epic);
        taskManager.save(path);
        assertTrue(file.exists());
        assertLinesMatch(List.of(HEADER, "1,EPIC,Summary,NEW,Description,null,0,null", ""),
                Files.readAllLines(file.toPath()));

    }
}