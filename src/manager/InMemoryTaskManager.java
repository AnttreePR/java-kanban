package manager;

import tasks.Epic;
import tasks.Subtask;
import tasks.Task;
import tasks.TaskStatus;

import java.util.HashMap;
import java.util.ArrayList;
import java.util.LinkedHashSet;


public class InMemoryTaskManager implements TaskManager {
    private final HashMap<Integer, Task> tasks = new HashMap<>();
    private final HashMap<Integer, Epic> epics = new HashMap<>();
    private final HashMap<Integer, Subtask> subtasks = new HashMap<>();
    private int nextId = 1;

    private int generateId() {
        return nextId++;
    }

    public Task createTask(String title, String description, TaskStatus taskStatus) {
        Task task = new Task(title, taskStatus, description);
        int id = generateId();
        task.setId(id);
        tasks.put(id, task);
        return task;
    }

    public Task createTask(Task task) {
        int id = generateId();
        task.setId(id);
        tasks.put(id, task);
        return task;
    }

    public Task getTaskById(int id) {
        InMemoryHistoryManager historyManager = new InMemoryHistoryManager();
        Task task = tasks.get(id);
        historyManager.add(task);
        return tasks.get(id);
    }

    public ArrayList<Task> getAllTasks() {
        return new ArrayList<>(tasks.values());
    }

    public boolean updateTask(Task task) {
        int id = task.getId();
        if (!tasks.containsKey(id)) return false;
        tasks.put(id, task);
        return true;
    }

    public boolean deleteTask(int id) {
        if (!tasks.containsKey(id)) return false;
        tasks.remove(id);
        return true;
    }

    public void deleteAllTasks() {
        tasks.clear();
    }

    private void recalcEpicStatus(int epicId) {
        Epic epic = epics.get(epicId);
        if (epic == null) return;
        LinkedHashSet<Integer> list = epic.getSubtaskIds();
        if (list.isEmpty()) {
            epic.setTaskStatus(TaskStatus.NEW);
            return;
        }
        boolean allNew = true;
        boolean allDone = true;
        for (Integer subtaskId : list) {
            Subtask subtask = subtasks.get(subtaskId);
            if (subtask == null) continue;
            switch (subtask.getTaskStatus()) {
                case IN_PROGRESS:
                    epic.setTaskStatus(TaskStatus.IN_PROGRESS);
                    return;
                case DONE:
                    allNew = false;
                    break;
                case NEW:
                    allDone = false;
                    break;
                default:
                    break;
            }
            if (allDone) {
                epic.setTaskStatus(TaskStatus.DONE);
            } else if (allNew) {
                epic.setTaskStatus(TaskStatus.NEW);
            } else {
                epic.setTaskStatus(TaskStatus.IN_PROGRESS);
            }
        }
    }

    public Epic createEpic(Epic epic) {
        int id = generateId();
        epic.setId(id);
        epic.setTaskStatus(TaskStatus.NEW);
        epics.put(id, epic);
        return epic;
    }

    public void updateEpic(Epic newEpic) {
        if (newEpic == null) return;
        int id = newEpic.getId();
        Epic epic = epics.get(id);
        if (epic == null) return;
        epic.setTitle(newEpic.getTitle());
        epic.setDescription(newEpic.getDescription());
        recalcEpicStatus(epic.getId());
    }

    public Epic getEpicById(int id) {
        InMemoryHistoryManager historyManager = new InMemoryHistoryManager();
        Epic epic = epics.get(id);
        historyManager.add(epic);
        return epics.get(id);
    }

    public ArrayList<Epic> getAllEpics() {
        return new ArrayList<>(epics.values());
    }

    public void deleteEpic(int id) {
        if (!epics.containsKey(id)) return;
        Epic epic = epics.get(id);
        for (Integer subtaskId : epic.getSubtaskIds()) {
            subtasks.remove(subtaskId);
        }
        epic.clearSubtaskIds();
        epics.remove(id);
    }

    public void deleteAllEpics() {
        epics.clear();
        subtasks.clear();
    }

    public Subtask createSubtask(Subtask s) {
        int epicId = s.getEpicId();
        if (!epics.containsKey(epicId)) return null;
        if (s.getId() == epicId) return null;
        int id = generateId();
        s.setId(id);
        subtasks.put(id, s);
        Epic epic = epics.get(epicId);
        epic.addSubtaskId(id);
        recalcEpicStatus(epicId);
        return s;
    }

    public Subtask getSubtaskById(int id) {
        InMemoryHistoryManager historyManager = new InMemoryHistoryManager();
        Subtask subtask = subtasks.get(id);
        historyManager.add(subtask);
        return subtasks.get(id);
    }

    public ArrayList<Subtask> getAllSubtasks() {
        return new ArrayList<>(subtasks.values());
    }

    public boolean updateSubtask(Subtask newSubtask) {
        if (newSubtask == null) return false;
        int id = newSubtask.getId();
        Subtask old = subtasks.get(id);
        if (old == null) return false;
        if (newSubtask.getEpicId() == id) return false;
        int oldEpicId = old.getEpicId();
        int newEpicId = newSubtask.getEpicId();
        if (oldEpicId != newEpicId) {
            if (!epics.containsKey(newEpicId)) return false;
            Epic oldEpic = epics.get(oldEpicId);
            if (oldEpic != null) oldEpic.removeSubtaskId(id);
            subtasks.put(id, newSubtask);
            Epic newEpic = epics.get(newEpicId);
            if (newEpic != null) newEpic.addSubtaskId(id);
            recalcEpicStatus(oldEpicId);
            recalcEpicStatus(newEpicId);
        } else {
            subtasks.put(id, newSubtask);
            recalcEpicStatus(newEpicId);
        }
        return true;
    }

    public boolean deleteSubtask(int id) {
        Subtask sub = subtasks.get(id);
        if (sub == null) return false;
        int epicId = sub.getEpicId();
        subtasks.remove(id);
        Epic epic = epics.get(epicId);
        if (epic != null) {
            epic.removeSubtaskId(id);
            recalcEpicStatus(epicId);
        }
        return true;
    }

    public void deleteAllSubtasks() {
        for (Epic epic : epics.values()) {
            epic.clearSubtaskIds();
            recalcEpicStatus(epic.getId());
        }
        subtasks.clear();
    }

    public ArrayList<Integer> getSubtaskIdsOfEpic(int epicId) {
        Epic epic = epics.get(epicId);
        if (epic == null) return new ArrayList<>();
        return new ArrayList<>(epic.getSubtaskIds());
    }

    public ArrayList<Subtask> getSubtasksOfEpic(int epicId) {
        Epic epic = epics.get(epicId);
        if (epic == null) return new ArrayList<>();
        ArrayList<Subtask> result = new ArrayList<>();
        for (Integer sid : epic.getSubtaskIds()) {
            Subtask st = subtasks.get(sid);
            if (st != null) result.add(st);
        }
        return result;
    }

}
