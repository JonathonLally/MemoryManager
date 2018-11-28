package controller;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.*;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Callback;
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
    @FXML    private ListView<String> memChart;
    @FXML    private TextField statsTotal;
    @FXML    private TextField statsFree;
    @FXML    private TextField statsPercent;
    @FXML    private Button statsPrint;

    //Variables
    private ObservableList<String> processAvailableList;     //Keeps track of what processes are available
    private ObservableList<String> processRemoveableList;    //Processes that can be removed
    private ArrayList<String> processesAvailable;
    private ArrayList<String> processesRemoveable;
    private ArrayList<MemProcess> pList;
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
        pList = new ArrayList<>();
        addProcessComboBox.setItems(processAvailableList);
        addProcessComboBox.getSelectionModel().select(0);
        setDefaultValues();
        initMemChart();

    }
    @FXML
    private void compactMemory(ActionEvent event) {
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
            else {
                MemProcess temp = new MemProcess(getProcessId_Add(), parsed);
                memsim.insertProcess(temp);
                pList.add(temp);
                System.out.println(pList.size());
                //TODO UPDATE ADD/REMOVE PROCESS LISTVIEWS
                String processId = getProcessId_Add();
                swapLists(processId, 0);
                swapComboBox(processId, 0);
                addProcessToChart(temp);
            }
        } catch (NumberFormatException e) {
            displayError("Process Illegal Char", "Please use integers");
        }

    }


    @FXML
    void removeProcess(ActionEvent event) {

        String processId = getProcessId_Remove();
        for (MemProcess memP : pList) {
            if (processId == memP.getpID()) {
                memsim.removeProcess(memP.getmemID());
            }
        }

        MemProcess temp = null;


        for(int i = 0; i < pList.size(); i++)
            if(pList.get(i).getpID().equals(processId))
                temp = pList.get(i);


        removeProcessFromChart(temp, temp.getpSize());
        swapLists(processId, 1);
        swapComboBox(processId, 1);
    }

    @FXML
    void CreateMemorySim(ActionEvent event) {
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
        statsTotal.setText(String.valueOf(memsim.getTotalSize()));
        statsFree.setText(String.valueOf(memsim.getFreeMemory()));
        Double percent = ((double)memsim.getUsedMemory() / (double)memsim.getTotalSize()* 100);
        statsPercent.setText(String.valueOf(percent));
        System.out.println(memsim.toString());

    }

    @FXML
    void ResetMemory(ActionEvent event) {                           //Resets Application
        processAvailableList.clear();
        processesAvailable.clear();
        processesRemoveable.clear();
        pList.clear();
        addProcessComboBox.setItems(null);
        removeProcessComboBox.setItems(null);
        memsim = null;
        initialize();
    }

    @FXML
    void exit(ActionEvent event) {
        Platform.exit();
    }

    @FXML
    void printMemoryArray(ActionEvent event) {
        System.out.println(memsim.findHoles().toString());
        memsim.printMemory();
        System.out.println(memsim.getProcessList().toString());
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

    private void initMemChart() {
        memChart.setCellFactory(new Callback<ListView<String>, ListCell<String>>() {
            @Override
            public ListCell<String> call(ListView<String> param) {
                return new ListCell<String>() {
                    @Override
                    protected void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty);

                        if (item == null || empty) {
                            setText(null);
                            setStyle("-fx-control-inner-background: derive(-fx-base,80%);"); //default
                        } else {
                            setText(item);
                            if (item.startsWith("H")) {
                                setStyle("-fx-control-inner-background: derive(#ffffff,0%);");
                            } else if (item.startsWith("O")) {
                                setStyle("-fx-control-inner-background: derive(#000000,0%);");
                            } else {
                                setStyle("-fx-control-inner-background: derive(#ffff00,0%);");
                            }
                        }
                    }
                };
            }
        });

        pList.add(new MemProcess("OS", Integer.parseInt(osSizeField.getText())));
        memChart.getItems().add(0, "OS\n" + "(" + osSizeField.getText() + "KB)");
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

    //Adds a process to the BarGraph
    private void addProcessToChart(int index, MemProcess mp) {
        memChart.getItems().add(index, mp.getpID() + "\n" + "(" + mp.getpSize() + "KB)");
    }

    private void addProcessToChart(MemProcess mp) {
        memChart.getItems().add(mp.getpID() + "\n" + "(" + mp.getpSize() + "KB)");
    }

    private void consolidateHoles(int index, MemProcess mp) {

        if (index == 1) {
            if (pList.get(index + 1).getpID().equals("HOLE")) {
                mp.setpSize(mp.getpSize() + pList.get(index + 1).getpSize());
                memChart.getItems().remove(index + 1);
                pList.remove(index + 1);
            }
        } else if (index == pList.size() - 1) {
            if (pList.get(index - 1).getpID().equals("HOLE")) {
                mp.setpSize(mp.getpSize() + pList.get(index - 1).getpSize());
                memChart.getItems().remove(index - 1);
                pList.remove(index - 1);
                index--;
            }
        } else {
            if (pList.get(index + 1).getpID().equals("HOLE")) {
                System.out.println("If statement #2 tripped");
                mp.setpSize(mp.getpSize() + pList.get(index + 1).getpSize());
                memChart.getItems().remove(index + 1);
                pList.remove(index + 1);
            }
            if (pList.get(index - 1).getpID().equals("HOLE")) {
                System.out.println("If statement #1 tripped");
                mp.setpSize(mp.getpSize() + pList.get(index - 1).getpSize());
                memChart.getItems().remove(index - 1);
                pList.remove(index - 1);
                index--;
            }
        }

        pList.remove(index);
        memChart.getItems().remove(index);
        pList.add(index, mp);
        addProcessToChart(index, mp);
        pList.trimToSize();
    }

    //Removes a process from the BarGraph and replaces it with a hole
    private void removeProcessFromChart(MemProcess mp, int size) {
        MemProcess hole = new MemProcess("HOLE", size);
        int index = pList.indexOf(mp);
        consolidateHoles(index, hole);
    }

}
