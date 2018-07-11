package frontEnd.buttons;

import backEnd.Task;
import frontEnd.Main;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class EditButton extends Button {
    private static final int width = 15;
    private static final int height = 15;
    public EditButton(Main main, Task task){
        setGraphic(new ImageView(new Image("menu.png", width, height, true, true)));
        setOnAction(e -> main.showEditDialog(task));
    }
}