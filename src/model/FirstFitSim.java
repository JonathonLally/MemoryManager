package model;

import java.util.ArrayList;

public class FirstFitSim extends MemSim {
    //Constructor for FirstFit
    public FirstFitSim(int totalMemory, int osMemory) {
        super(totalMemory, osMemory);
    }

    //Finds and Inserts Processes for FirstFit
    public void insertProcess(MemProcess insert) {
        System.out.println("Attempting to add Process");
        int size = insert.getpSize();
        ArrayList<Hole> holeList = findHoles();

        for (Hole ho : holeList) {  //For all holes, if process fits in hole insert
            if (ho.getSize() >= insert.getpSize()) {
                System.out.println("Proper Hole Found");
                addToMemory(ho.getStart(), insert.getpSize(), insert.getmemID());
                insert.setStartLocation(ho.getStart());
                insert.setEndLocation(ho.getStart() + insert.getpSize());
                processList.add(insert);
                System.out.println("Process added at " + insert.getStartLocation() + " with size of " + insert.getpSize());
                freeMemory -= insert.getpSize();
                break;
            }
            else {
                System.out.println("Process won't fit, adding to Waitlist");
                waitList.add(insert);
            }
        }


    }

}
