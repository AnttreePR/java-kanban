package taskManager.manager;

import taskManager.api.manager.TaskManager;

public class Managers {
    public static TaskManager getDefault() {
        return new InMemoryTaskManager();
    }
}
