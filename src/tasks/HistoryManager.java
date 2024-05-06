package tasks;

import java.util.ArrayList;

public interface HistoryManager {
    void historyAdd(Task task);

    ArrayList<Task> getHistory();
}
