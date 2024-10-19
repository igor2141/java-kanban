import org.junit.jupiter.api.Test;
import service.TaskManager;
import tasks.Epic;
import tasks.Status;
import tasks.Subtask;
import tasks.Task;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static service.Managers.getDefault;

class SubtaskTest {

    @Test
    void addNewSubtask() {
        TaskManager tm = getDefault();
        Epic epic = new Epic("name3", "desc3");
        tm.createEpic(epic);
        Subtask subtask = new Subtask("name5", "desc5",
                (LocalDateTime.of(2024, 10, 17, 14, 26)), 1,1);
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