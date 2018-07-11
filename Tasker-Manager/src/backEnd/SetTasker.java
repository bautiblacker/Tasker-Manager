package backEnd;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

public class SetTasker implements TaskManager {
    private Set<Task> tasks = new TreeSet<>();
    private int counter = 1;

    @Override
    public void add(String description) {
        tasks.add(new Task(description, getNextId()));
    }

    @Override
    public void add(String description, LocalDate date) {
        tasks.add(new Task(description, date, getNextId()));
    }

    @Override
    public void delete(int id) {
        tasks.removeIf(task -> task.getId() == id);
    }

    @Override
    public void complete(int id) {
        Task aux = null;
        for(Task task : tasks){
            if (task.getId() == id){
                aux = task;
                tasks.remove(task);
                break;
            }
        }
        if(aux != null) {
            aux.complete();
            tasks.add(aux);
        }

    }

    @Override
    public void ac() {
        tasks.removeIf(Task::isCompleted);
    }


    @Override
    public List<Task> findTasks(String description) {
        return  tasks
                .stream()
                .filter(task -> task.getDescription().toUpperCase().contains(description.toUpperCase()))
                .collect(Collectors.toList());
    }

    @Override
    public List<Task> getTasks(ListCondition listCondition) {
        return  tasks
                .stream()
                .filter(listCondition.getPredicate())
                .collect(Collectors.toList());
    }

    @Override
    public void editTask(int id, String description, LocalDate date, boolean completed) {
        tasks.removeIf(task -> task.getId() == id);
        Task aux;
        if(date == null)
            aux = new Task(description, id);
        else
            aux = new Task(description, date, id);
        if(completed)
            aux.complete();
        tasks.add(aux);
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
