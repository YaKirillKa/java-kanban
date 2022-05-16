package org.kamenkov.java_kanban.managers;

public class InMemoryIdManager implements IdManager {

    private Long lastId;
    private static final InMemoryIdManager IN_MEMORY_ID_MANAGER = new InMemoryIdManager();

    /**
     * Private constructor to avoid duplicating of the manager.
     */
    private InMemoryIdManager() {
        lastId = 0L;
    }

    /**
     * Returns instance of {@link InMemoryIdManager}.
     *
     * @return {@link InMemoryIdManager}.
     */
    public static IdManager getInstance() {
        return IN_MEMORY_ID_MANAGER;
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
