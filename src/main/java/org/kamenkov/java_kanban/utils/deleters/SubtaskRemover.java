package org.kamenkov.java_kanban.utils.deleters;

import com.sun.net.httpserver.HttpExchange;
import org.kamenkov.java_kanban.managers.TaskManager;
import org.kamenkov.java_kanban.task.Type;

public class SubtaskRemover implements Remover {

    @Override
    public boolean support(Type type) {
        return Type.SUBTASK == type;
    }

    @Override
    public boolean remove(TaskManager taskManager, HttpExchange exchange) {
        String query = exchange.getRequestURI().getQuery();
        if (query == null) {
            taskManager.removeAllSubtaskObjects();
            return true;
        } else {
            Long id = Long.parseLong(query.split("=")[1]);
            if (taskManager.getSubtaskObjectById(id) != null) {
                taskManager.removeSubtask(id);
                return true;
            }
        }
        return false;
    }
}
