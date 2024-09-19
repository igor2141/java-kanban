package service;

import tasks.Task;

import java.util.List;

public interface HistoryManager {
    void historyAdd(Task task);

    void historyRemove(int id);

    List<Task> getHistory();
}
