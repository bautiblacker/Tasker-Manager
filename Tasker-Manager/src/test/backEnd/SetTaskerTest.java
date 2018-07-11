package test.backEnd;

import backEnd.ListCondition;
import backEnd.SetTasker;
import backEnd.Task;
import backEnd.TaskManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SetTaskerTest {
    private TaskManager tasker;
    private Task noDateTask = new Task("no date", 1);
    private Task withBeforeDateTask = new Task("overdue task", LocalDate.now().minusYears(1), 2);
    private Task withAfterDateTask = new Task("due task", LocalDate.now().plusYears(1), 3);
    private Task withTodayDateTask = new Task("due today task", LocalDate.now(), 4);



    @BeforeEach
    void setUp() {
        tasker = new SetTasker();
    }

    @Test
    void delete() {
        addAll();
        tasker.delete(2);
        List<Task> expected = Arrays.asList(noDateTask, withTodayDateTask, withAfterDateTask);
        assertArrayEquals(expected.toArray(), tasker.getTasks(ListCondition.ALL).toArray());
    }

    @Test
    void complete() {
        addAll();
        tasker.complete(2);
        assert(tasker.getTasks(ListCondition.ALL).get(3).isCompleted());
    }

    @Test
    void ac() {
        addAll();
        tasker.complete(1);
        tasker.complete(2);
        tasker.complete(3);
        tasker.ac();
        assertEquals(withTodayDateTask, tasker.getTasks(ListCondition.ALL).get(0));
    }

    @Test
    void findTasksCaseSensitive1() {
        Task allMinusTask = new Task("aa", 1);
        Task allMinusWithDate = new Task("aa", LocalDate.now(), 2);
        List<Task> expected = Arrays.asList(allMinusTask, allMinusWithDate);
        tasker.add("aa");
        tasker.add("aa", LocalDate.now());

        assertArrayEquals(expected.toArray(), tasker.findTasks("A").toArray());
    }

    @Test
    void findTaskCaseSensitive2() {
        Task allMinusTask = new Task("AA", 1);
        Task allMinusWithDate = new Task("AA", LocalDate.now(), 2);
        List<Task> expected = Arrays.asList(allMinusTask, allMinusWithDate);
        tasker.add("AA");
        tasker.add("AA", LocalDate.now());
        assertArrayEquals(expected.toArray(), tasker.findTasks("a").toArray());
    }

    @Test
    void findTasksNotConsecutiveStrings() {
        Task task = new Task("abc defg", 1);
        Task taskWithDate = new Task("abc defg", LocalDate.now(), 2);
        List<Task> expected = new ArrayList<>(); //No tasks should appear.
        tasker.add("abc defg");
        tasker.add("abc defg", LocalDate.now());
        assertArrayEquals(expected.toArray(), tasker.findTasks("ad").toArray());
        expected = Arrays.asList(task, taskWithDate); //Should recognize second word.
        assertArrayEquals(expected.toArray(), tasker.findTasks("def").toArray());

    }

    @Test
    void getTasks() {
        addAll();

        List<Task> expected = Arrays.asList(withTodayDateTask, withAfterDateTask);
        assertArrayEquals(expected.toArray(), tasker.getTasks(ListCondition.DUE).toArray());

        expected = Arrays.asList(withBeforeDateTask);
        assertArrayEquals(expected.toArray(), tasker.getTasks(ListCondition.OVERDUE).toArray());

        expected = Arrays.asList(withTodayDateTask);
        assertArrayEquals(expected.toArray(), tasker.getTasks(ListCondition.DUE_TODAY).toArray());

        expected = Arrays.asList(noDateTask, withBeforeDateTask, withTodayDateTask, withAfterDateTask);
        assertArrayEquals(expected.toArray(), tasker.getTasks(ListCondition.ALL).toArray());


    }

    private void addAll() {
        tasker.add("no date");
        tasker.add("overdue task", LocalDate.now().minusYears(1));
        tasker.add("due task", LocalDate.now().plusYears(1));
        tasker.add("due today task", LocalDate.now());
    }

}