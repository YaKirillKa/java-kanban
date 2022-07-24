package org.kamenkov.java_kanban.managers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.InstanceCreator;
import org.kamenkov.java_kanban.KVTaskClient;
import org.kamenkov.java_kanban.exceptions.ManagerSaveException;
import org.kamenkov.java_kanban.utils.adapters.HistoryAdapter;

import java.io.IOException;
import java.net.URI;

public class HttpTasksManager extends FileBackedTasksManager {

    private static final Gson GSON = new GsonBuilder()
            .registerTypeAdapter(HistoryManager.class, new HistoryAdapter())
            .registerTypeAdapter(IdManager.class, (InstanceCreator<IdManager>) type -> new InMemoryIdManager())
            .serializeNulls()
            .create();
    private KVTaskClient client;
    private URI uri;

    public HttpTasksManager(URI uri) throws IOException, InterruptedException {
        super("test");
        this.uri = uri;
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

    public static HttpTasksManager load(URI clientUrl, String path) throws IOException, InterruptedException {
        String backup = new KVTaskClient(clientUrl).load(path);
        return GSON.fromJson(backup, HttpTasksManager.class);
    }
}
