package org.kamenkov.java_kanban.utils.deleters;

import com.sun.net.httpserver.HttpExchange;
import org.kamenkov.java_kanban.managers.TaskManager;
import org.kamenkov.java_kanban.task.Type;

public interface Remover {

    boolean support(Type type);

    boolean remove(TaskManager taskManager, HttpExchange exchange);
}
