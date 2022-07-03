package org.kamenkov.java_kanban.task;

import org.kamenkov.java_kanban.Status;

import java.time.LocalDate;

public interface Task {

    /**
     * Returns Type of {@link Task}.
     *
     * @return type.
     */
    Type getType();

    /**
     * Returns summary of {@link Task}.
     *
     * @return summary.
     */
    String getSummary();

    /**
     * Update summary of {@link Task}
     * @param summary {@link String} that should be saved in summary.
     */
    void setSummary(String summary);

    /**
     * Returns description of {@link Task}.
     *
     * @return description.
     */
    String getDescription();

    /**
     * Update description of {@link Task}
     * @param description {@link String} that should be saved in description.
     */
    void setDescription(String description);

    /**
     * Returns status of {@link Task}.
     *
     * @return status.
     */
    Status getStatus();

    /**
     * Update status of {@link Task}
     * @param status {@link Status} that should be saved in status.
     */
    void setStatus(Status status);

    /**
     * Returns id of {@link Task}.
     *
     * @return unique id of {@link Task}.
     */
    Long getId();

    /**
     * Update unique id of {@link Task}.
     * @param id {@link Long} that should be saved in id.
     */
    void setId(Long id);

    LocalDate getStartDate();

    void setStartDate(LocalDate startDate);

    LocalDate getEndDate();

    long getDurationInMinutes();

    void setDurationInMinutes(long durationInMinutes);
}
