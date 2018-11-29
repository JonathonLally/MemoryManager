package model;

import java.util.ArrayList;

public class FirstFitSim extends MemSim {
    public FirstFitSim(int totalMemory, int osMemory) {
        super(totalMemory, osMemory);
    }

    public void insertProcess(MemProcess insert) {
        System.out.println("Attempting to add Process");
        int size = insert.getpSize();
        ArrayList<Hole> holeList = findHoles();
        System.out.println(holeList.toString());

        for (Hole ho : holeList) {
            if (ho.getSize() >= insert.getpSize()) {
                System.out.println("Proper Hole Found");
                addToMemory(ho.getStart(), insert.getpSize(), insert.getmemID());
                insert.setStartLocation(ho.getStart());
                insert.setEndLocation(ho.getStart() + insert.getpSize());
                processList.add(insert);
                System.out.println("Process added at " + insert.getStartLocation() + " with size of " + insert.getpSize());
                break;
            }
            else {
                System.out.println("Process won't fit, adding to Waitlist");
                waitList.add(insert);
            }
        }
        System.out.println(usedMemory);
        usedMemory += insert.getpSize();
        System.out.println(usedMemory);

    }

}
