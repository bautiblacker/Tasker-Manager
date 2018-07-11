package backEnd;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;


public interface TaskManager extends Serializable {
    void add(String description);
    void add(String description, LocalDate date);
    void delete(int id);
    void complete(int id);
    void ac();
    void editTask(int id, String description, LocalDate date, boolean completed);

    boolean isEmpty();
    void clear();
    
    List<Task> findTasks(String description);

    List<Task> getTasks(ListCondition listCondition);
}
