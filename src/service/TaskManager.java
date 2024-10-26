package service;

import tasks.Epic;
import tasks.Status;
import tasks.Subtask;
import tasks.Task;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface TaskManager {
    /* 2 */
    /* a */
    Map<Integer, Task> getTasks();

    Map<Integer, Epic> getEpics();

    Map<Integer, Subtask> getSubtasks();

    Set<Task> getPrioritizedTasks();

    List<Task> returnTasks();

    List<Epic> returnEpics();

    List<Subtask> returnSubtasks();

    /* b */
    void clearTasks();

    void clearEpics();

    void clearSubtasks();

    /* c */
    Task returnTask(int id);

    Epic returnEpic(int id);

    Subtask returnSubtask(int id);

    /* d */
    void createTask(Task task, Status status);

    void createEpic(Epic epic);

    void createSubtask(Subtask subtask, Status status);

    /* e */
    void updateTask(Task task);

    void updateEpic(Epic epic);

    void updateSubtask(Subtask subtask);

    /* f */
    void deleteTask(int id);

    void deleteEpic(int id);

    void deleteSubtask(int id);

    /* 3 */
    /* a */
    List<Integer> returnEpicSubtasks(int id);

    List<Task> getHistory();

}
