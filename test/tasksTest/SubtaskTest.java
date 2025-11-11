package tasksTest;

import taskManager.tasks.Subtask;
import taskManager.tasks.TaskStatus;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class SubtaskTest {

    @Test
    public void shouldReturnCorrectEpicId() {
        Subtask subtask = new Subtask("title", TaskStatus.NEW, "description", 42);
        assertEquals(42, subtask.getEpicId());
    }

    @Test
    public void shouldUpdateEpicId() {
        Subtask subtask = new Subtask("title", TaskStatus.NEW, "description", 42);
        subtask.setEpicId(100);
        assertEquals(100, subtask.getEpicId());
    }
}
