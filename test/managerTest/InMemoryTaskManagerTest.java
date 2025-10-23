package managerTest;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import manager.*;
import tasks.*;

class InMemoryTaskManagerTest {

    @Test
    void createTask() {
        InMemoryTaskManager manager = new InMemoryTaskManager();
        Task task = manager.createTask("a", "b", TaskStatus.NEW);
        assertNotNull(task);
        assertEquals("a", task.getTitle());
        assertEquals("b", task.getDescription());
        assertEquals(TaskStatus.NEW, task.getTaskStatus());
        assertEquals(task, manager.getTaskById(task.getId()));
    }

    @Test
    void getTaskById() {
        InMemoryTaskManager manager = new InMemoryTaskManager();
        Task task = manager.createTask("a", "b", TaskStatus.NEW);
        assertEquals(task, manager.getTaskById(task.getId()));
        assertNull(manager.getTaskById(-1));
    }

    @Test
    void getAllTasks() {
        InMemoryTaskManager manager = new InMemoryTaskManager();
        Task t1 = manager.createTask("a", "b", TaskStatus.NEW);
        Task t2 = manager.createTask("a", "b", TaskStatus.NEW);
        ArrayList<Task> tasks = manager.getAllTasks();
        assertEquals(2, tasks.size());
        assertTrue(tasks.contains(t1));
        assertTrue(tasks.contains(t2));
    }

    @Test
    void updateTask() {
        InMemoryTaskManager manager = new InMemoryTaskManager();
        Task task = manager.createTask("a", "b", TaskStatus.NEW);
        task.setTitle("x");
        task.setDescription("y");
        task.setTaskStatus(TaskStatus.DONE);
        assertTrue(manager.updateTask(task));
        Task updated = manager.getTaskById(task.getId());
        assertEquals("x", updated.getTitle());
        assertEquals("y", updated.getDescription());
        assertEquals(TaskStatus.DONE, updated.getTaskStatus());
    }

    @Test
    void updateNonExistentTaskShouldReturnFalse() {
        InMemoryTaskManager manager = new InMemoryTaskManager();
        Task ghost = new Task("a", TaskStatus.NEW, "b");
        ghost.setId(999);
        assertFalse(manager.updateTask(ghost));
    }

    @Test
    void deleteTask() {
        InMemoryTaskManager manager = new InMemoryTaskManager();
        Task task = manager.createTask("a", "b", TaskStatus.NEW);
        assertTrue(manager.deleteTask(task.getId()));
        assertNull(manager.getTaskById(task.getId()));
        assertFalse(manager.deleteTask(999));
    }

    @Test
    void deleteAllTasks() {
        InMemoryTaskManager manager = new InMemoryTaskManager();
        manager.createTask("a", "b", TaskStatus.NEW);
        manager.createTask("a", "b", TaskStatus.NEW);
        manager.deleteAllTasks();
        assertTrue(manager.getAllTasks().isEmpty());
    }

    @Test
    void createEpic() {
        InMemoryTaskManager manager = new InMemoryTaskManager();
        Epic epic = manager.createEpic(new Epic("e", "d"));
        assertNotNull(manager.getEpicById(epic.getId()));
        assertEquals(TaskStatus.NEW, manager.getEpicById(epic.getId()).getTaskStatus());
        assertTrue(manager.getSubtasksOfEpic(epic.getId()).isEmpty());
    }

    @Test
    void getEpicById() {
        InMemoryTaskManager manager = new InMemoryTaskManager();
        Epic epic = manager.createEpic(new Epic("e", "d"));
        assertEquals(epic, manager.getEpicById(epic.getId()));
        assertNull(manager.getEpicById(-1));
    }

    @Test
    void getAllEpics() {
        InMemoryTaskManager manager = new InMemoryTaskManager();
        Epic e1 = manager.createEpic(new Epic("a", "b"));
        Epic e2 = manager.createEpic(new Epic("c", "d"));
        ArrayList<Epic> epics = manager.getAllEpics();
        assertEquals(2, epics.size());
        assertTrue(epics.contains(e1));
        assertTrue(epics.contains(e2));
    }

    @Test
    void updateEpic() {
        InMemoryTaskManager manager = new InMemoryTaskManager();
        Epic epic = manager.createEpic(new Epic("a", "b"));
        epic.setTitle("x");
        epic.setDescription("y");
        manager.updateEpic(epic);
        Epic updated = manager.getEpicById(epic.getId());
        assertEquals("x", updated.getTitle());
        assertEquals("y", updated.getDescription());
        assertEquals(TaskStatus.NEW, updated.getTaskStatus());
    }

    @Test
    void deleteEpicShouldAlsoDeleteSubtasks() {
        InMemoryTaskManager manager = new InMemoryTaskManager();
        Epic epic = manager.createEpic(new Epic("a", "b"));
        Subtask s = manager.createSubtask(new Subtask("s", TaskStatus.NEW, "d", epic.getId()));
        manager.deleteEpic(epic.getId());
        assertTrue(manager.getAllEpics().isEmpty());
        assertTrue(manager.getAllSubtasks().isEmpty());
    }

    @Test
    void deleteAllEpicsShouldAlsoDeleteAllSubtasks() {
        InMemoryTaskManager manager = new InMemoryTaskManager();
        Epic e1 = manager.createEpic(new Epic("a", "b"));
        manager.createSubtask(new Subtask("s1", TaskStatus.NEW, "d", e1.getId()));
        Epic e2 = manager.createEpic(new Epic("c", "d"));
        manager.createSubtask(new Subtask("s2", TaskStatus.NEW, "d", e2.getId()));
        manager.deleteAllEpics();
        assertTrue(manager.getAllEpics().isEmpty());
        assertTrue(manager.getAllSubtasks().isEmpty());
    }

    @Test
    void createSubtask() {
        InMemoryTaskManager manager = new InMemoryTaskManager();
        Epic epic = manager.createEpic(new Epic("a", "b"));
        Subtask sub = manager.createSubtask(new Subtask("s", TaskStatus.NEW, "d", epic.getId()));
        assertNotNull(manager.getSubtaskById(sub.getId()));
        assertTrue(manager.getSubtaskIdsOfEpic(epic.getId()).contains(sub.getId()));
    }

    @Test
    void getSubtaskById() {
        InMemoryTaskManager manager = new InMemoryTaskManager();
        Epic epic = manager.createEpic(new Epic("a", "b"));
        Subtask sub = manager.createSubtask(new Subtask("s", TaskStatus.NEW, "d", epic.getId()));
        assertEquals(sub, manager.getSubtaskById(sub.getId()));
        assertNull(manager.getSubtaskById(-1));
    }

    @Test
    void getAllSubtasks() {
        InMemoryTaskManager manager = new InMemoryTaskManager();
        Epic epic = manager.createEpic(new Epic("a", "b"));
        Subtask s1 = manager.createSubtask(new Subtask("s1", TaskStatus.NEW, "d", epic.getId()));
        Subtask s2 = manager.createSubtask(new Subtask("s2", TaskStatus.NEW, "d", epic.getId()));
        ArrayList<Subtask> subs = manager.getAllSubtasks();
        assertEquals(2, subs.size());
        assertTrue(subs.contains(s1));
        assertTrue(subs.contains(s2));
    }

    @Test
    void updateSubtaskAndEpicStatus() {
        InMemoryTaskManager manager = new InMemoryTaskManager();
        Epic epic = manager.createEpic(new Epic("a", "b"));
        Subtask sub = manager.createSubtask(new Subtask("s", TaskStatus.NEW, "d", epic.getId()));
        sub.setTaskStatus(TaskStatus.DONE);
        assertTrue(manager.updateSubtask(sub));
        assertEquals(TaskStatus.DONE, manager.getSubtaskById(sub.getId()).getTaskStatus());
        assertEquals(TaskStatus.DONE, manager.getEpicById(epic.getId()).getTaskStatus());
    }

    @Test
    void deleteSubtask() {
        InMemoryTaskManager manager = new InMemoryTaskManager();
        Epic epic = manager.createEpic(new Epic("a", "b"));
        Subtask sub = manager.createSubtask(new Subtask("s", TaskStatus.NEW, "d", epic.getId()));
        assertNotNull(manager.getSubtaskById(sub.getId()));
        assertTrue(manager.deleteSubtask(sub.getId()));
        assertNull(manager.getSubtaskById(sub.getId()));
        assertFalse(manager.deleteSubtask(999));
    }

    @Test
    void deleteAllSubtasks() {
        InMemoryTaskManager manager = new InMemoryTaskManager();
        Epic epic = manager.createEpic(new Epic("a", "b"));
        manager.createSubtask(new Subtask("s1", TaskStatus.NEW, "d1", epic.getId()));
        manager.createSubtask(new Subtask("s2", TaskStatus.DONE, "d2", epic.getId()));
        manager.deleteAllSubtasks();
        assertTrue(manager.getAllSubtasks().isEmpty());
        assertTrue(manager.getSubtaskIdsOfEpic(epic.getId()).isEmpty());
        assertEquals(TaskStatus.NEW, manager.getEpicById(epic.getId()).getTaskStatus());
    }

    @Test
    void getSubtaskIdsOfEpic() {
        InMemoryTaskManager manager = new InMemoryTaskManager();
        Epic epic = manager.createEpic(new Epic("a", "b"));
        Subtask s1 = manager.createSubtask(new Subtask("s1", TaskStatus.NEW, "d1", epic.getId()));
        Subtask s2 = manager.createSubtask(new Subtask("s2", TaskStatus.NEW, "d2", epic.getId()));
        ArrayList<Integer> ids = manager.getSubtaskIdsOfEpic(epic.getId());
        assertEquals(2, ids.size());
        assertTrue(ids.contains(s1.getId()));
        assertTrue(ids.contains(s2.getId()));
    }

    @Test
    void getSubtasksOfEpic() {
        InMemoryTaskManager manager = new InMemoryTaskManager();
        Epic epic = manager.createEpic(new Epic("a", "b"));
        Subtask s1 = manager.createSubtask(new Subtask("s1", TaskStatus.NEW, "d1", epic.getId()));
        Subtask s2 = manager.createSubtask(new Subtask("s2", TaskStatus.NEW, "d2", epic.getId()));
        ArrayList<Subtask> subs = manager.getSubtasksOfEpic(epic.getId());
        assertEquals(2, subs.size());
        assertTrue(subs.contains(s1));
        assertTrue(subs.contains(s2));
    }

    @Test
    void tasksWithSameIdShouldBeEqual() {
        Task t1 = new Task("a", TaskStatus.NEW, "d");
        Task t2 = new Task("b", TaskStatus.DONE, "x");
        t1.setId(1);
        t2.setId(1);
        assertEquals(t1, t2);
    }

    @Test
    void epicStatusShouldRecalculate() {
        InMemoryTaskManager manager = new InMemoryTaskManager();
        Epic epic = manager.createEpic(new Epic("e", "d"));
        Subtask s1 = manager.createSubtask(new Subtask("s1", TaskStatus.NEW, "d1", epic.getId()));
        Subtask s2 = manager.createSubtask(new Subtask("s2", TaskStatus.NEW, "d2", epic.getId()));
        assertEquals(TaskStatus.NEW, manager.getEpicById(epic.getId()).getTaskStatus());

        s1.setTaskStatus(TaskStatus.DONE);
        manager.updateSubtask(s1);
        assertEquals(TaskStatus.IN_PROGRESS, manager.getEpicById(epic.getId()).getTaskStatus());

        s2.setTaskStatus(TaskStatus.DONE);
        manager.updateSubtask(s2);
        assertEquals(TaskStatus.DONE, manager.getEpicById(epic.getId()).getTaskStatus());
    }

    @Test
    void epicCannotBeOwnSubtask() {
        InMemoryTaskManager manager = new InMemoryTaskManager();
        Epic epic = manager.createEpic(new Epic("e", "d"));
        Subtask fake = new Subtask("f", TaskStatus.NEW, "zzz", epic.getId());
        fake.setId(epic.getId());
        assertNull(manager.createSubtask(fake));
    }

    @Test
    void subtaskCannotReferToItself() {
        InMemoryTaskManager manager = new InMemoryTaskManager();
        Subtask sub = new Subtask("s", TaskStatus.NEW, "d", -1);
        assertNull(manager.createSubtask(sub));
    }


}
