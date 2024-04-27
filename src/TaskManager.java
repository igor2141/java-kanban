import java.util.ArrayList;
import java.util.HashMap;

public class TaskManager {
    private int idCount = 0;
/* 1 */
    private HashMap<Integer, Task> tasks = new HashMap<>();
    private HashMap<Integer, Epic> epics = new HashMap<>();
    private HashMap<Integer, Subtask> subtasks = new HashMap<>();
/* 2 */
/* a */
    public ArrayList<Task> returnTasks() {
        return new ArrayList<>(tasks.values());
    }

    public ArrayList<Epic> returnEpics() {
        return new ArrayList<>(epics.values());
    }

    public ArrayList<Subtask> returnSubtasks() {
        return new ArrayList<>(subtasks.values());
    }
/* b */
    public void clearTasks() {
        tasks.clear();
    }

    public void clearEpics() {
        epics.clear();
        subtasks.clear();
    }

    public void clearSubtasks() {
        subtasks.clear();
        for (Integer i : epics.keySet()) {
            epics.get(i).clearList();
            epicStatusUpdate(i);
        }
    }
/* c */
    public Task returnTask(int id) {
        return tasks.get(id);
    }

    public Epic returnEpic(int id) {
        return epics.get(id);
    }

    public Subtask returnSubtask(int id) {
        return subtasks.get(id);
    }
/* d */
    public void createTask(Task task, Status status) {
        idCount++;
        task.setId(idCount);
        task.setStatus(status);
        tasks.put(idCount, task);
    }

    public void createEpic(Epic epic) {
        idCount++;
        epic.setId(idCount);
        epic.setStatus(Status.NEW);
        epics.put(idCount, epic);
    }

    public void createSubtask(Subtask subtask, Status status) {
        idCount++;
        subtask.setId(idCount);
        subtask.setStatus(status);
        subtasks.put(idCount, subtask);
        epics.get(subtask.getEpicID()).getSubtaskArrayList().add(idCount);
        epicStatusUpdate(subtask.getEpicID());
    }
/* e */
    public void updateTask(Task task) {
        if (tasks.containsKey(task.getId())) {
            tasks.put(task.getId(), task);
        }
    }

    public void updateEpic(Epic epic) {
        if (epics.containsKey(epic.getId())) {
            epic.setSubtaskArrayList(epics.get(epic.getId()).getSubtaskArrayList());
            epics.put(epic.getId(), epic);
            epicStatusUpdate(epic.getId());
        }
    }

    public void updateSubtask(Subtask subtask) {
        if (subtasks.containsKey(subtask.getId())) {
            subtasks.put(subtask.getId(), subtask);
            epicStatusUpdate(subtask.getEpicID());
        }

    }
/* f */
    public void deleteTask(int id) {
        tasks.remove(id);
    }

    public void deleteEpic(int id) {
        for (Integer i : epics.get(id).getSubtaskArrayList()) {
            if (subtasks.containsKey(i)) {
                subtasks.remove(i);
            }
        }
        epics.remove(id);
    }

    public void deleteSubtask(int id) {
        int epicId = subtasks.get(id).getEpicID();
        epics.get(subtasks.get(id).getEpicID()).getSubtaskArrayList().remove((Integer) id);
        subtasks.remove(id);
        epicStatusUpdate(epicId);
    }
/* 3 */
/* a */
    public ArrayList<Integer> returnEpicSubtasks(int id) {
        if (epics.containsKey(id)) {
            return epics.get(id).getSubtaskArrayList();
        } else {
            return null;
        }
    }

    private void epicStatusUpdate(int id) {
        int checkDone = 0;
        int checkNew = 0;

        for (Integer i : epics.get(id).getSubtaskArrayList()) {
            if (subtasks.get(i).getStatus().equals(Status.DONE)) {
                checkDone++;
            }
            if (subtasks.get(i).getStatus().equals(Status.NEW)) {
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
