package org.kamenkov.java_kanban.utils;

import com.google.gson.*;
import com.sun.net.httpserver.HttpExchange;
import org.kamenkov.java_kanban.managers.TaskManager;
import org.kamenkov.java_kanban.task.Type;
import org.kamenkov.java_kanban.utils.converters.*;
import org.kamenkov.java_kanban.utils.deleters.EpicRemover;
import org.kamenkov.java_kanban.utils.deleters.Remover;
import org.kamenkov.java_kanban.utils.deleters.SubtaskRemover;
import org.kamenkov.java_kanban.utils.deleters.TaskRemover;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.List;

import static java.nio.charset.StandardCharsets.UTF_8;

public class Utils {

    private Utils() {
    }

    private static final Gson GSON = new GsonBuilder().create();
    private static final List<JsonConverter> TASK_JSON_CONVERTERS = List.of(
            new TaskJsonConverter(GSON),
            new EpicJsonConverter(GSON),
            new SubtaskJsonConverter(GSON)
    );

    private static final List<TaskConverter> JSON_TASK_CONVERTERS = List.of(
            new JsonTaskConverter(GSON),
            new JsonEpicConverter(GSON),
            new JsonSubtaskConverter(GSON)
    );

    private static final List<Remover> REMOVERS = List.of(
            new TaskRemover(),
            new EpicRemover(),
            new SubtaskRemover()
    );


    public static Long createTaskFromExchange(HttpExchange exchange, TaskManager taskManager) {
        InputStreamReader streamReader = new InputStreamReader(exchange.getRequestBody(), UTF_8);
        BufferedReader bufferedReader = new BufferedReader(streamReader);
        JsonElement jsonElement = JsonParser.parseReader(bufferedReader);
        JsonObject jsonObject;
        if (jsonElement.isJsonObject()) {
            jsonObject = jsonElement.getAsJsonObject();
        } else {
            throw new IllegalArgumentException("Incorrect JSON object");
        }
        for (TaskConverter converter : JSON_TASK_CONVERTERS) {
            if (converter.support(jsonObject.get("type").getAsString())) {
                return converter.transform(taskManager, jsonObject);
            }
        }
        return null;
    }

    public static String transformSubtasksToJson(HttpExchange exchange, TaskManager taskManager) {
        String query = exchange.getRequestURI().getQuery();
        if (query == null) {
            return null;
        }
        Long id = Long.parseLong(query.split("=")[1]);
        return GSON.toJson(taskManager.getSubtaskObjectsByParentId(id));
    }

    public static String transformTaskToJson(HttpExchange exchange, TaskManager taskManager, Type type) {
        for (JsonConverter converter : TASK_JSON_CONVERTERS) {
            if (converter.support(type)) {
                return converter.transform(taskManager, exchange);
            }
        }
        return null;
    }

    public static String transformTasksToJson(TaskManager taskManager) {
        return GSON.toJson(taskManager.getPrioritizedTasks());
    }

    public static boolean deleteTask(HttpExchange exchange, TaskManager taskManager, Type type) {
        for (Remover remover : REMOVERS) {
            if (remover.support(type)) {
                return remover.remove(taskManager, exchange);
            }
        }
        return false;
    }
}
