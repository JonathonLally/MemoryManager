package controller;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.*;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Callback;
import model.*;

import java.util.*;

import static jdk.nashorn.internal.objects.NativeMath.round;

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
    @FXML    private ListView<String> memChart;
    @FXML    private TextField statsTotal;
    @FXML    private TextField statsFree;
    @FXML    private TextField statsPercent;
    @FXML    private Button statsPrint;
    @FXML    private VBox osBox;
    @FXML    private VBox p1Box;
    @FXML    private VBox p2Box;
    @FXML    private VBox p3Box;
    @FXML    private VBox p4Box;
    @FXML    private VBox p5Box;
    @FXML    private VBox p6Box;
    @FXML    private VBox p7Box;
    @FXML    private VBox p8Box;
    @FXML    private Text osText;
    @FXML    private Text p1Text;
    @FXML    private Text p2Text;
    @FXML    private Text p3Text;
    @FXML    private Text p4Text;
    @FXML    private Text p5Text;
    @FXML    private Text p6Text;
    @FXML    private Text p7Text;
    @FXML    private Text p8Text;

    //Variables
    private ObservableList<String> processAvailableList;     //Keeps track of what processes are available
    private ObservableList<String> processRemoveableList;    //Processes that can be removed
    private ArrayList<String> processesAvailable;
    private ArrayList<String> processesRemoveable;
    private Alert error = new Alert(Alert.AlertType.ERROR);
    private MemSim memsim;

    //Final Vars
    private final int
            OS_MIN = 512,
            OS_MAX = 2048,
            PROCESS_MIN = 1,
            PROCESS_MAX = 16384;
    //Size of Memory Graphic = 500;

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
    }
    @FXML
    private void compactMemory(ActionEvent event) {
        setOutputArea("Compacting Memory");
        memsim.compact();
        updateVBoxs();
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

            if (processSizeInvalid(parsed))
                displayError("Invalid Process Size", "Please enter an integer between 1 and 16384");
            else if (parsed > memsim.getFreeMemory()) {
                displayError("Memory Full", "Added to Waitlist");
            }

            else {
                memsim.insertProcess(new MemProcess(getProcessId_Add(), parsed));
                String processId = getProcessId_Add();
                swapLists(processId, 0);
                swapComboBox(processId, 0);
                updateStats((double)memsim.getFreeMemory());
                processId = processId.replaceAll("\\D+","");
                int tempInt = Integer.parseInt(processId);
                updateVBoxs();
                setOutputArea("Adding Process" + processId + " of size " + parsed);
            }
        } catch (NumberFormatException e) {
            displayError("Process Illegal Char", "Please use integers");
        }

    }


    @FXML
    void removeProcess(ActionEvent event) {

        String remove = null;
        try {
            remove = getProcessId_Remove();
            swapLists(remove, 1);
            swapComboBox(remove, 1);
            remove = remove.replaceAll("\\D+","");
            int removeint = Integer.parseInt(remove);
            memsim.removeProcess(removeint);
            hideProcessBox(removeint);
            setOutputArea("Removing Process " + remove);
        } catch (Exception e) {
            displayError("Halp Process Remove", "No Process to be Removed");
        }
        updateStats((double)memsim.getFreeMemory());
    }

    @FXML
    void CreateMemorySim(ActionEvent event) {
        try {
            if (getAlgorithmType().equals("First Fit")) {
                setOutputArea("Creating First Fit Sim");
                memsim = new FirstFitSim(getTotalMemory(), getOSMemory());
                outputArea.appendText("\nTotal size of " + getTotalMemory());
            }
            else if (getAlgorithmType().equals("Best Fit")) {
                setOutputArea("Creating Best Fit Sim");
                memsim = new BestFitSim(getTotalMemory(), getOSMemory());
                outputArea.appendText("\nTotal size of " + getTotalMemory());
            }
            else if (getAlgorithmType().equals("Worst Fit")) {
                setOutputArea("Creating Worst Fit Sim");
                memsim = new WorstFitSim(getTotalMemory(), getOSMemory());
                outputArea.appendText("\nTotal size of " + getTotalMemory());
            }
            else {
                setOutputArea("Error with CreateMemorySim()");
            }
            showBox(osBox);
            osText.setText(String.valueOf(memsim.getOsSize()));
            changeBoxSize(osBox, memsim.getOsSize());
            statsTotal.setText(String.valueOf(memsim.getTotalSize()) + "K");
            updateStats((double)memsim.getFreeMemory());
            System.out.println(memsim.toString());
        } catch (Exception e) {
            displayError("Cannot Create New Memory Sim", "One Already Created");
        }

    }

    @FXML
    void showBox(VBox box) {
        box.setVisible(true);
    }
    @FXML
    void hideBox(VBox box) {
        box.setVisible(false);
    }
    @FXML
    void hideAllBoxes() {
        osBox.setVisible(false);
        p1Box.setVisible(false);
        p2Box.setVisible(false);
        p3Box.setVisible(false);
        p4Box.setVisible(false);
        p5Box.setVisible(false);
        p6Box.setVisible(false);
        p7Box.setVisible(false);
        p8Box.setVisible(false);
    }

    @FXML void showProcessBox(int choice, int size, int start) {
        switch (choice) {
            case 1:
                p1Box.setVisible(true);
                changeBoxSize(p1Box, size);
                changeBoxLoc(p1Box, start);
                p1Text.setText(String.valueOf(size));
                break;
            case 2:
                p2Box.setVisible(true);
                changeBoxSize(p2Box, size);
                changeBoxLoc(p2Box, start);
                p2Text.setText(String.valueOf(size));
                break;
            case 3:
                p3Box.setVisible(true);
                changeBoxSize(p3Box, size);
                changeBoxLoc(p3Box, start);
                p3Text.setText(String.valueOf(size));
                break;
            case 4:
                p4Box.setVisible(true);
                changeBoxSize(p4Box, size);
                changeBoxLoc(p4Box, start);
                p4Text.setText(String.valueOf(size));
                break;
            case 5:
                p5Box.setVisible(true);
                changeBoxSize(p5Box, size);
                changeBoxLoc(p5Box, start);
                p5Text.setText(String.valueOf(size));
                break;
            case 6:
                p6Box.setVisible(true);
                changeBoxSize(p6Box, size);
                changeBoxLoc(p6Box, start);
                p6Text.setText(String.valueOf(size));
                break;
            case 7:
                p7Box.setVisible(true);
                changeBoxSize(p7Box, size);
                changeBoxLoc(p7Box, start);
                p7Text.setText(String.valueOf(size));
                break;
            case 8:
                p8Box.setVisible(true);
                changeBoxSize(p8Box, size);
                changeBoxLoc(p8Box, start);
                p8Text.setText(String.valueOf(size));
                break;
            default:
                System.out.println("Error Switch Statement");
                break;

        }

    }

    @FXML void hideProcessBox(int choice) {
        switch (choice) {
            case 1:
                p1Box.setVisible(false);
                break;
            case 2:
                p2Box.setVisible(false);
                break;
            case 3:
                p3Box.setVisible(false);
                break;
            case 4:
                p4Box.setVisible(false);
                break;
            case 5:
                p5Box.setVisible(false);
                break;
            case 6:
                p6Box.setVisible(false);
                break;
            case 7:
                p7Box.setVisible(false);
                break;
            case 8:
                p8Box.setVisible(false);
                break;
            default:
                System.out.println("Error Switch Statement");
                break;

        }

    }

    @FXML void changeBoxSize(VBox box, double size) {
        size = size / memsim.getTotalSize();    //Percent of total array size
        size = size * 500;                      //* Total size of graphic
        box.setMinHeight(size);                 //Resize VBox
        box.setMaxHeight(size);
    }

    public void changeBoxLoc(VBox box, int start) {
        double loc = (double)start / (double)memsim.getTotalSize();
        loc = loc * 500;
        box.setTranslateY(loc);
    }

    public void updateVBoxs() {
        for (MemProcess mem : memsim.getProcessList()) {
            showProcessBox(mem.getmemID(), mem.getpSize(), mem.getStartLocation());
        }
    }

    public void rebuildVBoxs() {

    }

    @FXML
    void updateStats(double free) {
        statsFree.setText(String.valueOf(free));
        Double percent = free / (double)memsim.getTotalSize();
        round(percent,3);
        percent = percent * 100;
        statsPercent.setText(String.valueOf(percent));
    }

    @FXML
    void ResetMemory(ActionEvent event) {                           //Resets Application
        processAvailableList.clear();
        processesAvailable.clear();
        processesRemoveable.clear();
        addProcessComboBox.setItems(null);
        removeProcessComboBox.setItems(null);
        memsim = null;
        hideAllBoxes();
        algorithmComboBox.getSelectionModel().select(0);
        processAvailableList = FXCollections.observableArrayList("Process 1", "Process 2", "Process 3", "Process 4",
                "Process 5", "Process 6", "Process 7", "Process 8");
        processesAvailable = new ArrayList<>(Arrays.asList("Process 1", "Process 2", "Process 3", "Process 4",
                "Process 5", "Process 6", "Process 7", "Process 8"));
        processesRemoveable = new ArrayList<>();
        addProcessComboBox.setItems(processAvailableList);
        addProcessComboBox.getSelectionModel().select(0);
        statsTotal.clear();
        statsFree.clear();
        statsPercent.clear();
        setOutputArea("Application Reset");
    }

    @FXML
    void exit(ActionEvent event) {
        Platform.exit();
    }

    @FXML
    void printMemoryArray(ActionEvent event) {
        setOutputArea(memsim.toOutputString());
    }

    //Methods

    private String getAlgorithmType() {     //Gets Algorithm Choice from ComboBox
        return algorithmComboBox.getValue();
    }

    private String getProcessId_Add() {
        return addProcessComboBox.getValue();
    }

    private String getProcessId_Remove() { return removeProcessComboBox.getValue(); }

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

    public void setOutputArea(String in) {                      //Writes to textArea in GUI, can use to write to users
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
        if(processId != null) {
            if (signal == 0) {
                processesAvailable.remove(processId);
                processesRemoveable.add(processId);
            } else {
                processesRemoveable.remove(processId);
                processesAvailable.add(processId);
            }
        }
    }

    //Swaps a process from one ComboBox to another. If signal = 0, addProcess -> removeProcess. Else, removeProcess -> addProcess
    private void swapComboBox(String processId, int signal) {
        if(processId != null) {
            if (signal == 0) {
                selectNextAvailableOption(addProcessComboBox.getItems().size(), 0);
                addProcessComboBox.getItems().remove(processId);
                removeProcessComboBox.getItems().add(processId);
                removeProcessComboBox.getSelectionModel().select(removeProcessComboBox.getItems().size() - 1);
            } else {
                selectNextAvailableOption(removeProcessComboBox.getItems().size(), 1);
                removeProcessComboBox.getItems().remove(processId);
                addProcessComboBox.getItems().add(processId);
                addProcessComboBox.getSelectionModel().select(addProcessComboBox.getItems().size() - 1);
            }
        }

        //Integrated sorting for addProcessComboBox since removeProcess -> addProcess causes the list to go in reverse
        //NOTE: No two elements are ever equal, such that accounting for that case is unnecessary
        addProcessComboBox.getItems().sort((o1, o2) -> {
            int i1 = Integer.parseInt(o1.substring(o1.length() - 1)), i2 = Integer.parseInt(o2.substring(o2.length() - 1));

            if(i1 > i2)
                return 1;
            else
                return -1;
        });
    }

    //Selects next available option since the selected option was removed. If signal = 0, do it for addProcess. Else, do it for removeProcess
    private void selectNextAvailableOption(int size, int signal) {
        if(signal == 0 && size != 1)
            addProcessComboBox.getSelectionModel().selectNext();
        else if (size != 1)
            removeProcessComboBox.getSelectionModel().selectNext();
    }




}
