

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

        Task test1 = new Task("name11", "desc11");
        test1.setId(1);
        test1.setStatus(Status.DONE);
        tm.updateTask(test1);

        Subtask test2 = new Subtask("name55", "desc55", 3);
        test2.setId(5);
        test2.setStatus(Status.DONE);
        tm.updateSubtask(test2);

        Subtask test3 = new Subtask("name66", "desc66", 3);
        test3.setId(6);
        test3.setStatus(Status.DONE);
        tm.updateSubtask(test3);

        Epic test4 = new Epic("name33", "desc33");
        test4.setId(3);
        test4.getSubtaskArrayList().add(5);
        test4.getSubtaskArrayList().add(6);
        tm.updateEpic(test4);

        tm.deleteSubtask(7);
        tm.deleteEpic(3);

    }
}
