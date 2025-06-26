//package Project2;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class Main {
    public static int days;
    public static int warehouseNum;
    public static int freightNum;
    public static int freightCap;
    public static int supplierNum;
    public static int supplierMin;
    public static int supplierMax;
    public static int factoryNum;
    public static int factoryMax;

    public static Warehouse[] warehouses;
    public static Freight[] freights;
    public static ArrayList<FactoryThread> factories = new ArrayList<>();
    public static int parties;
    public static int arrived = 0;
    public static final Object lock = new Object();

    public static void syncPoint() {
        synchronized (lock) {
            arrived++;
            if (arrived < parties) {
                try {
                    lock.wait();
                } catch (InterruptedException ignored) {}
            } else {
                arrived = 0;
                lock.notifyAll();
            }
        }
    }

    public static void printAligned(String name, String message) {
        System.out.printf("%-18s>> %s%n", name, message);
    }

    public static void main(String[] args) {
        Thread.currentThread().setName("main");
        try {
            Scanner sc = new Scanner(new File("src/config_1.txt"));
            days = Integer.parseInt(sc.nextLine().split(",")[1].trim());
            warehouseNum = Integer.parseInt(sc.nextLine().split(",")[1].trim());

            String[] freightLine = sc.nextLine().split(",");
            freightNum = Integer.parseInt(freightLine[1].trim());
            freightCap = Integer.parseInt(freightLine[2].trim());

            String[] supplierLine = sc.nextLine().split(",");
            supplierNum = Integer.parseInt(supplierLine[1].trim());
            supplierMin = Integer.parseInt(supplierLine[2].trim());
            supplierMax = Integer.parseInt(supplierLine[3].trim());

            String[] factoryLine = sc.nextLine().split(",");
            factoryNum = Integer.parseInt(factoryLine[1].trim());
            factoryMax = Integer.parseInt(factoryLine[2].trim());
        } catch (FileNotFoundException e) {
            System.out.println("main >> Config file not found.");
            return;
        }

        parties = 1 + supplierNum + factoryNum;

        warehouses = new Warehouse[warehouseNum];
        for (int i = 0; i < warehouseNum; i++) {
            warehouses[i] = new Warehouse(i);
        }

        freights = new Freight[freightNum];
        for (int i = 0; i < freightNum; i++) {
            freights[i] = new Freight(i, freightCap);
        }

        SupplierThread[] suppliers = new SupplierThread[supplierNum];
        for (int i = 0; i < supplierNum; i++) {
            suppliers[i] = new SupplierThread(i, supplierMin, supplierMax);
        }

        for (int i = 0; i < factoryNum; i++) {
            FactoryThread f = new FactoryThread(i, factoryMax);
            factories.add(f);
        }

        for (SupplierThread s : suppliers) s.start();
        for (FactoryThread f : factories) f.start();

        for (int d = 1; d <= days; d++) {
            printAligned("main", "==============================");
            printAligned("main", "Day " + d);
            printAligned("main", "==============================");

            for (Freight f : freights) {
                f.reset();
                printAligned("main", f.getName() + " capacity = " + f.getRemainingCapacity());
            }

            syncPoint();
            syncPoint();
            syncPoint();
        }

        for (FactoryThread f : factories) {
            try {
                f.join();
            } catch (InterruptedException ignored) {}
        }

        printAligned("main", "==============================");
        printAligned("main", "Summary");

        factories.sort((f1, f2) -> {
            int diff = f2.getTotalMade() - f1.getTotalMade();
            return (diff != 0) ? diff : f1.getName().compareTo(f2.getName());
        });

        for (FactoryThread f : factories) {
            int total = f.getTotalMade();
            int shipped = f.getTotalShipped();
            double percent = (total == 0) ? 0.0 : (100.0 * shipped / total);
            printAligned("main", String.format("%s >> total products = %d  shipped = %d  (%.2f%%)",
                    f.getName(), total, shipped, percent));
        }
    }
}


