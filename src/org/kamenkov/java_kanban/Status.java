package org.kamenkov.java_kanban;

/**
 * {@link Enum} that contains all Statuses and its names.
 */
//TODO: replace immutable Enum to IssueType.
public enum Status {
    NEW("NEW"),
    IN_PROGRESS("IN PROGRESS"),
    DONE("DONE");

    private final String name;

    Status(String name) {
        this.name = name;
    }
}
