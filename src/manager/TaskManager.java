package manager;

import tasks.Epic;
import tasks.Subtask;
import tasks.Task;
import tasks.TaskStatus;

import java.util.ArrayList;

public interface TaskManager {
    // Tasks
    Task createTask(String title, String description, TaskStatus taskStatus);
    Task createTask(Task task);
    Task getTaskById(int id);
    ArrayList<Task> getAllTasks();
    boolean updateTask(Task task);
    boolean deleteTask(int id);
    void deleteAllTasks();

    // Epics
    Epic createEpic(Epic epic);
    void updateEpic(Epic newEpic);
    Epic getEpicById(int id);
    ArrayList<Epic> getAllEpics();
    void deleteEpic(int id);
    void deleteAllEpics();

    // Subtasks
    Subtask createSubtask(Subtask s);
    Subtask getSubtaskById(int id);
    ArrayList<Subtask> getAllSubtasks();
    boolean updateSubtask(Subtask newSubtask);
    boolean deleteSubtask(int id);
    void deleteAllSubtasks();
    ArrayList<Integer> getSubtaskIdsOfEpic(int epicId);
    ArrayList<Subtask> getSubtasksOfEpic(int epicId);

}
