import org.junit.jupiter.api.Test;
import service.HistoryManager;
import tasks.Epic;
import tasks.Task;

import java.time.LocalDateTime;
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
        Task task1 = new Task("name1", "desc1",
                LocalDateTime.of(2024, 11, 18, 15, 36), 5);
        task1.setId(1);
        Task task2 = new Task("name2", "desc2",
                LocalDateTime.of(2024, 12, 18, 18, 36), 5);
        task2.setId(2);
        historyManager.historyAdd(task1);
        historyManager.historyAdd(task2);
        historyManager.historyAdd(task1);
        final List<Task> history = historyManager.getHistory();
        assertEquals(2, history.size());
        assertEquals(task2, history.get(0));
        assertEquals(task1, history.get(1));
    }

    @Test
    void historyTest() {
        HistoryManager hm = getDefaultHistory();
        assertTrue(hm.getHistory().isEmpty());
        Epic epic1 = new Epic("1", "2");
        epic1.setId(1);
        hm.historyAdd(epic1);
        Epic epic2 = new Epic("2", "2");
        epic2.setId(2);
        hm.historyAdd(epic2);
        Epic epic3 = new Epic("3", "2");
        epic3.setId(3);
        hm.historyAdd(epic3);
        Epic epic4 = new Epic("4", "2");
        epic4.setId(4);
        hm.historyAdd(epic4);
        Epic epic5 = new Epic("5", "2");
        epic5.setId(5);
        hm.historyAdd(epic5);
        Epic epic6 = new Epic("6", "2");
        epic6.setId(6);
        hm.historyAdd(epic6);
        Epic epic7 = new Epic("7", "2");
        epic7.setId(7);
        hm.historyAdd(epic7);
        Epic epic8 = new Epic("8", "2");
        epic8.setId(8);
        hm.historyAdd(epic8);
        Epic epic9 = new Epic("9", "2");
        epic9.setId(9);
        hm.historyAdd(epic9);
        Epic epic10 = new Epic("10", "2");
        epic10.setId(10);
        hm.historyAdd(epic10);
        hm.historyRemove(1);
        hm.historyRemove(5);
        hm.historyRemove(10);
        final List<Task> history = hm.getHistory();
        assertEquals(7, history.size());
    }
}