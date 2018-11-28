package model;

import java.util.ArrayList;

public class FirstFitSim extends MemSim {
    public FirstFitSim(int totalMemory, int osMemory) {
        super(totalMemory, osMemory);
    }

    public boolean canAllocate(ArrayList<MemProcess> pList, MemProcess mp, int totalMemory) {
        int sum = 0;
        int freeMemory, holeSize;
        MemProcess temp;

        for(MemProcess process : pList)
            if(!process.getpID().equals("HOLE"))
                sum += process.getpSize();

         return false;
    }
}
