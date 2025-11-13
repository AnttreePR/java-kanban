package todo.manager;

import todo.api.manager.HistoryManager;
import todo.tasks.Task;

import java.util.*;

public class InMemoryHistoryManager implements HistoryManager {
    private final Map<Integer, Node> history = new HashMap<>();

    private Node head;
    private Node tail;


    public void add(Task task) {
        if (task == null) {
            return;
        }

        if (history.containsKey(task.getId())) {
            removeNode(history.get(task.getId()));
        }

        Node newNode = new Node(tail, task, null);

        if (tail == null) {
            head = newNode;
            tail = newNode;
        } else {
            tail.next = newNode;
            tail = newNode;
        }

        history.put(task.getId(), newNode);
    }

    public List<Task> getHistory() {
        List<Task> tasks = new LinkedList<>();
        Node current = head;
        while (current != null) {
            tasks.add(current.data);
            current = current.next;
        }
        return tasks;
    }

    public void remove(int id) {
        Node node = history.remove(id);
        if (node != null) {
            removeNode(node);
        }
    }

    void removeNode(Node node) {
        if (node == null) return;

        Node prev = node.prev;
        Node next = node.next;

        if (prev != null) {
            prev.next = next;
        } else {
            head = next;
        }

        if (next != null) {
            next.prev = prev;
        } else {
            tail = prev;
        }

        node.prev = null;
        node.next = null;
        node.data = null;
    }


    class Node {
        Task data;
        Node prev;
        Node next;

        Node(Node prev, Task data, Node next) {
            this.prev = prev;
            this.next = next;
            this.data = data;
        }

    }
}
