package org.kamenkov.java_kanban.managers;

import org.kamenkov.java_kanban.task.Task;

import java.util.LinkedList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {

    /* How many items should be saved in history */
    private static final int HISTORY_DEPTH = 10;

    private final List<Task> taskHistory;
    private static HistoryManager historyManager;

    /**
     * Private constructor to avoid duplicating of the manager.
     */
    private InMemoryHistoryManager() {
        taskHistory = new LinkedList<>();
    }

    /**
     * Returns instance of {@link InMemoryHistoryManager}.
     * If it doesn't exist, creates.
     *
     * @return {@link InMemoryHistoryManager}.
     */
    public static HistoryManager getInstance() {
        if (historyManager == null) {
            historyManager = new InMemoryHistoryManager();
        }
        return historyManager;
    }

    @Override
    public void add(Task task) {
        taskHistory.add(0, task);
        if (taskHistory.size() == HISTORY_DEPTH) {
            taskHistory.remove(HISTORY_DEPTH - 1);
        }
    }

    @Override
    public List<Task> getHistory() {
        return taskHistory;
    }
}
