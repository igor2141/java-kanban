package http;

import com.google.gson.*;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import exception.NotFoundException;
import exception.TimeOverlapException;
import service.TaskManager;
import tasks.Epic;
import tasks.Status;
import tasks.Subtask;
import tasks.Task;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;

public class TaskHandler implements HttpHandler {
    private final TaskManager tm;
    final String name = "name";
    final String description = "description";
    final String startTime = "startTime";
    final String duration = "duration";
    final String status = "status";
    final String epicID = "epicID";

    public TaskHandler(TaskManager tm) {
        this.tm = tm;
    }

    private static final Gson gson = new GsonBuilder()
            .setPrettyPrinting()
            .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeTypeAdapter())
            .create();

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        Endpoint endpoint = getEndpoint(httpExchange.getRequestURI().getPath(), httpExchange.getRequestMethod());

        switch (endpoint) {
            case GET_TASKS -> {
                handleGetTasks(httpExchange);
                break;
            }
            case GET_TASK -> {
                handleGetTask(httpExchange);
                break;
            }
            case POST_TASK -> {
                handlePostTask(httpExchange);
                break;
            }
            case POST_UPDATED_TASK -> {
                handlePostUpdatedTask(httpExchange);
                break;
            }
            case DELETE_TASK -> {
                handleDeleteTask(httpExchange);
                break;
            }
            case GET_SUBTASKS -> {
                handleGetSubtasks(httpExchange);
                break;
            }
            case GET_SUBTASK -> {
                handleGetSubtask(httpExchange);
                break;
            }
            case POST_SUBTASK -> {
                handlePostSubtask(httpExchange);
                break;
            }
            case POST_UPDATED_SUBTASK -> {
                handlePostUpdatedSubtask(httpExchange);
                break;
            }
            case DELETE_SUBTASK -> {
                handleDeleteSubtask(httpExchange);
                break;
            }
            case GET_EPICS -> {
                handleGetEpics(httpExchange);
                break;
            }
            case GET_EPIC -> {
                handleGetEpic(httpExchange);
                break;
            }
            case POST_EPIC -> {
                handlePostEpic(httpExchange);
                break;
            }
            case POST_UPDATED_EPIC -> {
                handlePostUpdatedEpic(httpExchange);
                break;
            }
            case DELETE_EPIC -> {
                handleDeleteEpic(httpExchange);
                break;
            }
            case GET_EPIC_SUBTASKS -> {
                handleGetEpicSubtasks(httpExchange);
                break;
            }
            case GET_HISTORY -> {
                handleGetHistory(httpExchange);
                break;
            }
            case GET_PRIORITIZED -> {
                handleGetPrioritized(httpExchange);
                break;
            }
            default -> {
                httpExchange.sendResponseHeaders(404, 0);
                httpExchange.close();
                break;
            }
        }
    }

    private void handleGetTasks(HttpExchange httpExchange) throws IOException {
        try {
            sendText(httpExchange, gson.toJson(tm.returnTasks()));
        } catch (Exception e) {
            httpExchange.sendResponseHeaders(500, 0);
        } finally {
            httpExchange.close();
        }
    }

    private void handleGetTask(HttpExchange httpExchange) throws IOException {
        try {
            sendText(httpExchange, gson.toJson(tm.returnTask(pathId(httpExchange))));
        } catch (NotFoundException e) {
            httpExchange.sendResponseHeaders(404, 0);
        } catch (Exception e) {
            httpExchange.sendResponseHeaders(500, 0);
        } finally {
            httpExchange.close();
        }
    }

    private void handlePostTask(HttpExchange httpExchange) throws IOException {
        try {
            JsonElement jsonElement = JsonParser.parseString(getBodyFromRequest(httpExchange));
            JsonObject jsonObject = jsonElement.getAsJsonObject();
            tm.createTask(new Task(jsonObject.get(name).getAsString(),
                            jsonObject.get(description).getAsString(),
                            LocalDateTime.parse(jsonObject.get(startTime).getAsString()),
                            jsonObject.get(duration).getAsInt()),
                    Status.valueOf(jsonObject.get(status).getAsString()));
            httpExchange.sendResponseHeaders(201,0);
        } catch (TimeOverlapException e) {
            httpExchange.sendResponseHeaders(406, 0);
        } catch (Exception e) {
            httpExchange.sendResponseHeaders(500, 0);
        } finally {
            httpExchange.close();
        }
    }

    private void handlePostUpdatedTask(HttpExchange httpExchange) throws IOException {
        try {
            JsonElement jsonElement = JsonParser.parseString(getBodyFromRequest(httpExchange));
            JsonObject jsonObject = jsonElement.getAsJsonObject();
            Task task = new Task(jsonObject.get(name).getAsString(),
                    jsonObject.get(description).getAsString(),
                    LocalDateTime.parse(jsonObject.get(startTime).getAsString()),
                    jsonObject.get(duration).getAsInt());
            task.setId(pathId(httpExchange));
            task.setStatus(Status.valueOf(jsonObject.get(status).getAsString()));
            tm.updateTask(task);
            httpExchange.sendResponseHeaders(201, 0);
        }  catch (NotFoundException e) {
            httpExchange.sendResponseHeaders(404, 0);
        } catch (TimeOverlapException e) {
            httpExchange.sendResponseHeaders(406, 0);
        } catch (Exception e) {
            httpExchange.sendResponseHeaders(500, 0);
        } finally {
            httpExchange.close();
        }
    }

    private void handleDeleteTask(HttpExchange httpExchange) throws IOException {
        try {
            tm.deleteTask(pathId(httpExchange));
            httpExchange.sendResponseHeaders(200, 0);
        } catch (Exception e) {
            httpExchange.sendResponseHeaders(500, 0);
        } finally {
            httpExchange.close();
        }
    }

    private void handleGetSubtasks(HttpExchange httpExchange) throws IOException {
        try {
            sendText(httpExchange, gson.toJson(tm.returnSubtasks()));
        } catch (Exception e) {
            httpExchange.sendResponseHeaders(500, 0);
        } finally {
            httpExchange.close();
        }
    }

    private void handleGetSubtask(HttpExchange httpExchange) throws IOException {
        try {
            sendText(httpExchange, gson.toJson(tm.returnSubtask(pathId(httpExchange))));
        } catch (NotFoundException e) {
            httpExchange.sendResponseHeaders(404, 0);
        } catch (Exception e) {
            httpExchange.sendResponseHeaders(500, 0);
        } finally {
            httpExchange.close();
        }
    }

    private void handlePostSubtask(HttpExchange httpExchange) throws IOException {
        try {
            JsonElement jsonElement = JsonParser.parseString(getBodyFromRequest(httpExchange));
            JsonObject jsonObject = jsonElement.getAsJsonObject();
            tm.createSubtask(new Subtask(jsonObject.get(name).getAsString(),
                            jsonObject.get(description).getAsString(),
                            LocalDateTime.parse(jsonObject.get(startTime).getAsString()),
                            jsonObject.get(duration).getAsInt(),
                            jsonObject.get(epicID).getAsInt()),
                    Status.valueOf(jsonObject.get(status).getAsString()));
            httpExchange.sendResponseHeaders(201, 0);
        } catch (TimeOverlapException e) {
            httpExchange.sendResponseHeaders(406, 0);
        } catch (Exception e) {
            httpExchange.sendResponseHeaders(500, 0);
        } finally {
            httpExchange.close();
        }
    }

    private void handlePostUpdatedSubtask(HttpExchange httpExchange) throws IOException {
        try {
            JsonElement jsonElement = JsonParser.parseString(getBodyFromRequest(httpExchange));
            JsonObject jsonObject = jsonElement.getAsJsonObject();
            Subtask subtask = new Subtask(jsonObject.get(name).getAsString(),
                    jsonObject.get(description).getAsString(),
                    LocalDateTime.parse(jsonObject.get(startTime).getAsString()),
                    jsonObject.get(duration).getAsInt(),
                    jsonObject.get(epicID).getAsInt());
            subtask.setId(pathId(httpExchange));
            subtask.setStatus(Status.valueOf(jsonObject.get(status).getAsString()));
            tm.updateSubtask(subtask);
            httpExchange.sendResponseHeaders(201, 0);
        }  catch (NotFoundException e) {
            httpExchange.sendResponseHeaders(404, 0);
        } catch (TimeOverlapException e) {
            httpExchange.sendResponseHeaders(406, 0);
        } catch (Exception e) {
            httpExchange.sendResponseHeaders(500, 0);
        } finally {
            httpExchange.close();
        }
    }

    private void handleDeleteSubtask(HttpExchange httpExchange) throws IOException {
        try {
            tm.deleteSubtask(pathId(httpExchange));
            httpExchange.sendResponseHeaders(200, 0);
        } catch (Exception e) {
            httpExchange.sendResponseHeaders(500, 0);
        } finally {
            httpExchange.close();
        }
    }

    private void handleGetEpics(HttpExchange httpExchange) throws IOException {
        try {
            sendText(httpExchange, gson.toJson(tm.returnEpics()));
        } catch (Exception e) {
            httpExchange.sendResponseHeaders(500, 0);
        } finally {
            httpExchange.close();
        }
    }

    private void handleGetEpic(HttpExchange httpExchange) throws IOException {
        try {
            sendText(httpExchange, gson.toJson(tm.returnEpic(pathId(httpExchange))));
        } catch (NotFoundException e) {
            httpExchange.sendResponseHeaders(404, 0);
        } catch (Exception e) {
            httpExchange.sendResponseHeaders(500, 0);
        } finally {
            httpExchange.close();
        }
    }

    private void handlePostEpic(HttpExchange httpExchange) throws IOException {
        try {
            JsonElement jsonElement = JsonParser.parseString(getBodyFromRequest(httpExchange));
            JsonObject jsonObject = jsonElement.getAsJsonObject();
            tm.createEpic(new Epic(jsonObject.get(name).getAsString(),
                    jsonObject.get(description).getAsString()));
            httpExchange.sendResponseHeaders(201, 0);
        } catch (Exception e) {
            httpExchange.sendResponseHeaders(500, 0);
        } finally {
            httpExchange.close();
        }
    }

    private void handlePostUpdatedEpic(HttpExchange httpExchange) throws IOException {
        try {
            JsonElement jsonElement = JsonParser.parseString(getBodyFromRequest(httpExchange));
            JsonObject jsonObject = jsonElement.getAsJsonObject();
            Epic epic = new Epic(jsonObject.get(name).getAsString(),
                    jsonObject.get(description).getAsString());
            epic.setId((pathId(httpExchange)));
            tm.updateEpic(epic);
            httpExchange.sendResponseHeaders(201, 0);
        } catch (NotFoundException e) {
            httpExchange.sendResponseHeaders(404, 0);
        } catch (Exception e) {
            httpExchange.sendResponseHeaders(500, 0);
        } finally {
            httpExchange.close();
        }
    }

    private void handleDeleteEpic(HttpExchange httpExchange) throws IOException {
        try {
            tm.deleteEpic(pathId(httpExchange));
            httpExchange.sendResponseHeaders(200, 0);
        } catch (Exception e) {
            httpExchange.sendResponseHeaders(500, 0);
        } finally {
            httpExchange.close();
        }
    }

    private void handleGetEpicSubtasks(HttpExchange httpExchange) throws IOException {
        try {
            sendText(httpExchange, gson.toJson(tm.returnEpicSubtasks(pathId(httpExchange))));
        } catch (NotFoundException e) {
            httpExchange.sendResponseHeaders(404, 0);
        } catch (Exception e) {
            httpExchange.sendResponseHeaders(500, 0);
        } finally {
            httpExchange.close();
        }
    }

    private void handleGetHistory(HttpExchange httpExchange) throws IOException {
        try {
            sendText(httpExchange, gson.toJson(tm.getHistory()));
        } catch (Exception e) {
            httpExchange.sendResponseHeaders(500, 0);
        } finally {
            httpExchange.close();
        }
    }

    private void handleGetPrioritized(HttpExchange httpExchange) throws IOException {
        try {
            sendText(httpExchange, gson.toJson(tm.getPrioritizedTasks()));
        } catch (Exception e) {
            httpExchange.sendResponseHeaders(500, 0);
        } finally {
            httpExchange.close();
        }
    }

    private Endpoint getEndpoint(String requestPath, String requestMethod) {
        String[] pathParts = requestPath.split("/");

        if (pathParts.length == 2 && pathParts[1].equals("tasks")) {
            if (requestMethod.equals("GET")) {
                return Endpoint.GET_TASKS;
            }
            if (requestMethod.equals("POST")) {
                return Endpoint.POST_TASK;
            }
        }
        if (pathParts.length == 3 && pathParts[1].equals("tasks")) {
            if (requestMethod.equals("GET")) {
                return Endpoint.GET_TASK;
            }
            if (requestMethod.equals("POST")) {
                return Endpoint.POST_UPDATED_TASK;
            }
            if (requestMethod.equals("DELETE")) {
                return Endpoint.DELETE_TASK;
            }
        }
        if (pathParts.length == 2 && pathParts[1].equals("subtasks")) {
            if (requestMethod.equals("GET")) {
                return Endpoint.GET_SUBTASKS;
            }
            if (requestMethod.equals("POST")) {
                return Endpoint.POST_SUBTASK;
            }
        }
        if (pathParts.length == 3 && pathParts[1].equals("subtasks")) {
            if (requestMethod.equals("GET")) {
                return Endpoint.GET_SUBTASK;
            }
            if (requestMethod.equals("POST")) {
                return Endpoint.POST_UPDATED_SUBTASK;
            }
            if (requestMethod.equals("DELETE")) {
                return Endpoint.DELETE_SUBTASK;
            }
        }
        if (pathParts.length == 2 && pathParts[1].equals("epics")) {
            if (requestMethod.equals("GET")) {
                return Endpoint.GET_EPICS;
            }
            if (requestMethod.equals("POST")) {
                return Endpoint.POST_EPIC;
            }
        }
        if (pathParts.length == 3 && pathParts[1].equals("epics")) {
            if (requestMethod.equals("GET")) {
                return Endpoint.GET_EPIC;
            }
            if (requestMethod.equals("POST")) {
                return Endpoint.POST_UPDATED_EPIC;
            }
            if (requestMethod.equals("DELETE")) {
                return Endpoint.DELETE_EPIC;
            }
        }
        if (pathParts.length == 2 && pathParts[1].equals("prioritized")) {
            return Endpoint.GET_PRIORITIZED;
        }
        if (pathParts.length == 2 && pathParts[1].equals("history")) {
            return Endpoint.GET_HISTORY;
        }
        if (pathParts.length == 4 && pathParts[1].equals("epics") &&
                pathParts[3].equals("subtasks") && requestMethod.equals("GET")) {
            return Endpoint.GET_EPIC_SUBTASKS;
        }
        return Endpoint.UNKNOWN;
    }

    private int pathId(HttpExchange httpExchange) {
        String[] pathParts = httpExchange.getRequestURI().getPath().split("/");
        return Integer.parseInt(pathParts[2]);
    }

    private String getBodyFromRequest(HttpExchange httpExchange) throws IOException {
        return new String(httpExchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
    }

    protected void sendText(HttpExchange h, String text) throws IOException {
        byte[] resp = text.getBytes(StandardCharsets.UTF_8);
        h.getResponseHeaders().add("Content-Type", "application/json;charset=utf-8");
        h.sendResponseHeaders(200, resp.length);
        h.getResponseBody().write(resp);
        h.close();
    }

}
