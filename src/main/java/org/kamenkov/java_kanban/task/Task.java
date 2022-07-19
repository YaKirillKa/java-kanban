package org.kamenkov.java_kanban.task;

import org.kamenkov.java_kanban.Status;

import java.time.LocalDateTime;

public class Task {

    private Type type;
    private Long id;
    private String summary;
    private String description;
    private Status status;
    private LocalDateTime startDate;
    long durationInMinutes;

    public Task(String summary, String description) {
        this.summary = summary;
        this.description = description;
        this.status = Status.NEW;
        this.type = Type.TASK;
    }

    /**
     * Returns Type of {@link Task}.
     *
     * @return type.
     */
    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    /**
     * Returns summary of {@link Task}.
     *
     * @return summary.
     */
    public String getSummary() {
        return summary;
    }

    /**
     * Update summary of {@link Task}
     * @param summary {@link String} that should be saved in summary.
     */
    public void setSummary(String summary) {
        this.summary = summary;
    }

    /**
     * Returns description of {@link Task}.
     *
     * @return description.
     */
    public String getDescription() {
        return description;
    }

    /**
     * Update description of {@link Task}
     * @param description {@link String} that should be saved in description.
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Returns status of {@link Task}.
     *
     * @return status.
     */
    public Status getStatus() {
        return status;
    }

    /**
     * Update status of {@link Task}
     * @param status {@link Status} that should be saved in status.
     */
    public void setStatus(Status status) {
        this.status = status;
    }

    /**
     * Returns id of {@link Task}.
     *
     * @return unique id of {@link Task}.
     */
    public Long getId() {
        return id;
    }

    /**
     * Update unique id of {@link Task}.
     * @param id {@link Long} that should be saved in id.
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Returns start date of the task.
     *
     * @return startDate.
     */
    public LocalDateTime getStartDate() {
        return startDate;
    }

    /**
     * Updates start date of the task.
     * @param startDate the date should be set.
     */
    public void setStartDate(LocalDateTime startDate) {
        this.startDate = startDate;
    }

    /**
     * Returns end date of the task.
     *
     * @return endDate.
     */
    public LocalDateTime getEndDate() {
        return startDate == null ? null : startDate.plusMinutes(durationInMinutes);
    }

    /**
     * Returns task duration in minutes.
     *
     * @return duration.
     */
    public long getDurationInMinutes() {
        return durationInMinutes;
    }

    /**
     * Updates task duration in minutes.
     * @param durationInMinutes duration of the task that should be set.
     */
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

        Task task = (Task) o;

        return getId() != null ? getId().equals(task.getId()) : task.getId() == null;
    }

    @Override
    public int hashCode() {
        return getId() != null ? getId().hashCode() : 0;
    }
}
