/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Boundary;

import ADT.MyArrayList;
import ADT.ListInterface;
import Control.MedicineMaintenance;
import Entity.Medicine;
import java.util.Scanner;

/**
 *
 * @author yapji
 */
public class PharmacyUI {
    private final Scanner scanner = new Scanner(System.in);
    private final MedicineMaintenance medicineControl;

    public PharmacyUI(MedicineMaintenance medicineControl) {
        this.medicineControl = medicineControl;
        // Medicines are now initialized in the main application
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
        System.out.println("8. Pharmacy Analytics");
        System.out.println("9. View Diagnosis-Medicine Relationships");
        System.out.println("10. Get Medicines for Diagnosis");
        System.out.println("11. Manage Diagnosis-Medicine Relationships");
        System.out.println("12. Calculate Medicine Revenue");
        System.out.println("13. Dispensed Medicine Summary Report");
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
                case 8 -> pharmacyAnalytics();
                case 9 -> viewDiagnosisMedicineRelationships();
                case 10 -> getMedicinesForDiagnosis();
                case 11 -> manageDiagnosisMedicineRelationships();
                case 12 -> calculateMedicineRevenue();
                case 13 -> dispensedMedicineSummaryReport();
                case 0 -> System.out.println("Exiting...");
                default -> System.out.println("Invalid choice.");
            }
        } while (choice != 0);
    }

    private void addMedicine() {
        // Auto-generate medicine ID
        String id = medicineControl.generateMedicineId();
        System.out.println("Auto-generated Medicine ID: " + id);
        
        System.out.print("Enter Medicine Name: ");
        String name = scanner.nextLine();
        
        // Show available diagnosis categories
        ListInterface<String> diagnoses = medicineControl.getAvailableDiagnoses();
        System.out.println("\nAvailable Diagnosis Categories:");
        for (int i = 0; i < diagnoses.size(); i++) {
            System.out.println((i + 1) + ". " + diagnoses.get(i));
        }
        System.out.println((diagnoses.size() + 1) + ". General Medication");
        
        System.out.print("Select Category (1-" + (diagnoses.size() + 1) + "): ");
        int categoryChoice = scanner.nextInt(); scanner.nextLine();
        
        String category;
        if (categoryChoice <= diagnoses.size()) {
            category = diagnoses.get(categoryChoice - 1);
        } else {
            category = "General Medication";
        }
        
        System.out.print("Enter Stock Quantity: ");
        int stock = scanner.nextInt(); scanner.nextLine();
        System.out.print("Enter Expiry Date (YYYY-MM-DD): ");
        String expiry = scanner.nextLine();

        Medicine med = new Medicine(id, name, category, stock, expiry);
        medicineControl.addMedicine(med);
        
        System.out.println("\nMedicine added successfully!");
        System.out.println("   ID: " + id);
        System.out.println("   Name: " + name);
        System.out.println("   Category: " + category);
        System.out.println("   Stock: " + stock);
        System.out.println("   Expiry: " + expiry);
        
        // Show potential revenue
        double basePrice = medicineControl.calculateRevenueFromMedicine(id, 1);
        System.out.println("   Base Price: RM" + String.format("%.2f", basePrice));
    }

    private void viewMedicines() {
        MyArrayList<Medicine> list = medicineControl.getAllMedicines();
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
        Medicine med = medicineControl.findByName(name);
        if (med != null) { 
            System.out.println("Found: " + med);
        } else {
            System.out.println("Medicine not found.");
        }
    }

    private void sortMedicines() {
        medicineControl.sortMedicinesByStock();
        System.out.println("Medicines sorted by stock (descending).");
    }

    private void dispenseMedicine() {
        System.out.print("Enter Medicine ID: ");
        String id = scanner.nextLine();
        System.out.print("Enter quantity to dispense: ");
        int qty = scanner.nextInt(); scanner.nextLine();
        boolean success = medicineControl.dispenseMedicine(id, qty);
        if (success) {
            System.out.println("Dispensed successfully.");
        } else {
            System.out.println("Failed. Either not found or insufficient stock.");
        }
    }

    private void removeMedicine() {
        System.out.print("Enter Medicine ID to remove: ");
        String id = scanner.nextLine();
        boolean removed = medicineControl.removeMedicine(id);
        if (removed) {
            System.out.println("Medicine removed.");
        } else {
            System.out.println("Medicine not found.");
        }
    }

    private void lowStockReport() {
        System.out.print("Enter stock threshold: ");
        int threshold = scanner.nextInt(); scanner.nextLine();
        medicineControl.generateLowStockReport(threshold);
    }
    
    private void pharmacyAnalytics() {
        MyArrayList<Medicine> list = medicineControl.getAllMedicines();
        if (list.isEmpty()) {
            System.out.println("No medicines available for analytics.");
            return;
        }
        
        System.out.println("\nPHARMACY ANALYTICS DASHBOARD");
        System.out.println("=".repeat(50));
        
        // 🔍 Most Popular Medicine Category (using frequency tracking)
        Medicine mostFrequent = list.getMostFrequent();
        if (mostFrequent != null) {
            System.out.println("Most Popular Category: " + mostFrequent.getCategory());
            System.out.println("   (Based on frequency in inventory)");
        }
        
        // Stock Distribution Analysis
        System.out.println("\nSTOCK DISTRIBUTION:");
        int highStock = list.filter(med -> med.getStock() > 50).size();
        int mediumStock = list.filter(med -> med.getStock() >= 20 && med.getStock() <= 50).size();
        int lowStock = list.filter(med -> med.getStock() < 20).size();
        
        System.out.println("   High Stock (>50): " + highStock + " medicines");
        System.out.println("   Medium Stock (20-50): " + mediumStock + " medicines");
        System.out.println("   Low Stock (<20): " + lowStock + " medicines");
        
        // Category Breakdown
        System.out.println("\nMEDICINE CATEGORIES:");
        int painkillers = list.filter(med -> med.getCategory().equalsIgnoreCase("Painkiller")).size();
        int antibiotics = list.filter(med -> med.getCategory().equalsIgnoreCase("Antibiotic")).size();
        int vitamins = list.filter(med -> med.getCategory().equalsIgnoreCase("Vitamin")).size();
        int others = list.size() - painkillers - antibiotics - vitamins;
        
        System.out.println("   Painkillers: " + painkillers);
        System.out.println("   Antibiotics: " + antibiotics);
        System.out.println("   Vitamins: " + vitamins);
        System.out.println("   Others: " + others);
        
        // Urgent Alerts
        System.out.println("\nURGENT ALERTS:");
        int expiringSoon = list.filter(med -> med.getExpiryDate().contains("2025-06")).size();
        if (expiringSoon > 0) {
            System.out.println(expiringSoon + " medicines expiring in June 2025!");
        } else {
            System.out.println("No urgent expiry alerts");
        }
        
        // Random Medicine Spotlight (for staff training)
        Medicine randomMed = list.getRandom();
        if (randomMed != null) {
            System.out.println("\nSTAFF TRAINING SPOTLIGHT:");
            System.out.println("   Today's featured medicine: " + randomMed.getName());
            System.out.println("   Category: " + randomMed.getCategory());
            System.out.println("   Current Stock: " + randomMed.getStock());
        }        
        
        System.out.println("\n" + "=".repeat(50));
    }
    
    private void viewDiagnosisMedicineRelationships() {
        System.out.println("\nDIAGNOSIS-MEDICINE RELATIONSHIPS");
        System.out.println("=".repeat(50));
        medicineControl.generateDiagnosisMedicineReport();
    }
    
    private void getMedicinesForDiagnosis() {
        System.out.println("\nGET MEDICINES FOR DIAGNOSIS");
        System.out.println("=".repeat(40));
        
        // Show available diagnoses
        ListInterface<String> diagnoses = medicineControl.getAllDiagnoses();
        System.out.println("Available Diagnoses:");
        for (int i = 0; i < diagnoses.size(); i++) {
            System.out.println((i + 1) + ". " + diagnoses.get(i));
        }
        
        System.out.print("\nEnter diagnosis name: ");
        String diagnosis = scanner.nextLine();
        
        ListInterface<Medicine> medicines = medicineControl.getMedicinesForDiagnosis(diagnosis);
        
        if (medicines.isEmpty()) {
            System.out.println("No medicines found for diagnosis: " + diagnosis);
        } else {
            System.out.println("\nPrescribed Medicines for " + diagnosis + ":");
            System.out.println("-".repeat(40));
            for (int i = 0; i < medicines.size(); i++) {
                Medicine med = medicines.get(i);
                System.out.println((i + 1) + ". " + med.getName() + " (ID: " + med.getMedicineID() + ")");
                System.out.println("   Category: " + med.getCategory() + " | Stock: " + med.getStock() + " | Expiry: " + med.getExpiryDate());
                    }
                }
    }
    
    private void calculateMedicineRevenue() {
        System.out.println("\nCALCULATE MEDICINE REVENUE");
        System.out.println("=".repeat(40));
        
        // Show all medicines with their base prices
        ListInterface<Medicine> medicines = medicineControl.getAllMedicines();
        if (medicines.isEmpty()) {
            System.out.println("No medicines available.");
            return;
        }
        
        System.out.println("Available Medicines:");
        for (int i = 0; i < medicines.size(); i++) {
            Medicine med = medicines.get(i);
            double basePrice = medicineControl.calculateRevenueFromMedicine(med.getMedicineID(), 1);
            System.out.println((i + 1) + ". " + med.getName() + " (ID: " + med.getMedicineID() + ")");
            System.out.println("   Category: " + med.getCategory() + " | Base Price: RM" + String.format("%.2f", basePrice));
        }
        
        System.out.print("\nEnter Medicine ID: ");
        String medicineId = scanner.nextLine();
        
        System.out.print("Enter Quantity to Sell: ");
        int quantity = scanner.nextInt(); scanner.nextLine();
        
        double revenue = medicineControl.calculateRevenueFromMedicine(medicineId, quantity);
        
        if (revenue > 0) {
            System.out.println("\nREVENUE CALCULATION:");
            System.out.println("   Medicine ID: " + medicineId);
            System.out.println("   Quantity: " + quantity);
            System.out.println("   Total Revenue: RM" + String.format("%.2f", revenue));
            
            // Check if medicine exists and show details
            Medicine medicine = medicineControl.findById(medicineId);
            if (medicine != null) {
                System.out.println("   Medicine: " + medicine.getName());
                System.out.println("   Category: " + medicine.getCategory());
                System.out.println("   Current Stock: " + medicine.getStock());
                
                if (quantity > medicine.getStock()) {
                    System.out.println("   WARNING: Insufficient stock! Only " + medicine.getStock() + " available.");
                }
            }
        } else {
            System.out.println("Medicine not found or invalid quantity.");
        }
    }
    
    private void manageDiagnosisMedicineRelationships() {
        System.out.println("\nMANAGE DIAGNOSIS-MEDICINE RELATIONSHIPS");
        System.out.println("=".repeat(50));
        System.out.println("1. Add new diagnosis-medicine relationship");
        System.out.println("2. Remove diagnosis-medicine relationship");
        System.out.println("3. View all relationships");
        System.out.print("Enter choice: ");
        int choice = scanner.nextInt(); scanner.nextLine();
        
        switch (choice) {
            case 1 -> {
                System.out.print("Enter diagnosis: ");
                String diagnosis = scanner.nextLine();
                System.out.print("Enter medicine ID: ");
                String medicineId = scanner.nextLine();
                
                // Check if medicine exists
                Medicine medicine = medicineControl.findById(medicineId);
                if (medicine != null) {
                    medicineControl.addDiagnosisMedicineRelationship(diagnosis, medicineId);
                    System.out.println("Relationship added: " + diagnosis + " → " + medicine.getName());
                } else {
                    System.out.println("Medicine with ID " + medicineId + " not found!");
                }
            }
            case 2 -> {
                System.out.print("Enter diagnosis: ");
                String diagnosis = scanner.nextLine();
                System.out.print("Enter medicine ID: ");
                String medicineId = scanner.nextLine();
                
                medicineControl.removeDiagnosisMedicineRelationship(diagnosis, medicineId);
                System.out.println("Relationship removed: " + diagnosis + " → " + medicineId);
            }
            case 3 -> {
                viewDiagnosisMedicineRelationships();
            }
            default -> System.out.println("Invalid choice.");
        }
    }
    
    private void dispensedMedicineSummaryReport() {
        System.out.println("\nDISPENSED MEDICINE SUMMARY REPORT");
        System.out.println("=".repeat(50));
        
        ListInterface<Medicine> medicines = medicineControl.getAllMedicines();
        if (medicines.isEmpty()) {
            System.out.println("No medicines available for report.");
            return;
        }
        
        // Calculate total inventory value
        double totalInventoryValue = 0.0;
        int totalStock = 0;
        
        System.out.println("MEDICINE INVENTORY STATUS:");
        System.out.println("-".repeat(40));
        
        for (int i = 0; i < medicines.size(); i++) {
            Medicine med = medicines.get(i);
            double medicineValue = medicineControl.calculateRevenueFromMedicine(med.getMedicineID(), med.getStock());
            totalInventoryValue += medicineValue;
            totalStock += med.getStock();
            
            System.out.println((i + 1) + ". " + med.getName() + " (ID: " + med.getMedicineID() + ")");
            System.out.println("   Category: " + med.getCategory());
            System.out.println("   Current Stock: " + med.getStock() + " units");
            System.out.println("   Stock Value: RM" + String.format("%.2f", medicineValue));
            
            // Stock level indicators
            if (med.getStock() < 20) {
                System.out.println("   LOW STOCK ALERT!");
            } else if (med.getStock() > 100) {
                System.out.println("   Good stock level");
            } else {
                System.out.println("   Moderate stock level");
            }
            System.out.println();
        }
        
        System.out.println("SUMMARY STATISTICS:");
        System.out.println("-".repeat(40));
        System.out.println("   Total Medicines: " + medicines.size());
        System.out.println("   Total Stock Units: " + totalStock);
        System.out.println("   Total Inventory Value: RM" + String.format("%.2f", totalInventoryValue));
        
        // Category breakdown
        int painkillers = medicines.filter(med -> med.getCategory().equalsIgnoreCase("Painkiller")).size();
        int coldMedicine = medicines.filter(med -> med.getCategory().equalsIgnoreCase("Cold Medicine")).size();
        int digestive = medicines.filter(med -> med.getCategory().equalsIgnoreCase("Digestive")).size();
        int others = medicines.size() - painkillers - coldMedicine - digestive;
        
        System.out.println("\nCATEGORY BREAKDOWN:");
        System.out.println("   Painkillers: " + painkillers + " medicines");
        System.out.println("   Cold Medicine: " + coldMedicine + " medicines");
        System.out.println("   Digestive: " + digestive + " medicines");
        System.out.println("   Others: " + others + " medicines");
        
        // Low stock alerts
        ListInterface<Medicine> lowStockMedicines = medicines.filter(med -> med.getStock() < 20);
        if (!lowStockMedicines.isEmpty()) {
            System.out.println("\nLOW STOCK ALERTS:");
            for (int i = 0; i < lowStockMedicines.size(); i++) {
                Medicine med = lowStockMedicines.get(i);
                System.out.println("   • " + med.getName() + " - Only " + med.getStock() + " units remaining");
            }
        }
        
        System.out.println("\n" + "=".repeat(50));
    }
}