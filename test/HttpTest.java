import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import http.LocalDateTimeTypeAdapter;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.InMemoryTaskManager;
import service.TaskManager;
import tasks.Epic;
import tasks.Status;
import tasks.Subtask;
import tasks.Task;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class HttpTest {

    TaskManager tm = new InMemoryTaskManager();

    HttpTaskServer taskServer = new HttpTaskServer(tm);

    Gson gson = new GsonBuilder()
            .setPrettyPrinting()
            .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeTypeAdapter())
            .create();

    Gson epicGson = new GsonBuilder()
            .setPrettyPrinting()
            .create();

    public HttpTest() throws IOException {
    }

    @BeforeEach
    public void setUp() throws IOException {
        tm.clearTasks();
        tm.clearSubtasks();
        tm.clearEpics();
        taskServer.start();
    }

    @AfterEach
    public void shutDown() {
        taskServer.stop();
    }

    @Test
    public void testTasksEndpoints() throws IOException, InterruptedException {

        Task task = new Task("Test 2", "Testing task 2",
                LocalDateTime.now(), 5);
        task.setStatus(Status.NEW);

        String taskJson = gson.toJson(task);

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .POST(HttpRequest.BodyPublishers.ofString(taskJson))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(201, response.statusCode());

        List<Task> tasksFromManager = tm.returnTasks();

        assertNotNull(tasksFromManager, "Задачи не возвращаются");
        assertEquals(1, tasksFromManager.size(), "Некорректное количество задач");
        assertEquals("Test 2", tasksFromManager.get(0).getName(), "Некорректное имя задачи");

        Task task1 = new Task("Test 1", "Testing task 1",
                LocalDateTime.now().plus(Duration.ofMinutes(30)), 5);
        task1.setStatus(Status.DONE);
        HttpResponse<String> resp2 = client.send(HttpRequest.newBuilder()
                        .uri(URI.create("http://localhost:8080/tasks/1"))
                        .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(task1)))
                        .build(),
                HttpResponse.BodyHandlers.ofString());
        assertEquals(201, resp2.statusCode());
        assertEquals(1, tm.returnTasks().size());

        HttpResponse<String> resp = client.send(HttpRequest.newBuilder()
                        .uri(URI.create("http://localhost:8080/tasks"))
                        .GET()
                        .build(),
                HttpResponse.BodyHandlers.ofString());
        assertEquals(200, resp.statusCode());

        HttpResponse<String> resp1 = client.send(HttpRequest.newBuilder()
                        .uri(URI.create("http://localhost:8080/tasks/1"))
                        .GET()
                        .build(),
                HttpResponse.BodyHandlers.ofString());
        assertEquals(200, resp1.statusCode());

        HttpResponse<String> resp4 = client.send(HttpRequest.newBuilder()
                        .uri(URI.create("http://localhost:8080/tasks/1"))
                        .DELETE()
                        .build(),
                HttpResponse.BodyHandlers.ofString());
        assertEquals(200, resp4.statusCode());
        assertEquals(0, tm.returnTasks().size());
    }

    @Test
    public void testEpicsEndpoints() throws IOException, InterruptedException {
        Epic epic = new Epic("Test 2", "Testing task 2");
        String epicJson = gson.toJson(epic);

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/epics");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .POST(HttpRequest.BodyPublishers.ofString(epicJson))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(201, response.statusCode());

        List<Epic> epicsFromManager = tm.returnEpics();

        assertNotNull(epicsFromManager, "Задачи не возвращаются");
        assertEquals(1, epicsFromManager.size(), "Некорректное количество задач");
        assertEquals("Test 2", epicsFromManager.get(0).getName(), "Некорректное имя задачи");

        Subtask subtask = new Subtask("Test 1", "Testing task 1",
                LocalDateTime.now(), 5, 1);
        subtask.setStatus(Status.DONE);
        String subtaskJson = gson.toJson(subtask);
        HttpRequest request2 = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/subtasks"))
                .POST(HttpRequest.BodyPublishers.ofString(subtaskJson))
                .build();
        client.send(request2, HttpResponse.BodyHandlers.ofString());

        List<Integer> subtasksFromEpic = tm.returnEpicSubtasks(1);

        assertEquals(2, subtasksFromEpic.get(0));

        HttpResponse<String> resp = client.send(HttpRequest.newBuilder()
                        .uri(URI.create("http://localhost:8080/epics"))
                        .GET()
                        .build(),
                HttpResponse.BodyHandlers.ofString());
        assertEquals(200, resp.statusCode());

        HttpResponse<String> resp1 = client.send(HttpRequest.newBuilder()
                        .uri(URI.create("http://localhost:8080/epics/1"))
                        .GET()
                        .build(),
                HttpResponse.BodyHandlers.ofString());
        assertEquals(200, resp1.statusCode());

        HttpResponse<String> resp2 = client.send(HttpRequest.newBuilder()
                        .uri(URI.create("http://localhost:8080/epics/1"))
                        .POST(HttpRequest.BodyPublishers.ofString(epicJson))
                        .build(),
                HttpResponse.BodyHandlers.ofString());
        assertEquals(201, resp2.statusCode());
        assertEquals(1, tm.returnEpics().size());
        assertEquals(2, tm.returnEpicSubtasks(1).get(0));

        HttpResponse<String> resp3 = client.send(HttpRequest.newBuilder()
                        .uri(URI.create("http://localhost:8080/epics/1/subtasks"))
                        .GET()
                        .build(),
                HttpResponse.BodyHandlers.ofString());
        assertEquals(200, resp3.statusCode());

        HttpResponse<String> resp4 = client.send(HttpRequest.newBuilder()
                        .uri(URI.create("http://localhost:8080/epics/1"))
                        .DELETE()
                        .build(),
                HttpResponse.BodyHandlers.ofString());
        assertEquals(200, resp4.statusCode());
        assertEquals(0, tm.returnEpics().size());
        assertEquals(0, tm.returnSubtasks().size());
    }

    @Test
    public void testSubtasksEndpoints() throws IOException, InterruptedException {
        Epic epic = new Epic("Test 2", "Testing task 2");
        String epicJson = gson.toJson(epic);

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/epics");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .POST(HttpRequest.BodyPublishers.ofString(epicJson))
                .build();
        client.send(request, HttpResponse.BodyHandlers.ofString());

        Subtask subtask = new Subtask("Test 1", "Testing task 1",
                LocalDateTime.now(), 5, 1);
        subtask.setStatus(Status.DONE);
        String subtaskJson = gson.toJson(subtask);
        HttpRequest request2 = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/subtasks"))
                .POST(HttpRequest.BodyPublishers.ofString(subtaskJson))
                .build();
        HttpResponse<String> response2 = client.send(request2, HttpResponse.BodyHandlers.ofString());
        assertEquals(201, response2.statusCode());

        List<Subtask> subtasksFromManager = tm.returnSubtasks();
        List<Integer> subtasksFromEpic = tm.returnEpicSubtasks(1);

        assertNotNull(subtasksFromManager, "Задачи не возвращаются");
        assertEquals(1, subtasksFromManager.size(), "Некорректное количество задач");
        assertEquals("Test 1", subtasksFromManager.get(0).getName(), "Некорректное имя задачи");
        assertEquals(2, subtasksFromEpic.get(0));
        Subtask sub = new Subtask("Test 1", "Testing task 1",
                LocalDateTime.now().plus(Duration.ofMinutes(30)), 5, 1);
        sub.setStatus(Status.DONE);
        HttpResponse<String> resp2 = client.send(HttpRequest.newBuilder()
                        .uri(URI.create("http://localhost:8080/subtasks/2"))
                        .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(sub)))
                        .build(),
                HttpResponse.BodyHandlers.ofString());
        assertEquals(201, resp2.statusCode());
        assertEquals(1, tm.returnSubtasks().size());
        assertEquals(2, tm.returnEpicSubtasks(1).get(0));

        HttpResponse<String> resp = client.send(HttpRequest.newBuilder()
                        .uri(URI.create("http://localhost:8080/subtasks"))
                        .GET()
                        .build(),
                HttpResponse.BodyHandlers.ofString());
        assertEquals(200, resp.statusCode());

        HttpResponse<String> resp1 = client.send(HttpRequest.newBuilder()
                        .uri(URI.create("http://localhost:8080/subtasks/2"))
                        .GET()
                        .build(),
                HttpResponse.BodyHandlers.ofString());
        assertEquals(200, resp1.statusCode());

        HttpResponse<String> resp4 = client.send(HttpRequest.newBuilder()
                        .uri(URI.create("http://localhost:8080/subtasks/2"))
                        .DELETE()
                        .build(),
                HttpResponse.BodyHandlers.ofString());
        assertEquals(200, resp4.statusCode());
        assertEquals(0, tm.returnEpic(1).getSubtaskArrayList().size());
        assertEquals(0, tm.returnSubtasks().size());
    }

    @Test
    public void testCollectionEndpoints() throws IOException, InterruptedException {
        Epic epic = new Epic("Test 1", "Testing task 1");
        String epicJson = gson.toJson(epic);

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/epics");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .POST(HttpRequest.BodyPublishers.ofString(epicJson))
                .build();
        client.send(request, HttpResponse.BodyHandlers.ofString());

        Subtask subtask = new Subtask("Test 2", "Testing task 2",
                LocalDateTime.now(), 5, 1);
        subtask.setStatus(Status.DONE);
        String subtaskJson = gson.toJson(subtask);
        HttpRequest request2 = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/subtasks"))
                .POST(HttpRequest.BodyPublishers.ofString(subtaskJson))
                .build();
        client.send(request2, HttpResponse.BodyHandlers.ofString());

        Task task = new Task("Test 3", "Testing task 3",
                LocalDateTime.now().plus(Duration.ofMinutes(60)), 5);
        task.setStatus(Status.IN_PROGRESS);
        client.send(HttpRequest.newBuilder()
                        .uri(URI.create("http://localhost:8080/tasks"))
                        .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(task)))
                        .build(),
                HttpResponse.BodyHandlers.ofString());

        Task task1 = new Task("Test 4", "Testing task 4",
                LocalDateTime.now().plus(Duration.ofMinutes(30)), 5);
        task1.setStatus(Status.DONE);
        client.send(HttpRequest.newBuilder()
                        .uri(URI.create("http://localhost:8080/tasks"))
                        .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(task1)))
                        .build(),
                HttpResponse.BodyHandlers.ofString());

        HttpResponse<String> resp = client.send(HttpRequest.newBuilder()
                        .uri(URI.create("http://localhost:8080/prioritized"))
                        .GET()
                        .build(),
                HttpResponse.BodyHandlers.ofString());
        assertEquals(200, resp.statusCode());
        assertFalse(tm.getPrioritizedTasks().isEmpty());

        client.send(HttpRequest.newBuilder()
                        .uri(URI.create("http://localhost:8080/subtasks/2"))
                        .GET()
                        .build(),
                HttpResponse.BodyHandlers.ofString());
        client.send(HttpRequest.newBuilder()
                        .uri(URI.create("http://localhost:8080/tasks/4"))
                        .GET()
                        .build(),
                HttpResponse.BodyHandlers.ofString());
        client.send(HttpRequest.newBuilder()
                        .uri(URI.create("http://localhost:8080/tasks/3"))
                        .GET()
                        .build(),
                HttpResponse.BodyHandlers.ofString());
        client.send(HttpRequest.newBuilder()
                        .uri(URI.create("http://localhost:8080/epics/1"))
                        .GET()
                        .build(),
                HttpResponse.BodyHandlers.ofString());

        HttpResponse<String> r = client.send(HttpRequest.newBuilder()
                        .uri(URI.create("http://localhost:8080/history"))
                        .GET()
                        .build(),
                HttpResponse.BodyHandlers.ofString());
        assertEquals(200, r.statusCode());
        assertFalse(tm.getHistory().isEmpty());
    }
}

