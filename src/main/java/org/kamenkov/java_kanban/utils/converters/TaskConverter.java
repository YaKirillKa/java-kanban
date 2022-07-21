package org.kamenkov.java_kanban.utils.converters;

import com.google.gson.JsonObject;
import org.kamenkov.java_kanban.managers.TaskManager;

public interface TaskConverter {

    boolean support(String type);

    Long transform(TaskManager taskManager, JsonObject jsonObject);
}
