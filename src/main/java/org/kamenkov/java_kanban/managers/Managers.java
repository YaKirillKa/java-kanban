package org.kamenkov.java_kanban.managers;

import java.io.IOException;
import java.net.URI;

public class Managers {

    private Managers() {}

    public static TaskManager getDefault() throws IOException, InterruptedException {
        return new HttpTasksManager(URI.create("http://localhost:8078"));
    }

    public static HistoryManager getDefaultHistoryManager() {
        return new InMemoryHistoryManager();
    }

    public static IdManager getDefaultIdManager() {
        return new InMemoryIdManager();
    }
}
