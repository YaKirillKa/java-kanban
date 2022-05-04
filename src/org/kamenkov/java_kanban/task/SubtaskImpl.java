package org.kamenkov.java_kanban.task;

public class SubtaskImpl extends TaskImpl implements Subtask {

    private final Long parentId;

    public SubtaskImpl(String summary, String description, Long parentId) {
        super(summary, description);
        this.parentId = parentId;
    }

    @Override
    public Long getParentId() {
        return parentId;
    }

    @Override
    public String toString() {
        return "SubtaskImpl{" +
                "id=" + getId() +
                ", summary='" + getSummary() + '\'' +
                ", description='" + getDescription() + '\'' +
                ", status=" + getStatus() +
                ", parentId=" + parentId +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        SubtaskImpl subtask = (SubtaskImpl) o;

        return getParentId() != null ? getParentId().equals(subtask.getParentId()) : subtask.getParentId() == null;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (getParentId() != null ? getParentId().hashCode() : 0);
        return result;
    }
}
