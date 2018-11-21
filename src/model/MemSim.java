package model;

public class MemSim {

    private int totalSize;
    private int freeMemory;
    private int usedMemory;
    private int osSize;

    public MemSim(int totalSize, int osSize) {
        this.totalSize = totalSize;
        this.osSize = osSize;
    }
}
