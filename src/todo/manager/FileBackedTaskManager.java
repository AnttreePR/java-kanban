package todo.manager;

import todo.convertor.CSVTaskConverter;
import todo.exceptions.ManagerReadException;
import todo.exceptions.ManagerSaveException;
import todo.tasks.*;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;


public class FileBackedTaskManager extends InMemoryTaskManager {

    private final Path storage;

    public FileBackedTaskManager(Path storage) {
        this.storage = storage;
    }

    //tasks
    @Override
    public Task createTask(String title, String description, TaskStatus taskStatus) {
        Task task = super.createTask(title, description, taskStatus);
        save();
        return task;
    }

    @Override
    public Task createTask(Task task) {
        Task created = super.createTask(task);
        save();
        return created;
    }

    @Override
    public boolean updateTask(Task task) {
        boolean updateResult = super.updateTask(task);
        if (updateResult) save();
        return updateResult;
    }

    @Override
    public boolean deleteTask(int id) {
        boolean deleteResult = super.deleteTask(id);
        if (deleteResult) save();
        return deleteResult;
    }

    @Override
    public void deleteAllTasks() {
        super.deleteAllTasks();
        save();
    }

    //epics
    @Override
    public Epic createEpic(Epic epic) {
        Epic created = super.createEpic(epic);
        save();
        return created;
    }

    @Override
    public void updateEpic(Epic newEpic) {
        super.updateEpic(newEpic);
        save();
    }

    @Override
    public void deleteEpic(int id) {
        super.deleteEpic(id);
        save();
    }

    @Override
    public void deleteAllEpics() {
        super.deleteAllEpics();
        save();
    }


    //subtasks
    @Override
    public Subtask createSubtask(Subtask subtask) {
        Subtask created = super.createSubtask(subtask);
        save();
        return created;
    }

    @Override
    public boolean updateSubtask(Subtask newSubtask) {
        boolean updateResult = super.updateSubtask(newSubtask);
        if (updateResult) save();
        return updateResult;
    }

    @Override
    public boolean deleteSubtask(int id) {
        boolean deleteResult = super.deleteSubtask(id);
        if (deleteResult) save();
        return deleteResult;
    }

    @Override
    public void deleteAllSubtasks() {
        super.deleteAllSubtasks();
        save();
    }

    protected void save() {
        try (var writer = Files.newBufferedWriter(storage, StandardCharsets.UTF_8)) {
            writer.write("id,type,name,status,description,epic");
            writer.newLine();

            for (Task task : tasks.values()) {
                writer.write(CSVTaskConverter.toString(task));
                writer.newLine();
            }

            for (Epic epic : epics.values()) {
                writer.write(CSVTaskConverter.toString(epic));
                writer.newLine();
            }

            for (Subtask subtask : subtasks.values()) {
                writer.write(CSVTaskConverter.toString(subtask));
                writer.newLine();
            }

            writer.newLine();

        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка при сохранении данных в файл", e);
        }
    }

    public void loadFromFile(File file) {
        try (var reader = new BufferedReader(new FileReader(file))) {
            reader.readLine(); // пропускаем заголовок
            String line;
            while ((line = reader.readLine()) != null && !line.isEmpty()) {

                Task task = CSVTaskConverter.fromString(line);

                if (task instanceof Epic epic) {
                    epics.put(epic.getId(), epic);
                } else if (task instanceof Subtask subtask) {
                    subtasks.put(subtask.getId(), subtask);
                } else {
                    tasks.put(task.getId(), task);
                }

            }
        } catch (IOException e) {
            throw new ManagerReadException("Ошибка при чтении данных из файла", e);
        }
    }

}




