package tasks;

public class Subtask extends Task {

    private int epicID;

    public Subtask(String name, String description, int epicID) {
        super(name, description);
        this.epicID = epicID;
    }

    public int getEpicID() {
        return epicID;
    }

}