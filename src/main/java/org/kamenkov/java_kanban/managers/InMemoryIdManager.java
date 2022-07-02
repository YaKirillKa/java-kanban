package org.kamenkov.java_kanban.managers;

public class InMemoryIdManager implements IdManager {

    private Long lastId;

    /**
     * Constructor without parameters.
     */
    InMemoryIdManager() {
        lastId = 0L;
    }

    /**
     * Constructor for initializing the manager with the last ID.
     *
     * @param lastId the last ID used.
     */
    InMemoryIdManager(Long lastId) {
        this.lastId = lastId;
    }

    /**
     * Increments and returns last available ID.
     *
     * @return {@link Long} ID.
     */
    @Override
    public Long getLastId() {
        return ++lastId;
    }

}
