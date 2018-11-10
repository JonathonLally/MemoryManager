package controller;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
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

    //Variables
    private ObservableList processAvailableList;     //Keeps track of what processes are available
    private ObservableList processRemoveableList;    //Processes that can be removed

    //FXML Methods

    @FXML
    void initialize() {         //Initializes MainView
        setOutputArea("Memory Manager Sim Started");
        algorithmComboBox.getItems().addAll("First Fit", "Best Fit","Worst Fit");
        processAvailableList = FXCollections.observableArrayList("Process 1", "Process 2", "Process 3", "Process 4",
        "Process 5", "Process 6", "Process 7", "Process 8");
        addProcessComboBox.setItems(processAvailableList);

    }
    @FXML
    void CompactMemory(ActionEvent event) {

    }

    @FXML
    void CreateMemorySim(ActionEvent event) {

    }

    @FXML
    void ResetMemory(ActionEvent event) {

    }

    @FXML
    void exit(ActionEvent event) {
        Platform.exit();
    }



    //Methods
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

}
