package org.kamenkov.java_kanban.task;

import org.kamenkov.java_kanban.Status;

public class TaskImpl implements Task {

    private Long id;
    private String summary;
    private String description;
    private Status status;
    private static final Type type = Type.TASK;

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
    public String toString() {
        return id + "," + type + "," + summary + "," + status + "," + description;
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
