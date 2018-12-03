package model;

//Used to mimic holes in memory
public class Hole {
    private int size;
    private int start;
    private int end;

    //Constructor for holes which are open spots in memory
    public Hole(int size, int start) {
        this.size = size;
        this.start = start;
        this.end = start + size;
    }

    public int getSize() {
        return size;
    }

    public int getStart() {
        return start;
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
