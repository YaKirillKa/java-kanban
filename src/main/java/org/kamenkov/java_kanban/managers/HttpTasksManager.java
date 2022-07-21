package org.kamenkov.java_kanban.managers;

import com.google.gson.Gson;
import org.kamenkov.java_kanban.KVTaskClient;
import org.kamenkov.java_kanban.exceptions.ManagerSaveException;

import java.io.IOException;
import java.net.URI;

public class HttpTasksManager extends FileBackedTasksManager {

    private static final Gson GSON = new Gson();
    private final KVTaskClient client;

    public HttpTasksManager(URI uri) throws IOException, InterruptedException {
        client = new KVTaskClient(uri);
    }

    @Override
    public void save(String path) {
        try {
            client.put(path, GSON.toJson(this));
        } catch (IOException | InterruptedException e) {
            throw new ManagerSaveException(e.getMessage());
        }
    }
}
