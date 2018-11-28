package model;

public class MemSim {

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
                holes.add(new Hole(count, i));
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
}
