package org.kamenkov.java_kanban.task;

/**
 * {@link Enum} that contains all task types and its names.
 */
public enum Type {
    TASK("TASK"),
    EPIC("EPIC"),
    SUBTASK("SUBTASK");

    private final String name;

    Type(String name) {
        this.name = name;
    }
}
