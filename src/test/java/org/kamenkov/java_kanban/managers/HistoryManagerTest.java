package org.kamenkov.java_kanban.managers;

import org.junit.jupiter.api.Test;
import org.kamenkov.java_kanban.task.Epic;
import org.kamenkov.java_kanban.task.Subtask;
import org.kamenkov.java_kanban.task.Task;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

abstract class HistoryManagerTest<T extends HistoryManager> {

    T historyManager;

    @Test
    void add() {
        Task task = new Task("Summary", "Description");
        task.setId(1L);
        historyManager.add(task);
        final List<Task> history = historyManager.getHistory();
        assertEquals(1, history.size());
        assertEquals(List.of(task), history);
    }

    @Test
    void addDuplicate() {
        Task task = new Task("Summary", "Description");
        task.setId(1L);
        historyManager.add(task);
        List<Task> history = historyManager.getHistory();
        assertEquals(1, history.size());
        assertEquals(List.of(task), history);
        historyManager.add(task);
        assertEquals(1, history.size());
        assertEquals(List.of(task), history);
    }

    @Test
    void addDuplicateFromMiddle() {
        List<Task> tasks = new ArrayList<>();
        Task task1 = new Task("Summary", "Description");
        task1.setId(1L);
        tasks.add(task1);
        Task task2 = new Task("Summary", "Description");
        task2.setId(2L);
        tasks.add(task2);
        Epic epic = new Epic("Summary", "Description");
        epic.setId(3L);
        tasks.add(epic);
        Subtask subtask = new Subtask("Summary", "Description", 3L);
        subtask.setId(4L);
        tasks.add(subtask);
        for (Task task : tasks) {
            historyManager.add(task);
        }
        List<Task> history = historyManager.getHistory();
        assertEquals(4, history.size());
        assertEquals(tasks, history);
        historyManager.add(task2);
        history = historyManager.getHistory();
        assertEquals(4, history.size());
        assertNotEquals(tasks, history);
        tasks.remove(task2);
        tasks.add(task2);
        assertEquals(tasks, history);
    }

    @Test
    void removeFromBeginning() {
        List<Task> tasks = new ArrayList<>();
        Task task1 = new Task("Summary", "Description");
        task1.setId(1L);
        tasks.add(task1);
        Task task2 = new Task("Summary", "Description");
        task2.setId(2L);
        tasks.add(task2);
        Epic epic = new Epic("Summary", "Description");
        epic.setId(3L);
        tasks.add(epic);
        Subtask subtask = new Subtask("Summary", "Description", 3L);
        subtask.setId(4L);
        tasks.add(subtask);
        for (Task task : tasks) {
            historyManager.add(task);
        }
        List<Task> history = historyManager.getHistory();
        assertEquals(4, history.size());
        assertEquals(tasks, history);
        tasks.remove(task1);
        historyManager.remove(task1.getId());
        history = historyManager.getHistory();
        assertEquals(3, history.size());
        assertEquals(tasks, history);
    }

    @Test
    void removeFromMiddle() {
        List<Task> tasks = new ArrayList<>();
        Task task1 = new Task("Summary", "Description");
        task1.setId(1L);
        tasks.add(task1);
        Task task2 = new Task("Summary", "Description");
        task2.setId(2L);
        tasks.add(task2);
        Epic epic = new Epic("Summary", "Description");
        epic.setId(3L);
        tasks.add(epic);
        Subtask subtask = new Subtask("Summary", "Description", 3L);
        subtask.setId(4L);
        tasks.add(subtask);
        for (Task task : tasks) {
            historyManager.add(task);
        }
        List<Task> history = historyManager.getHistory();
        assertEquals(4, history.size());
        assertEquals(tasks, history);
        tasks.remove(task2);
        historyManager.remove(task2.getId());
        history = historyManager.getHistory();
        assertEquals(3, history.size());
        assertEquals(tasks, history);
    }

    @Test
    void removeFromEnd() {
        List<Task> tasks = new ArrayList<>();
        Task task1 = new Task("Summary", "Description");
        task1.setId(1L);
        tasks.add(task1);
        Task task2 = new Task("Summary", "Description");
        task2.setId(2L);
        tasks.add(task2);
        Epic epic = new Epic("Summary", "Description");
        epic.setId(3L);
        tasks.add(epic);
        Subtask subtask = new Subtask("Summary", "Description", 3L);
        subtask.setId(4L);
        tasks.add(subtask);
        for (Task task : tasks) {
            historyManager.add(task);
        }
        List<Task> history = historyManager.getHistory();
        assertEquals(4, history.size());
        assertEquals(tasks, history);
        tasks.remove(subtask);
        historyManager.remove(subtask.getId());
        history = historyManager.getHistory();
        assertEquals(3, history.size());
        assertEquals(tasks, history);
    }


    @Test
    void getHistory() {
        List<Task> tasks = new ArrayList<>();
        Task task1 = new Task("Summary", "Description");
        task1.setId(1L);
        tasks.add(task1);
        Task task2 = new Task("Summary", "Description");
        task2.setId(2L);
        tasks.add(task2);
        Epic epic = new Epic("Summary", "Description");
        epic.setId(3L);
        tasks.add(epic);
        Subtask subtask = new Subtask("Summary", "Description", 3L);
        subtask.setId(4L);
        tasks.add(subtask);
        Collections.shuffle(tasks);
        for (Task task : tasks) {
            historyManager.add(task);
        }
        List<Task> history = historyManager.getHistory();
        assertEquals(4, history.size());
        assertEquals(tasks, history);
    }

    @Test
    void getHistoryIfEmpty() {
        assertTrue(historyManager.getHistory().isEmpty());
    }
}