package org.kamenkov.java_kanban.managers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.junit.jupiter.api.*;
import org.kamenkov.java_kanban.KVTaskClient;
import org.kamenkov.java_kanban.servers.HttpTaskServer;
import org.kamenkov.java_kanban.servers.KVServer;
import org.kamenkov.java_kanban.utils.adapters.HistoryAdapter;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Files;
import java.nio.file.Path;

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
        Path path = Path.of("src/test/resources/FullBackup.json");
        String key = "test";
        kvTaskClient.put(key, String.join("", Files.readAllLines(path)));
        HttpTasksManager loadedManager = HttpTasksManager.load(KVSERVER_URL, key);
        Assertions.assertFalse(loadedManager.getAllTaskObjects().isEmpty());
        Assertions.assertFalse(loadedManager.getAllSubtaskObjects().isEmpty());
        Assertions.assertFalse(loadedManager.getAllEpicObjects().isEmpty());
        Assertions.assertFalse(loadedManager.getPrioritizedTasks().isEmpty());
        Assertions.assertFalse(loadedManager.getHistory().isEmpty());
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

    private HttpResponse<String> sendFileAsPostMethodBody(String filename, String endpoint) throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        Path path = Path.of("src/test/resources/" + filename);
        HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofFile(path);
        URI putUrl = URI.create(TASK_MANAGER_URL + "/" + endpoint);
        HttpRequest request = HttpRequest.newBuilder().uri(putUrl).POST(body).build();
        return client.send(request, HttpResponse.BodyHandlers.ofString());
    }

    @AfterEach
    void afterEach() {
        kvServer.stop();
        httpTaskServer.stop();
    }

}
