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

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int choice;

        do {
            System.out.println("\n" + "=".repeat(50));
            System.out.println("    CLINIC MANAGEMENT SYSTEM");
            System.out.println("=".repeat(50));
            System.out.println("1. Consultation Management Module");
            System.out.println("2. Medical Treatment Management Module");
            System.out.println("3. Pharmacy Management Module");
            System.out.println("4. 📊 System Overview & Analytics");
            System.out.println("0. Exit System");
            System.out.println("=".repeat(50));
            System.out.print("Select Module: ");
            choice = scanner.nextInt(); scanner.nextLine();

            switch (choice) {
                case 1 -> {
                    System.out.println("\n👨‍⚕️ Starting Consultation Management Module...");
                    new ConsultationUI(sharedConsultationControl, sharedTreatmentControl).run();
                }
                case 2 -> {
                    System.out.println("\n🩺 Starting Medical Treatment Management Module...");
                    new MedicalTreatmentUI(sharedTreatmentControl, sharedConsultationControl).run();
                }
                case 3 -> {
                    System.out.println("\n💊 Starting Pharmacy Management Module...");
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
        System.out.println("\n📊 CLINIC SYSTEM OVERVIEW");
        System.out.println("=".repeat(60));

        System.out.println("🔧 SYSTEM ARCHITECTURE:");
        System.out.println("   • Universal ADT (MyArrayList<T>) used across all modules");
        System.out.println("   • Same data structure handles different entity types:");
        System.out.println("     - Medicine objects (Pharmacy Module)");
        System.out.println("     - Patient objects (Consultation Module)");
        System.out.println("     - Treatment objects (Medical Treatment Module)");
        System.out.println("   • Shared data persistence across modules");

        System.out.println("\n📈 MODULE STATUS:");
        System.out.println("   ✅ Pharmacy Module: Active (Medicine Management)");
        System.out.println("   ✅ Consultation Module: Active (Patient Management)");
        System.out.println("   ✅ Medical Treatment Module: Active (Treatment Management)");

        // Show current data statistics
        System.out.println("\n📊 CURRENT DATA STATISTICS:");
        System.out.println("   Consultations: " + sharedConsultationControl.getAllConsultations().size() + " records");
        System.out.println("   Treatments: " + sharedTreatmentControl.getAllTreatments().size() + " records");
        System.out.println("   Medicines: " + sharedMedicineControl.getAllMedicines().size() + " records");

        System.out.println("\n🎯 ADT CAPABILITIES DEMONSTRATED:");
        System.out.println("   • Smart Filtering: Find medicines by category, stock level");
        System.out.println("   • Frequency Tracking: Most popular medicine categories");
        System.out.println("   • Random Access: Staff training spotlights");
        System.out.println("   • Custom Sorting: Stock levels, expiry dates");
        System.out.println("   • Statistical Analysis: Inventory distribution");
        System.out.println("   • Multi-level Queue: Emergency, Walk-in, Scheduled");
        System.out.println("   • Treatment History: Patient treatment tracking");
        System.out.println("   • Financial Analytics: Revenue tracking and projections");

        System.out.println("\n💡 BUSINESS VALUE:");
        System.out.println("   • Improved inventory management");
        System.out.println("   • Better staff training opportunities");
        System.out.println("   • Proactive expiry date monitoring");
        System.out.println("   • Data-driven decision making");
        System.out.println("   • Efficient patient flow management");
        System.out.println("   • Comprehensive treatment tracking");
        System.out.println("   • Integrated pharmacy workflow");

        System.out.println("\n🔄 WORKFLOW INTEGRATION:");
        System.out.println("   Consultation → Diagnosis → Treatment → Pharmacy Dispensing");

        System.out.println("\n" + "=".repeat(60));
    }
}
