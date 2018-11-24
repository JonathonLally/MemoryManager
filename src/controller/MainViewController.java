package controller;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.*;

import java.util.*;

public class MainViewController {

    //FXML Variables
    @FXML    private ComboBox<String> algorithmComboBox;
    @FXML    private TextField totalMemoryField;
    @FXML    private TextField osSizeField;
    @FXML    private Button createMemoryButton;
    @FXML    private Button resetMemoryButton;
    @FXML    private ComboBox<String> addProcessComboBox;
    @FXML    private TextField processSizeField;
    @FXML    private ComboBox<String> removeProcessComboBox;
    @FXML    private Button compactMemoryButton;
    @FXML    private TextArea outputArea;
    @FXML    private VBox memoryBox;
    @FXML    private BarChart memChart;
    @FXML    private CategoryAxis xAxis;
    @FXML    private NumberAxis yAxis;

    //Variables
    private ObservableList<String> processAvailableList;     //Keeps track of what processes are available
    private ObservableList<String> processRemoveableList;    //Processes that can be removed
    private ArrayList<String> processesAvailable;
    private ArrayList<String> processesRemoveable;
    private int freeMemory;
    private int usedMemory;
    private Alert error = new Alert(Alert.AlertType.ERROR);
    private MemSim memsim;

    //Final Vars
    private final int
            OS_MIN = 512,
            OS_MAX = 2048,
            PROCESS_MIN = 1,
            PROCESS_MAX = 16384;

    //FXML Methods

    @FXML
    public void initialize() {         //Initializes MainView
        setOutputArea("Memory Manager Sim Started");
        algorithmComboBox.getItems().addAll("First Fit", "Best Fit","Worst Fit");
        algorithmComboBox.getSelectionModel().select(0);
        processAvailableList = FXCollections.observableArrayList("Process 1", "Process 2", "Process 3", "Process 4",
        "Process 5", "Process 6", "Process 7", "Process 8");
        processesAvailable = new ArrayList<>(Arrays.asList("Process 1", "Process 2", "Process 3", "Process 4",
                "Process 5", "Process 6", "Process 7", "Process 8"));
        processesRemoveable = new ArrayList<>();
        addProcessComboBox.setItems(processAvailableList);
        addProcessComboBox.getSelectionModel().select(0);
        setDefaultValues();
        initMemChart();
        //memoryBox.getChildren(memChart);    //TODO add Graphic Here

    }
    @FXML
    private void compactMemory(ActionEvent event) {


    }

    @FXML
    private void updateListViews() {

    }

    @FXML
    private void setDefaultValues() {                       //Sets default values for GUI text fields
        totalMemoryField.setText("8192");
        osSizeField.setText("1024");
        processSizeField.setText("512");
    }


    @FXML
    private void addProcess(ActionEvent event) {        //Checks checkProcess field for proper int value
        try {
            int parsed = Integer.parseInt(processSizeField.getText());
            if (processSizeInvalid(parsed)) {
                displayError("Invalid Process Size", "Please enter an integer between 1 and 16384");
            }
            else {
                setOutputArea("Process check " + parsed);   //Valid Process Input
                new MemProcess(getProcessId(), parsed);
                //TODO UPDATE ADD/REMOVE PROCESS LISTVIEWS
                swapLists(getProcessId(), 0);
                swapComboBox(getProcessId(), 0);
            }
        } catch (NumberFormatException e) {
            displayError("Process Illegal Char", "Please use integers");
        }

    }


    @FXML
    void removeProcess(ActionEvent event) {

    }

    @FXML
    void CreateMemorySim(ActionEvent event) {
        if (getAlgorithmType().equals("First Fit")) {
            setOutputArea("Creating First Fit Sim");
            memsim = new FirstFitSim(getTotalMemory(), getOSMemory());
        }
        else if (getAlgorithmType().equals("Best Fit")) {
            setOutputArea("Creating Best Fit Sim");
            memsim = new BestFitSim(getTotalMemory(), getOSMemory());
        }
        else if (getAlgorithmType().equals("Worst Fit")) {
            setOutputArea("Creating Worst Fit Sim");
            memsim = new WorstFitSim(getTotalMemory(), getOSMemory());
        }
        else {
            setOutputArea("Error with CreateMemorySim()");
        }

    }

    @FXML
    void ResetMemory(ActionEvent event) {                           //Resets Application
        algorithmComboBox.getSelectionModel().select(0);
        setOutputArea("Application Reset");
        setDefaultValues();
        memsim = null;
    }

    @FXML
    void exit(ActionEvent event) {
        Platform.exit();
    }



    //Methods
    private void switchProcessView(String process) {     //Switches process from add listview to remove and vice versa
    
    }

    private String getAlgorithmType() {     //Gets Algorithm Choice from ComboBox
        return algorithmComboBox.getValue();
    }

    private String getProcessId() {
        return addProcessComboBox.getValue();
    }

    private int getTotalMemory() {          //Gets value from totalMemory size TextField
        try {
            int parsed = Integer.parseInt(totalMemoryField.getText());

            if(processSizeInvalid(parsed)) {
                displayError("Total Memory Out of Range", "Please enter an integer between 4096 and 16384");
            }
            else {
                return parsed;
            }
        } catch (NumberFormatException e) {
            displayError("Total Memory Illegal Char", "Please use integers");
        }
        return 0;
    }

    private int getOSMemory() {             //Gets value from OSMemory size TextField
        try {
            int parsed = Integer.parseInt(osSizeField.getText());

            if(osSizeInvalid(parsed)) {
                displayError("OS Memory Out of Range", "Please enter an integer between 512 and 2048");
            }
            else {
                return parsed;
            }
        } catch (NumberFormatException e) {
            displayError("OS Memory Illegal Char", "Please use integers");
        }
        return 0;
    }
    private void initMemChart() {

    }

    private void setOutputArea(String in) {                      //Writes to textArea in GUI, can use to write to users
        try {
            outputArea.clear();
            outputArea.setText(in);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void launchAboutView() {                                                         //Launches About us window
        try {
            Stage thirdStage = new Stage();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/AboutView.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            //scene.getStylesheets().add(getClass().getResource("/resources/material-fx-v0_3.css").toExternalForm());
            thirdStage.setScene(scene);
            thirdStage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void displayError(String head, String content) {    //sets text for the Alert and displays it
        error.setHeaderText(head);
        error.setContentText(content);
        error.showAndWait();
    }

    //Checks if a requested Process size is out-of-bounds
    private boolean processSizeInvalid(int requestedSize) {
        return requestedSize < PROCESS_MIN || requestedSize > PROCESS_MAX;
    }

    //Checks if a requested OS size is out-of-bounds
    private boolean osSizeInvalid(int requestedSize) {
        return requestedSize < OS_MIN || requestedSize > OS_MAX;
    }

    //Swaps a process from one List to another. If signal = 0, available -> removable. Else, removable -> available
    private void swapLists(String processId, int signal) {
        if(signal == 0) {
            processesAvailable.remove(processId);
            processesRemoveable.add(processId);
        } else {
            processesRemoveable.remove(processId);
            processesAvailable.add(processId);
        }
    }

    //Swaps a process from one ComboBox to another. If signal = 0, addProcess -> removeProcess. Else, removeProcess -> addProcess
    private void swapComboBox(String processId, int signal) {
        if(signal == 0) {
            //selectNextAvailableOption(addProcessComboBox.getItems().size(), 0);
            addProcessComboBox.getItems().remove(processId);
            removeProcessComboBox.getItems().add(processId);
            removeProcessComboBox.getSelectionModel().select(removeProcessComboBox.getItems().size() - 1);
        } else {
            //selectNextAvailableOption(removeProcessComboBox.getItems().size(), 1);
            removeProcessComboBox.getItems().remove(processId);
            addProcessComboBox.getItems().add(processId);
            addProcessComboBox.getSelectionModel().select(addProcessComboBox.getItems().size() - 1);
        }
    }

    // !! CURRENTLY RESULTS IN AN ERROR !!

    //Selects next available option since the selected option was removed. If signal = 0, do it for addProcess. Else, do it for removeProcess
    /*private void selectNextAvailableOption(int size, int signal) {
        if(signal == 0) {
            if(addProcessComboBox.getItems().get(size + 1) != null)
                addProcessComboBox.getSelectionModel().selectNext();
            else if(addProcessComboBox.getItems().get(size - 1) != null)
                addProcessComboBox.getSelectionModel().selectPrevious();
            else
                addProcessComboBox.getSelectionModel().selectFirst();
        } else {
            if(removeProcessComboBox.getItems().get(size + 1) != null)
                removeProcessComboBox.getSelectionModel().selectNext();
            else if(removeProcessComboBox.getItems().get(size - 1) != null)
                removeProcessComboBox.getSelectionModel().selectPrevious();
            else
                removeProcessComboBox.getSelectionModel().selectFirst();
        }
    }*/

}
