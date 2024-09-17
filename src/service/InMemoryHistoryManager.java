package service;

import tasks.Task;

import java.util.*;

public class InMemoryHistoryManager implements HistoryManager {

    private Map<Integer, Node> nodes = new HashMap<>();
    private CustomLinkedList<Task> history = new CustomLinkedList<>();

    class CustomLinkedList<T> {
        private Node<T> head;
        private Node<T> tail;

        public void addLast(T element) {
            Node<T> oldTail = tail;
            Node<T> newNode = new Node<>(tail, element, null);
            tail = newNode;

            if (oldTail != null) {
                oldTail.next = newNode;
            } else {
                head = newNode;
            }
        }

        public List<T> getTasks() {
            Node<T> curHead = head;
            List<T> tasks = new ArrayList<>();
            while (curHead != null) {
                tasks.add(curHead.data);
                curHead = curHead.next;
            }
            return tasks;
        }
    }

    @Override
    public void historyAdd(Task task) {
        if (task == null) {
            return;
        }
        if (nodes.containsKey(task.getId())) {
            removeNode(nodes.get(task.getId()));
            nodes.remove(task.getId());
        }
        if (!nodes.isEmpty()) {
            int tailID = history.tail.data.getId();
            history.addLast(task);
            nodes.put(task.getId(), history.tail);
            nodes.replace(tailID, history.tail.prev);
        } else {
            history.addLast(task);
            nodes.put(task.getId(), history.tail);
        }

    }

    @Override
    public List<Task> getHistory() {
        return history.getTasks();
    }

    public void removeNode(Node node) {
        Node prevNode = node.prev;
        Node nextNode = node.next;
        if (history.head.equals(node)) {
            history.head = history.head.next;
            history.head.prev = null;
        } else if (history.tail.equals(node)) {
            history.tail = history.tail.prev;
        } else {
            prevNode.next = node.next;
            nextNode.prev = node.prev;
        }
    }
}