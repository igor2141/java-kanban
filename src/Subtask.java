public class Subtask extends Task {

    int epicID;

    public int getEpicID() {
        return epicID;
    }

    public Subtask(String name, String description, int epicID) {
        super(name, description);
        this.epicID = epicID;
    }

}