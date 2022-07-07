package org.kamenkov.java_kanban.task;

import org.kamenkov.java_kanban.Status;

import java.time.LocalDateTime;

public class TaskImpl implements Task {

    private static final Type type = Type.TASK;
    private Long id;
    private String summary;
    private String description;
    private Status status;
    private LocalDateTime startDate;
    long durationInMinutes;

    public TaskImpl(String summary, String description) {
        this.summary = summary;
        this.description = description;
        this.status = Status.NEW;
    }

    @Override
    public Type getType() {
        return type;
    }

    @Override
    public void setStatus(Status status) {
        this.status = status;
    }

    @Override
    public String getSummary() {
        return summary;
    }

    @Override
    public void setSummary(String summary) {
        this.summary = summary;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public Status getStatus() {
        return status;
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public LocalDateTime getStartDate() {
        return startDate;
    }

    @Override
    public void setStartDate(LocalDateTime startDate) {
        this.startDate = startDate;
    }

    @Override
    public LocalDateTime getEndDate() {
        return startDate == null ? null : startDate.plusMinutes(durationInMinutes);
    }

    @Override
    public long getDurationInMinutes() {
        return durationInMinutes;
    }

    @Override
    public void setDurationInMinutes(long durationInMinutes) {
        this.durationInMinutes = durationInMinutes;
    }

    @Override
    public String toString() {
        return id + "," + type + "," + summary + "," + status + ","
                + description + "," + startDate + "," + durationInMinutes + "," + getEndDate();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TaskImpl task = (TaskImpl) o;

        if (getId() != null ? !getId().equals(task.getId()) : task.getId() != null) return false;
        if (getSummary() != null ? !getSummary().equals(task.getSummary()) : task.getSummary() != null) return false;
        return getDescription() != null ? getDescription().equals(task.getDescription()) : task.getDescription() == null;
    }

    @Override
    public int hashCode() {
        int result = getId() != null ? getId().hashCode() : 0;
        result = 31 * result + (getSummary() != null ? getSummary().hashCode() : 0);
        result = 31 * result + (getDescription() != null ? getDescription().hashCode() : 0);
        return result;
    }
}
