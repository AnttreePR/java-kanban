package TaskManager.manager;

import TaskManager.convertor.CSVTaskConverter;
import TaskManager.tasks.Task;
import TaskManager.tasks.TaskStatus;
import TaskManager.tasks.Epic;
import TaskManager.tasks.Subtask;

import java.io.File;
import java.nio.file.Path;

public class Main {

    public static void main(String[] args) {
        System.out.println("Поехали!");
        InMemoryTaskManager manager = new InMemoryTaskManager();

        Task task1 = manager.createTask("Задача 1", "Описание задачи 1", TaskStatus.NEW);
        Task task2 = manager.createTask("Задача 2", "Описание задачи 2", TaskStatus.NEW);

        Epic epic1 = manager.createEpic(new Epic("Эпик 1", "Описание эпика 1"));
        Subtask sub11 = manager.createSubtask(new Subtask(
                "Подзадача 1-1", TaskStatus.NEW, "Описание подзадачи 1-1", epic1.getId()
        ));
        Subtask sub12 = manager.createSubtask(new Subtask(
                "Подзадача 1-2", TaskStatus.NEW, "Описание подзадачи 1-2", epic1.getId()
        ));

        Epic epic2 = manager.createEpic(new Epic("Эпик 2", "Описание эпика 2"));
        Subtask sub21 = manager.createSubtask(new Subtask(
                "Подзадача 2-1", TaskStatus.NEW, "Описание подзадачи 2-1", epic2.getId()
        ));

        System.out.println("Начальные списки");
        System.out.println("Задачи: " + manager.getAllTasks());
        System.out.println("Эпики:     " + manager.getAllEpics());
        System.out.println("Подзадачи: " + manager.getAllSubtasks());

        task1.setTaskStatus(TaskStatus.IN_PROGRESS);
        manager.updateTask(task1);

        sub11.setTaskStatus(TaskStatus.IN_PROGRESS);
        manager.updateSubtask(sub11);

        task2.setTaskStatus(TaskStatus.DONE);
        manager.updateTask(task2);

        sub21.setTaskStatus(TaskStatus.DONE);
        manager.updateSubtask(sub21);

        System.out.println("\nПосле изменения статусов");
        System.out.println("Задачи: " + manager.getAllTasks());
        System.out.println("Эпики: " + manager.getAllEpics());
        System.out.println("Подзадачи: " + manager.getAllSubtasks());

        sub11.setTaskStatus(TaskStatus.DONE);
        manager.updateSubtask(sub11);
        sub12.setTaskStatus(TaskStatus.DONE);
        manager.updateSubtask(sub12);

        System.out.println("\nПосле завершения всех подзадач эпика 1");
        System.out.println("Эпики: " + manager.getAllEpics());

        manager.deleteTask(task2.getId());
        manager.deleteEpic(epic2.getId());

        System.out.println("\nПосле удаления задачи и эпика");
        System.out.println("Задачи: " + manager.getAllTasks());
        System.out.println("Эпики: " + manager.getAllEpics());
        System.out.println("Подзадачи: " + manager.getAllSubtasks());


        Task t1 = manager.createTask("Задача 1", "Описание 1", TaskStatus.NEW);
        Task t2 = manager.createTask("Задача 2", "Описание 2", TaskStatus.NEW);
        Epic epic = manager.createEpic(new Epic("Эпик 1", "Большой эпик"));
        Subtask s1 = manager.createSubtask(new Subtask("Сабтаск 1", TaskStatus.NEW, "Описание", epic.getId()));

        manager.getTaskById(t1.getId());
        manager.getEpicById(epic.getId());
        manager.getSubtaskById(s1.getId());
        manager.getTaskById(t2.getId());
        InMemoryHistoryManager historyManager = new InMemoryHistoryManager();

        System.out.println("История просмотров:");

        for (Task task : historyManager.getHistory()) {
            System.out.println(task);
        }

        System.out.println("\n=== Демонстрация FileBackedTaskManager ===");

        java.nio.file.Path storagePath = java.nio.file.Path.of("src", "TaskManager", "data", "tasks.csv");
        java.io.File storageFile = storagePath.toFile();

        FileBackedTaskManager fb = new FileBackedTaskManager(storagePath);

        if (storageFile.exists() && storageFile.isFile()) {
            fb.loadFromFile(storageFile);
            System.out.println("Загружено из файла: ");
            System.out.println("Задачи: " + fb.getAllTasks());
            System.out.println("Эпики: " + fb.getAllEpics());
            System.out.println("Подзадачи: " + fb.getAllSubtasks());
        }

        Task ft1 = fb.createTask("FB: Задача A", "Сохранится в файл", TaskStatus.NEW);
        Epic fe1 = fb.createEpic(new Epic("FB: Эпик A", "Эпик для файла"));
        Subtask fs1 = fb.createSubtask(new Subtask("FB: Сабтаск A1", TaskStatus.IN_PROGRESS, "внутри эпика A", fe1.getId()));

        System.out.println("\nСостояние FileBackedTaskManager (также в CSV):");
        System.out.println("Задачи: " + fb.getAllTasks());
        System.out.println("Эпики: " + fb.getAllEpics());
        System.out.println("Подзадачи: " + fb.getAllSubtasks());

        FileBackedTaskManager fbReloaded = new FileBackedTaskManager(storagePath);
        fbReloaded.loadFromFile(storageFile);

        System.out.println("\nПосле перезагрузки из CSV:");
        System.out.println("Задачи: " + fbReloaded.getAllTasks());
        System.out.println("Эпики: " + fbReloaded.getAllEpics());
        System.out.println("Подзадачи: " + fbReloaded.getAllSubtasks());

    }
}
