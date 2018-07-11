package test.frontEnd;

import backEnd.ListCondition;
import backEnd.SetTasker;
import backEnd.TaskManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

class SetTaskerStressTest {
    private TaskManager tasker;




    @BeforeEach
    void setUp() {
        tasker = new SetTasker();
        for(int i=0; i<10000; i++){
            tasker.add("test", LocalDate.now());
            tasker.getTasks(ListCondition.ALL);
        }
    }



    @Test
    void stressCompleteTest() {

        for (int i = 0; i < 10000; i++) {
            tasker.complete(i);
            tasker.getTasks(ListCondition.ALL);
        }
    }
    @Test
    void stressAcTest() {
        tasker.ac();
        tasker.getTasks(ListCondition.ALL);
    }
    @Test
    void stressDeleteTest() {
        for (int i = 2000; i < 4000; i++) {
            tasker.delete(i);
            tasker.getTasks(ListCondition.ALL);
        }
    }
    @Test
    void stressEditTest(){
        for(int i=8000; i<10000; i++){
            tasker.editTask(i, "test2", LocalDate.now().plusYears(1), true);
            tasker.getTasks(ListCondition.ALL);
        }

    }



}