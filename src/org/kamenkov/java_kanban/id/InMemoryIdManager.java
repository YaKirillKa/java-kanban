package org.kamenkov.java_kanban.id;

public class InMemoryIdManager implements IdManager {

    private Long lastId;
    private static InMemoryIdManager inMemoryIdManager;

    private InMemoryIdManager() {
        lastId = 0L;
    }

    public static IdManager getInstance() {
        if (inMemoryIdManager == null) {
            inMemoryIdManager = new InMemoryIdManager();
        }
        return inMemoryIdManager;
    }

    @Override
    public Long getLastId() {
        return ++lastId;
    }
}
