package taskmanager.convertor;

import taskmanager.tasks.*;

public class CSVTaskConverter {

    public static String toString(Task task) {
        String epicIdField = "";

        if (task instanceof Subtask) {
            epicIdField = String.valueOf(((Subtask) task).getEpicId());
        }

        return String.format("%d,%s,%s,%s,%s,%s",
                task.getId(),
                task.getClass().getSimpleName().toUpperCase(),
                task.getTitle(),
                task.getTaskStatus(),
                task.getDescription(),
                epicIdField

        );
    }

    public static Task fromString(String task) {
        String[] parametrs = task.split(",");
        TaskType type = TaskType.valueOf(parametrs[1]);
        switch (type) {
            case TASK:
                Task taskObj  = new Task(parametrs[2], TaskStatus.valueOf(parametrs[3]), parametrs[4]);
                taskObj.setId(Integer.parseInt(parametrs[0]));
                return taskObj;
            case EPIC:
                Epic epic = new Epic(parametrs[2], parametrs[4]);
                epic.setId(Integer.parseInt(parametrs[0]));
                return epic;
            case SUBTASK:
                Subtask subtask = new Subtask(parametrs[2], TaskStatus.valueOf(parametrs[3]), parametrs[4],
                        Integer.parseInt(parametrs[5]));
                subtask.setId(Integer.parseInt(parametrs[0]));
                return subtask;
            default:
                throw new IllegalArgumentException("Unknown task type: " + type);

        }
    }
}

