package org.kamenkov.java_kanban.utils.converters;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.kamenkov.java_kanban.managers.TaskManager;
import org.kamenkov.java_kanban.task.Subtask;
import org.kamenkov.java_kanban.task.Type;

public class JsonSubtaskConverter implements TaskConverter {

    private final Gson gson;

    public JsonSubtaskConverter(Gson gson) {
        this.gson = gson;
    }

    @Override
    public boolean support(String type) {
        return Type.SUBTASK.name().equals(type);
    }

    @Override
    public Long transform(TaskManager taskManager, JsonObject jsonObject) {
        Long id = -1L;
        Subtask task = gson.fromJson(jsonObject, Subtask.class);
        if (task.getId() == null) {
            id = taskManager.createSubtask(task);
        } else {
            taskManager.updateSubtask(task, task.getId());
        }
        return id;
    }
}
