package org.kamenkov.java_kanban.utils.converters;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import org.kamenkov.java_kanban.managers.TaskManager;
import org.kamenkov.java_kanban.task.Epic;
import org.kamenkov.java_kanban.task.Type;

public class EpicJsonConverter implements JsonConverter {

    private final Gson gson;

    public EpicJsonConverter(Gson gson) {
        this.gson = gson;
    }

    @Override
    public boolean support(Type type) {
        return Type.EPIC == type;
    }

    @Override
    public String transform(TaskManager taskManager, HttpExchange exchange) {
        String json;
        String query = exchange.getRequestURI().getQuery();
        if (query == null) {
            json = gson.toJson(taskManager.getAllEpicObjects());
        } else {
            Long id = Long.parseLong(query.split("=")[1]);
            Epic epic = taskManager.getEpicObjectById(id);
            if (epic == null) {
                return null;
            }
            json = gson.toJson(epic);
        }
        return json;
    }
}
