package org.kamenkov.java_kanban.managers;

import org.kamenkov.java_kanban.task.Task;

import java.util.List;

public interface HistoryManager {

    /**
     * Adds the given {@link Task} to the history.
     *
     * @param task {@link Task} that should be saved.
     */
    void add(Task task);

    /**
     * Returns a {@link List} of {@link Task}s that have been viewed by users.
     *
     * @return {@link List} of {@link Task} or empty {@link List}
     */
    List<Task> getHistory();

}
