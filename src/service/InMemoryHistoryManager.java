package service;

import tasks.Task;

import java.util.*;

public class InMemoryHistoryManager implements HistoryManager {

    Set<Task> history = new LinkedHashSet<>();

    @Override
    public void historyAdd(Task task) {
        if (history.contains(task)) {
            history.remove(task);
        }
        history.add(task);
    }

    @Override
    public List<Task> getHistory() {
        return new ArrayList<>(history);
    }

}