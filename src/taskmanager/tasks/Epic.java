package taskmanager.tasks;

import java.util.LinkedHashSet;

public class Epic extends Task {
    private LinkedHashSet<Integer> subtaskIds;

    public Epic(String title, String description) {
        super(title, TaskStatus.NEW, description);
        this.subtaskIds = new LinkedHashSet<>();
    }


    public LinkedHashSet<Integer> getSubtaskIds() {
        return new LinkedHashSet<>(subtaskIds);
    }


    public void addSubtaskId(int subtaskId) {
        subtaskIds.add(subtaskId);
    }

    public void removeSubtaskId(int subtaskId) {
        subtaskIds.remove(subtaskId);
    }

    public void clearSubtaskIds() {
        subtaskIds.clear();
    }
}
