package frontEnd;

import backEnd.Task;
import backEnd.TaskManager;
import frontEnd.utils.AlertHandler;
import frontEnd.utils.Format;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

import java.time.LocalDate;


public class EditController {
    @FXML
    private TextField editTaskField;
    @FXML
    private DatePicker editDateField;
    @FXML
    private CheckBox completedBox;


    //Task being edited.
    private Task task;

    //Main app.
    private Main mainApp;

    //Popup edit dialog stage.
    private Stage dialogStage;

    //Task manager instance which holds all tasks.
    private TaskManager tasker;

    //Instance of the task TaskController.
    //Used to access the writeToTable method.
    private TaskController taskController;


    public EditController() {}
    void setMainApp(Main mainApp) {
        this.mainApp = mainApp;
    }

    public void initialize() {
        //Sets the date-picker format.
        Format.setDatePickerFormat(editDateField);
    }

    @FXML
    private void handleCancel() {
        dialogStage.close();
    }
    //Edits the task when the confirm button is clicked.
    @FXML
    private void handleEdit() {
        String description = editTaskField.getText();
        LocalDate date = editDateField.getValue();
        boolean completed = completedBox.isSelected();
        if(Format.validateTaskDescription(description)) {
            tasker.editTask(task.getId(), description, date, completed);
            //Used to refresh the list.
            taskController.writeToTable();
            dialogStage.close();
        }
        else
            AlertHandler.sendErrorAlert(mainApp.getPrimaryStage(), "Task description cannot be empty.");
    }

    //Deletes the task when the button is clicked.
    @FXML
    private void handleDelete() {
        boolean okClicked;
        okClicked = AlertHandler.sendConfirmationAlert(mainApp.getPrimaryStage(),
                "Are you sure you want to delete the task?\nThis operation cannot be undone.");

        if(okClicked) {
            tasker.delete(task.getId());
            taskController.writeToTable();
            dialogStage.close();
        }
    }

    @FXML
    private void handleKeyPressed(KeyEvent keyEvent) {
        if(keyEvent.getCode().equals(KeyCode.ENTER))
            handleEdit();
        if(keyEvent.getCode().equals(KeyCode.ESCAPE))
            handleCancel();
    }

    //Sets the stage of this dialog
    void setDialogStage(Stage dialogStage) {this.dialogStage = dialogStage; }

    //Sets the task to be edited in the dialog.
    public void setTask(Task task) {
        this.task = task;
        editTaskField.setText(task.getDescription());
        if(task.getDate().equals(LocalDate.MIN))
            editDateField.setValue(null);
        else
            editDateField.setValue(task.getDate());
        completedBox.setSelected(task.isCompleted());
    }


    //Called when the user clicks ok.
    void setTaskManager(TaskManager taskManager) {
        this.tasker = taskManager;
    }

    void setTaskController(TaskController taskController) {
        this.taskController = taskController;
    }
}
