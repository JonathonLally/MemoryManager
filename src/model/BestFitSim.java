package model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class BestFitSim extends MemSim {

    //Constructor
    public BestFitSim(int totalMemory, int osMemory) {
        super(totalMemory, osMemory);
    }


    //Finds and Inserts Process for BestFit
    public void insertProcess(MemProcess insert) {
        int size = insert.getpSize();
        ArrayList<Hole> holeList = findHoles();
        //Sort Array List and continue
        Collections.sort(holeList, (Comparator.comparingInt(Hole::getSize)));

        for (Hole ho : holeList) {  //For all holes, check if process fits in hole, if so insert
            System.out.println(ho.toString());
            if (ho.getSize() >= insert.getpSize()) {
                System.out.println("Proper Hole Found");
                addToMemory(ho.getStart(), insert.getpSize(), insert.getmemID());
                insert.setStartLocation(ho.getStart());
                insert.setEndLocation(ho.getStart() + insert.getpSize());
                processList.add(insert);
                System.out.println("Process added at " + insert.getStartLocation() + " with size of " + insert.getpSize());
                freeMemory -= insert.getpSize();
                break;
            } else {
                System.out.println("Process won't fit, adding to Waitlist");
                waitList.add(insert);
            }
        }

    }
}
