package model;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class BestFitSim extends MemSim {
    public BestFitSim(int totalMemory, int osMemory) {
        super(totalMemory, osMemory);
    }

    public void insertProcess(MemProcess insert) {
        int size = insert.getpSize();
        ArrayList<Hole> holeList = findHoles();
        //Sort Array List and continue

        for (Hole ho : holeList) {
            if (ho.getSize() <= insert.getpSize()) {
                addToMemory(ho.getStart(), insert.getpSize(), Integer.parseInt(insert.getpID()));
                processList.add(insert);
                break;
            }
        }

    }
}
