//package Project2z_2;

import java.util.Random;

public class FactoryThread extends Thread {
    private int maxProduction;
    private int unshipped = 0;
    private int total = 0;
    private int shipped = 0;

    public FactoryThread(int id, int max) {
        super("FactoryThread_" + id);
        this.maxProduction = max;
    }

    public void run() {
        for (int day = 0; day < Main.days; day++) {
            Main.syncPoint();
            Main.syncPoint();

            Random rand = new Random();
            int target = rand.nextInt(Main.warehouseNum);
            int amount = Main.warehouses[target].getMaterial(maxProduction);
            total += amount;
            unshipped += amount;

            Main.syncPoint();

            int toShip = unshipped;
            for (Freight f : Main.freights) {
                if (toShip <= 0) break;
                int shippedNow = f.ship(toShip);
                toShip -= shippedNow;
                unshipped -= shippedNow;
                shipped += shippedNow;
            }

            Main.printAligned(getName(), "unshipped products = " + unshipped);
        }
    }

    public int getTotalMade() {
        return total;
    }

    public int getTotalShipped() {
        return shipped;
    }
}






