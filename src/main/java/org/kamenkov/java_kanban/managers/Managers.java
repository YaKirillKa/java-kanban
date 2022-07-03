package org.kamenkov.java_kanban.managers;

public class Managers {

    private Managers() {}

    public static TaskManager getDefault() {
        return new FileBackedTasksManager();
    }

    public static HistoryManager getDefaultHistoryManager() {
        return new InMemoryHistoryManager();
    }

    public static IdManager getDefaultIdManager() {
        return new InMemoryIdManager();
    }
}
