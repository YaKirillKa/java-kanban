package org.kamenkov.java_kanban.utils.converters;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.kamenkov.java_kanban.managers.TaskManager;
import org.kamenkov.java_kanban.task.Epic;
import org.kamenkov.java_kanban.task.Type;

public class JsonEpicConverter implements TaskConverter {

    private final Gson gson;

    public JsonEpicConverter(Gson gson) {
        this.gson = gson;
    }

    @Override
    public boolean support(String type) {
        return Type.TASK.name().equals(type);
    }

    @Override
    public Long transform(TaskManager taskManager, JsonObject jsonObject) {
        Long id = -1L;
        Epic task = gson.fromJson(jsonObject, Epic.class);
        if (task.getId() == null) {
            id = taskManager.createEpic(task);
        } else {
            taskManager.updateEpic(task, task.getId());
        }
        return id;
    }
}
