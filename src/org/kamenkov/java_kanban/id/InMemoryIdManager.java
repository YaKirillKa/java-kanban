package org.kamenkov.java_kanban.id;

public class InMemoryIdManager implements IdManager {

    private Long lastId;
    private static InMemoryIdManager inMemoryIdManager;

    /**
     * Private constructor to avoid duplicating of the manager.
     */
    private InMemoryIdManager() {
        lastId = 0L;
    }

    /**
     * Returns instance of {@link InMemoryIdManager}.
     * If it doesn't exist, creates.
     *
     * @return {@link InMemoryIdManager}.
     */
    public static IdManager getInstance() {
        if (inMemoryIdManager == null) {
            inMemoryIdManager = new InMemoryIdManager();
        }
        return inMemoryIdManager;
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
