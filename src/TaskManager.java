import java.util.ArrayList;
import java.util.HashMap;

public class TaskManager {
    private int idCount = 0;
/* 1 */
    HashMap <Integer, Task> taskHashMap = new HashMap<>();
    HashMap <Integer, Epic> epicHashMap = new HashMap<>();
    HashMap <Integer, Subtask> subtaskHashMap = new HashMap<>();
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
    }

    public void clearSubtasks() {
        subtaskHashMap.clear();
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
    public void updateTask(int id, Task task, Status status) {
        task.setId(id);
        task.setStatus(status);
        taskHashMap.put(id, task);
    }

    public void updateEpic(int id, Epic epic) {
        epic.setSubtaskArrayList(epicHashMap.get(id).getSubtaskArrayList());
        epic.setId(id);
        epicHashMap.put(id, epic);

        int checkDone = 0;
        int checkNew = 0;
        for (Integer i : epicHashMap.get(id).getSubtaskArrayList()) {
            if (subtaskHashMap.get(i).getStatus().equals(Status.DONE)) {
                checkDone++;
            }
            if (subtaskHashMap.get(i).getStatus().equals(Status.NEW)) {
                checkNew++;
            }
        }
        if ((epicHashMap.get(id).getSubtaskArrayList().isEmpty()) || (checkNew == epicHashMap.get(id).getSubtaskArrayList().size())) {
            epicHashMap.get(id).setStatus(Status.NEW);
        } else if (checkDone == epicHashMap.get(id).getSubtaskArrayList().size()) {
            epicHashMap.get(id).setStatus(Status.DONE);
        } else {
            epicHashMap.get(id).setStatus(Status.IN_PROGRESS);
        }
    }

    public void updateSubtask(int id, Subtask subtask, Status status) {
        subtask.setId(id);
        subtask.setStatus(status);
        subtaskHashMap.put(id, subtask);

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
/* f */
    public void deleteTask(int id) {
        taskHashMap.remove(id);
    }

    public void deleteEpic(int id) {
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
