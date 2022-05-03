package org.kamenkov.java_kanban.task;

import org.kamenkov.java_kanban.Status;

public class TaskImpl implements Task {

    private Long id;
    private String summary;
    private String description;
    private Status status;

    public TaskImpl(String summary, String description) {
        this.summary = summary;
        this.description = description;
        status = Status.NEW;
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
        return "TaskImpl{" +
                "id=" + id +
                ", summary='" + summary + '\'' +
                ", description='" + description + '\'' +
                ", status=" + status +
                '}';
    }
}
