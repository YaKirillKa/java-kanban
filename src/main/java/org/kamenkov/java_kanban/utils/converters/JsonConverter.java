package org.kamenkov.java_kanban.utils.converters;

import com.sun.net.httpserver.HttpExchange;
import org.kamenkov.java_kanban.managers.TaskManager;
import org.kamenkov.java_kanban.task.Type;

public interface JsonConverter {

    boolean support(Type type);

    String transform(TaskManager taskManager, HttpExchange exchange);
}
