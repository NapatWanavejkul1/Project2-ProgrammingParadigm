//package Project2;

public class Freight {
    private int id;
    private int capacity;
    private int remaining;

    public Freight(int id, int capacity) {
        this.id = id;
        this.capacity = capacity;
        this.remaining = capacity;
    }

    public void reset() {
        remaining = capacity;
    }

    public synchronized int ship(int request) {
        int actual = Math.min(remaining, request);
        remaining -= actual;
        Main.printAligned(Thread.currentThread().getName(),
                String.format("ship %d products  Freight_%d remaining capacity = %d", actual, id, remaining));
        return actual;
    }

    public int getRemainingCapacity() {
        return remaining;
    }

    public String getName() {
        return "Freight_" + id;
    }
}







