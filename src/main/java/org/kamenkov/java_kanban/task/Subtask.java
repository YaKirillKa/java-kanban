package org.kamenkov.java_kanban.task;

public class Subtask extends Task {

    private final Long parentId;

    public Subtask(String summary, String description, Long parentId) {
        super(summary, description);
        this.parentId = parentId;
        setType(Type.SUBTASK);
    }

    /**
     * Returns the unique ID of the parent {@link Epic}.
     *
     * @return parent {@link Epic} id.
     */
    public Long getParentId() {
        return parentId;
    }

    @Override
    public String toString() {
        return getId() + "," + getType() + "," + getSummary() + "," + getStatus() + "," + getDescription()
                + "," + getStartDate() + "," + getDurationInMinutes() + "," + getEndDate() + "," + getParentId();
    }

}
