
public class Main {

    public static void main(String[] args) {

/* debug */
        TaskManager tm = new TaskManager();

        tm.createTask(new Task("name1", "desc1"), Status.NEW);
        tm.createTask(new Task("name2", "desc2"), Status.IN_PROGRESS);
        tm.createEpic(new Epic("name3", "desc3"));
        tm.createEpic(new Epic("name4", "desc4"));
        tm.createSubtask(new Subtask("name5", "desc5", 3), Status.NEW);
        tm.createSubtask(new Subtask("name6", "desc6", 3), Status.NEW);
        tm.createSubtask(new Subtask("name7", "desc7", 4), Status.IN_PROGRESS);
        tm.updateTask(1, new Task("name11", "desc11"), Status.DONE);
        tm.updateSubtask(5, new Subtask("name55", "desc55", 3), Status.DONE);
        tm.updateSubtask(6, new Subtask("name66", "desc66", 3), Status.DONE);
        tm.updateEpic(3, new Epic("name33", "desc33"));
        tm.deleteSubtask(7);
        tm.deleteEpic(4);

    }
}
