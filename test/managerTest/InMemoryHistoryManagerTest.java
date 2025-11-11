package managerTest;

import taskmanager.manager.InMemoryHistoryManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import taskmanager.tasks.Task;
import taskmanager.tasks.TaskStatus;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class InMemoryHistoryManagerTest {
    private InMemoryHistoryManager history;
    private Task task1;
    private Task task2;
    private Task task3;

    @BeforeEach
    void setUp() {
        history = new InMemoryHistoryManager();
        task1 = new Task("Task 1", TaskStatus.NEW, "Description 1");
        task1.setId(1);
        task2 = new Task("Task 2", TaskStatus.NEW, "Description 2");
        task2.setId(2);
        task3 = new Task("Task 3", TaskStatus.NEW, "Description 3");
        task3.setId(3);
    }

    @Test
    void shouldAddTasksToHistory() {
        history.add(task1);
        history.add(task2);

        List<Task> result = history.getHistory();

        assertEquals(2, result.size());
        assertEquals(task1, result.get(0));
        assertEquals(task2, result.get(1));
    }

    @Test
    void shouldNotAddNullTask() {
        history.add(null);
        assertTrue(history.getHistory().isEmpty());
    }

    @Test
    void shouldRemoveTaskFromHistoryById() {
        history.add(task1);
        history.add(task2);
        history.add(task3);

        history.remove(task2.getId());
        List<Task> result = history.getHistory();

        assertEquals(2, result.size());
        assertFalse(result.contains(task2));
        assertEquals(List.of(task1, task3), result);
    }

    @Test
    void shouldMoveTaskToEndIfReadded() {
        history.add(task1);
        history.add(task2);
        history.add(task3);
        history.add(task1);

        List<Task> result = history.getHistory();

        assertEquals(3, result.size());
        assertEquals(List.of(task2, task3, task1), result);
    }

    @Test
    void shouldReturnEmptyListIfNoHistory() {
        assertTrue(history.getHistory().isEmpty());
    }

    @Test
    void shouldCorrectlyHandleRemovalOfFirstAndLast() {
        history.add(task1);
        history.add(task2);
        history.add(task3);

        history.remove(task1.getId());
        assertEquals(List.of(task2, task3), history.getHistory());

        history.remove(task3.getId());
        assertEquals(List.of(task2), history.getHistory());
    }
}
