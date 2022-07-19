package org.kamenkov.java_kanban.servers;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import org.kamenkov.java_kanban.managers.Managers;
import org.kamenkov.java_kanban.managers.TaskManager;
import org.kamenkov.java_kanban.task.Epic;
import org.kamenkov.java_kanban.task.Subtask;
import org.kamenkov.java_kanban.task.Task;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static java.nio.charset.StandardCharsets.UTF_8;

public class HttpTaskServer {
    private static final String CONTENT_TYPE = "Content-Type";
    private static final List<String> JSON = Collections.singletonList("application/json");

    final Gson gson = new Gson();
    TaskManager taskManager;
    HttpServer server;

    public HttpTaskServer() throws IOException {
        taskManager = Managers.getDefault();
        server = HttpServer.create(new InetSocketAddress(8080), 0);
        server.createContext("/tasks", new TasksHandler());
        server.createContext("/tasks/epic", new EpicHandler());
        server.createContext("/tasks/task", new TaskHandler());
        server.createContext("/tasks/subtask", new SubtaskHandler());
        server.createContext("/tasks/history", new HistoryHandler());
        server.start();
    }

    public void stop() {
        server.stop(5);
    }

    private class TaskHandler implements HttpHandler {

        @Override
        public void handle(HttpExchange exchange) throws IOException {
            exchange.getResponseHeaders().put(CONTENT_TYPE, JSON);
            String method = exchange.getRequestMethod();
            String query = exchange.getRequestURI().getQuery();
            Long id;
            switch (method) {
                case "POST":
                    InputStreamReader streamReader = new InputStreamReader(exchange.getRequestBody(), UTF_8);
                    BufferedReader bufferedReader = new BufferedReader(streamReader);
                    String body = bufferedReader.lines().collect(Collectors.joining(""));
                    Task task = gson.fromJson(body, Task.class);
                    if (task.getId() != null) {
                        try {
                            taskManager.updateTask(task, task.getId());
                            exchange.sendResponseHeaders(200, -1);
                        } catch (IllegalArgumentException e) {
                            exchange.sendResponseHeaders(400, 0);
                            try (OutputStream os = exchange.getResponseBody()) {
                                os.write(e.getMessage().getBytes());
                            }
                        }

                    } else {
                        try {
                            taskManager.createTask(task);
                            exchange.sendResponseHeaders(201, -1);
                        } catch (IllegalArgumentException e) {
                            exchange.sendResponseHeaders(400, 0);
                            try (OutputStream os = exchange.getResponseBody()) {
                                os.write(e.getMessage().getBytes());
                            }
                        }
                    }
                    break;
                case "GET":
                    if (query == null) {
                        String response = gson.toJson(taskManager.getAllTaskObjects());
                        exchange.sendResponseHeaders(200, 0);
                        try (OutputStream os = exchange.getResponseBody()) {
                            os.write(response.getBytes());
                        }
                    } else {
                        id = Long.parseLong(query.split("=")[1]);
                        if (taskManager.getTaskObjectById(id) != null) {
                            String response = gson.toJson(taskManager.getTaskObjectById(id));
                            exchange.sendResponseHeaders(200, 0);
                            try (OutputStream os = exchange.getResponseBody()) {
                                os.write(response.getBytes());
                            }
                        } else {
                            exchange.sendResponseHeaders(404, -1);
                        }
                    }
                    break;
                case "DELETE":
                    if (query == null) {
                        taskManager.removeAllTaskObjects();
                        exchange.sendResponseHeaders(200, -1);
                    } else {
                        id = Long.parseLong(query.split("=")[1]);
                        if (taskManager.getTaskObjectById(id) != null) {
                            taskManager.removeTask(id);
                            exchange.sendResponseHeaders(200, -1);
                        } else {
                            exchange.sendResponseHeaders(404, -1);
                        }
                    }
                    break;
                default:
                    exchange.sendResponseHeaders(405, -1);
            }
        }
    }

    private class EpicHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            exchange.getResponseHeaders().put(CONTENT_TYPE, JSON);
            String method = exchange.getRequestMethod();
            String query = exchange.getRequestURI().getQuery();
            Long id;
            switch (method) {
                case "POST":
                    InputStreamReader streamReader = new InputStreamReader(exchange.getRequestBody(), UTF_8);
                    BufferedReader bufferedReader = new BufferedReader(streamReader);
                    String body = bufferedReader.lines().collect(Collectors.joining(""));
                    Epic task = gson.fromJson(body, Epic.class);
                    if (task.getId() != null) {
                        try {
                            taskManager.updateEpic(task, task.getId());
                            exchange.sendResponseHeaders(200, -1);
                        } catch (IllegalArgumentException e) {
                            exchange.sendResponseHeaders(400, 0);
                            try (OutputStream os = exchange.getResponseBody()) {
                                os.write(e.getMessage().getBytes());
                            }
                        }

                    } else {
                        try {
                            taskManager.createEpic(task);
                            exchange.sendResponseHeaders(201, -1);
                        } catch (IllegalArgumentException e) {
                            exchange.sendResponseHeaders(400, 0);
                            try (OutputStream os = exchange.getResponseBody()) {
                                os.write(e.getMessage().getBytes());
                            }
                        }
                    }
                    break;
                case "GET":
                    if (query == null) {
                        String response = gson.toJson(taskManager.getAllEpicObjects());
                        exchange.sendResponseHeaders(200, 0);
                        try (OutputStream os = exchange.getResponseBody()) {
                            os.write(response.getBytes());
                        }
                    } else {
                        id = Long.parseLong(query.split("=")[1]);
                        if (taskManager.getEpicObjectById(id) != null) {
                            String response = gson.toJson(taskManager.getEpicObjectById(id));
                            exchange.sendResponseHeaders(200, 0);
                            try (OutputStream os = exchange.getResponseBody()) {
                                os.write(response.getBytes());
                            }
                        } else {
                            exchange.sendResponseHeaders(404, -1);
                        }
                    }
                    break;
                case "DELETE":
                    if (query == null) {
                        taskManager.removeAllEpicObjects();
                        exchange.sendResponseHeaders(200, -1);
                    } else {
                        id = Long.parseLong(query.split("=")[1]);
                        if (taskManager.getEpicObjectById(id) != null) {
                            taskManager.removeEpic(id);
                            exchange.sendResponseHeaders(200, -1);
                        } else {
                            exchange.sendResponseHeaders(404, -1);
                        }
                    }
                    break;
                default:
                    exchange.sendResponseHeaders(405, -1);
            }
        }
    }

    private class SubtaskHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            exchange.getResponseHeaders().put(CONTENT_TYPE, JSON);
            String method = exchange.getRequestMethod();
            String query = exchange.getRequestURI().getQuery();
            Long id;
            switch (method) {
                case "POST":
                    InputStreamReader streamReader = new InputStreamReader(exchange.getRequestBody(), UTF_8);
                    BufferedReader bufferedReader = new BufferedReader(streamReader);
                    String body = bufferedReader.lines().collect(Collectors.joining(""));
                    Subtask task = gson.fromJson(body, Subtask.class);
                    if (task.getId() != null) {
                        try {
                            taskManager.updateSubtask(task, task.getId());
                            exchange.sendResponseHeaders(200, -1);
                        } catch (IllegalArgumentException e) {
                            exchange.sendResponseHeaders(400, 0);
                            try (OutputStream os = exchange.getResponseBody()) {
                                os.write(e.getMessage().getBytes());
                            }
                        }

                    } else {
                        try {
                            taskManager.createSubtask(task);
                            exchange.sendResponseHeaders(201, -1);
                        } catch (IllegalArgumentException e) {
                            exchange.sendResponseHeaders(400, 0);
                            try (OutputStream os = exchange.getResponseBody()) {
                                os.write(e.getMessage().getBytes());
                            }
                        }
                    }
                    break;
                case "GET":
                    if (query == null) {
                        String response = gson.toJson(taskManager.getAllSubtaskObjects());
                        exchange.sendResponseHeaders(200, 0);
                        try (OutputStream os = exchange.getResponseBody()) {
                            os.write(response.getBytes());
                        }
                    } else {
                        id = Long.parseLong(query.split("=")[1]);
                        if (taskManager.getSubtaskObjectById(id) != null) {
                            String response = gson.toJson(taskManager.getSubtaskObjectById(id));
                            exchange.sendResponseHeaders(200, 0);
                            try (OutputStream os = exchange.getResponseBody()) {
                                os.write(response.getBytes());
                            }
                        } else {
                            exchange.sendResponseHeaders(404, -1);
                        }
                    }
                    break;
                case "DELETE":
                    if (query == null) {
                        taskManager.removeAllSubtaskObjects();
                        exchange.sendResponseHeaders(200, -1);
                    } else {
                        id = Long.parseLong(query.split("=")[1]);
                        if (taskManager.getSubtaskObjectById(id) != null) {
                            taskManager.removeSubtask(id);
                            exchange.sendResponseHeaders(200, -1);
                        } else {
                            exchange.sendResponseHeaders(404, -1);
                        }
                    }
                    break;
                default:
                    exchange.sendResponseHeaders(405, -1);
            }
        }
    }

    private class TasksHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            exchange.getResponseHeaders().put(CONTENT_TYPE, JSON);
            String response = gson.toJson(taskManager.getPrioritizedTasks());
            exchange.sendResponseHeaders(200, 0);
            try (OutputStream os = exchange.getResponseBody()) {
                os.write(response.getBytes());
            }
        }
    }

    private class HistoryHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            exchange.getResponseHeaders().put(CONTENT_TYPE, JSON);
            String response = gson.toJson(taskManager.getHistory());
            exchange.sendResponseHeaders(200, 0);
            try (OutputStream os = exchange.getResponseBody()) {
                os.write(response.getBytes());
            }
        }
    }
}
