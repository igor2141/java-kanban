package tasks;

import java.util.ArrayList;

public class Epic extends Task {

    private ArrayList<Integer> subtaskArrayList;

    public Epic(String name, String description) {
        super(name, description);
        subtaskArrayList = new ArrayList<>();
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