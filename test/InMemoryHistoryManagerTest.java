import service.HistoryManager;
import org.junit.jupiter.api.Test;
import tasks.*;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import static service.Managers.getDefaultHistory;

class InMemoryHistoryManagerTest {

    @Test
    void addTask() {
        HistoryManager historyManager = getDefaultHistory();
        Task task = new Task("name1", "desc1");
        task.setId(1);
        historyManager.historyAdd(task);
        Task oldTask = task;
        final List<Task> history = historyManager.getHistory();
        assertEquals(oldTask, history.get(0));
    }

    @Test
    void addTasks() {
        HistoryManager historyManager = getDefaultHistory();
        Task task1 = new Task("name1", "desc1");
        task1.setId(1);
        Task task2 = new Task("name2", "desc2");
        task2.setId(2);
        Task task3 = new Task("name3", "desc3");
        task3.setId(3);
        historyManager.historyAdd(task1);
        historyManager.historyAdd(task2);
        historyManager.historyAdd(task3);
        List<Task> history = historyManager.getHistory();
        assertNotNull(history);
        assertEquals(3, history.size());
    }

    @Test
    void addSameTask() {
        HistoryManager historyManager = getDefaultHistory();
        Task task1 = new Task("name1", "desc1");
        task1.setId(1);
        Task task2 = new Task("name2", "desc2");
        task2.setId(2);
        historyManager.historyAdd(task1);
        historyManager.historyAdd(task2);
        historyManager.historyAdd(task1);
        final List<Task> history = historyManager.getHistory();
        assertEquals(2, history.size());
        assertEquals(task2, history.get(0));
        assertEquals(task1, history.get(1));
    }
}