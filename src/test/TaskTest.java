package test;

import service.TaskManager;
import org.junit.jupiter.api.Test;
import tasks.*;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import static service.Managers.getDefault;

class TaskTest {

    @Test
    void addNewTask() {
        TaskManager tm = getDefault();
        Task task = new Task("name1", "desc1");
        tm.createTask(task, Status.NEW);
        Task savedTask = tm.returnTask(1);
        assertNotNull(savedTask);
        assertEquals(task, savedTask);

        final List<Task> tasks = tm.returnTasks();
        assertNotNull(tasks);
        assertEquals(1, tasks.size());
        assertEquals(task, tasks.get(0));
    }
}