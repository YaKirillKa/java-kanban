package org.kamenkov.java_kanban.utils.converters;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import org.kamenkov.java_kanban.managers.TaskManager;
import org.kamenkov.java_kanban.task.Task;
import org.kamenkov.java_kanban.task.Type;

public class TaskJsonConverter implements JsonConverter {

    private final Gson gson;

    public TaskJsonConverter(Gson gson) {
        this.gson = gson;
    }

    @Override
    public boolean support(Type type) {
        return Type.TASK == type;
    }

    @Override
    public String transform(TaskManager taskManager, HttpExchange exchange) {
        String json;
        String query = exchange.getRequestURI().getQuery();
        if (query == null) {
            json = gson.toJson(taskManager.getAllTaskObjects());
        } else {
            Long id = Long.parseLong(query.split("=")[1]);
            Task task = taskManager.getTaskObjectById(id);
            if (task == null) {
                return null;
            }
            json = gson.toJson(task);
        }
        return json;
    }
}
