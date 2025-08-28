/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.clinicmanagementsystem;
import ADT.MyArrayList;
import Entity.Medicine;
import Boundary.ConsultationUI;
import Boundary.PharmacyUI;
import Boundary.MedicalTreatmentUI;
import Control.ConsultationManagement;
import Control.MedicalTreatmentManagement;
import Control.MedicineMaintenance;
import java.util.Scanner;
/**
 *
 * @author yapji
 */
public class ClinicManagementSystem {
    // Shared control instances that persist across modules
    private static final ConsultationManagement sharedConsultationControl = new ConsultationManagement();
    private static final MedicalTreatmentManagement sharedTreatmentControl = new MedicalTreatmentManagement();
    private static final MedicineMaintenance sharedMedicineControl = new MedicineMaintenance(new MyArrayList<>());
    
    // Initialize medicines immediately so they're available to all modules
    static {
        initializeMedicines();
    }
    
    private static void initializeMedicines() {
        System.out.println("Initializing Pharmacy Inventory...");
        
        // Add all medicines needed for diagnoses
        sharedMedicineControl.addMedicine(new Medicine("M001", "Paracetamol 500mg", "Painkiller", 150, "2025-12-31"));
        sharedMedicineControl.addMedicine(new Medicine("M002", "Ibuprofen 400mg", "Painkiller", 120, "2025-10-31"));
        sharedMedicineControl.addMedicine(new Medicine("M003", "Decongestant Syrup", "Cold Medicine", 80, "2025-08-31"));
        sharedMedicineControl.addMedicine(new Medicine("M004", "Cough Syrup", "Cold Medicine", 90, "2025-09-30"));
        sharedMedicineControl.addMedicine(new Medicine("M005", "Antacid Tablets", "Digestive", 100, "2025-11-30"));
        sharedMedicineControl.addMedicine(new Medicine("M006", "Omeprazole 20mg", "Digestive", 60, "2025-07-31"));
        sharedMedicineControl.addMedicine(new Medicine("M007", "Pain Relief Cream", "Topical", 75, "2025-06-30"));
        sharedMedicineControl.addMedicine(new Medicine("M008", "Anti-vertigo Medication", "Neurological", 40, "2025-05-31"));
        sharedMedicineControl.addMedicine(new Medicine("M009", "Anti-nausea Tablets", "Digestive", 55, "2025-08-31"));
        sharedMedicineControl.addMedicine(new Medicine("M010", "Multivitamin Supplements", "Vitamin", 200, "2025-12-31"));
        sharedMedicineControl.addMedicine(new Medicine("M011", "Throat Lozenges", "Cold Medicine", 120, "2025-09-30"));
        sharedMedicineControl.addMedicine(new Medicine("M012", "Amoxicillin 500mg", "Antibiotic", 70, "2025-07-31"));
        sharedMedicineControl.addMedicine(new Medicine("M013", "Muscle Relaxant", "Musculoskeletal", 45, "2025-06-30"));
        
        // Initialize diagnosis-medicine relationships
        sharedMedicineControl.initializeDiagnosisMedicineRelationships();
        
        System.out.println("Pharmacy inventory initialized with " + sharedMedicineControl.getAllMedicines().size() + " medicines");
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int choice;

        do {
            System.out.println("\n" + "=".repeat(50));
            System.out.println("              CLINIC MANAGEMENT SYSTEM");
            System.out.println("=".repeat(50));
            System.out.println("1. Consultation Management Module");
            System.out.println("2. Medical Treatment Management Module");
            System.out.println("3. Pharmacy Management Module");
            System.out.println("4. System Overview & Analytics");
            System.out.println("0. Exit System");
            System.out.println("=".repeat(50));
            System.out.print("Select Module: ");
            choice = scanner.nextInt(); scanner.nextLine();

            switch (choice) {
                case 1 -> {
                    System.out.println("\nStarting Consultation Management Module...");
                    new ConsultationUI(sharedConsultationControl).run();
                }
                case 2 -> {
                    System.out.println("\nStarting Medical Treatment Management Module...");
                    new MedicalTreatmentUI(sharedTreatmentControl, sharedConsultationControl, sharedMedicineControl).run();
                }
                case 3 -> {
                    System.out.println("\nStarting Pharmacy Management Module...");
                    new PharmacyUI(sharedMedicineControl).run();
                }
                case 4 -> showSystemOverview();
                case 0 -> System.out.println("Thank you for using Clinic Management System!");
                default -> System.out.println("Invalid choice. Please try again.");
            }
        } while (choice != 0);

        scanner.close();
    }

    private static void showSystemOverview() {
       

        // Show current data statistics
        System.out.println("\nCURRENT DATA STATISTICS:");
        System.out.println("   Consultations: " + sharedConsultationControl.getAllConsultations().size() + " records");
        System.out.println("   Treatments: " + sharedTreatmentControl.getAllTreatments().size() + " records");
        System.out.println("   Medicines: " + sharedMedicineControl.getAllMedicines().size() + " records");



        System.out.println("\n" + "=".repeat(60));
    }
}
