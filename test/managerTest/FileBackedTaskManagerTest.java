package managerTest;

import taskmanager.manager.FileBackedTaskManager;
import taskmanager.tasks.*;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.nio.file.Files;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class FileBackedTaskManagerTest {

    @Test
    void save_emptyManager_writesHeaderAndSeparator() throws Exception {
        File tmp = File.createTempFile("kanban", ".csv");
        tmp.deleteOnExit();

        FileBackedTaskManager m = new FileBackedTaskManager(tmp.toPath()); // если есть такой конструктор
        m.deleteAllTasks();
        m.deleteAllEpics();
        m.deleteAllSubtasks();

        List<String> lines = Files.readAllLines(tmp.toPath());
        assertFalse(lines.isEmpty());
        assertEquals("id,type,name,status,description,epic", lines.get(0));
    }

    @Test
    void saveAndLoad_cycle_restoresAllEntities() throws Exception {
        File tmp = File.createTempFile("kanban", ".csv");
        tmp.deleteOnExit();

        FileBackedTaskManager m1 = new FileBackedTaskManager(tmp.toPath());
        Task t = m1.createTask("T1", "D1", TaskStatus.NEW);
        Epic e = m1.createEpic(new Epic("E1", "ED1"));
        Subtask s = m1.createSubtask(new Subtask("S1", TaskStatus.DONE, "SD1", e.getId()));

        FileBackedTaskManager m2 = new FileBackedTaskManager(tmp.toPath());
        m2.loadFromFile(tmp);

        Task t2 = m2.getTaskById(t.getId());
        Epic e2 = m2.getEpicById(e.getId());
        Subtask s2 = m2.getSubtaskById(s.getId());

        assertEquals(t.getTitle(), t2.getTitle());
        assertEquals(e.getTitle(), e2.getTitle());
        assertEquals(s.getEpicId(), s2.getEpicId());
        assertEquals(TaskStatus.DONE, s2.getTaskStatus());
    }

    @Test
    void updateThenSave_shouldPersistChanges() throws Exception {
        File tmp = File.createTempFile("kanban", ".csv");
        tmp.deleteOnExit();

        FileBackedTaskManager m = new FileBackedTaskManager(tmp.toPath());
        Task t = m.createTask("T1", "D1", TaskStatus.NEW);

        t.setTaskStatus(TaskStatus.DONE);
        m.updateTask(t);

        List<String> lines = Files.readAllLines(tmp.toPath());
        String row = lines.stream().filter(l -> l.startsWith(t.getId() + ",")).findFirst().orElseThrow();
        assertTrue(row.contains(",DONE,"));
    }
}
