import org.junit.jupiter.api.Test;
import service.FileBackedTaskManager;
import service.ManagerSaveException;
import service.TaskManager;
import tasks.Epic;
import tasks.Status;
import tasks.Subtask;
import tasks.Task;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static service.Managers.loadFromFile;

class FileTest {

    @Test
    void emptyFile() throws IOException {
        File f = File.createTempFile("test", ".csv");
        FileBackedTaskManager tm = loadFromFile(f);
        tm.save();
    }

    @Test
    void writeTest() throws IOException {
        File f = File.createTempFile("test", ".csv");
        TaskManager tm = new FileBackedTaskManager(f);
        tm.createTask(new Task("name1", "desc1"), Status.NEW);
        tm.createEpic(new Epic("name2", "desc2"));
        tm.createSubtask(new Subtask("name3", "desc3", 2), Status.IN_PROGRESS);
        List<String> file = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(f))) {
            while (br.ready()) {
                file.add(br.readLine());
            }
        } catch (IOException e) {
            throw new ManagerSaveException();
        }
        assertEquals(file.get(0), "1,TASK,name1,NEW,desc1");
        assertEquals(file.get(1), "2,EPIC,name2,IN_PROGRESS,desc2");
        assertEquals(file.get(2), "3,SUBTASK,name3,IN_PROGRESS,desc3,2");
    }

    @Test
    void readTest() throws IOException {
        File f = File.createTempFile("test", ".csv");
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(f))) {
            bw.write("""
                    1,TASK,name1,NEW,desc1
                    2,EPIC,name2,IN_PROGRESS,desc2
                    3,SUBTASK,name3,IN_PROGRESS,desc3,2""");
        } catch (IOException e) {
            throw new ManagerSaveException();
        }
        TaskManager tm = loadFromFile(f);
        assertNotNull(tm.returnTask(1));
        assertNotNull(tm.returnEpic(2));
        assertNotNull(tm.returnSubtask(3));
    }
}
