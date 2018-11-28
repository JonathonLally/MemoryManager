package model;

import java.util.ArrayList;

public abstract class MemSim {

    protected int totalSize;
    protected int freeMemory;
    protected int usedMemory;
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
        usedMemory = osSize;
        insertOS();
    }

    public ArrayList<MemProcess> getProcessList() {
        return processList;
    }

    public ArrayList<MemProcess> getWaitList() {
        return waitList;
    }

    public void removeProcess(int id) {
        for (int i = 0; i <memory.length; i++) {
            if (memory[i] == id) {
                memory[i] = 0;
            }
        }
        for (MemProcess mem : processList) {
            if (mem.getmemID() == id) {
                processList.remove(mem);
                System.out.println("Removing Process " + mem.toString());
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

    public int getUsedMemory() {
        return usedMemory;
    }

    @Override
    public String toString() {
        return "MemSim{" +
                "totalSize=" + totalSize +
                ", freeMemory=" + freeMemory +
                ", usedMemory=" + usedMemory +
                ", osSize=" + osSize +
                ", processList=" + processList +
                ", waitList=" + waitList +
                '}';
    }
}
