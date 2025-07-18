/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Boundary;

import ADT.MyArrayList;
import Entity.Medicine;
import java.util.Scanner;

/**
 *
 * @author yapji
 */
public class PharmacyUI {
    private MyArrayList<Medicine> medicineList = new MyArrayList<>();
    private Scanner scanner = new Scanner(System.in);

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
            System.out.println("0. Exit");
            System.out.print("Enter choice: ");
            choice = scanner.nextInt();
            scanner.nextLine(); // consume newline

            switch (choice) {
                case 1 -> addMedicine();
                case 2 -> viewMedicines();
                case 3 -> searchMedicine();
                case 4 -> sortMedicines();
                case 5 -> dispenseMedicine();
                case 6 -> removeMedicine();
                case 0 -> System.out.println("Exiting...");
                default -> System.out.println("Invalid option.");
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
        int stock = scanner.nextInt();
        scanner.nextLine();
        System.out.print("Enter Expiry Date (YYYY-MM-DD): ");
        String expiry = scanner.nextLine();

        Medicine med = new Medicine(id, name, category, stock, expiry);
        medicineList.add(med);
        System.out.println("Medicine added.");
    }

    private void viewMedicines() {
        if (medicineList.isEmpty()) {
            System.out.println("No medicines available.");
            return;
        }
        for (int i = 0; i < medicineList.size(); i++) {
            System.out.println(medicineList.get(i));
        }
    }

    private void searchMedicine() {
        System.out.print("Enter name to search: ");
        String search = scanner.nextLine();
        boolean found = false;

        for (int i = 0; i < medicineList.size(); i++) {
            Medicine med = medicineList.get(i);
            if (med.getName().toLowerCase().contains(search.toLowerCase())) {
                System.out.println(med);
                found = true;
            }
        }

        if (!found) {
            System.out.println("No medicine found with that name.");
        }
    }

    private void sortMedicines() {
        medicineList.sortByStockDescending();
        System.out.println("Sorted medicines by stock (descending).");
    }

    private void dispenseMedicine() {
        System.out.print("Enter Medicine ID to dispense: ");
        String id = scanner.nextLine();
        System.out.print("Enter quantity to dispense: ");
        int quantity = scanner.nextInt();
        scanner.nextLine();

        for (int i = 0; i < medicineList.size(); i++) {
            Medicine med = medicineList.get(i);
            if (med.getMedicineID().equalsIgnoreCase(id)) {
                if (med.getStock() >= quantity) {
                    med.reduceStock(quantity);
                    System.out.println("Dispensed. Remaining stock: " + med.getStock());
                } else {
                    System.out.println("Not enough stock.");
                }
                return;
            }
        }

        System.out.println("Medicine ID not found.");
    }

    private void removeMedicine() {
        System.out.print("Enter Medicine ID to remove: ");
        String id = scanner.nextLine();

        for (int i = 0; i < medicineList.size(); i++) {
            Medicine med = medicineList.get(i);
            if (med.getMedicineID().equalsIgnoreCase(id)) {
                boolean removed = medicineList.remove(med);
                if (removed) {
                    System.out.println("Medicine removed.");
                }
                return;
            }
        }

        System.out.println("Medicine not found.");
    }
}