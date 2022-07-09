package org.kamenkov.java_kanban.managers;

import org.junit.jupiter.api.BeforeEach;

class InMemoryHistoryManagerTest extends HistoryManagerTest<InMemoryHistoryManager> {

    @BeforeEach
    void beforeEach() {
        historyManager = new InMemoryHistoryManager();
    }

}