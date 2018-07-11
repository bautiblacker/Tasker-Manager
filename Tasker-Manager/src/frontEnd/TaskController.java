package frontEnd;

import backEnd.ListCondition;
import backEnd.Task;
import backEnd.TaskManager;
import frontEnd.buttons.CompleteButton;
import frontEnd.buttons.EditButton;
import frontEnd.utils.AlertHandler;
import frontEnd.utils.Format;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.*;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

public class TaskController {
    @FXML
    private DatePicker dateField;
    @FXML
    private TextField taskField;
    @FXML
    private TextField searchField;
    @FXML
    private MenuButton orderMenu;

    @FXML
    private TableView<Task> taskTable;
    @FXML
    private TableColumn<Task, String> descriptionColumn;
    @FXML
    private TableColumn<Task, String> dateColumn;
    @FXML
    private TableColumn<Task, CompleteButton> completedColumn;
    @FXML
    private TableColumn<Task, Button> editColumn;

    private FilteredList<Task> filteredData;
    private ObservableList<Task> masterData = FXCollections.observableArrayList();
    private ListCondition currentOrder = ListCondition.ALL;
    private Main mainApp;

    //Initialize method called when FXML is loaded.
    public void initialize() {
        //Sets the date-picker format.
        Format.setDatePickerFormat(dateField);
        //Initializes the TableView.
        setTableViewFormat();
        //Initializes the OrderMenu.
        setOrderMenuFormat();


    }

    //Loading and saving methods.
    private void loadData(File file) throws IOException, ClassNotFoundException {
        FileInputStream f = new FileInputStream(file);
        ObjectInputStream o = new ObjectInputStream(f);
        mainApp.setTasker((TaskManager) o.readObject());
        f.close();
        o.close();

    }
    private void saveData(File file) throws IOException {
        FileOutputStream f = new FileOutputStream(file);
        ObjectOutputStream o = new ObjectOutputStream(f);
        o.writeObject(getTasker());
        o.close();
        f.close();
    }

    //Handler methods. This methods are called as defined on the FXML.
    @FXML
    private void handleOpen() {
        //Sends the pending tasks warning if there are tasks showing.
        boolean okClicked = true;
        if(!getTasker().isEmpty()) {
           okClicked= AlertHandler.sendConfirmationAlert(mainApp.getPrimaryStage(),
                   "This list will be erased, are you sure you want to proceed?");
        }

        if(okClicked) {
            File selectedFile = getFileChooser().showOpenDialog(new Stage());
            if (selectedFile != null) {
                try {
                    //Empties the tasker, to load the new ones.
                    getTasker().clear();
                    //Loads the new tasks.
                    loadData(selectedFile);
                    writeToTable();
                } catch (Exception ex) {
                    //Catches any problems with loading the file and sends an alert.
                    AlertHandler.sendErrorAlert(mainApp.getPrimaryStage(), "Error loading file");
                }
            }
        }
    }
    @FXML
    private void handleSave() {
        File selectedFile = getFileChooser().showSaveDialog(new Stage());
        if(selectedFile != null) {
            try {
                saveData(selectedFile);
                AlertHandler.sendConfirmationAlert(mainApp.getPrimaryStage(),
                        "Task list successfully saved");
            }catch (Exception ex) {
                AlertHandler.sendErrorAlert(mainApp.getPrimaryStage(), "Error saving file");
            }
        }
    }
    @FXML
    private void handleQuit(){
        boolean okClicked = AlertHandler.sendConfirmationAlert(mainApp.getPrimaryStage(),
                "Are you sure you want to quit?");
        if(okClicked)
            System.exit(0);
    }
    @FXML
    private void handleAbout(){
        AlertHandler.sendInformationConfirmation(mainApp.getPrimaryStage(), "About",
                "Task Manager 1.0\nBy Bautista Blacker and Santiago Reyes");
    }
    @FXML
    public void handleAdd(){
        String description = taskField.getText();
        taskField.clear();
        if(!Format.validateTaskDescription(description)) {
            AlertHandler.sendErrorAlert(mainApp.getPrimaryStage(), "Task description cannot be empty.");
        }
        else {
            if (dateField.getValue() != null)
                getTasker().add(description, dateField.getValue());
            else
                getTasker().add(description);
            dateField.setValue(null);
            writeToTable();
        }
    }
    @FXML
    public void handleArchive(){
        getTasker().ac();
        writeToTable();
    }
    @FXML
    private void handleKeyPressed(KeyEvent keyEvent) {
        if(keyEvent.getCode().equals(KeyCode.ENTER))
            handleAdd();
    }


    void setMainApp(Main mainApp) {
        this.mainApp = mainApp;
    }
    private void setCurrentOrder(ListCondition currentOrder) {
        this.currentOrder = currentOrder;
    }
    private void setOrderMenuFormat(){
        MenuItem all = new OrderMenuItem( ListCondition.ALL, "All");
        MenuItem dueToday = new OrderMenuItem( ListCondition.DUE_TODAY, "Due Today");
        MenuItem overdue = new OrderMenuItem( ListCondition.OVERDUE, "Overdue");
        MenuItem due = new OrderMenuItem( ListCondition.DUE, "Due");
        List<MenuItem> menuItemList = Arrays.asList(all, due, dueToday, overdue);

        orderMenu.getItems().clear();
        orderMenu.setText("All");
        orderMenu.getItems().addAll(menuItemList);
    }
    private void setTableViewFormat() {
        descriptionColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getDescription()));

        dateColumn.setCellValueFactory(cellData -> {
            if(cellData.getValue().hasDate())
                return Format.formatTableDate(cellData.getValue().getDate());
            else
                return null;
        });

        completedColumn.setCellValueFactory(cellData ->
                new SimpleObjectProperty<>(new CompleteButton(mainApp, cellData.getValue())));

        editColumn.setCellValueFactory(cellData ->
                new SimpleObjectProperty<>(new EditButton(mainApp, cellData.getValue())));
    }
    private void setListener(FilteredList<Task> filteredData){
        this.filteredData = filteredData;
        searchField.textProperty().addListener(((observable, oldValue, newValue) -> filteredData.setPredicate(task -> {
            if(newValue == null || newValue.isEmpty())
                return true;
            String lowerCaseFilter = newValue.toLowerCase();

            return task.getDescription().toLowerCase().contains(lowerCaseFilter);
        })));
    }

    private TaskManager getTasker() {
        return mainApp.getTasker();
    }
    private FileChooser getFileChooser() {
        //Opens the file chooser at the default location as the current location.
        FileChooser chooser = new FileChooser();
        chooser.setInitialDirectory(Paths.get(".").toFile());
        return chooser;
    }


    public void writeToTable() {
        refreshList();
        setListener(filteredData);
        taskTable.setItems(filteredData);
    }
    private void refreshList() {
        masterData.clear();
        masterData.addAll(getTasker().getTasks(currentOrder));
        filteredData = new FilteredList<>(masterData, p->true);
    }
    private class OrderMenuItem extends MenuItem {
        private OrderMenuItem(ListCondition listCondition, String title) {
            super(title);
            setOnAction(event -> {
                setCurrentOrder(listCondition);
                writeToTable();
            });
        }
    }
}
