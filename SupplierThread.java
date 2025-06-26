//package Project2;

import java.util.Random;

public class SupplierThread extends Thread {
    private int min, max;

    public SupplierThread(int id, int min, int max) {
        super("SupplierThread_" + id);
        this.min = min;
        this.max = max;
    }

    public void run() {
        for (int day = 0; day < Main.days; day++) {
            Main.syncPoint();

            Random rand = new Random();
            int amount = rand.nextInt(max - min + 1) + min;
            int target = rand.nextInt(Main.warehouseNum);

            Main.warehouses[target].putMaterial(amount);

            Main.syncPoint();
            Main.syncPoint();
        }
    }
}







