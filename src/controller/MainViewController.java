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
    private ObservableList processAvailableList;     //Keeps track of what processes are available
    private ObservableList processRemoveableList;    //Processes that can be removed
    private int freeMemory;
    private int usedMemory;
    private Alert error = new Alert(Alert.AlertType.ERROR);

    //FXML Methods

    @FXML
    void initialize() {         //Initializes MainView
        setOutputArea("Memory Manager Sim Started");
        algorithmComboBox.getItems().addAll("First Fit", "Best Fit","Worst Fit");
        algorithmComboBox.getSelectionModel().select(0);
        processAvailableList = FXCollections.observableArrayList("Process 1", "Process 2", "Process 3", "Process 4",
        "Process 5", "Process 6", "Process 7", "Process 8");
        addProcessComboBox.setItems(processAvailableList);
        addProcessComboBox.getSelectionModel().select(0);
        totalMemoryField.setText("8192");
        osSizeField.setText("1024");
        processSizeField.setText("512");
        initMemChart();
        //memoryBox.getChildren(memChart);    //TODO add Graphic Here

    }
    @FXML
    void compactMemory(ActionEvent event) {

    }

    @FXML
    void checkProcess(ActionEvent event) {        //Checks checkProcess field for proper int value
        try {
            int parsed = Integer.parseInt(processSizeField.getText());
            if (parsed < 1 || parsed > 16384) {
                displayError("Invalid Process Size", "Please enter an integer between 1 and 16384");
            }
            else {
                setOutputArea("Process check " + parsed);
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
        setOutputArea(getAlgorithmType() + getTotalMemory() + getOSMemory());

    }

    @FXML
    void ResetMemory(ActionEvent event) {
        algorithmComboBox.getSelectionModel().select(0);
        totalMemoryField.setText("8192");
        processSizeField.setText("512");
        setOutputArea("Application Reset");

    }

    @FXML
    void exit(ActionEvent event) {
        Platform.exit();
    }



    //Methods
    private String getAlgorithmType() {     //Gets Algorithm Choice from ComboBox
        return algorithmComboBox.getValue();
    }

    private int getTotalMemory() {          //Gets value from totalMemory size TextField
        try {
            int parsed = Integer.parseInt(totalMemoryField.getText());

            if(parsed < 4096 || parsed > 16384) {
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

            if(parsed < 512 || parsed > 2048) {
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

}
