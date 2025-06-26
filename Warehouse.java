//package Project2;

public class Warehouse {
    private int id;
    private int material = 0;

    public Warehouse(int id) {
        this.id = id;
    }

    public synchronized void putMaterial(int amount) {
        material += amount;
        Main.printAligned(Thread.currentThread().getName(),
                String.format("put %d materials  Warehouse_%d balance = %d", amount, id, material));
    }

    public synchronized int getMaterial(int request) {
        int actual = Math.min(request, material);
        material -= actual;
        Main.printAligned(Thread.currentThread().getName(),
                String.format("get %d materials  Warehouse_%d balance = %d", actual, id, material));
        return actual;
    }
}



