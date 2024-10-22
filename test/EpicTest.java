import service.TaskManager;
import org.junit.jupiter.api.Test;
import tasks.*;

import java.time.LocalDateTime;
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
        Subtask subtask = new Subtask("name1", "desc1",
                (LocalDateTime.of(2024, 10, 17, 14, 26)), 1, 1);
        tm.createSubtask(subtask, Status.NEW);
        tm.deleteSubtask(2);
        assertEquals(0, epic.getSubtaskArrayList().size());
    }

    @Test
    void epicStatus() {
        TaskManager tm = getDefault();
        tm.createEpic(new Epic("1", "2"));
        tm.createSubtask(new Subtask("1", "2",
                (LocalDateTime.of(2021,2, 1, 11, 1)), 5,1), Status.NEW);
        tm.createSubtask(new Subtask("1", "2",
                (LocalDateTime.of(2022,2, 1, 11, 1)), 5,1), Status.NEW);
        assertEquals(tm.returnEpic(1).getStatus(), Status.NEW);
        tm.clearSubtasks();
        tm.createSubtask(new Subtask("1", "2",
                (LocalDateTime.of(2021,2, 1, 11, 1)), 5,1), Status.DONE);
        tm.createSubtask(new Subtask("1", "2",
                (LocalDateTime.of(2022,2, 1, 11, 1)), 5,1), Status.DONE);
        assertEquals(tm.returnEpic(1).getStatus(), Status.DONE);
        tm.clearSubtasks();
        tm.createSubtask(new Subtask("1", "2",
                (LocalDateTime.of(2021,2, 1, 11, 1)), 5,1), Status.NEW);
        tm.createSubtask(new Subtask("1", "2",
                (LocalDateTime.of(2022,2, 1, 11, 1)), 5,1), Status.DONE);
        assertEquals(tm.returnEpic(1).getStatus(), Status.IN_PROGRESS);
        tm.clearSubtasks();
        tm.createSubtask(new Subtask("1", "2",
                (LocalDateTime.of(2021,2, 1, 11, 1)), 5,1), Status.IN_PROGRESS);
        tm.createSubtask(new Subtask("1", "2",
                (LocalDateTime.of(2022,2, 1, 11, 1)), 5,1), Status.IN_PROGRESS);
        assertEquals(tm.returnEpic(1).getStatus(), Status.IN_PROGRESS);

    }
}