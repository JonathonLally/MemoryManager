package model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class WorstFitSim extends MemSim {
    public WorstFitSim(int totalMemory, int osMemory) {
        super(totalMemory, osMemory);
    }

    public void insertProcess(MemProcess insert) {
        int size = insert.getpSize();
        ArrayList<Hole> holeList = findHoles();
        //Sort Array List and continue
        Collections.sort(holeList, (Comparator.comparingInt(Hole::getSize)));
        Collections.reverse(holeList);

        for (Hole ho : holeList) {
            System.out.println(ho.toString());
            if (ho.getSize() >= insert.getpSize()) {
                System.out.println("Proper Hole Found");
                addToMemory(ho.getStart(), insert.getpSize(), insert.getmemID());
                insert.setStartLocation(ho.getStart());
                insert.setEndLocation(ho.getStart() + insert.getpSize());
                processList.add(insert);
                System.out.println("Process added at " + insert.getStartLocation() + " with size of " + insert.getpSize());
                break;
            } else {
                System.out.println("Process won't fit, adding to Waitlist");
                waitList.add(insert);
            }
            usedMemory += insert.getpSize();
        }

    }
}
