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
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import model.*;

import java.util.*;

import static jdk.nashorn.internal.objects.NativeMath.round;

public class MainViewController {

    //FXML Variables

    @FXML    private ComboBox<String> algorithmComboBox;
    @FXML    private ComboBox<String> addProcessComboBox;
    @FXML    private ComboBox<String> removeProcessComboBox;
    @FXML    private TextField totalMemoryField;
    @FXML    private TextField osSizeField;
    @FXML    private TextField processSizeField;
    @FXML    private TextField statsTotal;
    @FXML    private TextField statsFree;
    @FXML    private TextField statsPercent;
    @FXML    private TextArea outputArea;
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

    //FXML Methods

    @FXML   //Initializes MainView, Sets Items for ComboBoxes, Sets default selections
    public void initialize() {
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

    @FXML   //Activates Compact Memory Button, calls MemSim.compact();
    private void compactMemory(ActionEvent event) {
        setOutputArea("Compacting Memory");
        memsim.compact();
        updateVBoxs();
    }

    @FXML   //Sets Default Values for TextFields
    private void setDefaultValues() {
        totalMemoryField.setText("8192");
        osSizeField.setText("1024");
        processSizeField.setText("768");
    }


    @FXML   //Add Processes to Memory Sim
    private void addProcess(ActionEvent event) {        //Checks checkProcess field for proper int value
        try {
            int parsed = Integer.parseInt(processSizeField.getText());

            if (processSizeInvalid(parsed))
                displayError("Invalid Process Size", "Please enter an integer between 1 and 16384");
            else if (parsed > memsim.getFreeMemory()) {
                displayError("Memory Full", "Added to Waitlist");
            }

            else {  //Passes Checks for invalid input, adds process by using MemSim.insertProcess()
                memsim.insertProcess(new MemProcess(getProcessId_Add(), parsed));
                String processId = getProcessId_Add();

                //Checks if proper hole found and it was actually added
                if (checkProcess(processId) == true) {
                    swapLists(processId, 0);
                    swapComboBox(processId, 0);
                    updateStats((double)memsim.getFreeMemory());
                    processId = processId.replaceAll("\\D+","");
                    updateVBoxs();
                    setOutputArea("Adding Process" + processId + " of size " + parsed);
                } else {
                    displayError("No Valid Memory Hole", "Compaction Recommended!");
                }
            }
        } catch (NumberFormatException e) {
            displayError("Process Illegal Char", "Please use integers");
        } catch (NullPointerException n) {
            displayError("No more processes", "Please remove a process");
        }
    }


    @FXML   //Removes Processes for Memory Sim
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
            updateStats((double)memsim.getFreeMemory());
        } catch (Exception e) {
            displayError("Cannot Remove Process", "No Process to be Removed");
        }

    }

    @FXML   //Creates Memory Sim
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
        } catch (Exception e) {
            displayError("Cannot Create New Memory Sim", "One Already Created");
        }
    }

    @FXML   //Helper for showing Vboxes
    void showBox(VBox box) {
        box.setVisible(true);
    }

    @FXML   //Helper for hiding Vboxes
    void hideBox(VBox box) {
        box.setVisible(false);
    }

    @FXML   //Hides all process Vboxes
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

    @FXML   //Changes process Vbox visibility, shows processes after they are created
    void showProcessBox(int choice, int size, int start) {
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
                setOutputArea("Error Switch Statement");
                break;
        }
    }

    @FXML   //Hides processes vboxes when they are removed
    void hideProcessBox(int choice) {
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
                setOutputArea("Error Switch Statement");
                break;
        }
    }

    @FXML   //Resizes process Vbox scaled to Memory Size and GUI
    void changeBoxSize(VBox box, double size) {
        size = size / memsim.getTotalSize();    //Percent of total array size
        size = size * 500;                      //* Total size of graphic
        box.setMinHeight(size);                 //Resize VBox
        box.setMaxHeight(size);
    }
    //Moves process Vbox for Memory Sim GUI, Used when compacted and processes move
    public void changeBoxLoc(VBox box, int start) {
        double loc = (double)start / (double)memsim.getTotalSize();
        loc = loc * 500;
        box.setTranslateY(loc);
    }

    //When Memory Sim changes this updates all process VBoxes (Updates GUI with Model)
    public void updateVBoxs() {
        for (MemProcess mem : memsim.getProcessList()) {
            showProcessBox(mem.getmemID(), mem.getpSize(), mem.getStartLocation());
        }
    }

    @FXML   //Updates Statistics fields with model
    void updateStats(double free) {
        statsFree.setText(String.valueOf(free));
        Double percent = free / (double)memsim.getTotalSize();
        round(percent,3);
        percent = percent * 100;
        statsPercent.setText(String.valueOf(percent));
    }

    @FXML   //Resets Application, deletes MemSim, Resets GUI to default
    void ResetMemory(ActionEvent event) {
        processAvailableList.clear();
        processesAvailable.clear();
        processesRemoveable.clear();
        addProcessComboBox.setItems(null);
        removeProcessComboBox.getItems().removeAll(removeProcessComboBox.getItems());
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
        setDefaultValues();
        setOutputArea("Application Reset");
    }

    @FXML   //Exits App
    void exit(ActionEvent event) {
        Platform.exit();
    }

    @FXML   //Prints MemSim Arraylist to output text area
    void printMemoryArray(ActionEvent event) {
        try {
            setOutputArea(memsim.toOutputString());
        } catch (Exception e) {
            displayError("No Array to print", "Please wait to print until there is something to print");
        }
    }

    //Methods

    //Gets algorithm type
    private String getAlgorithmType() {
        return algorithmComboBox.getValue();
    }

    //Gets process to add
    private String getProcessId_Add() {
        return addProcessComboBox.getValue();
    }

    //Gets process to be removed
    private String getProcessId_Remove() {
        return removeProcessComboBox.getValue();
    }

    //Gets value to be used for Memory Size
    private int getTotalMemory() {
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

    //Gets value to be used for Operating System Size
    private int getOSMemory() {
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

    //Writes to the output area in GUI
    public void setOutputArea(String in) {
        try {
            outputArea.clear();
            outputArea.setText(in);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //Launches about us window
    public void launchAboutView() {
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

    //Launches error popup
    private void displayError(String head, String content) {
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

    //Checks if process was added to list, if not there wasn't a hole of proper size and returns false
    private Boolean checkProcess(String check) {
        check = check.replaceAll("\\D+","");
        for (MemProcess mem : memsim.getProcessList()) {
            if (check.equals(String.valueOf(mem.getmemID()))) {
                return true;
            }
        }
        return false;
    }
}
