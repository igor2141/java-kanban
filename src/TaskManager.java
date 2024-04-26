import java.util.ArrayList;
import java.util.HashMap;

public class TaskManager {
    private int idCount = 0;
/* 1 */
    private HashMap<Integer, Task> taskHashMap = new HashMap<>();
    private HashMap<Integer, Epic> epicHashMap = new HashMap<>();
    private HashMap<Integer, Subtask> subtaskHashMap = new HashMap<>();
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
        epicHashMap.get(subtask.getEpicID()).getSubtaskArrayList().add(idCount);
        epicStatusUpdate(subtask.getEpicID());
    }
/* e */
    public void updateTask(Task task) {
        if (taskHashMap.containsKey(task.getId())) {
            taskHashMap.put(task.getId(), task);
        }
    }

    public void updateEpic(Epic epic) {
        if (epicHashMap.containsKey(epic.getId())) {
            epic.setSubtaskArrayList(epicHashMap.get(epic.getId()).getSubtaskArrayList());
            epic.setStatus(epicHashMap.get(epic.getId()).getStatus());
            epicHashMap.put(epic.getId(), epic);
        }
    }

    public void updateSubtask(Subtask subtask) {
        if (subtaskHashMap.containsKey(subtask.getId())) {
            subtaskHashMap.put(subtask.getId(), subtask);
            epicStatusUpdate(subtask.getEpicID());
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
        int epicId = subtaskHashMap.get(id).getEpicID();
        epicHashMap.get(subtaskHashMap.get(id).getEpicID()).getSubtaskArrayList().remove((Integer) id);
        subtaskHashMap.remove(id);
        epicStatusUpdate(epicId);
    }
/* 3 */
/* a */
    public ArrayList<Integer> returnEpicSubtasks(int id) {
        if (epicHashMap.containsKey(id)) {
            return epicHashMap.get(id).getSubtaskArrayList();
        } else {
            return null;
        }
    }

    private void epicStatusUpdate(int id) {
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
        if (checkNew == epicHashMap.get(id).getSubtaskArrayList().size()) {
            epicHashMap.get(id).setStatus(Status.NEW);
        } else if (checkDone == epicHashMap.get(id).getSubtaskArrayList().size()) {
            epicHashMap.get(id).setStatus(Status.DONE);
        } else {
            epicHashMap.get(id).setStatus(Status.IN_PROGRESS);
        }
    }
}
