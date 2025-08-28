/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Boundary;

import Control.MedicalTreatmentManagement;
import Control.ConsultationManagement;
import Control.MedicineMaintenance;
import Entity.MedicalTreatment;
import Entity.Consultation;
import Entity.Medicine;
import ADT.ListInterface;
import ADT.MyArrayList;
import java.util.Scanner;

/**
 *
 * @author yapji
 */
public class MedicalTreatmentUI {
    private final Scanner scanner = new Scanner(System.in);
    private final MedicalTreatmentManagement treatmentControl;
    private final ConsultationManagement consultationControl;
    private final MedicineMaintenance medicineControl;

    public MedicalTreatmentUI(MedicalTreatmentManagement treatmentControl, ConsultationManagement consultationControl, MedicineMaintenance medicineControl) {
        this.treatmentControl = treatmentControl;
        this.consultationControl = consultationControl;
        this.medicineControl = medicineControl;
    }

    public void run() {
        int choice;
        do {
            System.out.println("\n=== Medical Treatment Management ===");
            System.out.println("1. Add New Treatment");
            System.out.println("2. View Treatment History by Patient");
            System.out.println("3. View All Treatments");
            System.out.println("4. Search by Diagnosis");
            System.out.println("5. Search by Doctor");
            System.out.println("6. Update Treatment Status");
            System.out.println("7. Generate Reports");
            System.out.println("0. Exit");
            System.out.print("Enter choice: ");
            choice = scanner.nextInt(); scanner.nextLine();

            switch (choice) {
                case 1 -> addNewTreatment();
                case 2 -> viewTreatmentHistoryByPatient();
                case 3 -> viewAllTreatments();
                case 4 -> searchByDiagnosis();
                case 5 -> searchByDoctor();
                case 6 -> updateTreatmentStatus();
                case 7 -> generateReports();
                case 0 -> System.out.println("Exiting...");
                default -> System.out.println("Invalid choice.");
            }
        } while (choice != 0);
    }

    private void addNewTreatment() {
        System.out.println("\n=== Add New Treatment (Automatic Priority) ===");
        
        // Get the highest priority patient automatically
        Consultation nextPatient = consultationControl.getNextPatient();
        
        if (nextPatient == null) {
            System.out.println("No patients waiting for treatment.");
            return;
        }
        
        // Check if treatment already exists for this consultation
        if (treatmentExistsForConsultation(nextPatient.getConsultationId())) {
            System.out.println("Treatment already exists for consultation " + nextPatient.getConsultationId());
            return;
        }
        
        // Auto-fill data from consultation
        String consultationId = nextPatient.getConsultationId();
        String patientId = nextPatient.getPatientId();
        String doctorId = nextPatient.getDoctorId();
        String diagnosis = nextPatient.getDiagnosis();
        
        System.out.println("\nAUTOMATIC PATIENT SELECTION:");
        System.out.println("   Priority: " + nextPatient.getQueueType());
        System.out.println("   Patient: " + nextPatient.getPatientName());
        System.out.println("   Doctor: " + nextPatient.getDoctorName());
        System.out.println("   Time Slot: " + nextPatient.getAppointmentTime());
        System.out.println("   Diagnosis: " + diagnosis);
        
        // Suggest prescription based on diagnosis
        String suggestedPrescription = suggestPrescription(diagnosis);
        System.out.println("\nSuggested Prescription for " + diagnosis + ":");
        System.out.println("   " + suggestedPrescription);
        
        System.out.print("Enter Prescription Details (or press Enter to use suggestion): ");
        String prescription = scanner.nextLine();
        
        if (prescription.trim().isEmpty()) {
            prescription = suggestedPrescription;
            System.out.println("Using suggested prescription: " + prescription);
        }
        
        // Auto-calculate cost based on diagnosis
        double suggestedCost = calculateSuggestedCost(diagnosis);
        System.out.println("\nSuggested Cost: RM" + String.format("%.2f", suggestedCost));
        
        System.out.print("Enter Treatment Cost (or press Enter to use suggestion): RM");
        String costInput = scanner.nextLine();
        double cost;
        
        if (costInput.trim().isEmpty()) {
            cost = suggestedCost;
            System.out.println("Using suggested cost: RM" + String.format("%.2f", cost));
        } else {
            try {
                cost = Double.parseDouble(costInput);
            } catch (NumberFormatException e) {
                System.out.println("Invalid cost format. Using suggested cost.");
                cost = suggestedCost;
            }
        }
        
        // Auto-generate treatment date (today)
        String treatmentDate = java.time.LocalDate.now().format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        System.out.println("Treatment Date: " + treatmentDate);
        
        // Auto-generate treatment ID
        String treatmentId = treatmentControl.generateTreatmentId();
        
        // Create and add treatment
        MedicalTreatment treatment = new MedicalTreatment(treatmentId, consultationId, patientId, doctorId,
                                                        diagnosis, prescription, treatmentDate, cost);
        treatmentControl.addTreatment(treatment);
        
        // Automatically change treatment status to COMPLETED for revenue tracking
        boolean treatmentStatusUpdated = treatmentControl.updateTreatmentStatus(treatmentId, "COMPLETED");
        
        // Automatically change consultation status to COMPLETED
        boolean consultationStatusUpdated = consultationControl.updateConsultationStatus(consultationId, "COMPLETED");
        
        System.out.println("\nTreatment added successfully!");
        System.out.println("   Treatment ID: " + treatmentId);
        System.out.println("   Prescription ready for pharmacy dispensing");
        System.out.println("   Cost: RM" + String.format("%.2f", cost));
        
        if (treatmentStatusUpdated) {
            System.out.println("   Treatment status automatically changed to COMPLETED");
            System.out.println("   Revenue immediately recorded: RM" + String.format("%.2f", cost));
        }
        
        if (consultationStatusUpdated) {
            System.out.println("   Consultation status automatically changed to COMPLETED");
            System.out.println("   Doctor " + nextPatient.getDoctorName() + " is now available for new patients");
            
            // Show next patient in queue
            Consultation nextInQueue = consultationControl.getNextPatient();
            if (nextInQueue != null) {
                System.out.println("   Next patient: " + nextInQueue.getPatientName() + " (" + nextInQueue.getQueueType() + ")");
            }
        }
        
        // Automatically deduct prescribed medicines from inventory
        deductPrescribedMedicines(diagnosis);
    }
    
    // Deduct prescribed medicines from inventory
    private void deductPrescribedMedicines(String diagnosis) {
        System.out.println("\nAUTOMATIC MEDICINE DISPENSING:");
        System.out.println("-".repeat(40));
        
        // Get medicines for this diagnosis
        ListInterface<Medicine> medicines = medicineControl.getMedicinesForDiagnosis(diagnosis);
        
        if (medicines.isEmpty()) {
            System.out.println("   No specific medicines found for " + diagnosis);
            return;
        }
        
        double totalDispensedValue = 0.0;
        
        for (int i = 0; i < medicines.size(); i++) {
            Medicine medicine = medicines.get(i);
            int quantity = calculateQuantityForMedicine(medicine, diagnosis);
            
            // Check if we have enough stock
            if (medicine.getStock() >= quantity) {
                // Deduct from inventory
                boolean success = medicineControl.dispenseMedicine(medicine.getMedicineID(), quantity);
                
                if (success) {
                    double medicineValue = medicineControl.calculateRevenueFromMedicine(medicine.getMedicineID(), quantity);
                    totalDispensedValue += medicineValue;
                    
                    System.out.println("   " + medicine.getName() + " - " + quantity + " units dispensed");
                    System.out.println("      Stock remaining: " + medicine.getStock() + " units");
                    System.out.println("      Value: RM" + String.format("%.2f", medicineValue));
                } else {
                    System.out.println("   Failed to dispense " + medicine.getName());
                }
            } else {
                System.out.println("   Insufficient stock for " + medicine.getName());
                System.out.println("      Required: " + quantity + " units, Available: " + medicine.getStock() + " units");
            }
        }
        
        System.out.println("   Total medicine value dispensed: RM" + String.format("%.2f", totalDispensedValue));
        System.out.println("   Inventory updated for " + diagnosis + " treatment");
    }
    
    // Calculate quantity for medicine (same logic as MedicineMaintenance)
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



    // Check if treatment already exists for consultation
    private boolean treatmentExistsForConsultation(String consultationId) {
        ListInterface<MedicalTreatment> allTreatments = treatmentControl.getAllTreatments();
        for (int i = 0; i < allTreatments.size(); i++) {
            MedicalTreatment treatment = allTreatments.get(i);
            if (treatment.getConsultationId().equals(consultationId)) {
                return true;
            }
        }
        return false;
    }

    // Suggest prescription based on diagnosis with quantity and revenue
    private String suggestPrescription(String diagnosis) {
        // Use the pharmacy control to get prescription with quantity and revenue
        return medicineControl.suggestPrescriptionWithQuantity(diagnosis);
    }

    // Calculate suggested cost based on diagnosis (in RM)
    private double calculateSuggestedCost(String diagnosis) {
        return switch (diagnosis.toLowerCase()) {
            case "fever" -> 15.00;
            case "common cold" -> 18.00;
            case "headache" -> 12.00;
            case "gastritis" -> 20.00;
            case "pain management" -> 25.00;
            case "dizziness" -> 30.00;
            case "nausea" -> 15.00;
            case "fatigue" -> 35.00;
            case "sore throat" -> 10.00;
            case "back pain" -> 40.00;
            default -> 18.00;
        };
    }

    private void viewTreatmentHistoryByPatient() {
        System.out.print("Enter Patient ID: ");
        String patientId = scanner.nextLine();
        
        ListInterface<MedicalTreatment> patientHistory = treatmentControl.getTreatmentHistoryByPatient(patientId);
        if (patientHistory.isEmpty()) {
            System.out.println("No treatment history found for this patient.");
            return;
        }
        
        System.out.println("\n=== Treatment History for Patient " + patientId + " ===");
        for (int i = 0; i < patientHistory.size(); i++) {
            MedicalTreatment treatment = patientHistory.get(i);
            System.out.println("\nTreatment " + (i + 1) + ":");
            System.out.println("  ID: " + treatment.getTreatmentId());
            System.out.println("  Consultation: " + treatment.getConsultationId());
            System.out.println("  Date: " + treatment.getTreatmentDate());
            System.out.println("  Diagnosis: " + treatment.getDiagnosis());
            System.out.println("  Status: " + treatment.getStatus());
            System.out.println("  Cost: RM" + String.format("%.2f", treatment.getCost()));
            System.out.println("  Prescription: " + treatment.getPrescription());
        }
    }

    private void viewAllTreatments() {
        ListInterface<MedicalTreatment> treatments = treatmentControl.getAllTreatments();
        if (treatments.isEmpty()) {
            System.out.println("No treatments found.");
            return;
        }
        
        System.out.println("\n=== All Treatments ===");
        for (int i = 0; i < treatments.size(); i++) {
            MedicalTreatment treatment = treatments.get(i);
            System.out.println(treatment);
        }
    }

    private void searchByDiagnosis() {
        System.out.print("Enter diagnosis to search for: ");
        String diagnosis = scanner.nextLine();
        
        ListInterface<MedicalTreatment> diagnosisTreatments = treatmentControl.getTreatmentsByDiagnosis(diagnosis);
        if (diagnosisTreatments.isEmpty()) {
            System.out.println("No treatments found with this diagnosis.");
            return;
        }
        
        System.out.println("\n=== Treatments for '" + diagnosis + "' ===");
        for (int i = 0; i < diagnosisTreatments.size(); i++) {
            MedicalTreatment treatment = diagnosisTreatments.get(i);
            System.out.println(treatment);
        }
    }

    private void searchByDoctor() {
        System.out.print("Enter Doctor ID: ");
        String doctorId = scanner.nextLine();
        
        ListInterface<MedicalTreatment> doctorTreatments = treatmentControl.getTreatmentsByDoctor(doctorId);
        if (doctorTreatments.isEmpty()) {
            System.out.println("No treatments found for this doctor.");
            return;
        }
        
        System.out.println("\n=== Treatments by Doctor " + doctorId + " ===");
        for (int i = 0; i < doctorTreatments.size(); i++) {
            MedicalTreatment treatment = doctorTreatments.get(i);
            System.out.println(treatment);
        }
    }

    private void updateTreatmentStatus() {
        System.out.print("Enter Treatment ID: ");
        String treatmentId = scanner.nextLine();
        
        System.out.println("Select new status:");
        System.out.println("1. PLANNED");
        System.out.println("2. IN_PROGRESS");
        System.out.println("3. COMPLETED");
        System.out.println("4. CANCELLED");
        System.out.print("Enter choice: ");
        int statusChoice = scanner.nextInt(); scanner.nextLine();
        
        String newStatus = switch (statusChoice) {
            case 1 -> "PLANNED";
            case 2 -> "IN_PROGRESS";
            case 3 -> "COMPLETED";
            case 4 -> "CANCELLED";
            default -> "PLANNED";
        };
        
        boolean success = treatmentControl.updateTreatmentStatus(treatmentId, newStatus);
        if (success) {
            System.out.println("Status updated successfully!");
            if (newStatus.equals("COMPLETED")) {
                System.out.println("Treatment completed - Revenue recorded");
            }
        } else {
            System.out.println("Treatment not found.");
        }
    }

    private void generateReports() {
        System.out.println("\n=== Generate Reports ===");
        System.out.println("1. General Treatment Report");
        System.out.println("2. Patient Treatment Summary");
        System.out.print("Enter choice: ");
        int reportChoice = scanner.nextInt(); scanner.nextLine();
        
        switch (reportChoice) {
            case 1 -> {
                System.out.println("\nGenerating General Treatment Report...");
                treatmentControl.generateTreatmentReport();
            }
            case 2 -> {
                System.out.print("Enter Patient ID for summary: ");
                String patientId = scanner.nextLine();
                treatmentControl.generatePatientTreatmentSummary(patientId);
            }
            default -> System.out.println("Invalid choice.");
        }
    }
}
