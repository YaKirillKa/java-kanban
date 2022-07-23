package org.kamenkov.java_kanban;

import org.kamenkov.java_kanban.servers.HttpTaskServer;
import org.kamenkov.java_kanban.servers.KVServer;

public class Main {
    public static void main(String[] args) {
        try {
            new KVServer().start();
            new HttpTaskServer().start();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
