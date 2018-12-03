package model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class WorstFitSim extends MemSim {
    //Constructor
    public WorstFitSim(int totalMemory, int osMemory) {
        super(totalMemory, osMemory);
    }

    //Inserts processes to proper holes for WorstFit
    public void insertProcess(MemProcess insert) {
        int size = insert.getpSize();
        ArrayList<Hole> holeList = findHoles();
        //Sort Array List and continue
        Collections.sort(holeList, (Comparator.comparingInt(Hole::getSize)));
        Collections.reverse(holeList);      //We want the worst fit so reverse

        for (Hole ho : holeList) {  //For all holes, if process fits insert
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
