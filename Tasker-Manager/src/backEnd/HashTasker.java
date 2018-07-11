package backEnd;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class HashTasker implements TaskManager {
    private Map<Integer, Task> tasks = new HashMap<>();
    private int counter = 1;

    @Override
    public void add(String description) {
        int id = getNextId();
        tasks.put(id, new Task(description, id));
    }

    @Override
    public void add(String description, LocalDate date) {
        int id = getNextId();
        tasks.put(id, new Task(description, date, id));
    }

    @Override
    public void delete(int id) {
        tasks.remove(id);
    }

    @Override
    public void complete(int id) {
        if(tasks.containsKey(id)) {
            tasks.get(id).complete();
        }
    }

    @Override
    public void ac() {
        tasks.entrySet().removeIf(entry -> entry.getValue().isCompleted());
    }


    @Override
    public List<Task> findTasks(String description) {
        return  tasks.values()
                .stream()
                .filter(task -> task.getDescription().toUpperCase().contains(description.toUpperCase()))
                .collect(Collectors.toList());
    }

    @Override
    public List<Task> getTasks(ListCondition listCondition) {
        return  tasks.values()
                .stream()
                .filter(listCondition.getPredicate())
                .sorted()
                .collect(Collectors.toList());
    }

    @Override
    public void editTask(int id, String description, LocalDate date, boolean completed) {
        if(description != null)
            tasks.get(id).setDescription(description);
        if(date != null)
            tasks.get(id).setDate(date);
        else
            tasks.get(id).setDate(LocalDate.MIN);
        tasks.get(id).setCompleted(completed);
    }

    @Override
    public boolean isEmpty() {
        return tasks.isEmpty();
    }

    @Override
    public void clear() {
        tasks.clear();
    }

    private int getNextId() {
        return counter++;
    }
}
