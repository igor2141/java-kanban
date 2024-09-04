import service.HistoryManager;
import org.junit.jupiter.api.Test;
import tasks.*;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import static service.Managers.getDefaultHistory;

class InMemoryHistoryManagerTest {

    @Test
    void addTasks() {
        HistoryManager historyManager = getDefaultHistory();
        Task task = new Task("name1", "desc1");
        historyManager.historyAdd(task);
        Task oldTask = task;
        task = new Task("name11", "desc11");
        historyManager.historyAdd(task);
        final List<Task> history = historyManager.getHistory();
        assertEquals(oldTask, history.get(0));
    }

    @Test
    void addTask() {
        HistoryManager historyManager = getDefaultHistory();
        Task task = new Task("name1", "desc1");
        historyManager.historyAdd(task);
        final List<Task> history = historyManager.getHistory();
        assertNotNull(history);
        assertEquals(1, history.size());
    }

    @Test
    void addSameTask() {
        HistoryManager historyManager = getDefaultHistory();
        Task task1 = new Task("name1", "desc1");
        Task task2 = new Task("name2", "desc2");
        historyManager.historyAdd(task1);
        historyManager.historyAdd(task2);
        historyManager.historyAdd(task1);
        final List<Task> history = historyManager.getHistory();
        assertEquals(2, history.size());
        assertEquals(task2, history.get(0));
    }
}