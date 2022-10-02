package ru.rsreu;

import java.util.ArrayList;
import java.util.List;

public class TaskExecutor {
    private static List<Task> tasks;

    public TaskExecutor() {
        tasks = new ArrayList<>();
    }

    public String getAllInfo() {
        StringBuilder builder = new StringBuilder();
        for (Task task : tasks) {
            builder.append(task.toString());
            builder.append("\n");
        }
        return builder.toString();
    }

    public int createTask(HeavyCircleSquareCalculator func) {
        Task task = new Task(tasks.size() + 1, func);
        task.start();
        tasks.add(task);
        return task.getId();
    }

    public void stopTask(int taskId) throws IllegalArgumentException {
        if (taskId > tasks.size()) {
            throw new IllegalArgumentException("There isn't task with id " + taskId);
        }
        tasks.get(taskId - 1).stop();
    }

    public void awaitTask(int taskId) throws IllegalArgumentException {
        if (taskId > tasks.size()) {
            throw new IllegalArgumentException("There isn't task with id " + taskId);
        }
        tasks.get(taskId - 1).join();
    }
}
