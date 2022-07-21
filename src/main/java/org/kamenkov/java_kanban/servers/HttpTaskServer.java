package org.kamenkov.java_kanban.servers;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import org.kamenkov.java_kanban.managers.Managers;
import org.kamenkov.java_kanban.managers.TaskManager;
import org.kamenkov.java_kanban.task.Type;
import org.kamenkov.java_kanban.utils.Utils;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public class HttpTaskServer {
    private static final String CONTENT_TYPE = "Content-Type";
    private static final List<String> JSON = Collections.singletonList("application/json");
    private static final String TASKS_ENDPOINT = "/tasks";
    private static final String TASK_ENDPOINT = "/tasks/task";
    private static final String EPIC_ENDPOINT = "/tasks/epic";
    private static final String SUBTASK_ENDPOINT = "/tasks/subtask";
    private static final String SUBTASK_EPIC_ENDPOINT = "/tasks/subtask/epic";
    private static final String HISTORY_ENDPOINT = "/tasks/history";
    private final TaskManager taskManager;
    private final Gson gson;
    private final HttpServer server;

    public HttpTaskServer() throws IOException {
        gson = new Gson();
        taskManager = Managers.getDefault();
        Map<Method, Function<HttpExchange, Response>> tasksMethods = Map.ofEntries(
                Map.entry(Method.GET, exchange -> {
                    String json = Utils.transformTasksToJson(taskManager);
                    return json != null ? new Response(200, json) : new Response(404, null);
                })
        );
        Map<Method, Function<HttpExchange, Response>> taskMethods = Map.ofEntries(
                Map.entry(Method.GET, exchange -> {
                    String json = Utils.transformTaskToJson(exchange, taskManager, Type.TASK);
                    return json != null ? new Response(200, json) : new Response(404, null);
                }),
                Map.entry(Method.POST, exchange -> {
                    Long id = Utils.createTaskFromExchange(exchange, taskManager);
                    if (id == null) {
                        return new Response(400, null);
                    }
                    return id > 0 ? new Response(201, String.valueOf(id)) : new Response(200, null);
                }),
                Map.entry(Method.DELETE, exchange -> {
                    boolean deleted = Utils.deleteTask(exchange, taskManager, Type.TASK);
                    return deleted ? new Response(200, null) : new Response(404, null);
                })
        );
        Map<Method, Function<HttpExchange, Response>> epicMethods = Map.ofEntries(
                Map.entry(Method.GET, exchange -> {
                    String json = Utils.transformTaskToJson(exchange, taskManager, Type.EPIC);
                    return json != null ? new Response(200, json) : new Response(404, null);
                }),
                Map.entry(Method.POST, exchange -> {
                    Long id = Utils.createTaskFromExchange(exchange, taskManager);
                    if (id == null) {
                        return new Response(400, null);
                    }
                    return id > 0 ? new Response(201, String.valueOf(id)) : new Response(200, null);
                }),
                Map.entry(Method.DELETE, exchange -> {
                    boolean deleted = Utils.deleteTask(exchange, taskManager, Type.EPIC);
                    return deleted ? new Response(200, null) : new Response(404, null);
                })
        );
        Map<Method, Function<HttpExchange, Response>> subtaskMethods = Map.ofEntries(
                Map.entry(Method.GET, exchange -> {
                    String json = Utils.transformTaskToJson(exchange, taskManager, Type.SUBTASK);
                    return json != null ? new Response(200, json) : new Response(404, null);
                }),
                Map.entry(Method.POST, exchange -> {
                    Long id = Utils.createTaskFromExchange(exchange, taskManager);
                    if (id == null) {
                        return new Response(400, null);
                    }
                    return id > 0 ? new Response(201, String.valueOf(id)) : new Response(200, null);
                }), Map.entry(Method.DELETE, exchange -> {
                    boolean deleted = Utils.deleteTask(exchange, taskManager, Type.SUBTASK);
                    return deleted ? new Response(200, null) : new Response(404, null);
                })
        );
        Map<Method, Function<HttpExchange, Response>> subtaskEpicMethods = Map.ofEntries(
                Map.entry(Method.GET, exchange -> {
                    String json = Utils.transformSubtasksToJson(exchange, taskManager);
                    return json != null ? new Response(200, json) : new Response(404, null);
                })
        );
        Map<Method, Function<HttpExchange, Response>> historyMethods = Map.ofEntries(
                Map.entry(Method.GET, exchange -> new Response(200, gson.toJson(taskManager.getHistory())))
        );
        server = HttpServer.create(new InetSocketAddress(8080), 0);
        server.createContext(TASKS_ENDPOINT, new DefaultHandler(tasksMethods));
        server.createContext(TASK_ENDPOINT, new DefaultHandler(taskMethods));
        server.createContext(EPIC_ENDPOINT, new DefaultHandler(epicMethods));
        server.createContext(SUBTASK_ENDPOINT, new DefaultHandler(subtaskMethods));
        server.createContext(SUBTASK_EPIC_ENDPOINT, new DefaultHandler(subtaskEpicMethods));
        server.createContext(HISTORY_ENDPOINT, new DefaultHandler(historyMethods));
    }

    public void start() {
        server.start();
    }

    public void stop() {
        server.stop(5);
    }

    private class DefaultHandler implements HttpHandler {

        private final Map<Method, Function<HttpExchange, Response>> operations;

        public DefaultHandler(Map<Method, Function<HttpExchange, Response>> operations) {
            this.operations = operations;
        }

        @Override
        public void handle(HttpExchange exchange) throws IOException {
            exchange.getResponseHeaders().put(CONTENT_TYPE, JSON);
            Method method = Method.valueOf(exchange.getRequestMethod());
            if (operations.containsKey(method)) {
                performRequest(exchange, operations.get(method));
            } else {
                exchange.sendResponseHeaders(405, -1);
            }
        }

        private void performRequest(HttpExchange exchange, Function<HttpExchange, Response> function) throws IOException {
            try {
                Response response = function.apply(exchange);
                if (response.body == null) {
                    exchange.sendResponseHeaders(response.getStatus(), -1);
                } else {
                    exchange.sendResponseHeaders(response.getStatus(), 0);
                    try (OutputStream os = exchange.getResponseBody()) {
                        os.write(response.getBody().getBytes());
                    }
                }
            } catch (Exception e) {
                exchange.sendResponseHeaders(500, -1);
            }
        }
    }

    private static class Response {
        int status;
        String body;

        public Response(int status, String body) {
            this.status = status;
            this.body = body;
        }

        public int getStatus() {
            return status;
        }

        public String getBody() {
            return body;
        }
    }

    private enum Method {GET, POST, DELETE}
}
