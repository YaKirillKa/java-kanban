package org.kamenkov.java_kanban;

import org.kamenkov.java_kanban.servers.HttpTaskServer;

import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        try {
            new HttpTaskServer();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
