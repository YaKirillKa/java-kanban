package org.kamenkov.java_kanban.utils.converters;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.kamenkov.java_kanban.managers.TaskManager;
import org.kamenkov.java_kanban.task.Task;
import org.kamenkov.java_kanban.task.Type;

public class JsonTaskConverter implements TaskConverter {

    private final Gson gson;

    public JsonTaskConverter(Gson gson) {
        this.gson = gson;
    }

    @Override
    public boolean support(String type) {
        return Type.TASK.name().equals(type);
    }

    @Override
    public Long transform(TaskManager taskManager, JsonObject jsonObject) {
        Long id = -1L;
        Task task = gson.fromJson(jsonObject, Task.class);
        if (task.getId() == null) {
            id = taskManager.createTask(task);
        } else {
            taskManager.updateTask(task, task.getId());
        }
        return id;
    }
}
