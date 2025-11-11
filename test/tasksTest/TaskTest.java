package tasksTest;

import TaskManager.tasks.Task;
import TaskManager.tasks.TaskStatus;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class TaskTest {
    @Test
    public void getTitle() {
        Task task = new Task("title", TaskStatus.NEW, "description");
        assertEquals("title", task.getTitle());
    }

    @Test
    public void setTitle() {
        Task task = new Task("title", TaskStatus.NEW, "description");
        assertEquals("title", task.getTitle());
        task.setTitle("new title");
        assertEquals("new title", task.getTitle());
    }

    @Test
    public void shouldUpdateIdAfterSetId() {
        Task task = new Task("title", TaskStatus.NEW, "description");
        task.setId(42);
        assertEquals(42, task.getId());
    }

    @Test
    public void getDescription() {
        Task task = new Task("title", TaskStatus.NEW, "description");
        assertEquals("description", task.getDescription());
    }

    @Test
    public void setDescription() {
        Task task = new Task("title", TaskStatus.NEW, "description");
        assertEquals("description", task.getDescription());
        task.setDescription("new description");
        assertEquals("new description", task.getDescription());
    }
}
