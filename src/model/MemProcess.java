package model;

public class MemProcess {
    private String pID;
    private int pSize;

    public String getpID() {
        return pID;
    }

    public void setpID(String pID) {
        this.pID = pID;
    }

    public int getpSize() {
        return pSize;
    }

    public void setpSize(int pSize) {
        this.pSize = pSize;
    }

    public MemProcess(String pID, int pSize) {
        this.pID = pID;
        this.pSize = pSize;
        setmemID();
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
