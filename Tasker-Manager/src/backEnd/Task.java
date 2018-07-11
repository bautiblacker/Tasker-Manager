package backEnd;
import java.io.Serializable;
import java.time.LocalDate;


public class Task implements Comparable<Task>, Serializable {
    private String description;
    private int id;
    private LocalDate date;
    private boolean completed;


    public Task(String description, int id) {
        this.description = description;
        this.completed = false;
        this.id = id;

        //Using MIN instead of null to facilitate comparator.
        //This should be addressed in the front-end.
        this.date = LocalDate.MIN;
    }

    public Task(String description, LocalDate date, int id) {
        this(description, id);
        this.date = date;

    }

    @Override
    public int compareTo(Task o) {
        int cmp = Boolean.compare(isCompleted(), o.isCompleted());
        if(cmp != 0)
            return cmp;
        cmp = getDate().compareTo(o.getDate());
        if(cmp != 0)
            return cmp;
        cmp = id - o.id;
        if(cmp != 0)
            return cmp;
        cmp = getDescription().compareTo(o.getDescription());
        if(cmp != 0)
            return cmp;
        return Boolean.compare(isCompleted(), o.isCompleted());
    }


    void complete() {
        completed = true;
    }

    public String getDescription() {
        return description;
    }

    public int getId() {
        return id;
    }

    public LocalDate getDate() {
        return date;
    }

    public boolean isCompleted() {
        return completed;
    }

    public boolean hasDate() {
        return !getDate().equals(LocalDate.MIN);
    }

    void setDescription(String description) {
        this.description = description;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    void setCompleted(boolean completed) {
        this.completed = completed;
    }

    @Override
    public String toString() {
        StringBuilder toReturn = new StringBuilder("Task[");
        toReturn.append(getId()).append("] ").append(getDescription());
        if(hasDate())
            toReturn.append(" {Due ").append(date).append("}");
        toReturn.append(" [").append(isCompleted()? "X":"").append("]");

        return toReturn.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!( o instanceof Task)) return false;
        Task task = (Task) o;
        return getId() == task.getId() &&
                getDescription().equals(task.getDescription()) &&
                getDate().equals(task.getDate()) &&
                isCompleted() == task.isCompleted();
    }


    @Override
    public int hashCode() {
        return getId();
    }
}
