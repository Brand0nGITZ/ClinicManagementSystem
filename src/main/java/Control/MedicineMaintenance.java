/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Control;
import Entity.Medicine;
import ADT.MyArrayList;
import ADT.ListInterface;
/**
 *
 * @author yapjinkai
 */
public class MedicineMaintenance {
    private final MyArrayList<Medicine> medicineList;
    private final MyArrayList<MyArrayList.KeyValuePair<String, MyArrayList<String>>> diagnosisMedicineMap; 

    public MedicineMaintenance(MyArrayList<Medicine> medicineList) {
        this.medicineList = medicineList;
        this.diagnosisMedicineMap = new MyArrayList<>();
        this.nextMedicineId = 14;
    }
    
    // Auto-generate medicine ID
    private int nextMedicineId = 14;
    public String generateMedicineId() {
        return String.format("M%03d", nextMedicineId++);
    }

    public void addMedicine(Medicine med) {
        medicineList.add(med);
    }

    public boolean removeMedicine(String id) {
        for (int i = 0; i < medicineList.size(); i++) {
            Medicine med = medicineList.get(i);
            if (med.getMedicineID().equalsIgnoreCase(id)) {
                return medicineList.remove(med);
            }
        }
        return false;
    }

    public boolean dispenseMedicine(String id, int quantity) {
        for (int i = 0; i < medicineList.size(); i++) {
            Medicine med = medicineList.get(i);
            if (med.getMedicineID().equalsIgnoreCase(id)) {
                if (med.getStock() >= quantity) {
                    med.reduceStock(quantity);
                    return true;
                }
                return false;
            }
        }
        return false;
    }

    public Medicine findByName(String name) {
        return medicineList.findFirst(med -> med.getName().equalsIgnoreCase(name));
    }

    public void sortMedicinesByStock() {
        medicineList.sort((med1, med2) -> Integer.compare(med2.getStock(), med1.getStock()));
    }

    public void generateLowStockReport(int threshold) {
        System.out.println("=== Low Stock Report ===");
        boolean found = false;
        for (int i = 0; i < medicineList.size(); i++) {
            Medicine med = medicineList.get(i);
            if (med.getStock() <= threshold) {
                System.out.println(med);
                found = true;
            }
        }
        if (!found) {
            System.out.println("All medicine stock is above threshold.");
        }
    }

    public MyArrayList<Medicine> getAllMedicines() {
        return medicineList;
    }
    
    // Revenue tracking methods
    public double calculateRevenueFromMedicine(String medicineId, int quantity) {
        Medicine medicine = findById(medicineId);
        if (medicine != null) {
            // Calculate revenue based on medicine type and quantity
            double basePrice = getBasePriceForMedicine(medicine);
            return basePrice * quantity;
        }
        return 0.0;
    }
    
    private double getBasePriceForMedicine(Medicine medicine) {
        // Base prices for different medicine categories (in RM)
        String category = medicine.getCategory();
        return switch (category.toLowerCase()) {
            case "painkiller" -> 8.50;
            case "cold medicine" -> 12.00;
            case "digestive" -> 15.00;
            case "topical" -> 10.00;
            case "neurological" -> 25.00;
            case "vitamin" -> 18.00;
            case "antibiotic" -> 22.00;
            case "musculoskeletal" -> 28.00;
            default -> 12.00; // General medication
        };
    }
    
    // Get available diagnosis categories for medicine selection
    public ListInterface<String> getAvailableDiagnoses() {
        return getAllDiagnoses();
    }
    
    // Suggest prescription with quantity and revenue
    public String suggestPrescriptionWithQuantity(String diagnosis) {
        ListInterface<Medicine> medicines = getMedicinesForDiagnosis(diagnosis);
        if (medicines.isEmpty()) {
            return "General medication - Follow doctor's instructions";
        }
        
        StringBuilder prescription = new StringBuilder();
        double totalRevenue = 0.0;
        
        for (int i = 0; i < medicines.size(); i++) {
            Medicine medicine = medicines.get(i);
            int quantity = calculateQuantityForMedicine(medicine, diagnosis);
            double revenue = calculateRevenueFromMedicine(medicine.getMedicineID(), quantity);
            totalRevenue += revenue;
            
            prescription.append(medicine.getName()).append(" - ");
            prescription.append(quantity).append(" units");
            
            if (i < medicines.size() - 1) {
                prescription.append(", ");
            }
        }
        
                    prescription.append(" | Total Revenue: RM").append(String.format("%.2f", totalRevenue));
        return prescription.toString();
    }
    
    private int calculateQuantityForMedicine(Medicine medicine, String diagnosis) {
        // Calculate quantity based on medicine type and diagnosis
        String category = medicine.getCategory();
        return switch (category.toLowerCase()) {
            case "painkiller" -> 20; // 20 tablets for painkillers
            case "cold medicine" -> 2; // 2 bottles for cold medicine
            case "digestive" -> 30; // 30 tablets for digestive
            case "topical" -> 1; // 1 tube/container for topical
            case "neurological" -> 10; // 10 tablets for neurological
            case "vitamin" -> 30; // 30 tablets for vitamins
            case "antibiotic" -> 14; // 14 tablets for antibiotics
            case "musculoskeletal" -> 15; // 15 tablets for musculoskeletal
            default -> 20; // Default quantity for general medication
        };
    }
    
    // Initialize diagnosis-medicine relationships
    public void initializeDiagnosisMedicineRelationships() {
        // Fever - Paracetamol, Ibuprofen
        addDiagnosisMedicine("Fever", "M001");
        addDiagnosisMedicine("Fever", "M002");
        
        // Common Cold - Decongestant, Cough Syrup
        addDiagnosisMedicine("Common Cold", "M003");
        addDiagnosisMedicine("Common Cold", "M004");
        
        // Headache - Ibuprofen, Paracetamol
        addDiagnosisMedicine("Headache", "M002");
        addDiagnosisMedicine("Headache", "M001");
        
        // Gastritis - Antacid, Omeprazole
        addDiagnosisMedicine("Gastritis", "M005");
        addDiagnosisMedicine("Gastritis", "M006");
        
        // Pain Management - Ibuprofen, Pain Relief Cream
        addDiagnosisMedicine("Pain Management", "M002");
        addDiagnosisMedicine("Pain Management", "M007");
        
        // Dizziness - Anti-vertigo medication
        addDiagnosisMedicine("Dizziness", "M008");
        
        // Nausea - Anti-nausea tablets
        addDiagnosisMedicine("Nausea", "M009");
        
        // Fatigue - Multivitamin supplements
        addDiagnosisMedicine("Fatigue", "M010");
        
        // Sore Throat - Throat lozenges, Antibiotics
        addDiagnosisMedicine("Sore Throat", "M011");
        addDiagnosisMedicine("Sore Throat", "M012");
        
        // Back Pain - Muscle relaxant, Pain Relief Cream
        addDiagnosisMedicine("Back Pain", "M013");
        addDiagnosisMedicine("Back Pain", "M007");
    }
    
    // Add medicine to diagnosis relationship using ADT HashMap functionality
    private void addDiagnosisMedicine(String diagnosis, String medicineId) {
        MyArrayList<String> medicineIds = diagnosisMedicineMap.get(diagnosis);
        if (medicineIds == null) {
            medicineIds = new MyArrayList<>();
            diagnosisMedicineMap.put(diagnosis, medicineIds);
        }
        medicineIds.add(medicineId);
    }
    
    // Get medicines for a specific diagnosis
    public ListInterface<Medicine> getMedicinesForDiagnosis(String diagnosis) {
        ListInterface<Medicine> medicines = new MyArrayList<>();
        MyArrayList<String> medicineIds = diagnosisMedicineMap.get(diagnosis);
        
        if (medicineIds != null) {
            for (int i = 0; i < medicineIds.size(); i++) {
                String medicineId = medicineIds.get(i);
                Medicine medicine = findById(medicineId);
                if (medicine != null) {
                    medicines.add(medicine);
                }
            }
        }
        
        return medicines;
    }
    
    // Find medicine by ID
    public Medicine findById(String medicineId) {
        return medicineList.findFirst(med -> med.getMedicineID().equalsIgnoreCase(medicineId));
    }
    
    // Get all diagnoses that have medicine relationships
    public ListInterface<String> getAllDiagnoses() {
        return diagnosisMedicineMap.keySet();
    }
    
    // Add new diagnosis-medicine relationship
    public void addDiagnosisMedicineRelationship(String diagnosis, String medicineId) {
        addDiagnosisMedicine(diagnosis, medicineId);
    }
    
    // Remove diagnosis-medicine relationship
    public void removeDiagnosisMedicineRelationship(String diagnosis, String medicineId) {
        MyArrayList<String> medicineIds = diagnosisMedicineMap.get(diagnosis);
        if (medicineIds != null) {
            medicineIds.remove(medicineId);
            if (medicineIds.isEmpty()) {
                diagnosisMedicineMap.removeByKey(diagnosis);
            }
        }
    }
    
    // Get diagnosis-medicine relationship report
    public void generateDiagnosisMedicineReport() {
        System.out.println("=== Diagnosis-Medicine Relationship Report ===");
        MyArrayList<MyArrayList.KeyValuePair<String, MyArrayList<String>>> entries = diagnosisMedicineMap.entrySet();
        
        for (int i = 0; i < entries.size(); i++) {
            MyArrayList.KeyValuePair<String, MyArrayList<String>> entry = entries.get(i);
            String diagnosis = entry.getKey();
            MyArrayList<String> medicineIds = entry.getValue();
            
            System.out.println("\nDiagnosis: " + diagnosis);
            System.out.println("Prescribed Medicines:");
            
            for (int j = 0; j < medicineIds.size(); j++) {
                String medicineId = medicineIds.get(j);
                Medicine medicine = findById(medicineId);
                if (medicine != null) {
                    System.out.println("  - " + medicine.getName() + " (ID: " + medicineId + ") - Stock: " + medicine.getStock());
                }
            }
        }
    }
}