package model;

import java.util.ArrayList;
import java.util.Collections;

public abstract class MemSim {

    protected int totalSize;
    protected int freeMemory;
    protected int osSize;
    protected ArrayList<MemProcess> processList;      //Contains processes in memory
    protected ArrayList<MemProcess> waitList;         //Contains processes in wait queue
    protected int[] memory;                           //Simulates memory

    public MemSim(int totalSize, int osSize) {
        this.totalSize = totalSize;
        this.osSize = osSize;
        memory = new int[totalSize];
        processList = new ArrayList<MemProcess>();
        waitList = new ArrayList<MemProcess>();
        freeMemory = totalSize - osSize;
        insertOS();
    }

    public ArrayList<MemProcess> getProcessList() {
        return processList;
    }

    public ArrayList<MemProcess> getWaitList() {
        return waitList;
    }

    public void removeProcess(int id) {
        int count = 0;
        for (int i = 0; i <memory.length; i++) {
            if (memory[i] == id) {
                memory[i] = 0;
                count++;
            }
        }
        for (MemProcess mem : processList) {
            if (mem.getmemID() == id) {
                processList.remove(mem);
                System.out.println("Removing Process " + mem.toString());
                freeMemory += mem.getpSize();
                break;
            }
        }

    }

    public void insertOS() {
        for (int i =0; i<osSize; i++) {
            memory[i] = 9;
        }
    }

    public abstract void insertProcess(MemProcess insert);

    public void printMemory() {
        for (int i =0; i<memory.length; i+=10) {
            System.out.println(memory[i]);
        }
    }

    public ArrayList<Hole> findHoles() {     //Returns the hole sizes of int[] memory
        ArrayList<Hole> holes = new ArrayList<Hole>();

        int count = 0;
        for (int i= 0; i<memory.length; i++) {

            if(memory[i] == 0) {
                count++;
            }
            else if (memory[i] != 0 & count != 0) {
                holes.add(new Hole(count, i - count));
                System.out.println("Found Hole " + count + "  " + i);
                count = 0;
            }

        }
        if (count != 0) {
            holes.add(new Hole(count,memory.length - count));
        }

        return holes;
    }

    //Compact Array, take advantage that we know processes and size and use arraylists
    public void compact() {
        ArrayList<Integer> tempP = new ArrayList<Integer>();
        for (int i = 0; i < memory.length; i++) {
            if (memory[i] != 0 & memory[i] != 9) {
                tempP.add(memory[i]);
            }
        }
        Collections.sort(tempP);
        ArrayList<Integer> tempOS = new ArrayList<Integer>();
        for (int i = 0; i < osSize; i++) {
            tempOS.add(9);
        }
        ArrayList<Integer> tempFree = new ArrayList<Integer>();
        for (int i = 0; i < freeMemory; i++) {
            tempFree.add(0);
        }
        tempOS.addAll(tempP);
        tempOS.addAll(tempFree);
        //Arraylist<Integer> - > Array[int]
        memory = tempOS.stream().mapToInt(Integer::intValue).toArray();
        rebuildArrayLists();
    }

    //Fixes ArrayList After Compaction
    public void rebuildArrayLists() {
        for (MemProcess mem : processList) {
            int count = 0;
            for (int i = 0; i <memory.length; i++) {
                if (memory[i] == mem.getmemID()) {
                    mem.setStartLocation(i);
                    mem.setEndLocation(i + count);
                    break;
                }
            }
        }

    }


    public void addToMemory(int start, int size, int pid) {         //Adds process to memory Array
        for (int i = 0; i< size; i++) {
            int j = start + i;
            memory[j] = pid;
        }

    }

    public int getTotalSize() {
        return totalSize;
    }

    public int getFreeMemory() {
        return freeMemory;
    }

    @Override
    public String toString() {
        return "MemSim{" +
                "totalSize=" + totalSize +
                ", freeMemory=" + freeMemory +
                ", osSize=" + osSize +
                ", processList=" + processList +
                ", waitList=" + waitList +
                '}';
    }
}
