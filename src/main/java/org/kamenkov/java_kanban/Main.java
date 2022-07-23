package org.kamenkov.java_kanban;

import org.kamenkov.java_kanban.managers.HttpTasksManager;
import org.kamenkov.java_kanban.servers.HttpTaskServer;
import org.kamenkov.java_kanban.servers.KVServer;

import java.net.URI;

public class Main {
    public static void main(String[] args) {
        try {
            new KVServer().start();
            new HttpTaskServer(new HttpTasksManager(URI.create("http://localhost:8078"))).start();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


}
