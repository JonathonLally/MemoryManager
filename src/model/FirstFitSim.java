package model;

import java.util.ArrayList;

public class FirstFitSim extends MemSim {
    public FirstFitSim(int totalMemory, int osMemory) {
        super(totalMemory, osMemory);
    }

    public boolean canAllocate(ArrayList<MemProcess> pList, MemProcess mp, int totalMemory) {
        int sum = 0;
        int freeMemory, adjacentFreeMemory;
        int[] holeMap = new int[pList.size()];
        MemProcess temp;

        for(int i = 0; i < pList.size(); i++) {
            temp = pList.get(i);

            if (!temp.getpID().equals("HOLE"))
                sum += temp.getpSize();
            else {
                holeMap[i] = 1;
            }
        }


        freeMemory = totalMemory - sum;

        if (freeMemory < mp.getpSize())
            return false;

        if(freeMemory < (1/8) * (totalMemory * 1.0)) {
            for (int i = 0; i < pList.size(); i++) {
                temp = pList.get(i);

                if (temp.getpID().equals("HOLE")) {
                    if (pList.get(i).getpSize() > mp.getpSize())
                        return true;
                    else {
                        adjacentFreeMemory = 0;

                        for (int j = i + 1; j < pList.size() - 1; j++)
                            if (holeMap[j] == 1)
                                adjacentFreeMemory += pList.get(i).getpSize() + pList.get(j).getpSize();
                            else
                                break;
                    }

                    if (adjacentFreeMemory > mp.getpSize())
                        return true;
                }
            }
        }


         return false;
    }
}
