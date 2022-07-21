package org.kamenkov.java_kanban.utils.converters;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import org.kamenkov.java_kanban.managers.TaskManager;
import org.kamenkov.java_kanban.task.Subtask;
import org.kamenkov.java_kanban.task.Type;

public class SubtaskJsonConverter implements JsonConverter {

    private final Gson gson;

    public SubtaskJsonConverter(Gson gson) {
        this.gson = gson;
    }

    @Override
    public boolean support(Type type) {
        return Type.SUBTASK == type;
    }

    @Override
    public String transform(TaskManager taskManager, HttpExchange exchange) {
        String json;
        String query = exchange.getRequestURI().getQuery();
        if (query == null) {
            json = gson.toJson(taskManager.getAllSubtaskObjects());
        } else {
            Long id = Long.parseLong(query.split("=")[1]);
            Subtask subtask = taskManager.getSubtaskObjectById(id);
            if (subtask == null) {
                return null;
            }
            json = gson.toJson(subtask);
        }
        return json;
    }
}
