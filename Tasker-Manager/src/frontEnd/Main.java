package frontEnd;

import backEnd.SetTasker;
import backEnd.Task;
import backEnd.TaskManager;
import frontEnd.utils.AlertHandler;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {
    private TaskManager tasker;
    private TaskController taskController;
    private Stage primaryStage;


    @Override
    public void start(Stage primaryStage) {
        try {
            //Load the fxml file and creat e a new stage for the task program.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("TaskerOverview.fxml"));
            Parent root = loader.load();

            //Set the stage for the task program.
            primaryStage.setTitle("Task Manager");
            primaryStage.getIcons().add(new Image("icon.png"));
            primaryStage.setScene(new Scene(root, 700, 775));
            primaryStage.setResizable(false);
            primaryStage.show();

            //Save the variables into the main class.
            tasker = new SetTasker();
            this.primaryStage = primaryStage;
            this.taskController = loader.getController();
            taskController.setMainApp(this);
        }catch (IOException e) {
            e.printStackTrace();
            AlertHandler.sendErrorAlert(primaryStage, "Error loading FXML file");
        }

    }

    public void showEditDialog(Task task) {
        try {
            //Load the fxml file and create a new stage for the popup dialog.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("TaskEditDialog.fxml"));
            Parent root = loader.load();

            //Create the dialog Stage.
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Edit Task");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(primaryStage);
            dialogStage.setScene(new Scene(root, 400, 400));

            //Set the task and all important variables into the taskController.
           EditController controller = loader.getController();
           controller.setMainApp(this);
            controller.setDialogStage(dialogStage);
            controller.setTask(task);
            controller.setTaskManager(tasker);
            controller.setTaskController(taskController);
            //Show de dialog and wait until the user closes it.
            dialogStage.showAndWait();

        } catch (IOException e) {
            e.printStackTrace();
            AlertHandler.sendErrorAlert(primaryStage, "Error loading FXML file");
        }
    }

    public TaskManager getTasker() {
        return tasker;
    }
    void setTasker(TaskManager tasker) {
        this.tasker = tasker;
    }

    public TaskController getTaskController() {
        return taskController;
    }
    Stage getPrimaryStage(){ return primaryStage; }

    public static void main(String[] args) {
        launch(args);
    }
}
