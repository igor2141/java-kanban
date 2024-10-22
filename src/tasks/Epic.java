package tasks;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class Epic extends Task {

    private ArrayList<Integer> subtaskArrayList;
    private LocalDateTime epicEndTime;

    public Epic(String name, String description) {
        super(name, description);
        subtaskArrayList = new ArrayList<>();
    }

    public LocalDateTime getEpicEndTime() {
        return epicEndTime;
    }

    public void setEpicEndTime(LocalDateTime epicEndTime) {
        this.epicEndTime = epicEndTime;
    }

    public void setSubtaskArrayList(ArrayList<Integer> subtaskArrayList) {
        this.subtaskArrayList = subtaskArrayList;
    }

    public ArrayList<Integer> getSubtaskArrayList() {
        return subtaskArrayList;
    }

    public void clearList() {
        subtaskArrayList.clear();
    }

}