package service;

import tasks.Epic;
import tasks.Status;
import tasks.Subtask;
import tasks.Task;

import java.time.LocalDateTime;
import java.util.*;

import static service.Managers.getDefaultHistory;


public class InMemoryTaskManager implements TaskManager {

    private int idCount = 0;
    /* 1 */
    private Map<Integer, Task> tasks = new HashMap<>();
    private Map<Integer, Epic> epics = new HashMap<>();
    private Map<Integer, Subtask> subtasks = new HashMap<>();

    HistoryManager historyManager = getDefaultHistory();

    protected void setIdCount(int idCount) {
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

    public boolean overlapCheck(Task task) {
        Set<Task> savedTasks = getPrioritizedTasks();
        if (!savedTasks.isEmpty()) {
            return savedTasks.stream().anyMatch(savedTask -> (task.getStartTime().isAfter(savedTask.getStartTime()) && task.getStartTime().isBefore(savedTask.getEndTime())) ||
                    (task.getEndTime().isAfter(savedTask.getStartTime()) && task.getEndTime().isBefore(savedTask.getEndTime())));
        }
        return false;
    }

    @Override
    public Set<Task> getPrioritizedTasks() {
        Comparator<Task> comparator = Comparator.comparing(Task::getStartTime);
        Set<Task> sortedTasks = new TreeSet<>(comparator);
        sortedTasks.addAll(tasks.values());
        sortedTasks.addAll(subtasks.values());
        return sortedTasks;
    }

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

    @Override
    public void clearTasks() {
        tasks.keySet().forEach(taskId -> historyManager.historyRemove(taskId));
        tasks.clear();
    }

    @Override
    public void clearEpics() {
        epics.keySet().forEach(epicId -> historyManager.historyRemove(epicId));
        epics.clear();
        subtasks.keySet().forEach(subtaskId -> historyManager.historyRemove(subtaskId));
        subtasks.clear();
    }

    @Override
    public void clearSubtasks() {
        subtasks.keySet().forEach(subtaskId -> historyManager.historyRemove(subtaskId));
        subtasks.clear();
        epics.values().forEach(epic -> {
            epic.clearList();
            epicStatusUpdate(epic.getId());
            epicTimeUpdate(epic.getId());
        });
    }

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

    @Override
    public void createTask(Task task, Status status) {
        if (overlapCheck(task)) {
            return;
        }
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
        if (overlapCheck(subtask)) {
            return;
        }
        idCount++;
        subtask.setId(idCount);
        subtask.setStatus(status);
        subtasks.put(idCount, subtask);
        epics.get(subtask.getEpicID()).getSubtaskArrayList().add(idCount);
        epicStatusUpdate(subtask.getEpicID());
        epicTimeUpdate(subtask.getEpicID());
    }

    @Override
    public void updateTask(Task task) {
        if (overlapCheck(task)) {
            return;
        }
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
            epicTimeUpdate(epic.getId());
        }
    }

    @Override
    public void updateSubtask(Subtask subtask) {
        if (overlapCheck(subtask)) {
            return;
        }
        if (subtasks.containsKey(subtask.getId())) {
            subtasks.put(subtask.getId(), subtask);
            epicStatusUpdate(subtask.getEpicID());
            epicTimeUpdate(subtask.getEpicID());
        }

    }

    @Override
    public void deleteTask(int id) {
        tasks.remove(id);
        historyManager.historyRemove(id);
    }

    @Override
    public void deleteEpic(int id) {
        epics.get(id).getSubtaskArrayList().stream().filter(subtaskId -> subtasks.containsKey(subtaskId)).forEach(subtaskId -> {
            subtasks.remove(subtaskId);
            historyManager.historyRemove(subtaskId);
        });
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
        epicTimeUpdate(epicId);
    }

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

    private void epicTimeUpdate(int id) {
        if (!epics.containsKey(id)) {
            return;
        }

        int epicDuration = 0;
        Epic epic = epics.get(id);

        if (epic.getSubtaskArrayList().isEmpty()) {
            epic.setDuration(epicDuration);
            epic.setStartTime(null);
            epic.setEpicEndTime(null);
            return;
        }
        LocalDateTime epicStart = subtasks.get(epic.getSubtaskArrayList().get(0)).getStartTime();
        LocalDateTime lastSubtaskEnd = subtasks.get(epic.getSubtaskArrayList().get(0)).getEndTime();
        for (Integer subId: epic.getSubtaskArrayList()) {
            epicDuration += (subtasks.get(subId).getDuration());
            if (epicStart.isAfter(subtasks.get(subId).getStartTime())) {
                epicStart = subtasks.get(subId).getStartTime();
            }
            if (lastSubtaskEnd.isBefore(subtasks.get(subId).getEndTime())) {
                lastSubtaskEnd = subtasks.get(subId).getEndTime();
            }
        }
        epic.setDuration(epicDuration);
        epic.setStartTime(epicStart);
        epic.setEpicEndTime(lastSubtaskEnd);
    }

}
