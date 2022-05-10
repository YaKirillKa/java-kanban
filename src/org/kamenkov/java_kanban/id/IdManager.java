package org.kamenkov.java_kanban.id;

public interface IdManager {
    /**
     * Returns last available ID.
     *
     * @return {@link Long} ID.
     */
    Long getLastId();
}
