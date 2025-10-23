package tasksTest;

import tasks.Epic;
import tasks.Subtask;

import org.junit.jupiter.api.Test;
import tasks.TaskStatus;

import static org.junit.jupiter.api.Assertions.*;

public class EpicTest {
    @Test
    public void shouldAddSubtaskIdToEpic() {
        Epic epic = new Epic("title", "description");

        assertEquals(0, epic.getSubtaskIds().size());

        Subtask s1 = new Subtask("title", TaskStatus.NEW, "description", epic.getId());
        s1.setId(100);

        epic.addSubtaskId(s1.getId());

        assertEquals(1, epic.getSubtaskIds().size());
        assertTrue(epic.getSubtaskIds().contains(100));
    }
    @Test
    public void shouldClearSubtaskIds() {
        Epic epic = new Epic("title", "description");

        Subtask s1 = new Subtask("title1", TaskStatus.NEW, "description1", epic.getId());
        Subtask s2 = new Subtask("title2", TaskStatus.NEW, "description2", epic.getId());
        s1.setId(101);
        s2.setId(102);

        epic.addSubtaskId(s1.getId());
        epic.addSubtaskId(s2.getId());

        assertEquals(2, epic.getSubtaskIds().size());

        epic.clearSubtaskIds();

        assertEquals(0, epic.getSubtaskIds().size());
    }

    @Test
    public void shouldRemoveSubtaskIdFromEpic() {
        Epic epic = new Epic("title", "description");

        Subtask s1 = new Subtask("title1", TaskStatus.NEW, "description1", epic.getId());
        Subtask s2 = new Subtask("title2", TaskStatus.NEW, "description2", epic.getId());
        s1.setId(201);
        s2.setId(202);

        epic.addSubtaskId(s1.getId());
        epic.addSubtaskId(s2.getId());

        assertEquals(2, epic.getSubtaskIds().size());
        assertTrue(epic.getSubtaskIds().contains(201));
        assertTrue(epic.getSubtaskIds().contains(202));

        epic.removeSubtaskId(201);

        assertEquals(1, epic.getSubtaskIds().size());
        assertFalse(epic.getSubtaskIds().contains(201));
        assertTrue(epic.getSubtaskIds().contains(202));
    }

}
