import service.TaskManager;
import org.junit.jupiter.api.Test;
import tasks.*;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import static service.Managers.getDefault;

class SubtaskTest {

    @Test
    void addNewSubtask() {
        TaskManager tm = getDefault();
        Epic epic = new Epic("name3", "desc3");
        tm.createEpic(epic);
        Subtask subtask = new Subtask("name5", "desc5", 1);
        tm.createSubtask(subtask, Status.NEW);
        Task savedTask = tm.returnSubtask(2);
        assertNotNull(savedTask);
        assertEquals(subtask, savedTask);

        final List<Subtask> tasks = tm.returnSubtasks();
        assertNotNull(tasks);
        assertEquals(1, tasks.size());
        assertEquals(subtask, tasks.get(0));
    }
}