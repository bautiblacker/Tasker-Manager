package frontEnd.buttons;
import backEnd.Task;
import frontEnd.Main;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;


public class CompleteButton extends Button {
    private static final int width = 15;
    private static final int height = 15;
    public CompleteButton(Main main, Task task) {
        boolean completed = task.isCompleted();
        if(completed)
            setGraphic(new ImageView(new Image("tick.png", width, height, true, true)));
        else
            setGraphic(new ImageView(new Image("cross.png", width, height, true, true)));
        setOnAction((event) -> {
            main.getTasker().complete(task.getId());
            main.getTaskController().writeToTable();
        });
    }
}
