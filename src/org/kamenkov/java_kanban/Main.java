package org.kamenkov.java_kanban;

import org.kamenkov.java_kanban.managers.InMemoryTaskManager;
import org.kamenkov.java_kanban.managers.TaskManager;
import org.kamenkov.java_kanban.task.*;

public class Main {
    public static void main(String[] args) {
        //TODO: create tests
        TaskManager taskManager = new InMemoryTaskManager();
        Long firstEpicId = taskManager.createEpic(new EpicImpl("Первый эпик", "Описание первого эпика"));
        Subtask firstSubtaskOfFirstEpic = new SubtaskImpl("Первая сабтаска первого эпика",
                "Описание первой сабтаски первого эпика", firstEpicId);
        taskManager.createSubtask(firstSubtaskOfFirstEpic);
        Subtask secondSubtaskOfFirstEpic = new SubtaskImpl("Вторая сабтаска первого эпика",
                "Описание второй сабтаски первого эпика", firstEpicId);
        taskManager.createSubtask(secondSubtaskOfFirstEpic);
        Long secondEpicId = taskManager.createEpic(new EpicImpl("Второй эпик", "Описание второго эпика"));
        Subtask firstSubtaskOfSecondEpic = new SubtaskImpl("Первая сабтаска второго эпика",
                "Описание первой сабтаски второго эпика", secondEpicId);
        taskManager.createSubtask(firstSubtaskOfSecondEpic);
        printAllTasks(taskManager);
        firstSubtaskOfFirstEpic = taskManager.getSubtaskObject(2L);
        firstSubtaskOfFirstEpic.setStatus(Status.IN_PROGRESS);
        taskManager.updateSubtask(firstSubtaskOfFirstEpic, 2L);
        firstSubtaskOfSecondEpic = taskManager.getSubtaskObject(5L);
        firstSubtaskOfSecondEpic.setStatus(Status.DONE);
        taskManager.updateSubtask(firstSubtaskOfSecondEpic, 5L);
        printAllTasks(taskManager);
        taskManager.removeEpic(1L);
        printAllTasks(taskManager);
    }

    private static void printAllTasks(TaskManager taskManager) {
        System.out.println("=========================================");
        System.out.println("Задачи: " + taskManager.getAllTaskObjects());
        System.out.println("Эпики: " + taskManager.getAllEpicObjects());
        System.out.println("Подзадачи: " + taskManager.getAllSubtaskObjects());
    }
}
