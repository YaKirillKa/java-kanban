package org.kamenkov.java_kanban.task;

import org.kamenkov.java_kanban.Status;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class Epic extends Task {

    private final List<Subtask> subtasks;
    private LocalDateTime endDate;

    public Epic(String summary, String description) {
        super(summary, description);
        subtasks = new ArrayList<>();
        setType(Type.EPIC);
        recalculateStatus();
    }

    /**
     * Returns all {@link Subtask}s form the exists {@link Epic}.
     *
     * @return {@link List} of {@link Subtask}.
     */
    public List<Subtask> getSubtaskObjects() {
        return subtasks;
    }

    private void setEndDate(LocalDateTime endDate) {
        this.endDate = endDate;
    }

    /**
     * Add {@link Subtask} to parent Epic.
     *
     * @param subtask what subtask should be added.
     */
    public void addSubtask(Subtask subtask) {
        if (subtask != null) {
            subtasks.add(subtask);
            recalculateStatus();
            recalculateDates();
        }
    }

    /**
     * Remove {@link Subtask} from parent Epic.
     *
     * @param subtask what subtask should be removed.
     */
    public void removeSubtask(Subtask subtask) {
        if (subtask != null) {
            subtasks.remove(subtask);
            recalculateStatus();
            recalculateDates();
        }
    }

    /**
     * Changes status of parent {@link Epic} based on its {@link Subtask}s.
     */
    public void recalculateStatus() {
        if (subtasks.isEmpty()) {
            setStatus(Status.NEW);
            return;
        }
        Set<Status> subtaskStatuses = subtasks.stream().map(Subtask::getStatus).collect(Collectors.toSet());
        if (subtaskStatuses.size() == 1) {
            setStatus(subtaskStatuses.stream().findFirst().orElse(Status.NEW));
            return;
        }
        setStatus(Status.IN_PROGRESS);
    }

    /**
     * Changes dates of parent {@link Epic} based on its {@link Subtask}s.
     */
    public void recalculateDates() {
        if (subtasks.isEmpty()) {
            return;
        }
        long duration = subtasks.get(0).getDurationInMinutes();
        LocalDateTime startDateToUpdate = subtasks.get(0).getStartDate();
        LocalDateTime endDateToUpdate = subtasks.get(0).getEndDate();
        for (int i = 1; i < subtasks.size(); i++) {
            final Subtask subtask = subtasks.get(i);
            duration += subtask.getDurationInMinutes();
            final LocalDateTime subtaskStartDate = subtask.getStartDate();
            if (subtaskStartDate != null && subtaskStartDate.isBefore(startDateToUpdate)) {
                startDateToUpdate = subtaskStartDate;
            }
            final LocalDateTime subtaskEndDate = subtask.getEndDate();
            if (subtaskEndDate != null && subtaskEndDate.isAfter(endDateToUpdate)) {
                endDateToUpdate = subtaskEndDate;
            }
        }
        setDurationInMinutes(duration);
        setStartDate(startDateToUpdate);
        setEndDate(endDateToUpdate);
    }

    @Override
    public String toString() {
        return getId() + "," + getType() + "," + getSummary() + "," + getStatus() + "," + getDescription() + ","
                + getStartDate() + "," + getDurationInMinutes() + "," + getEndDate();
    }

}
