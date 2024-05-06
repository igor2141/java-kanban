package test;

import org.junit.jupiter.api.Test;
import tasks.*;
import java.util.ArrayList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class TaskTest {



    @Test
    void addNewTask() {
        TaskManager tm = new InMemoryTaskManager();
        Task task = new Task("name1", "desc1");
        tm.createTask(task, Status.NEW);
        Task savedTask = tm.returnTask(1);

        assertNotNull(savedTask);
        assertEquals(task, savedTask);

        final ArrayList<Task> tasks = tm.returnTasks();

        assertNotNull(tasks);
        assertEquals(1, tasks.size());
        assertEquals(task, tasks.get(0));
    }
}