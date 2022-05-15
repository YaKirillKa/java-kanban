package org.kamenkov.java_kanban.managers;

public class Managers {

    private Managers() {}

    public static TaskManager getDefault() {
        return new InMemoryTaskManager();
    }

    public static HistoryManager getDefaultHistoryManager() {
        return InMemoryHistoryManager.getInstance();
    }

    public static IdManager getDefaultIdManager() {
        return InMemoryIdManager.getInstance();
    }
}
