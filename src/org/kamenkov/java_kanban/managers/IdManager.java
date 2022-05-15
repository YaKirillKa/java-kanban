package org.kamenkov.java_kanban.managers;

public interface IdManager {
    /**
     * Returns last available ID.
     *
     * @return {@link Long} ID.
     */
    Long getLastId();
}
