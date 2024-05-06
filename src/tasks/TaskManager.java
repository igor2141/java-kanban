package tasks;

import java.util.ArrayList;

public interface TaskManager {
    /* 2 */
    /* a */
    ArrayList<Task> returnTasks();

    ArrayList<Epic> returnEpics();

    ArrayList<Subtask> returnSubtasks();

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
    ArrayList<Integer> returnEpicSubtasks(int id);
}
