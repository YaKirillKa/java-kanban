package org.kamenkov.java_kanban.managers;

import org.kamenkov.java_kanban.Status;
import org.kamenkov.java_kanban.exceptions.ManagerSaveException;
import org.kamenkov.java_kanban.task.Epic;
import org.kamenkov.java_kanban.task.Subtask;
import org.kamenkov.java_kanban.task.Task;
import org.kamenkov.java_kanban.task.Type;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

public class FileBackedTasksManager extends InMemoryTaskManager {

    private static final String DEFAULT_FILE_PATH = "backup.csv";
    private static final String HEADER = "id,type,name,status,description,start_date,duration,end_date,epic\n";

    /**
     * Returns new {@link FileBackedTasksManager} with initialized fields and all tasks.
     *
     * @param file {@link File} from data should be load.
     * @return initialized {@link FileBackedTasksManager}.
     */
    public static FileBackedTasksManager loadFromFile(File file) {
        FileBackedTasksManager fileBackedTasksManager = new FileBackedTasksManager();
        Long lastId = 0L;
        Map<Long, Task> allTasks = new HashMap<>();
        try (Reader reader = new FileReader(file); BufferedReader bf = new BufferedReader(reader)) {
            bf.readLine();
            while (bf.ready()) {
                final String line = bf.readLine();
                if (line.isBlank()) {
                    break;
                }
                Task task = fileBackedTasksManager.createTaskFromString(line);
                lastId = task.getId();
                if (task instanceof Epic) {
                    fileBackedTasksManager.epics.put(task.getId(), (Epic) task);
                } else if (task instanceof Subtask) {
                    fileBackedTasksManager.subtasks.put(task.getId(), (Subtask) task);
                    Subtask subtask = fileBackedTasksManager.getSubtaskObjectById(task.getId());
                    Epic parent = fileBackedTasksManager.getEpicObjectById(subtask.getParentId());
                    parent.addSubtask(subtask);
                } else {
                    fileBackedTasksManager.tasks.put(task.getId(), task);
                }
                fileBackedTasksManager.prioritizedTasks.add(task);
                allTasks.put(task.getId(), task);
            }
            fileBackedTasksManager.idManager = new InMemoryIdManager(lastId);
            final String historyLine = bf.readLine();
            if (historyLine != null) {
                fileBackedTasksManager.historyManager = new InMemoryHistoryManager();
                fileBackedTasksManager.fillHistoryManagerFromString(historyLine, allTasks);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return fileBackedTasksManager;
    }

    /**
     * Parses string and returns new task.
     *
     * @param value {@link String} with all fields.
     * @return new {@link Task} object.
     */
    private Task createTaskFromString(String value) {
        String[] values = value.split(",");
        Long id = Long.parseLong(values[0]);
        Type type = Type.valueOf(values[1]);
        String summary = values[2];
        Status status = Status.valueOf(values[3]);
        String description = values[4];
        String startDateString = values[5];
        LocalDateTime startDate = "null".equals(startDateString) ? null : LocalDateTime.parse(startDateString);
        long duration = Long.parseLong(values[6]);
        Long parentId = null;
        if (type == Type.SUBTASK) {
            parentId = Long.parseLong(values[8]);
        }
        Task task = null;
        switch (type) {
            case EPIC:
                task = new Epic(summary, description);
                break;
            case TASK:
                task = new Task(summary, description);
                break;
            case SUBTASK:
                task = new Subtask(summary, description, parentId);
                break;
        }
        Objects.requireNonNull(task);
        task.setId(id);
        task.setStatus(status);
        task.setStartDate(startDate);
        task.setDurationInMinutes(duration);
        return task;
    }

    /**
     * Fills {@link HistoryManager} with tasks.
     *
     * @param value {@link String} that should be parsed.
     * @param allTasks the map that contains all tasks.
     */
    private void fillHistoryManagerFromString(String value, Map<Long, Task> allTasks) {
        String[] values = value.split(",");
        for (String s : values) {
            historyManager.add(allTasks.get(Long.parseLong(s)));
        }
    }

    /**
     * Writes all tasks and history to file.
     */
    public void save(File file) {
        if (file == null) {
            file = new File(DEFAULT_FILE_PATH);
        }
        try (FileWriter writer = new FileWriter(file, StandardCharsets.UTF_8)) {
            if (!file.exists()) {
                file.createNewFile();
            }
            List<Task> tasks = new ArrayList<>();
            tasks.addAll(getAllEpicObjects());
            tasks.addAll(getAllTaskObjects());
            tasks.addAll(getAllSubtaskObjects());
            tasks.sort(Comparator.comparingLong(Task::getId));
            StringBuilder sb = new StringBuilder(HEADER);
            for (Task task : tasks) {
                sb.append(task.toString()).append("\n");
            }
            sb.append("\n");
            sb.append(getHistory().stream().map(Task::getId).map(String::valueOf).collect(Collectors.joining(",")));
            writer.write(sb.toString());
        } catch (IOException e) {
            throw new ManagerSaveException(e.getMessage());
        }
    }

    @Override
    public Task getTaskObjectById(Long id) {
        final Task task = super.getTaskObjectById(id);
        save(null);
        return task;
    }

    @Override
    public Epic getEpicObjectById(Long id) {
        final Epic epic = super.getEpicObjectById(id);
        save(null);
        return epic;
    }

    @Override
    public Subtask getSubtaskObjectById(Long id) {
        final Subtask subtask = super.getSubtaskObjectById(id);
        save(null);
        return subtask;
    }

    @Override
    public void removeAllTaskObjects() {
        super.removeAllTaskObjects();
        save(null);
    }

    @Override
    public void removeAllEpicObjects() {
        super.removeAllEpicObjects();
        save(null);
    }

    @Override
    public void removeAllSubtaskObjects() {
        super.removeAllSubtaskObjects();
        save(null);
    }

    @Override
    <T extends Task> Long createTask(T taskObject, Map<Long, T> map) {
        final Long task = super.createTask(taskObject, map);
        save(null);
        return task;
    }

    @Override
    <T extends Task> void updateTask(T taskObject, Map<Long, T> map, Long id) {
        super.updateTask(taskObject, map, id);
        save(null);
    }

    @Override
    <T extends Task> void removeEntryFromMap(Map<Long, T> map, Long id) {
        super.removeEntryFromMap(map, id);
        save(null);
    }
}
