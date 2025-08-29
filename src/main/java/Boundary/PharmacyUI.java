/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Boundary;

import ADT.MyArrayList;
import ADT.ListInterface;
import Control.MedicineMaintenance;
import Control.MedicalTreatmentManagement;
import Control.ConsultationManagement;
import Entity.Medicine;
import Entity.MedicalTreatment;
import Entity.Consultation;
import Utility.LinearSearch;
import java.util.Scanner;

/**
 *
 * @author yapjinkai
 */
public class PharmacyUI {
    private final Scanner scanner = new Scanner(System.in);
    private final MedicineMaintenance medicineControl;
    private final MedicalTreatmentManagement treatmentControl;
    private final ConsultationManagement consultationControl;

    public PharmacyUI(MedicineMaintenance medicineControl, MedicalTreatmentManagement treatmentControl, ConsultationManagement consultationControl) {
        this.medicineControl = medicineControl;
        this.treatmentControl = treatmentControl;
        this.consultationControl = consultationControl;
        // Medicines are now initialized in the main application
    }



    public void run() {
        int choice;
                do {
            System.out.println("\n=== Pharmacy Management ===");
            System.out.println("1. Process Payment");
            System.out.println("2. Add Medicine");
            System.out.println("3. View All Medicines");
            System.out.println("4. Search Medicine by Name");
            System.out.println("5. Sort Medicines by Stock (Descending)");
            System.out.println("6. Remove Medicine");
            System.out.println("7. Pharmacy Analytics");
            System.out.println("8. View Diagnosis-Medicine Relationships");
            System.out.println("9. Get Medicines for Diagnosis");
            System.out.println("10. Manage Diagnosis-Medicine Relationships");
            System.out.println("11. Dispensed Medicine Summary Report");
            System.out.println("0. Exit");
            System.out.print("Enter choice: ");
            choice = scanner.nextInt(); scanner.nextLine();

            switch (choice) {
                case 1 -> processPayment();
                case 2 -> addMedicine();
                case 3 -> viewMedicines();
                case 4 -> searchMedicine();
                case 5 -> sortMedicines();
                case 6 -> removeMedicine();
                case 7 -> pharmacyAnalytics();
                case 8 -> viewDiagnosisMedicineRelationships();
                case 9 -> getMedicinesForDiagnosis();
                case 10 -> manageDiagnosisMedicineRelationships();
                case 11 -> dispensedMedicineSummaryReport();
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
        
        System.out.println("\n=== All Medicines ===");
        System.out.println("================================================================================================================");
        System.out.println("| #  | Medicine ID | Medicine Name           | Category        | Stock | Expiry Date | Base Price |");
        System.out.println("|==============================================================================================================|");
        
        for (int i = 0; i < list.size(); i++) {
            Medicine medicine = list.get(i);
            double basePrice = medicineControl.calculateRevenueFromMedicine(medicine.getMedicineID(), 1);
            
            System.out.printf("| %-2d | %-11s | %-22s | %-15s | %-5d | %-11s | RM%-8.2f |\n",
                (i + 1),
                medicine.getMedicineID(),
                medicine.getName(),
                medicine.getCategory(),
                medicine.getStock(),
                medicine.getExpiryDate(),
                basePrice);
        }
        System.out.println("================================================================================================================");
        System.out.printf("Total Medicines: %d\n", list.size());
    }

    private void searchMedicine() {
        System.out.print("Enter medicine name to search: ");
        String searchQuery = scanner.nextLine();
        
        // Use linear search with custom iterator
        ListInterface<Medicine> allMedicines = medicineControl.getAllMedicines();
        MyArrayList<LinearSearch.SearchResult<Medicine>> searchResults = 
            LinearSearch.search(searchQuery, allMedicines);
        
        if (searchResults.isEmpty()) {
            System.out.println("No medicines found matching '" + searchQuery + "'.");
            return;
        }
        
        System.out.println("\n=== Medicine Search Results (Linear Search) ===");
        System.out.println("(Using Linear Search with Custom Iterator)");
        System.out.println("================================================================================================================");
        System.out.println("| #  | Medicine ID | Medicine Name           | Category        | Stock | Expiry Date | Base Price |");
        System.out.println("|==============================================================================================================|");
        
        for (int i = 0; i < searchResults.size(); i++) {
            LinearSearch.SearchResult<Medicine> result = searchResults.get(i);
            Medicine medicine = result.item;
            double basePrice = medicineControl.calculateRevenueFromMedicine(medicine.getMedicineID(), 1);
            
            System.out.printf("| %-2d | %-11s | %-22s | %-15s | %-5d | %-11s | RM%-8.2f |\n",
                (i + 1),
                medicine.getMedicineID(),
                medicine.getName(),
                medicine.getCategory(),
                medicine.getStock(),
                medicine.getExpiryDate(),
                basePrice);
        }
        System.out.println("================================================================================================================");
        System.out.printf("Found %d medicine(s) matching '%s'\n", searchResults.size(), searchQuery);
    }

    private void sortMedicines() {
        medicineControl.sortMedicinesByStock();
        System.out.println("Medicines sorted by stock (descending).");
    }



    private void removeMedicine() {
        System.out.println("\n=== Remove Medicine ===");
        System.out.println("================================================================================================================");
        
        System.out.print("Enter Medicine ID or Name to remove: ");
        String searchQuery = scanner.nextLine();
        
        // Use linear search to find the medicine
        ListInterface<Medicine> allMedicines = medicineControl.getAllMedicines();
        MyArrayList<LinearSearch.SearchResult<Medicine>> searchResults = 
            LinearSearch.search(searchQuery, allMedicines);
        
        if (searchResults.isEmpty()) {
            System.out.println("| No medicines found matching '" + searchQuery + "'                                                      |");
            System.out.println("================================================================================================================");
            return;
        }
        
        if (searchResults.size() > 1) {
            System.out.println("| Multiple medicines found. Please select one:                                                           |");
            System.out.println("|==============================================================================================================|");
            System.out.println("| #  | Medicine ID | Medicine Name           | Category        | Stock | Expiry Date | Base Price |");
            System.out.println("|==============================================================================================================|");
            
            for (int i = 0; i < searchResults.size(); i++) {
                LinearSearch.SearchResult<Medicine> result = searchResults.get(i);
                Medicine medicine = result.item;
                double basePrice = medicineControl.calculateRevenueFromMedicine(medicine.getMedicineID(), 1);
                
                System.out.printf("| %-2d | %-11s | %-22s | %-15s | %-5d | %-11s | RM%-8.2f |\n",
                    (i + 1),
                    medicine.getMedicineID(),
                    medicine.getName(),
                    medicine.getCategory(),
                    medicine.getStock(),
                    medicine.getExpiryDate(),
                    basePrice);
            }
            System.out.println("|==============================================================================================================|");
            
            System.out.print("Select medicine to remove (1-" + searchResults.size() + "): ");
            int choice = scanner.nextInt(); scanner.nextLine();
            
            if (choice < 1 || choice > searchResults.size()) {
                System.out.println("Invalid selection!");
                return;
            }
            
            searchResults = new MyArrayList<>();
            searchResults.add(searchResults.get(choice - 1));
        }
        
        // Get the selected medicine
        Medicine selectedMedicine = searchResults.get(0).item;
        
        System.out.println("\n=== Medicine to Remove ===");
        System.out.println("================================================================================================================");
        System.out.printf("| Medicine ID: %-15s | Medicine Name: %-20s |\n", selectedMedicine.getMedicineID(), selectedMedicine.getName());
        System.out.printf("| Category: %-15s | Stock: %-5d | Expiry: %-11s |\n", selectedMedicine.getCategory(), selectedMedicine.getStock(), selectedMedicine.getExpiryDate());
        System.out.println("================================================================================================================");
        
        System.out.print("Are you sure you want to remove this medicine? (y/n): ");
        String confirm = scanner.nextLine();
        
        if (confirm.equalsIgnoreCase("y") || confirm.equalsIgnoreCase("yes")) {
            boolean removed = medicineControl.removeMedicine(selectedMedicine.getMedicineID());
            if (removed) {
                System.out.println("| Medicine removed successfully!                                                                           |");
                System.out.println("| Removed: " + selectedMedicine.getName() + " (" + selectedMedicine.getMedicineID() + ")                    |");
            } else {
                System.out.println("| Failed to remove medicine.                                                                              |");
            }
        } else {
            System.out.println("| Medicine removal cancelled.                                                                                |");
        }
        System.out.println("================================================================================================================");
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
        
        // Total Revenue Calculation from Actual Treatments
        System.out.println("\nTOTAL REVENUE:");
        ListInterface<MedicalTreatment> allTreatments = treatmentControl.getAllTreatments();
        double totalTreatmentRevenue = 0.0;
        double totalMedicineRevenue = 0.0;
        int paidTreatments = 0;
        
        for (int i = 0; i < allTreatments.size(); i++) {
            MedicalTreatment treatment = allTreatments.get(i);
            if ("PAID".equals(treatment.getStatus())) {
                paidTreatments++;
                totalTreatmentRevenue += treatment.getCost();
                
                // Calculate medicine revenue for this treatment
                String diagnosis = treatment.getDiagnosis();
                ListInterface<Medicine> treatmentMedicines = medicineControl.getMedicinesForDiagnosis(diagnosis);
                for (int j = 0; j < treatmentMedicines.size(); j++) {
                    Medicine med = treatmentMedicines.get(j);
                    int quantity = calculateQuantityForMedicine(med, diagnosis);
                    double medicineRevenue = medicineControl.calculateRevenueFromMedicine(med.getMedicineID(), quantity);
                    totalMedicineRevenue += medicineRevenue;
                }
            }
        }
        
        double totalRevenue = totalTreatmentRevenue + totalMedicineRevenue;
        
        System.out.println("   Paid Treatments: " + paidTreatments);
        System.out.println("   Treatment Revenue: RM" + String.format("%.2f", totalTreatmentRevenue));
        System.out.println("   Medicine Revenue: RM" + String.format("%.2f", totalMedicineRevenue));
        System.out.println("   Total Revenue: RM" + String.format("%.2f", totalRevenue));
        System.out.println("   (From completed and paid treatments)");
        
        // Urgent Alerts - Medicines expiring in 2 months
        System.out.println("\nURGENT ALERTS:");
        MyArrayList<Medicine> expiringMedicines = new MyArrayList<>();
        
        // Calculate 2 months from now
        java.time.LocalDate currentDate = java.time.LocalDate.now();
        java.time.LocalDate twoMonthsFromNow = currentDate.plusMonths(2);
        
        for (int i = 0; i < list.size(); i++) {
            Medicine medicine = list.get(i);
            try {
                java.time.LocalDate expiryDate = java.time.LocalDate.parse(medicine.getExpiryDate());
                if (expiryDate.isBefore(twoMonthsFromNow) && expiryDate.isAfter(currentDate)) {
                    expiringMedicines.add(medicine);
                }
            } catch (Exception e) {
                // Skip medicines with invalid date format
            }
        }
        
        if (!expiringMedicines.isEmpty()) {
            System.out.println(expiringMedicines.size() + " medicines expiring within 2 months!");
            System.out.println("Expiring medicines:");
            for (int i = 0; i < expiringMedicines.size(); i++) {
                Medicine med = expiringMedicines.get(i);
                System.out.println("   • " + med.getName() + " - Expires: " + med.getExpiryDate() + " (Stock: " + med.getStock() + ")");
            }
        } else {
            System.out.println("No medicines expiring within 2 months");
        }
        
        System.out.println("\n" + "=".repeat(50));
    }
    
    private void viewDiagnosisMedicineRelationships() {
        System.out.println("\n=== Diagnosis-Medicine Relationships ===");
        System.out.println("================================================================================================================");
        medicineControl.generateDiagnosisMedicineReport();
    }
    
    private void getMedicinesForDiagnosis() {
        System.out.println("\n=== Get Medicines for Diagnosis ===");
        System.out.println("================================================================================================================");
        
        // Show available diagnoses
        ListInterface<String> diagnoses = medicineControl.getAllDiagnoses();
        if (diagnoses.isEmpty()) {
            System.out.println("| No diagnoses available.                                                                                    |");
            System.out.println("================================================================================================================");
            return;
        }
        
        System.out.println("| Available Diagnoses:                                                                                           |");
        System.out.println("|==============================================================================================================|");
        for (int i = 0; i < diagnoses.size(); i++) {
            System.out.printf("| %-2d. %-60s |\n", (i + 1), diagnoses.get(i));
        }
        System.out.println("|==============================================================================================================|");
        
        System.out.print("Select diagnosis (1-" + diagnoses.size() + "): ");
        int choice = scanner.nextInt(); scanner.nextLine();
        
        if (choice < 1 || choice > diagnoses.size()) {
            System.out.println("Invalid selection!");
            return;
        }
        
        String diagnosis = diagnoses.get(choice - 1);
        ListInterface<Medicine> medicines = medicineControl.getMedicinesForDiagnosis(diagnosis);
        
        if (medicines.isEmpty()) {
            System.out.println("| No medicines found for diagnosis: " + diagnosis + "                                               |");
            System.out.println("================================================================================================================");
        } else {
            System.out.println("\n=== Prescribed Medicines for " + diagnosis + " ===");
            System.out.println("================================================================================================================");
            System.out.println("| #  | Medicine ID | Medicine Name           | Category        | Stock | Expiry Date | Base Price |");
            System.out.println("|==============================================================================================================|");
            
            for (int i = 0; i < medicines.size(); i++) {
                Medicine med = medicines.get(i);
                double basePrice = medicineControl.calculateRevenueFromMedicine(med.getMedicineID(), 1);
                
                System.out.printf("| %-2d | %-11s | %-22s | %-15s | %-5d | %-11s | RM%-8.2f |\n",
                    (i + 1),
                    med.getMedicineID(),
                    med.getName(),
                    med.getCategory(),
                    med.getStock(),
                    med.getExpiryDate(),
                    basePrice);
            }
            System.out.println("================================================================================================================");
            System.out.printf("Total medicines for %s: %d\n", diagnosis, medicines.size());
        }
    }
    

    
    private void manageDiagnosisMedicineRelationships() {
        System.out.println("\n=== Manage Diagnosis-Medicine Relationships ===");
        System.out.println("================================================================================================================");
        System.out.println("1. Add new diagnosis-medicine relationship");
        System.out.println("2. Remove diagnosis-medicine relationship");
        System.out.println("3. View all relationships");
        System.out.print("Enter choice: ");
        int choice = scanner.nextInt(); scanner.nextLine();
        
        switch (choice) {
            case 1 -> {
                System.out.print("Enter diagnosis: ");
                String diagnosis = scanner.nextLine();
                
                // Show all available medicines
                ListInterface<Medicine> allMedicines = medicineControl.getAllMedicines();
                if (allMedicines.isEmpty()) {
                    System.out.println("| No medicines available to add relationships.                                                      |");
                    System.out.println("================================================================================================================");
                    return;
                }
                
                System.out.println("\n=== Available Medicines ===");
                System.out.println("================================================================================================================");
                System.out.println("| #  | Medicine ID | Medicine Name           | Category        | Stock | Expiry Date | Base Price |");
                System.out.println("|==============================================================================================================|");
                
                for (int i = 0; i < allMedicines.size(); i++) {
                    Medicine medicine = allMedicines.get(i);
                    double basePrice = medicineControl.calculateRevenueFromMedicine(medicine.getMedicineID(), 1);
                    
                    System.out.printf("| %-2d | %-11s | %-22s | %-15s | %-5d | %-11s | RM%-8.2f |\n",
                        (i + 1),
                        medicine.getMedicineID(),
                        medicine.getName(),
                        medicine.getCategory(),
                        medicine.getStock(),
                        medicine.getExpiryDate(),
                        basePrice);
                }
                System.out.println("|==============================================================================================================|");
                
                System.out.print("Enter Medicine ID or Name: ");
                String medicineQuery = scanner.nextLine();
                
                // Use linear search to find the medicine
                MyArrayList<LinearSearch.SearchResult<Medicine>> searchResults = 
                    LinearSearch.search(medicineQuery, allMedicines);
                
                if (searchResults.isEmpty()) {
                    System.out.println("| Medicine not found: " + medicineQuery + "                                                           |");
                    System.out.println("================================================================================================================");
                    return;
                }
                
                Medicine selectedMedicine = searchResults.get(0).item;
                
                // Check if medicine exists
                if (selectedMedicine != null) {
                    medicineControl.addDiagnosisMedicineRelationship(diagnosis, selectedMedicine.getMedicineID());
                    System.out.println("| Relationship added: " + diagnosis + " → " + selectedMedicine.getName() + " (" + selectedMedicine.getMedicineID() + ") |");
                    System.out.println("================================================================================================================");
                } else {
                    System.out.println("| Medicine not found!                                                                                    |");
                    System.out.println("================================================================================================================");
                }
            }
            case 2 -> {
                // Show all available diagnoses
                ListInterface<String> allDiagnoses = medicineControl.getAllDiagnoses();
                if (allDiagnoses.isEmpty()) {
                    System.out.println("| No diagnoses available to remove relationships.                                                  |");
                    System.out.println("================================================================================================================");
                    return;
                }
                
                System.out.println("\n=== Available Diagnoses ===");
                System.out.println("================================================================================================================");
                System.out.println("| Available Diagnoses:                                                                                           |");
                System.out.println("|==============================================================================================================|");
                for (int i = 0; i < allDiagnoses.size(); i++) {
                    System.out.printf("| %-2d. %-60s |\n", (i + 1), allDiagnoses.get(i));
                }
                System.out.println("|==============================================================================================================|");
                
                System.out.print("Select diagnosis (1-" + allDiagnoses.size() + "): ");
                int diagnosisChoice = scanner.nextInt(); scanner.nextLine();
                
                if (diagnosisChoice < 1 || diagnosisChoice > allDiagnoses.size()) {
                    System.out.println("Invalid selection!");
                    return;
                }
                
                String diagnosis = allDiagnoses.get(diagnosisChoice - 1);
                
                // Show medicines for the selected diagnosis
                ListInterface<Medicine> diagnosisMedicines = medicineControl.getMedicinesForDiagnosis(diagnosis);
                if (diagnosisMedicines.isEmpty()) {
                    System.out.println("| No medicines found for diagnosis: " + diagnosis + "                                               |");
                    System.out.println("================================================================================================================");
                    return;
                }
                
                System.out.println("\n=== Medicines for " + diagnosis + " ===");
                System.out.println("================================================================================================================");
                System.out.println("| #  | Medicine ID | Medicine Name           | Category        | Stock | Expiry Date | Base Price |");
                System.out.println("|==============================================================================================================|");
                
                for (int i = 0; i < diagnosisMedicines.size(); i++) {
                    Medicine medicine = diagnosisMedicines.get(i);
                    double basePrice = medicineControl.calculateRevenueFromMedicine(medicine.getMedicineID(), 1);
                    
                    System.out.printf("| %-2d | %-11s | %-22s | %-15s | %-5d | %-11s | RM%-8.2f |\n",
                        (i + 1),
                        medicine.getMedicineID(),
                        medicine.getName(),
                        medicine.getCategory(),
                        medicine.getStock(),
                        medicine.getExpiryDate(),
                        basePrice);
                }
                System.out.println("|==============================================================================================================|");
                
                System.out.print("Select medicine to remove (1-" + diagnosisMedicines.size() + "): ");
                int medicineChoice = scanner.nextInt(); scanner.nextLine();
                
                if (medicineChoice < 1 || medicineChoice > diagnosisMedicines.size()) {
                    System.out.println("Invalid selection!");
                    return;
                }
                
                Medicine selectedMedicine = diagnosisMedicines.get(medicineChoice - 1);
                
                System.out.print("Are you sure you want to remove this relationship? (y/n): ");
                String confirm = scanner.nextLine();
                
                if (confirm.equalsIgnoreCase("y") || confirm.equalsIgnoreCase("yes")) {
                    medicineControl.removeDiagnosisMedicineRelationship(diagnosis, selectedMedicine.getMedicineID());
                    System.out.println("| Relationship removed: " + diagnosis + " → " + selectedMedicine.getName() + " (" + selectedMedicine.getMedicineID() + ") |");
                    System.out.println("================================================================================================================");
                } else {
                    System.out.println("| Relationship removal cancelled.                                                                              |");
                    System.out.println("================================================================================================================");
                }
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
    
    private void processPayment() {
        System.out.println("\n=== PROCESS PAYMENT ===");
        System.out.println("================================================================================================================");
        
        // Get all treatments that are COMPLETED but haven't been processed for payment
        ListInterface<MedicalTreatment> treatments = treatmentControl.getAllTreatments();
        if (treatments.isEmpty()) {
            System.out.println("| No treatments found for payment processing.                                                           |");
            System.out.println("================================================================================================================");
            return;
        }
        
        // Filter for COMPLETED treatments
        MyArrayList<MedicalTreatment> completedTreatments = new MyArrayList<>();
        for (int i = 0; i < treatments.size(); i++) {
            MedicalTreatment treatment = treatments.get(i);
            if ("COMPLETED".equals(treatment.getStatus())) {
                completedTreatments.add(treatment);
            }
        }
        
        if (completedTreatments.isEmpty()) {
            System.out.println("| No completed treatments found for payment processing.                                                |");
            System.out.println("================================================================================================================");
            return;
        }
        
        System.out.println("| PENDING PAYMENT TREATMENTS                                                                                   |");
        System.out.println("|==============================================================================================================|");
        System.out.println("| #  | Treatment ID | Patient ID (Name)        | Doctor ID (Name)         | Diagnosis | Cost    |");
        System.out.println("|==============================================================================================================|");
        
        for (int i = 0; i < completedTreatments.size(); i++) {
            MedicalTreatment treatment = completedTreatments.get(i);
            String patientInfo = String.format("%s (%s)", treatment.getPatientId(), getPatientNameFromId(treatment.getPatientId()));
            String doctorInfo = String.format("%s (%s)", treatment.getDoctorId(), getDoctorNameFromId(treatment.getDoctorId()));
            
            System.out.printf("| %-2d | %-12s | %-23s | %-24s | %-9s | RM%-6s |\n",
                (i + 1),
                treatment.getTreatmentId(),
                patientInfo,
                doctorInfo,
                treatment.getDiagnosis(),
                String.format("%.2f", treatment.getCost()));
        }
        System.out.println("|==============================================================================================================|");
        
        System.out.print("Select treatment for payment processing (1-" + completedTreatments.size() + "): ");
        int choice = scanner.nextInt(); scanner.nextLine();
        
        if (choice < 1 || choice > completedTreatments.size()) {
            System.out.println("Invalid selection!");
            return;
        }
        
        MedicalTreatment selectedTreatment = completedTreatments.get(choice - 1);
        String treatmentId = selectedTreatment.getTreatmentId();
        String diagnosis = selectedTreatment.getDiagnosis();
        double treatmentCost = selectedTreatment.getCost();
        
        System.out.println("\n=== PAYMENT PROCESSING ===");
        System.out.println("================================================================================================================");
        System.out.printf("| Treatment ID: %-15s | Patient: %-20s |\n", treatmentId, getPatientNameFromId(selectedTreatment.getPatientId()));
        System.out.printf("| Diagnosis: %-15s | Cost: RM%-10.2f |\n", diagnosis, treatmentCost);
        System.out.println("|==============================================================================================================|");
        
        // Process medicine dispensing and payment
        double totalMedicineCost = processMedicineDispensing(diagnosis, treatmentId);
        double totalPayment = treatmentCost + totalMedicineCost;
        
        System.out.println("|==============================================================================================================|");
        System.out.printf("| Treatment Cost: RM%-10.2f |\n", treatmentCost);
        System.out.printf("| Medicine Cost: RM%-10.2f |\n", totalMedicineCost);
        System.out.printf("| TOTAL PAYMENT: RM%-10.2f |\n", totalPayment);
        System.out.println("|==============================================================================================================|");
        System.out.println("| COST BREAKDOWN: Treatment + Medicine = Total Payment |");
        System.out.printf("| RM%-8.2f + RM%-8.2f = RM%-8.2f |\n", treatmentCost, totalMedicineCost, totalPayment);
        System.out.println("|==============================================================================================================|");
        
        System.out.print("Confirm payment processing? (y/n): ");
        String confirm = scanner.nextLine();
        
        if (confirm.equalsIgnoreCase("y") || confirm.equalsIgnoreCase("yes")) {
            // Update treatment status to PAID
            boolean statusUpdated = treatmentControl.updateTreatmentStatus(treatmentId, "PAID");
            
            if (statusUpdated) {
                System.out.println("| Payment processed successfully!                                                                    |");
                System.out.println("| Revenue recorded: RM" + String.format("%.2f", totalPayment) + "                                                      |");
                System.out.println("| Treatment status updated to PAID                                                                   |");
            } else {
                System.out.println("| Failed to update treatment status.                                                               |");
            }
        } else {
            System.out.println("| Payment processing cancelled.                                                                        |");
        }
        System.out.println("================================================================================================================");
    }
    
    private double processMedicineDispensing(String diagnosis, String treatmentId) {
        System.out.println("| MEDICINE DISPENSING PROCESS                                                                                 |");
        System.out.println("|==============================================================================================================|");
        
        // Get medicines for this diagnosis
        ListInterface<Medicine> medicines = medicineControl.getMedicinesForDiagnosis(diagnosis);
        
        if (medicines.isEmpty()) {
            System.out.println("| No specific medicines found for " + diagnosis + "                                                      |");
            return 0.0;
        }
        
        double totalMedicineCost = 0.0;
        MyArrayList<String> deductedMedicines = new MyArrayList<>();
        
        for (int i = 0; i < medicines.size(); i++) {
            Medicine medicine = medicines.get(i);
            int quantity = calculateQuantityForMedicine(medicine, diagnosis);
            
            // Check if we have enough stock
            if (medicine.getStock() >= quantity) {
                // Deduct from inventory
                boolean success = medicineControl.dispenseMedicine(medicine.getMedicineID(), quantity);
                
                if (success) {
                    double medicineValue = medicineControl.calculateRevenueFromMedicine(medicine.getMedicineID(), quantity);
                    totalMedicineCost += medicineValue;
                    deductedMedicines.add(medicine.getName() + " (" + quantity + " units)");
                    
                    System.out.printf("| %-20s | %-3d units | Stock: %-3d | RM%-8.2f |\n",
                        medicine.getName(), quantity, medicine.getStock(), medicineValue);
                } else {
                    System.out.printf("| %-20s | FAILED TO DISPENSE |\n", medicine.getName());
                }
            } else {
                System.out.printf("| %-20s | INSUFFICIENT STOCK | Required: %-3d | Available: %-3d |\n",
                    medicine.getName(), quantity, medicine.getStock());
            }
        }
        
        System.out.println("|==============================================================================================================|");
        System.out.printf("| Total medicine cost: RM%-10.2f |\n", totalMedicineCost);
        
        // Display deducted medicines summary
        if (!deductedMedicines.isEmpty()) {
            System.out.println("| MEDICINES DEDUCTED FROM INVENTORY:                                                                        |");
            System.out.println("|==============================================================================================================|");
            for (int i = 0; i < deductedMedicines.size(); i++) {
                System.out.printf("| %-2d. %-60s |\n", (i + 1), deductedMedicines.get(i));
            }
            System.out.println("|==============================================================================================================|");
        }
        
        return totalMedicineCost;
    }
    
    // Calculate quantity for medicine (same logic as MedicalTreatmentUI)
    private int calculateQuantityForMedicine(Medicine medicine, String diagnosis) {
        String category = medicine.getCategory();
        return switch (category.toLowerCase()) {
            case "painkiller" -> 20;
            case "cold medicine" -> 2;
            case "digestive" -> 30;
            case "topical" -> 1;
            case "neurological" -> 10;
            case "vitamin" -> 30;
            case "antibiotic" -> 14;
            case "musculoskeletal" -> 15;
            default -> 20;
        };
    }
    
    // Helper method to get patient name from ID using consultation data
    private String getPatientNameFromId(String patientId) {
        // Try to get patient name from consultation data first
        ListInterface<Consultation> allConsultations = consultationControl.getAllConsultations();
        for (int i = 0; i < allConsultations.size(); i++) {
            Consultation consultation = allConsultations.get(i);
            if (consultation.getPatientId().equals(patientId)) {
                return consultation.getPatientName();
            }
        }
        
        // If not found in consultations, return a generic name
        return "Patient " + patientId;
    }

    // Helper method to get doctor name from ID
    private String getDoctorNameFromId(String doctorId) {
        return switch (doctorId) {
            case "D001" -> "Dr. Smith";
            case "D002" -> "Dr. Johnson";
            default -> "Dr. Unknown";
        };
    }
}