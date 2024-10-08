import service.TaskManager;
import org.junit.jupiter.api.Test;
import tasks.*;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import static service.Managers.getDefault;

class EpicTest {

    @Test
    void addNewEpic() {
        TaskManager tm = getDefault();
        Epic epic = new Epic("name3", "desc3");
        tm.createEpic(epic);
        Epic savedEpic = tm.returnEpic(1);
        assertNotNull(savedEpic);
        assertEquals(epic, savedEpic);

        final List<Epic> tasks = tm.returnEpics();
        assertNotNull(tasks);
        assertEquals(1, tasks.size());
        assertEquals(epic, tasks.get(0));
    }

    @Test
    void deleteSubtask() {
        TaskManager tm = getDefault();
        Epic epic = new Epic("name3", "desc3");
        tm.createEpic(epic);
        Subtask subtask = new Subtask("name1", "desc1", 1);
        tm.createSubtask(subtask, Status.NEW);
        tm.deleteSubtask(2);
        assertEquals(0, epic.getSubtaskArrayList().size());
    }
}