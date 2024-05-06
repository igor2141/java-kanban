package test;

import org.junit.jupiter.api.Test;
import tasks.*;
import java.util.ArrayList;
import static org.junit.jupiter.api.Assertions.*;

class EpicTest {

    @Test
    void addNewEpic() {
        TaskManager tm = new InMemoryTaskManager();
        Epic epic = new Epic("name3", "desc3");
        tm.createEpic(epic);
        Epic savedEpic = tm.returnEpic(1);

        assertNotNull(savedEpic);
        assertEquals(epic, savedEpic);

        final ArrayList<Epic> tasks = tm.returnEpics();

        assertNotNull(tasks);
        assertEquals(1, tasks.size());
        assertEquals(epic, tasks.get(0));


    }


}