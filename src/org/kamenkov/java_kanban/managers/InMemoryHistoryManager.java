package org.kamenkov.java_kanban.managers;

import org.kamenkov.java_kanban.task.Task;

import java.util.LinkedList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {

    /* How many items should be saved in history */
    private static final int HISTORY_DEPTH = 10;

    private final LinkedList<Task> taskHistory;
    private static final HistoryManager HISTORY_MANAGER = new InMemoryHistoryManager();

    /**
     * Private constructor to avoid duplicating of the manager.
     */
    private InMemoryHistoryManager() {
        taskHistory = new LinkedList<>();
    }

    /**
     * Returns instance of {@link InMemoryHistoryManager}.
     *
     * @return {@link InMemoryHistoryManager}.
     */
    public static HistoryManager getInstance() {
        return HISTORY_MANAGER;
    }

    @Override
    public void add(Task task) {
        taskHistory.addLast(task);
        if (taskHistory.size() > HISTORY_DEPTH) {
            taskHistory.removeFirst();
        }
    }

    @Override
    public List<Task> getHistory() {
        return List.copyOf(taskHistory);
    }
}
