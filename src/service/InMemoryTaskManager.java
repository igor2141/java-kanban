package service;

import tasks.Epic;
import tasks.Status;
import tasks.Subtask;
import tasks.Task;

import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static service.Managers.getDefaultHistory;


public class InMemoryTaskManager implements TaskManager {

    private int idCount = 0;
    /* 1 */
    private Map<Integer, Task> tasks = new HashMap<>();
    private Map<Integer, Epic> epics = new HashMap<>();
    private Map<Integer, Subtask> subtasks = new HashMap<>();

    HistoryManager historyManager = getDefaultHistory();

    public void setIdCount(int idCount) {
        this.idCount = idCount;
    }

    public Map<Integer, Task> getTasks() {
        return tasks;
    }

    public Map<Integer, Epic> getEpics() {
        return epics;
    }

    public Map<Integer, Subtask> getSubtasks() {
        return subtasks;
    }

/* 2 */
/* a */
    @Override
    public List<Task> returnTasks() {
        return new ArrayList<>(tasks.values());
    }

    @Override
    public List<Epic> returnEpics() {
        return new ArrayList<>(epics.values());
    }

    @Override
    public List<Subtask> returnSubtasks() {
        return new ArrayList<>(subtasks.values());
    }
/* b */

    @Override
    public void clearTasks() {
        for (Integer taskId: tasks.keySet()) {
            historyManager.historyRemove(taskId);
        }
        tasks.clear();
    }

    @Override
    public void clearEpics() {
        for (Integer epicId: epics.keySet()) {
            historyManager.historyRemove(epicId);
        }
        epics.clear();
        for (Integer subtaskId: subtasks.keySet()) {
            historyManager.historyRemove(subtaskId);
        }
        subtasks.clear();
    }

    @Override
    public void clearSubtasks() {
        for (Integer subtaskId: subtasks.keySet()) {
            historyManager.historyRemove(subtaskId);
        }
        subtasks.clear();
        for (Epic epic : epics.values()) {
            epic.clearList();
            epicStatusUpdate(epic.getId());
        }
    }
/* c */

    @Override
    public Task returnTask(int id) {
        final Task task = tasks.get(id);
        historyManager.historyAdd(task);
        return task;
    }

    @Override
    public Epic returnEpic(int id) {
        final Epic epic = epics.get(id);
        historyManager.historyAdd(epic);
        return epic;
    }

    @Override
    public Subtask returnSubtask(int id) {
        final Subtask subtask = subtasks.get(id);
        historyManager.historyAdd(subtask);
        return subtask;
    }
/* d */

    @Override
    public void createTask(Task task, Status status) {
        idCount++;
        task.setId(idCount);
        task.setStatus(status);
        tasks.put(idCount, task);
    }

    @Override
    public void createEpic(Epic epic) {
        idCount++;
        epic.setId(idCount);
        epic.setStatus(Status.NEW);
        epics.put(idCount, epic);
    }

    @Override
    public void createSubtask(Subtask subtask, Status status) {
        idCount++;
        subtask.setId(idCount);
        subtask.setStatus(status);
        subtasks.put(idCount, subtask);
        epics.get(subtask.getEpicID()).getSubtaskArrayList().add(idCount);
        epicStatusUpdate(subtask.getEpicID());
    }
/* e */

    @Override
    public void updateTask(Task task) {
        if (tasks.containsKey(task.getId())) {
            tasks.put(task.getId(), task);
        }
    }

    @Override
    public void updateEpic(Epic epic) {
        if (epics.containsKey(epic.getId())) {
            epic.setSubtaskArrayList(epics.get(epic.getId()).getSubtaskArrayList());
            epics.put(epic.getId(), epic);
            epicStatusUpdate(epic.getId());
        }
    }

    @Override
    public void updateSubtask(Subtask subtask) {
        if (subtasks.containsKey(subtask.getId())) {
            subtasks.put(subtask.getId(), subtask);
            epicStatusUpdate(subtask.getEpicID());
        }

    }
/* f */

    @Override
    public void deleteTask(int id) {
        tasks.remove(id);
        historyManager.historyRemove(id);
    }

    @Override
    public void deleteEpic(int id) {
        for (Integer subtaskId : epics.get(id).getSubtaskArrayList()) {
            if (subtasks.containsKey(subtaskId)) {
                subtasks.remove(subtaskId);
                historyManager.historyRemove(subtaskId);
            }
        }
        epics.remove(id);
        historyManager.historyRemove(id);
    }

    @Override
    public void deleteSubtask(int id) {
        int epicId = subtasks.get(id).getEpicID();
        epics.get(subtasks.get(id).getEpicID()).getSubtaskArrayList().remove((Integer) id);
        subtasks.remove(id);
        historyManager.historyRemove(id);
        epicStatusUpdate(epicId);
    }
/* 3 */
/* a */

    @Override
    public List<Integer> returnEpicSubtasks(int id) {
        if (epics.containsKey(id)) {
            return epics.get(id).getSubtaskArrayList();
        } else {
            return null;
        }
    }

    @Override
    public List<Task> getHistory() {
        return historyManager.getHistory();
    }

    private void epicStatusUpdate(int id) {
        int checkDone = 0;
        int checkNew = 0;

        for (Integer subtaskId : epics.get(id).getSubtaskArrayList()) {
            if (subtasks.get(subtaskId).getStatus().equals(Status.DONE)) {
                checkDone++;
            }
            if (subtasks.get(subtaskId).getStatus().equals(Status.NEW)) {
                checkNew++;
            }
        }
        if (checkNew == epics.get(id).getSubtaskArrayList().size()) {
            epics.get(id).setStatus(Status.NEW);
        } else if (checkDone == epics.get(id).getSubtaskArrayList().size()) {
            epics.get(id).setStatus(Status.DONE);
        } else {
            epics.get(id).setStatus(Status.IN_PROGRESS);
        }
    }
}
