package org.kamenkov.java_kanban.managers;

import org.kamenkov.java_kanban.task.Epic;
import org.kamenkov.java_kanban.task.Subtask;
import org.kamenkov.java_kanban.task.Task;

import java.util.Collection;

public interface TaskManager {

    /**
     * Returns all task objects.
     *
     * @return {@link Collection} of {@link Task}.
     */
    Collection<Task> getAllTaskObjects();

    /**
     * Returns all epic objects.
     *
     * @return {@link Collection} of {@link Epic}.
     */
    Collection<Epic> getAllEpicObjects();

    /**
     * Returns all subtask objects.
     *
     * @return {@link Collection} of {@link Subtask}.
     */
    Collection<Subtask> getAllSubtaskObjects();

    /**
     * Returns task object by its ID.
     *
     * @param id unique id of task.
     * @return {@link Task}.
     */
    Task getTaskObjectById(Long id);

    /**
     * Returns epic object by its ID.
     *
     * @param id unique id of epic.
     * @return {@link Epic}.
     */
    Epic getEpicObjectById(Long id);

    /**
     * Returns subtask object by its ID.
     *
     * @param id unique id of subtask.
     * @return {@link Subtask}.
     */
    Subtask getSubtaskObject(Long id);

    /**
     * Removes all {@link Task} from memory.
     */
    void removeAllTaskObjects();

    /**
     * Removes all {@link Epic} from memory.
     */
    void removeAllEpicObjects();

    /**
     * Removes all {@link Subtask} from memory.
     */
    void removeAllSubtaskObjects();

    /**
     * Put the given {@link Task} to memory.
     *
     * @param task that should be created.
     */
    Long createTask(Task task);

    /**
     * Put the given {@link Epic} to memory.
     *
     * @param epic that should be created.
     */
    Long createEpic(Epic epic);

    /**
     * Put the given {@link Subtask} to memory.
     *
     * @param subtask that should be created.
     */
    Long createSubtask(Subtask subtask);

    /**
     * Update the given {@link Task} in memory.
     *
     * @param task that should be saved.
     */
    void updateTask(Task task, Long id);

    /**
     * Update the given {@link Epic} in memory.
     *
     * @param epic that should be saved.
     */
    void updateEpic(Epic epic, Long id);

    /**
     * Update the given {@link Subtask} in memory.
     *
     * @param subtask that should be saved.
     */
    void updateSubtask(Subtask subtask, Long id);

    /**
     * Remove the given {@link Task} from memory.
     *
     * @param id of {@link Task} should be deleted.
     */
    void removeTask(Long id);

    /**
     * Remove the given {@link Epic} from memory.
     *
     * @param id of {@link Epic} should be deleted.
     */
    void removeEpic(Long id);

    /**
     * Remove the given {@link Subtask} from memory.
     *
     * @param id of {@link Subtask} should be deleted.
     */
    void removeSubtask(Long id);

    /**
     * Returns all {@link Subtask} from given parent object.
     *
     * @param epic subtasks of this should be retrieved.
     * @return {@link Collection} of {@link Subtask}.
     */
    Collection<Subtask> getSubtaskObjectsByParent(Epic epic);
}
