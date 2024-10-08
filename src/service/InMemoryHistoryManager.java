package service;

import tasks.Task;

import java.util.*;

public class InMemoryHistoryManager implements HistoryManager {

    private CustomLinkedList history = new CustomLinkedList();

    @Override
    public void historyAdd(Task task) {
        if (task == null) {
            return;
        }
        history.addLast(task);
    }

    @Override
    public void historyRemove(int id) {
        history.removeNode(history.nodes.remove(id));
    }

    @Override
    public List<Task> getHistory() {
        return history.getTasks();
    }

    private static class Node {
        public Task data;
        public Node next;
        public Node prev;

        public Node(Node prev, Task data, Node next) {
            this.data = data;
            this.next = next;
            this.prev = prev;
        }
    }

    private class CustomLinkedList {
        private Node head;
        private Node tail;
        private Map<Integer, Node> nodes = new HashMap<>();

        public void addLast(Task task) {
            if (nodes.containsKey(task.getId())) {
                removeNode(nodes.get(task.getId()));
            }

            Node oldTail = tail;
            Node newNode = new Node(tail, task, null);
            tail = newNode;

            if (oldTail != null) {
                oldTail.next = newNode;
            } else {
                head = newNode;
            }
            nodes.put(task.getId(), tail);
        }

        public List<Task> getTasks() {
            Node curHead = head;
            List<Task> tasks = new LinkedList<>();
            while (curHead != null) {
                tasks.add(curHead.data);
                curHead = curHead.next;
            }
            return tasks;
        }

        public void removeNode(Node node) {
            if (node == null) {
                return;
            }

            Node prevNode = node.prev;
            Node nextNode = node.next;

            if ((head.equals(node)) && (tail.equals(node))) {
                head = null;
                tail = null;
            } else if (head.equals(node)) {
                head = head.next;
                head.prev = null;
            } else if (tail.equals(node)) {
                tail = tail.prev;
                tail.next = null;
            } else {
                prevNode.next = node.next;
                nextNode.prev = node.prev;
            }
        }
    }

}