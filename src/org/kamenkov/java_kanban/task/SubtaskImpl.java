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
}
