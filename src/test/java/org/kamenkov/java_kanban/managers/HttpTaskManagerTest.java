package org.kamenkov.java_kanban.managers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import org.junit.jupiter.api.*;
import org.kamenkov.java_kanban.KVTaskClient;
import org.kamenkov.java_kanban.servers.HttpTaskServer;
import org.kamenkov.java_kanban.servers.KVServer;
import org.kamenkov.java_kanban.task.Epic;
import org.kamenkov.java_kanban.task.Subtask;
import org.kamenkov.java_kanban.task.Task;
import org.kamenkov.java_kanban.utils.adapters.HistoryAdapter;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class HttpTaskManagerTest extends TaskManagerTest<HttpTasksManager> {

    private static final URI TASK_MANAGER_URL = URI.create("http://localhost:8080/tasks");
    private static final URI KVSERVER_URL = URI.create("http://localhost:8078");
    KVServer kvServer;
    KVTaskClient kvTaskClient;
    HttpTaskServer httpTaskServer;

    static Gson gson;

    @BeforeAll
    static void beforeAll() {
        gson = new GsonBuilder().registerTypeAdapter(InMemoryHistoryManager.class, new HistoryAdapter()).create();
    }

    @BeforeEach
    void beforeEach() throws IOException, InterruptedException {
        kvServer = new KVServer();
        kvServer.start();
        taskManager = (HttpTasksManager) Managers.getDefault();
        httpTaskServer = new HttpTaskServer(taskManager);
        httpTaskServer.start();
        kvTaskClient = new KVTaskClient(KVSERVER_URL);
    }

    @Test
    void checkBackup() throws IOException, InterruptedException {
        HttpTasksManager loadedManager = getLoadedHttpTasksManager();
        Assertions.assertFalse(loadedManager.getAllTaskObjects().isEmpty());
        Assertions.assertFalse(loadedManager.getAllSubtaskObjects().isEmpty());
        Assertions.assertFalse(loadedManager.getAllEpicObjects().isEmpty());
        Assertions.assertFalse(loadedManager.getPrioritizedTasks().isEmpty());
        Assertions.assertFalse(loadedManager.getHistory().isEmpty());
    }

    @Test
    void getTaskObject() throws IOException, InterruptedException {
        Task basicTask = new Task("summary", "description");
        taskManager.createTask(basicTask);
        Task task = gson.fromJson(getJsonTaskFromServer("task?id=1").body(), Task.class);
        Assertions.assertEquals(basicTask, task);
    }

    @Test
    void getEpicObject() throws IOException, InterruptedException {
        Epic basicEpic = new Epic("summary", "description");
        taskManager.createEpic(basicEpic);
        Epic epic = gson.fromJson(getJsonTaskFromServer("epic?id=1").body(), Epic.class);
        Assertions.assertEquals(basicEpic, epic);
    }

    @Test
    void getSubtaskObject() throws IOException, InterruptedException {
        Epic basicEpic = new Epic("summary", "description");
        taskManager.createEpic(basicEpic);
        Subtask basicSubtask = new Subtask("summary", "description", 1L);
        taskManager.createSubtask(basicSubtask);
        Subtask subtask = gson.fromJson(getJsonTaskFromServer("subtask?id=2").body(), Subtask.class);
        Assertions.assertEquals(basicSubtask, subtask);
    }

    @Test
    void deleteTaskObject() throws IOException, InterruptedException {
        Task basicTask = new Task("summary", "description");
        taskManager.createTask(basicTask);
        Assertions.assertFalse(taskManager.getAllTaskObjects().isEmpty());
        HttpResponse<String> response = deleteTaskFromServer("task?id=1");
        Assertions.assertEquals(200, response.statusCode());
        Assertions.assertTrue(taskManager.getAllTaskObjects().isEmpty());
    }

    @Test
    void deleteEpicObject() throws IOException, InterruptedException {
        Epic basicEpic = new Epic("summary", "description");
        taskManager.createEpic(basicEpic);
        Assertions.assertFalse(taskManager.getAllEpicObjects().isEmpty());
        HttpResponse<String> response = deleteTaskFromServer("epic?id=1");
        Assertions.assertEquals(200, response.statusCode());
        Assertions.assertTrue(taskManager.getAllEpicObjects().isEmpty());
    }

    @Test
    void deleteSubtaskObject() throws IOException, InterruptedException {
        Epic basicEpic = new Epic("summary", "description");
        taskManager.createEpic(basicEpic);
        Subtask basicSubtask = new Subtask("summary", "description", 1L);
        taskManager.createSubtask(basicSubtask);
        Assertions.assertFalse(taskManager.getAllSubtaskObjects().isEmpty());
        HttpResponse<String> response = deleteTaskFromServer("subtask?id=2");
        Assertions.assertEquals(200, response.statusCode());
        Assertions.assertTrue(taskManager.getAllSubtaskObjects().isEmpty());
    }

    @Test
    void deleteAllTaskObject() throws IOException, InterruptedException {
        Task basicTask = new Task("summary", "description");
        taskManager.createTask(basicTask);
        taskManager.createTask(basicTask);
        Assertions.assertEquals(2, taskManager.getAllTaskObjects().size());
        HttpResponse<String> response = deleteTaskFromServer("task");
        Assertions.assertEquals(200, response.statusCode());
        Assertions.assertTrue(taskManager.getAllTaskObjects().isEmpty());
    }

    @Test
    void deleteAllEpicObject() throws IOException, InterruptedException {
        Epic basicEpic = new Epic("summary", "description");
        taskManager.createEpic(basicEpic);
        taskManager.createEpic(basicEpic);
        Assertions.assertEquals(2, taskManager.getAllEpicObjects().size());
        HttpResponse<String> response = deleteTaskFromServer("epic");
        Assertions.assertEquals(200, response.statusCode());
        Assertions.assertTrue(taskManager.getAllEpicObjects().isEmpty());
    }

    @Test
    void deleteAllSubtaskObject() throws IOException, InterruptedException {
        Epic basicEpic = new Epic("summary", "description");
        taskManager.createEpic(basicEpic);
        Subtask basicSubtask = new Subtask("summary", "description", 1L);
        taskManager.createSubtask(basicSubtask);
        taskManager.createSubtask(basicSubtask);
        Assertions.assertEquals(2, taskManager.getAllSubtaskObjects().size());
        HttpResponse<String> response = deleteTaskFromServer("subtask");
        Assertions.assertEquals(200, response.statusCode());
        Assertions.assertTrue(taskManager.getAllSubtaskObjects().isEmpty());
    }

    @Test
    void createEpicFromPost() throws IOException, InterruptedException {
        Assertions.assertTrue(taskManager.getAllEpicObjects().isEmpty());
        HttpResponse<String> response = sendFileAsPostMethodBody("DefaultEpic.json", "epic");
        Assertions.assertEquals(201, response.statusCode());
        Assertions.assertFalse(taskManager.getAllEpicObjects().isEmpty());
    }

    @Test
    void createSubtaskFromPost() throws IOException, InterruptedException {
        sendFileAsPostMethodBody("DefaultEpic.json", "epic");
        Assertions.assertTrue(taskManager.getAllSubtaskObjects().isEmpty());
        HttpResponse<String> response = sendFileAsPostMethodBody("DefaultSubtask.json", "subtask");
        Assertions.assertEquals(201, response.statusCode());
        Assertions.assertFalse(taskManager.getAllSubtaskObjects().isEmpty());
    }

    @Test
    void createTaskFromPost() throws IOException, InterruptedException {
        Assertions.assertTrue(taskManager.getAllTaskObjects().isEmpty());
        HttpResponse<String> response = sendFileAsPostMethodBody("DefaultTask.json", "task");
        Assertions.assertEquals(201, response.statusCode());
        Assertions.assertFalse(taskManager.getAllTaskObjects().isEmpty());
    }

    @Test
    void getAllTasks() throws IOException, InterruptedException {
        Epic basicEpic = new Epic("summary", "description");
        taskManager.createEpic(basicEpic);
        Subtask basicSubtask = new Subtask("summary", "description", 1L);
        taskManager.createSubtask(basicSubtask);
        Task basicTask = new Task("summary", "description");
        taskManager.createTask(basicTask);
        HttpResponse<String> response = getJsonTaskFromServer("");
        Assertions.assertEquals(200, response.statusCode());
        List<Task> tasks = gson.fromJson(response.body(), new TypeToken<ArrayList<Task>>() {}.getType());
        Assertions.assertEquals(taskManager.getPrioritizedTasks().size(), tasks.size());
    }

    @Test
    void getHistoryFromServer() throws InterruptedException, IOException {
        Epic basicEpic = new Epic("summary", "description");
        taskManager.createEpic(basicEpic);
        Subtask basicSubtask = new Subtask("summary", "description", 1L);
        taskManager.createSubtask(basicSubtask);
        Task basicTask = new Task("summary", "description");
        taskManager.createTask(basicTask);
        taskManager.getTaskObjectById(3L);
        taskManager.getEpicObjectById(1L);
        taskManager.getSubtaskObjectById(2L);
        HttpResponse<String> response = getJsonTaskFromServer("history");
        Assertions.assertEquals(200, response.statusCode());
        List<Task> tasks = gson.fromJson(response.body(), new TypeToken<ArrayList<Task>>() {}.getType());
        Assertions.assertEquals(taskManager.getHistory().size(), tasks.size());
    }

    @AfterEach
    void afterEach() {
        kvServer.stop();
        httpTaskServer.stop();
    }

    private HttpResponse<String> sendFileAsPostMethodBody(String filename, String endpoint) throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        Path path = Path.of("src/test/resources/" + filename);
        HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofFile(path);
        URI putUrl = URI.create(TASK_MANAGER_URL + "/" + endpoint);
        HttpRequest request = HttpRequest.newBuilder().uri(putUrl).POST(body).build();
        return client.send(request, HttpResponse.BodyHandlers.ofString());
    }

    private HttpResponse<String> getJsonTaskFromServer(String endpoint) throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create(TASK_MANAGER_URL + "/" + endpoint);
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        return client.send(request, HttpResponse.BodyHandlers.ofString());
    }

    private HttpResponse<String> deleteTaskFromServer(String endpoint) throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create(TASK_MANAGER_URL + "/" + endpoint);
        HttpRequest request = HttpRequest.newBuilder().uri(url).DELETE().build();
        return client.send(request, HttpResponse.BodyHandlers.ofString());
    }

    private HttpTasksManager getLoadedHttpTasksManager() throws IOException, InterruptedException {
        Path path = Path.of("src/test/resources/FullBackup.json");
        String key = "test";
        kvTaskClient.put(key, String.join("", Files.readAllLines(path)));
        return HttpTasksManager.load(KVSERVER_URL, key);
    }

}
