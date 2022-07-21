package org.kamenkov.java_kanban.utils.deleters;

import com.sun.net.httpserver.HttpExchange;
import org.kamenkov.java_kanban.managers.TaskManager;
import org.kamenkov.java_kanban.task.Type;

public class EpicRemover implements Remover {

    @Override
    public boolean support(Type type) {
        return Type.EPIC == type;
    }

    @Override
    public boolean remove(TaskManager taskManager, HttpExchange exchange) {
        String query = exchange.getRequestURI().getQuery();
        if (query == null) {
            taskManager.removeAllEpicObjects();
            return true;
        } else {
            Long id = Long.parseLong(query.split("=")[1]);
            if (taskManager.getEpicObjectById(id) != null) {
                taskManager.removeEpic(id);
                return true;
            }
        }
        return false;
    }
}
