package org.kamenkov.java_kanban.task;

public class Subtask extends Task {

    private final Long parentId;
    private static final Type type = Type.SUBTASK;

    public Subtask(String summary, String description, Long parentId) {
        super(summary, description);
        this.parentId = parentId;
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
        return getId() + "," + type + "," + getSummary() + "," + getStatus() + "," + getDescription()
                + "," + getStartDate() + "," + getDurationInMinutes() + "," + getEndDate() + "," + getParentId();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        Subtask subtask = (Subtask) o;

        return getParentId() != null ? getParentId().equals(subtask.getParentId()) : subtask.getParentId() == null;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (getParentId() != null ? getParentId().hashCode() : 0);
        return result;
    }
}
