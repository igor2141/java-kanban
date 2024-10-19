package service;

import tasks.Epic;
import tasks.Status;
import tasks.Subtask;
import tasks.Task;

import java.io.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class FileBackedTaskManager extends InMemoryTaskManager {

    private File data;

    public FileBackedTaskManager(File data) {
        this.data = data;
    }

    public void save() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(data))) {
            for (Task task: getTasks().values()) {
                bw.write(toString(task) + "\n");
            }
            for (Epic epic: getEpics().values()) {
                bw.write(toString(epic) + "\n");
            }
            for (Subtask subtask: getSubtasks().values()) {
                bw.write(toString(subtask) + "\n");
            }
        } catch (IOException exc) {
            throw new ManagerSaveException();
        }
    }

    public void read() {
        List<String> lines = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(data))) {
            while (br.ready()) {
                lines.add(br.readLine());
            }
            for (String line: lines) {
                String[] words = line.split(",");
                int lastId = 0;
                switch (Type.valueOf(words[1])) {
                    case TASK -> {
                        int savedId = Integer.parseInt(words[0]);
                        if (savedId > lastId) {
                            lastId = savedId;
                        }
                        setIdCount(savedId - 1);
                        createTask(new Task(words[2], words[4], LocalDateTime.parse(words[5]),
                                Integer.parseInt(words[6])), Status.valueOf(words[3]));
                    }
                    case EPIC -> {
                        int savedId = Integer.parseInt(words[0]);
                        if (savedId > lastId) {
                            lastId = savedId;
                        }
                        setIdCount(savedId - 1);
                        createEpic(new Epic(words[2], words[4]));
                    }
                    case SUBTASK -> {
                        int savedId = Integer.parseInt(words[0]);
                        if (savedId > lastId) {
                            lastId = savedId;
                        }
                        setIdCount(savedId - 1);
                        createSubtask(new Subtask(words[2], words[4], LocalDateTime.parse(words[5]),
                                Integer.parseInt(words[6]), Integer.parseInt(words[7])), Status.valueOf(words[3]));
                    }
                }
                setIdCount(lastId);
            }
        } catch (IOException exc) {
            throw new ManagerSaveException();
        }
    }

    @Override
    public void clearTasks() {
        super.clearTasks();
        save();
    }

    @Override
    public void clearEpics() {
        super.clearEpics();
        save();
    }

    @Override
    public void clearSubtasks() {
        super.clearSubtasks();
        save();
    }

    @Override
    public void createTask(Task task, Status status) {
        super.createTask(task, status);
        save();
    }

    @Override
    public void createEpic(Epic epic) {
        super.createEpic(epic);
        save();
    }

    @Override
    public void createSubtask(Subtask subtask, Status status) {
        super.createSubtask(subtask, status);
        save();
    }

    @Override
    public void updateTask(Task task) {
        super.updateTask(task);
        save();
    }

    @Override
    public void updateEpic(Epic epic) {
        super.updateEpic(epic);
        save();
    }

    @Override
    public void updateSubtask(Subtask subtask) {
        super.updateSubtask(subtask);
        save();
    }

    @Override
    public void deleteTask(int id) {
        super.deleteTask(id);
        save();
    }

    @Override
    public void deleteEpic(int id) {
        super.deleteEpic(id);
        save();
    }

    @Override
    public void deleteSubtask(int id) {
        super.deleteSubtask(id);
        save();
    }

    public String toString(Task task) {
        return task.getId() + "," + Type.TASK + "," + task.getName()
                + "," + task.getStatus() + "," + task.getDescription() + "," + task.getStartTime() + "," +
                task.getDuration();
    }

    public String toString(Epic epic) {
        return epic.getId() + "," + Type.EPIC + "," + epic.getName()
                + "," + epic.getStatus() + "," + epic.getDescription();
    }

    public String toString(Subtask subtask) {
        return subtask.getId() + "," + Type.SUBTASK + "," + subtask.getName()
                + "," + subtask.getStatus() + "," + subtask.getDescription() + "," +
                subtask.getStartTime() + "," + subtask.getDuration() + "," + subtask.getEpicID();
    }
}
