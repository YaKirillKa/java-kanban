package org.kamenkov.java_kanban.task;

import java.util.List;

public interface Epic extends Task {

    /**
     * Returns all {@link Subtask}s form the exists {@link Epic}.
     *
     * @return {@link List} of {@link Subtask}.
     */
    List<Subtask> getSubtaskObjects();

    /**
     * Add {@link Subtask} to parent Epic.
     *
     * @param subtask what subtask should be added.
     */
    void addSubtask(Subtask subtask);

    /**
     * Remove {@link Subtask} from parent Epic.
     *
     * @param subtask what subtask should be removed.
     */
    void removeSubtask(Subtask subtask);

    /**
     * Changes status of parent {@link Epic} based on its {@link Subtask}s.
     */
    void recalculateStatus();

    /**
     * Changes dates of parent {@link Epic} based on its {@link Subtask}s.
     */
    void recalculateDates();

}
