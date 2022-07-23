package org.kamenkov.java_kanban.utils.adapters;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import org.kamenkov.java_kanban.managers.InMemoryHistoryManager;
import org.kamenkov.java_kanban.task.Task;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class HistoryAdapter extends TypeAdapter<InMemoryHistoryManager> {
    final Gson gson = new Gson();

    @Override
    public void write(JsonWriter jsonWriter, InMemoryHistoryManager manager) throws IOException {
        jsonWriter.jsonValue(gson.toJson(manager.getHistory(), new TypeToken<List<Task>>() {}.getType()));
    }

    @Override
    public InMemoryHistoryManager read(JsonReader jsonReader) {
        InMemoryHistoryManager manager = new InMemoryHistoryManager();
        List<Task> history = gson.fromJson(jsonReader, new TypeToken<ArrayList<Task>>() {}.getType());
        for (Task task : history) {
            manager.add(task);
        }
        return manager;
    }
}