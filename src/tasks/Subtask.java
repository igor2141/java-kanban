package tasks;

import java.time.LocalDateTime;

public class Subtask extends Task {

    private int epicID;

    public Subtask(String name, String description, LocalDateTime startTime, int duration, int epicID) {
        super(name, description, startTime, duration);
        this.epicID = epicID;
    }

    public int getEpicID() {
        return epicID;
    }

}