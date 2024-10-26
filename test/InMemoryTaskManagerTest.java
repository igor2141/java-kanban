import exception.TimeOverlapException;
import org.junit.jupiter.api.Test;
import service.TaskManager;
import tasks.Epic;
import tasks.Status;
import tasks.Subtask;
import tasks.Task;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static service.Managers.getDefault;

class InMemoryTaskManagerTest {

    @Test
    void createTasks() {
        TaskManager tm = getDefault();
        tm.createTask(new Task("name2", "desc2",
                LocalDateTime.of(2023, 10, 17, 14, 26),5), Status.IN_PROGRESS);
        assertNotNull(tm.returnTask(1));

        tm.createEpic(new Epic("name4", "desc4"));
        assertNotNull(tm.returnEpic(2));

        tm.createSubtask(new Subtask("name7", "desc7",
                (LocalDateTime.of(2024, 10, 17, 14, 26)), 1,2), Status.IN_PROGRESS);
        assertNotNull(tm.returnSubtask(3));
    }

    @Test
    void manualIdSet() {
        TaskManager tm = getDefault();
        tm.createTask(new Task("name2", "desc2",
                LocalDateTime.of(2023, 10, 17, 14, 26), 5), Status.IN_PROGRESS);
        Task task = new Task("name1", "desc2",
                LocalDateTime.of(2024, 10, 17, 14, 26), 6);
        task.setId(1);
        tm.createTask(task, Status.NEW);
        assertNotEquals(1, tm.returnTask(2).getId());
    }

    @Test
    void creationTest() {
        TaskManager tm = getDefault();
        Epic epic = new Epic("name3", "desc3");
        tm.createEpic(epic);
        assertEquals(epic, tm.returnEpic(1));
    }

    @Test
    void legacyTests() {
        try {
        TaskManager tm = getDefault();
        tm.createTask(new Task("name1", "desc1",
                LocalDateTime.of(2023, 10, 17, 14, 26), 60), Status.NEW);
        tm.createTask(new Task("name2", "desc2",
                LocalDateTime.of(2023, 10, 17, 15, 26), 60), Status.IN_PROGRESS);
        tm.createEpic(new Epic("name3", "desc3"));
        tm.createEpic(new Epic("name4", "desc4"));
        tm.createSubtask(new Subtask("name5", "desc5",
                LocalDateTime.of(2023, 10, 17, 16, 26), 60,3), Status.NEW);
        tm.createSubtask(new Subtask("name5", "desc5",
                    LocalDateTime.of(2023, 10, 17, 16, 56), 60, 3), Status.NEW);

        assertEquals(1, tm.returnSubtasks().size());

        tm.createSubtask(new Subtask("name6", "desc6",
                LocalDateTime.of(2023, 10, 17, 17, 26), 60, 3), Status.NEW);
        tm.createSubtask(new Subtask("name7", "desc7",
                LocalDateTime.of(2023, 10, 17, 18, 26), 60, 4), Status.IN_PROGRESS);
        assertEquals(5, tm.getPrioritizedTasks().size());

        Task test1 = new Task("name11", "desc11",
                LocalDateTime.of(2023, 10, 17, 14, 26), 60);
        test1.setId(1);
        test1.setStatus(Status.DONE);
        tm.updateTask(test1);
        assertEquals(tm.returnTasks().get(0), test1);

        Subtask test2 = new Subtask("name55", "desc55",
                LocalDateTime.of(2023, 10, 17, 16, 26), 60,3);
        test2.setId(5);
        test2.setStatus(Status.DONE);
        tm.updateSubtask(test2);
        assertEquals(tm.returnSubtasks().get(0), test2);

        Epic test3 = new Epic("name33", "desc33");
        test3.setId(3);
        tm.updateEpic(test3);
        assertEquals(tm.returnEpics().get(0), test3);

        assertNotNull(tm.returnTask(1));
        assertNotNull(tm.returnEpic(3));
        assertNotNull(tm.returnSubtask(5));

        tm.deleteTask(1);
        assertEquals(1, tm.returnTasks().size());
        tm.deleteSubtask(7);
        assertEquals(2, tm.returnSubtasks().size());
        tm.deleteEpic(3);
        assertEquals(1, tm.returnEpics().size());
        assertTrue(tm.returnSubtasks().isEmpty());

        tm.clearTasks();
        assertTrue(tm.returnTasks().isEmpty());
        tm.clearSubtasks();
        assertTrue(tm.returnSubtasks().isEmpty());
        tm.clearEpics();
        assertTrue(tm.returnEpics().isEmpty());
        } catch (TimeOverlapException e) {
            e.printStackTrace();
        }
    }
}