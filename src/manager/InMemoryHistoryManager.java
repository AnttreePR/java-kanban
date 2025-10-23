package manager;

import tasks.Task;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {
    private final LinkedList<Task> history = new LinkedList<>();
    public void addToHistory(Task task) {
        if (history.size() < 10) {
            history.add(0, task);
        } else {
            history.remove(history.size() - 1);
            history.add(0, task);
        }
    }

    public List<Task> getHistory() {
        return new ArrayList<>(history);
    }
}
