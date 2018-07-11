package backEnd;

import java.time.LocalDate;
import java.util.function.Predicate;

public enum ListCondition {
    OVERDUE(task -> {
        if(task.hasDate())
            return LocalDate.now().isAfter(task.getDate());
        return false;
    }),
    DUE_TODAY(task -> {
        if(task.hasDate())
            return LocalDate.now().isEqual(task.getDate());
        return false;
    }),
    DUE(task -> {
        if(task.hasDate())
            return LocalDate.now().isEqual(task.getDate()) ||
                    LocalDate.now().isBefore(task.getDate());
        return false;
    }),
    ALL(task -> true);

    Predicate<Task> predicate;
    ListCondition(Predicate<Task> predicate) {
        this.predicate = predicate;
    }

    public Predicate<Task> getPredicate() {
        return predicate;
    }
}