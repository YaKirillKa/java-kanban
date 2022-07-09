package org.kamenkov.java_kanban.managers;

import org.kamenkov.java_kanban.task.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InMemoryHistoryManager implements HistoryManager {

    private final Map<Long, Node> nodeMap = new HashMap<>();
    private Node first;
    private Node last;

    @Override
    public void add(Task task) {
        if (nodeMap.containsKey(task.getId())) {
            remove(task.getId());
        }
        linkLast(task);
    }

    @Override
    public void remove(Long id) {
        Node node = nodeMap.remove(id);
        if (node != null) {
            removeNode(node);
        }
    }

    @Override
    public List<Task> getHistory() {
        List<Task> taskHistory = new ArrayList<>();
        if (first != null) {
            for (Node node = first; node != null; node = node.next) {
                taskHistory.add(node.data);
            }
        }
        return taskHistory;
    }

    /**
     * Appends the {@link Task} to the end of this list.
     *
     * @param task which should be added to the list.
     */
    private void linkLast(Task task) {
        final Node oldTail = last;
        final Node newNode = new Node(oldTail, task, null);
        nodeMap.put(task.getId(), newNode);
        last = newNode;
        if (oldTail == null) {
            first = newNode;
        } else {
            oldTail.next = newNode;
        }
    }

    /**
     * Removes the {@link Node} from the list.
     *
     * @param node which will be unlinked from the list.
     */
    private void removeNode(Node node) {
        final Node prev = node.prev;
        final Node next = node.next;

        if (prev == null) {
            first = next;
        } else {
            prev.next = next;
        }

        if (next == null) {
            last = prev;
        } else {
            next.prev = next;
        }
    }

    private static class Node {
        Task data;
        Node next;
        Node prev;

        public Node(Node prev, Task data, Node next) {
            this.data = data;
            this.next = next;
            this.prev = prev;
        }
    }

}
