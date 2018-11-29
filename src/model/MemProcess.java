package model;

public class MemProcess {
    private String pID;
    private int pSize;
    private int startLocation;
    private int endLocation;
    private int memId;

    public MemProcess(String pID, int pSize) {
        this.pID = pID;
        this.pSize = pSize;
        setmemID();
    }

    public MemProcess(String pID, int pSize, int start, int end, int memId) {
        this.pID = pID;
        this.pSize = pSize;
        this.startLocation = start;
        this.endLocation = end;
        this.memId = memId;
    }

    public int getStartLocation() {
        return startLocation;
    }

    public void setStartLocation(int startLocation) {
        this.startLocation = startLocation;
    }

    public int getEndLocation() {
        return endLocation;
    }

    public void setEndLocation(int endLocation) {
        this.endLocation = endLocation;
    }

    public String getpID() {
        return pID;
    }

    public void setpID(String pID) {
        this.pID = pID;
    }

    @Override
    public String toString() {
        return  "pID='" + pID + '\'' +
                ", pSize=" + pSize +
                ", start=" + startLocation +
                ", end=" + endLocation +
                ", memId=" + memId +
                '}' + '\n';
    }

    public int getpSize() {
        return pSize;
    }

    public void setpSize(int pSize) {
        this.pSize = pSize;
    }

    private void setmemID() {
        try {
            String[] temp = pID.split(" ");
            memId = Integer.parseInt(temp[1]);
        } catch (Exception e) {
            memId = 9;              //OS CORRECT
        }
    }


    public int getmemID() {
        return memId;
    }
}
