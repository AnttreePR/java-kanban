package managerTest;

import manager.InMemoryHistoryManager;
import org.junit.jupiter.api.Test;
import tasks.Task;
import tasks.TaskStatus;

import static org.junit.jupiter.api.Assertions.*;

public class InMemoryHistoryManagerTest {
    @Test
    void historyShouldKeepLast10Tasks() {
        InMemoryHistoryManager history = new InMemoryHistoryManager();

        assertEquals(0, history.getHistory().size());

        Task t = new Task("t", TaskStatus.NEW, "d");
        Task t2 = new Task("t2", TaskStatus.NEW, "d");

        history.addToHistory(t);
        assertEquals(1, history.getHistory().size());

        final int historyLimit = 10;
        for (int taskCount = 1; taskCount < historyLimit; taskCount++) {
            history.addToHistory(t);
        }
        assertEquals(10, history.getHistory().size());
        assertEquals(t, history.getHistory().get(0));

        history.addToHistory(t2);
        assertEquals(t2, history.getHistory().get(0));
        assertEquals(10, history.getHistory().size());
    }

    @Test
    void getHistoryShouldReturnHistory() {
        InMemoryHistoryManager history = new InMemoryHistoryManager();
        assertEquals(history.getHistory().size(), 0);
        Task t = new Task("t", TaskStatus.NEW, "d");
        history.addToHistory(t);
        assertEquals(history.getHistory().size(), 1);
    }
}
