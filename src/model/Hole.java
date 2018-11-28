package model;

public class Hole {
    private int size;
    private int start;
    private int end;

    public Hole(int size, int start) {
        this.size = size;
        this.start = start;
        this.end = start + size;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public int getEnd() {
        return end;
    }

    public void setEnd(int end) {
        this.end = end;
    }

    @Override
    public String toString() {
        return "Hole{" +
                "size=" + size +
                ", start=" + start +
                ", end=" + end +
                '}';
    }
}
