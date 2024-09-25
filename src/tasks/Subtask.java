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

    @Override
    public String toString() {
        return getId() + ",SUBTASK," + getName() + "," + getStatus() + "," + getDescription() + "," + epicID;
    }
}