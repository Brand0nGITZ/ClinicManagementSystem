/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Boundary;

import ADT.MyArrayList;
import Control.MedicineMaintenance;
import Entity.Medicine;
import java.util.Scanner;

/**
 *
 * @author yapji
 */
public class PharmacyUI {
    private final Scanner scanner = new Scanner(System.in);
    private final MedicineMaintenance medControl;

    public PharmacyUI() {
        MyArrayList<Medicine> medicineList = new MyArrayList<>();
        this.medControl = new MedicineMaintenance(medicineList);
    }

    public void run() {
        int choice;
        do {
            System.out.println("\n=== Pharmacy Management ===");
            System.out.println("1. Add Medicine");
            System.out.println("2. View All Medicines");
            System.out.println("3. Search Medicine by Name");
            System.out.println("4. Sort Medicines by Stock (Descending)");
            System.out.println("5. Dispense Medicine (Reduce Stock)");
            System.out.println("6. Remove Medicine");
            System.out.println("7. Low Stock Report");
            System.out.println("0. Exit");
            System.out.print("Enter choice: ");
            choice = scanner.nextInt(); scanner.nextLine();

            switch (choice) {
                case 1 -> addMedicine();
                case 2 -> viewMedicines();
                case 3 -> searchMedicine();
                case 4 -> sortMedicines();
                case 5 -> dispenseMedicine();
                case 6 -> removeMedicine();
                case 7 -> lowStockReport();
                case 0 -> System.out.println("Exiting...");
                default -> System.out.println("Invalid choice.");
            }
        } while (choice != 0);
    }

    private void addMedicine() {
        System.out.print("Enter Medicine ID: ");
        String id = scanner.nextLine();
        System.out.print("Enter Name: ");
        String name = scanner.nextLine();
        System.out.print("Enter Category: ");
        String category = scanner.nextLine();
        System.out.print("Enter Stock: ");
        int stock = scanner.nextInt(); scanner.nextLine();
        System.out.print("Enter Expiry Date (YYYY-MM-DD): ");
        String expiry = scanner.nextLine();

        Medicine med = new Medicine(id, name, category, stock, expiry);
        medControl.addMedicine(med);
        System.out.println(med.getName() + " | " +  med.getCategory() + " | "  + med.getStock() +  " | " + med.getExpiryDate() + " | "  );
    }

    private void viewMedicines() {
        MyArrayList<Medicine> list = medControl.getAllMedicines();
        if (list.isEmpty()) {
            System.out.println("No medicines available.");
            return;
        }
        System.out.println("=== Medicine List ===");
        for (int i = 0; i < list.size(); i++) {
            System.out.println(list.get(i));
        }
    }

    private void searchMedicine() {
        System.out.print("Enter medicine name to search: ");
        String name = scanner.nextLine();
        Medicine med = medControl.findByName(name);
        if (med != null) { 
            System.out.println("Found: " + med);
        } else {
            System.out.println("Medicine not found.");
        }
    }

    private void sortMedicines() {
        medControl.sortMedicinesByStock();
        System.out.println("Medicines sorted by stock (descending).");
    }

    private void dispenseMedicine() {
        System.out.print("Enter Medicine ID: ");
        String id = scanner.nextLine();
        System.out.print("Enter quantity to dispense: ");
        int qty = scanner.nextInt(); scanner.nextLine();
        boolean success = medControl.dispenseMedicine(id, qty);
        if (success) {
            System.out.println("Dispensed successfully.");
        } else {
            System.out.println("Failed. Either not found or insufficient stock.");
        }
    }

    private void removeMedicine() {
        System.out.print("Enter Medicine ID to remove: ");
        String id = scanner.nextLine();
        boolean removed = medControl.removeMedicine(id);
        if (removed) {
            System.out.println("✅ Medicine removed.");
        } else {
            System.out.println("❌ Medicine not found.");
        }
    }

    private void lowStockReport() {
        System.out.print("Enter stock threshold: ");
        int threshold = scanner.nextInt(); scanner.nextLine();
        medControl.generateLowStockReport(threshold);
    }
}