package service;

import tasks.Task;

import java.util.ArrayList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {

    private List<Task> history = new ArrayList<>();

    @Override
    public void historyAdd(Task task) {
        historyUpdateCheck();
        history.add(task);
    }

    @Override
    public List<Task> getHistory() {
        return new ArrayList<>(history);
    }

    private void historyUpdateCheck() {
        if (history.size() > 9) {
            history.remove(0);
        }
    }
}
