package todo.tasks;

public class Subtask extends Task {
    private int epicId;

    public Subtask(String title, TaskStatus taskStatus, String description, int epicId) {
        super(title, taskStatus, description);
        this.epicId = epicId;
    }


    public int getEpicId() {
        return epicId;
    }

    public void setEpicId(int epicId) {
        this.epicId = epicId;
    }
}
