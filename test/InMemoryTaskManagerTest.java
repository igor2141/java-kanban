import service.TaskManager;
import org.junit.jupiter.api.Test;
import tasks.*;
import static org.junit.jupiter.api.Assertions.*;
import static service.Managers.getDefault;

class InMemoryTaskManagerTest {

    @Test
    void createTasks() {
        TaskManager tm = getDefault();
        tm.createTask(new Task("name2", "desc2"), Status.IN_PROGRESS);
        assertNotNull(tm.returnTask(1));

        tm.createEpic(new Epic("name4", "desc4"));
        assertNotNull(tm.returnEpic(2));

        tm.createSubtask(new Subtask("name7", "desc7", 2), Status.IN_PROGRESS);
        assertNotNull(tm.returnSubtask(3));
    }

    @Test
    void manualIdSet() {
        TaskManager tm = getDefault();
        tm.createTask(new Task("name2", "desc2"), Status.IN_PROGRESS);
        Task task = new Task("name1", "desc2");
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
        TaskManager tm = getDefault();

        tm.createTask(new Task("name1", "desc1"), Status.NEW);
        tm.createTask(new Task("name2", "desc2"), Status.IN_PROGRESS);
        tm.createEpic(new Epic("name3", "desc3"));
        tm.createEpic(new Epic("name4", "desc4"));
        tm.createSubtask(new Subtask("name5", "desc5", 3), Status.NEW);
        tm.createSubtask(new Subtask("name6", "desc6", 3), Status.NEW);
        tm.createSubtask(new Subtask("name7", "desc7", 4), Status.IN_PROGRESS);

        Task test1 = new Task("name11", "desc11");
        test1.setId(1);
        test1.setStatus(Status.DONE);
        tm.updateTask(test1);
        assertEquals(tm.returnTask(1).getStatus(), Status.DONE);

        Subtask test2 = new Subtask("name55", "desc55", 3);
        test2.setId(5);
        test2.setStatus(Status.DONE);
        tm.updateSubtask(test2);
        assertEquals(tm.returnSubtask(5).getStatus(), Status.DONE);

        Subtask test3 = new Subtask("name66", "desc66", 3);
        test3.setId(6);
        test3.setStatus(Status.DONE);
        tm.updateSubtask(test3);

        Epic test4 = new Epic("name33", "desc33");
        test4.setId(3);
        test4.getSubtaskArrayList().add(55);
        test4.getSubtaskArrayList().add(66);
        tm.updateEpic(test4);
        assertEquals(tm.returnEpic(3).getStatus(), Status.DONE);

        tm.deleteSubtask(7);
        assertNull(tm.returnSubtask(7));

        tm.deleteEpic(3);
        assertNull(tm.returnEpic(3));
    }
}