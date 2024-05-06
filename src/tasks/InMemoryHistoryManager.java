package tasks;

import java.util.ArrayList;

public class InMemoryHistoryManager implements HistoryManager {

    private ArrayList<Task> history = new ArrayList<>();

    @Override
    public void historyAdd(Task task) {
        historyUpdateCheck();
        history.add(task);
    }

    @Override
    public ArrayList<Task> getHistory() {
        return history;
    }

    private void historyUpdateCheck() {
        if (history.size() > 9) {
            history.remove(0);
        }
    }
}
