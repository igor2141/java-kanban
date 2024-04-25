import java.util.ArrayList;
import java.util.HashMap;

public class TaskManager {
    private int idCount = 0;
/* 1 */
    private HashMap <Integer, Task> taskHashMap = new HashMap<>();
    private HashMap <Integer, Epic> epicHashMap = new HashMap<>();
    private HashMap <Integer, Subtask> subtaskHashMap = new HashMap<>();
/* 2 */
/* a */
    public HashMap<Integer, Task> returnTasks() {
        return taskHashMap;
    }

    public HashMap<Integer, Epic> returnEpics() {
        return epicHashMap;
    }

    public HashMap<Integer, Subtask> returnSubtasks() {
        return subtaskHashMap;
    }
/* b */
    public void clearTasks() {
        taskHashMap.clear();
    }

    public void clearEpics() {
        epicHashMap.clear();
        subtaskHashMap.clear();
    }

    public void clearSubtasks() {
        subtaskHashMap.clear();
        for (Integer i : epicHashMap.keySet()) {
            epicHashMap.get(i).getSubtaskArrayList().clear();
            epicHashMap.get(i).setStatus(Status.NEW);
        }
    }
/* c */
    public Task returnTask(int id) {
        return taskHashMap.get(id);
    }

    public Epic returnEpic(int id) {
        return epicHashMap.get(id);
    }

    public Subtask returnSubtask(int id) {
        return subtaskHashMap.get(id);
    }
/* d */
    public void createTask(Task task, Status status) {
        idCount++;
        task.setId(idCount);
        task.setStatus(status);
        taskHashMap.put(idCount, task);
    }

    public void createEpic(Epic epic) {
        idCount++;
        epic.setId(idCount);
        epic.setStatus(Status.NEW);
        epicHashMap.put(idCount, epic);
    }

    public void createSubtask(Subtask subtask, Status status) {
        idCount++;
        subtask.setId(idCount);
        subtask.setStatus(status);
        subtaskHashMap.put(idCount, subtask);
        epicHashMap.get(subtask.epicID).getSubtaskArrayList().add(idCount);

        int checkDone = 0;
        int checkNew = 0;
        for (Integer i : epicHashMap.get(subtask.epicID).getSubtaskArrayList()) {
            if (subtaskHashMap.get(i).getStatus().equals(Status.DONE)) {
                checkDone++;
            }
            if (subtaskHashMap.get(i).getStatus().equals(Status.NEW)) {
                checkNew++;
            }
        }
        if (checkNew == epicHashMap.get(subtask.epicID).getSubtaskArrayList().size()) {
            epicHashMap.get(subtask.epicID).setStatus(Status.NEW);
        } else if (checkDone == epicHashMap.get(subtask.epicID).getSubtaskArrayList().size()) {
            epicHashMap.get(subtask.epicID).setStatus(Status.DONE);
        } else {
            epicHashMap.get(subtask.epicID).setStatus(Status.IN_PROGRESS);
        }
    }
/* e */
    public void updateTask(Task task) {
        if (taskHashMap.containsKey(task.getId())) {
            taskHashMap.put(task.getId(), task);
        }
    }

    public void updateEpic(Epic epic) {
        if (epicHashMap.containsKey(epic.getId())) {
            epicHashMap.put(epic.getId(), epic);

            int checkDone = 0;
            int checkNew = 0;
            for (Integer i : epicHashMap.get(epic.getId()).getSubtaskArrayList()) {
                if (subtaskHashMap.get(i).getStatus().equals(Status.DONE)) {
                    checkDone++;
                }
                if (subtaskHashMap.get(i).getStatus().equals(Status.NEW)) {
                    checkNew++;
                }
            }
            if (checkNew == epicHashMap.get(epic.getId()).getSubtaskArrayList().size()) {
                epicHashMap.get(epic.getId()).setStatus(Status.NEW);
            } else if (checkDone == epicHashMap.get(epic.getId()).getSubtaskArrayList().size()) {
                epicHashMap.get(epic.getId()).setStatus(Status.DONE);
            } else {
                epicHashMap.get(epic.getId()).setStatus(Status.IN_PROGRESS);
            }
        }
    }

    public void updateSubtask(Subtask subtask) {
        if (subtaskHashMap.containsKey(subtask.getId())) {
            subtaskHashMap.put(subtask.getId(), subtask);

            int checkDone = 0;
            int checkNew = 0;
            for (Integer i : epicHashMap.get(subtask.epicID).getSubtaskArrayList()) {
                if (subtaskHashMap.get(i).getStatus().equals(Status.DONE)) {
                    checkDone++;
                }
                if (subtaskHashMap.get(i).getStatus().equals(Status.NEW)) {
                    checkNew++;
                }
            }
            if (checkNew == epicHashMap.get(subtask.epicID).getSubtaskArrayList().size()) {
                epicHashMap.get(subtask.epicID).setStatus(Status.NEW);
            } else if (checkDone == epicHashMap.get(subtask.epicID).getSubtaskArrayList().size()) {
                epicHashMap.get(subtask.epicID).setStatus(Status.DONE);
            } else {
                epicHashMap.get(subtask.epicID).setStatus(Status.IN_PROGRESS);
            }
        }

    }
/* f */
    public void deleteTask(int id) {
        taskHashMap.remove(id);
    }

    public void deleteEpic(int id) {
        for (Integer i : epicHashMap.get(id).getSubtaskArrayList()) {
            if (subtaskHashMap.containsKey(i)) {
                subtaskHashMap.remove(i);
            }
        }
        epicHashMap.remove(id);
    }

    public void deleteSubtask(int id) {
        int epicId = subtaskHashMap.get(id).epicID;
        epicHashMap.get(subtaskHashMap.get(id).epicID).getSubtaskArrayList().remove((Integer) id);
        subtaskHashMap.remove(id);

        int checkDone = 0;
        int checkNew = 0;
        for (Integer i : epicHashMap.get(epicId).getSubtaskArrayList()) {
            if (subtaskHashMap.get(i).getStatus().equals(Status.DONE)) {
                checkDone++;
            }
            if (subtaskHashMap.get(i).getStatus().equals(Status.NEW)) {
                checkNew++;
            }
        }
        if ((epicHashMap.get(epicId).getSubtaskArrayList().isEmpty()) || (checkNew == epicHashMap.get(epicId).getSubtaskArrayList().size())) {
            epicHashMap.get(epicId).setStatus(Status.NEW);
        } else if (checkDone == epicHashMap.get(epicId).getSubtaskArrayList().size()) {
            epicHashMap.get(epicId).setStatus(Status.DONE);
        } else {
            epicHashMap.get(epicId).setStatus(Status.IN_PROGRESS);
        }
    }
/* 3 */
/* a */
    public ArrayList<Integer> returnEpicSubtasks(int id) {
        return epicHashMap.get(id).getSubtaskArrayList();
    }

 }
