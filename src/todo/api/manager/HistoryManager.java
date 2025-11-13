package todo.api.manager;

import todo.tasks.Task;

import java.util.List;

public interface HistoryManager {
    public void add(Task task);

    public List<Task> getHistory();

    void remove(int id);
}
