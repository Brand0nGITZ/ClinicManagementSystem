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
import Utility.LinearSearch;
import java.util.Scanner;

/**
 *
 * @author yapjinkai
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
            System.out.println("5. Update Treatment Status");
            System.out.println("6. Generate Reports");
            System.out.println("0. Exit");
            System.out.print("Enter choice: ");
            choice = scanner.nextInt(); scanner.nextLine();

            switch (choice) {
                case 1 -> addNewTreatment();
                case 2 -> viewTreatmentHistoryByPatient();
                case 3 -> viewAllTreatments();
                case 4 -> searchByDiagnosis();
                case 5 -> updateTreatmentStatus();
                case 6 -> generateReports();
                case 0 -> System.out.println("Exiting...");
                default -> System.out.println("Invalid choice.");
            }
        } while (choice != 0);
    }

    private void addNewTreatment() {
        System.out.println("\n=== Add New Treatment (Automatic Priority) ===");
        System.out.println("================================================================================================================");
        
        // Get the highest priority patient automatically
        Consultation nextPatient = consultationControl.getNextPatient();
        
        if (nextPatient == null) {
            System.out.println("| No patients waiting for treatment.                                                                        |");
            System.out.println("================================================================================================================");
            return;
        }
        
        // Check if treatment already exists for this consultation
        if (treatmentExistsForConsultation(nextPatient.getConsultationId())) {
            System.out.println("| Treatment already exists for consultation " + nextPatient.getConsultationId() + "                    |");
            System.out.println("================================================================================================================");
            return;
        }
        
        // Auto-fill data from consultation
        String consultationId = nextPatient.getConsultationId();
        String patientId = nextPatient.getPatientId();
        String doctorId = nextPatient.getDoctorId();
        String diagnosis = nextPatient.getDiagnosis();
        
        // PATIENT SELECTION SUMMARY
        System.out.println("| AUTOMATIC PATIENT SELECTION                                                                                  |");
        System.out.println("|==============================================================================================================|");
        System.out.printf("| Priority:   %-15s | Patient: %-20s |\n", nextPatient.getQueueType(), nextPatient.getPatientName());
        System.out.printf("| Doctor:     %-15s | Time:   %-20s |\n", nextPatient.getDoctorName(), nextPatient.getAppointmentTime());
        System.out.printf("| Diagnosis:  %-15s | Status: %-20s |\n", diagnosis, nextPatient.getStatus());
        System.out.println("|==============================================================================================================|");
        
        // PRESCRIPTION DETAILS
        String suggestedPrescription = suggestPrescription(diagnosis);
        System.out.println("| PRESCRIPTION DETAILS                                                                                         |");
        System.out.println("|==============================================================================================================|");
        System.out.printf("| Suggested for %-15s: %s\n", diagnosis, suggestedPrescription);
        System.out.println("|==============================================================================================================|");
        
        System.out.print("| Enter Prescription Details (or press Enter to use suggestion): ");
        String prescription = scanner.nextLine();
        
        if (prescription.trim().isEmpty()) {
            prescription = suggestedPrescription;
            System.out.println("| Using suggested prescription: " + prescription);
        }
        System.out.println("|==============================================================================================================|");
        
        // COST DETAILS
        double suggestedCost = calculateSuggestedCost(diagnosis);
        System.out.println("| COST DETAILS                                                                                                 |");
        System.out.println("|==============================================================================================================|");
        System.out.printf("| Suggested Cost: RM%-10.2f                                                                                    |\n", suggestedCost);
        System.out.println("|==============================================================================================================|");
        
        System.out.print("| Enter Treatment Cost (or press Enter to use suggestion): RM");
        String costInput = scanner.nextLine();
        double cost;
        
        if (costInput.trim().isEmpty()) {
            cost = suggestedCost;
            System.out.println("| Using suggested cost: RM" + String.format("%.2f", cost));
        } else {
            try {
                cost = Double.parseDouble(costInput);
            } catch (NumberFormatException e) {
                System.out.println("| Invalid cost format. Using suggested cost.");
                cost = suggestedCost;
            }
        }
        System.out.println("|==============================================================================================================|");
        
        // TREATMENT CREATION
        String treatmentDate = java.time.LocalDate.now().format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        String treatmentId = treatmentControl.generateTreatmentId();
        
        System.out.println("| TREATMENT CREATION                                                                                           |");
        System.out.println("|==============================================================================================================|");
        System.out.printf("| Treatment ID: %-15s | Date: %-15s |\n", treatmentId, treatmentDate);
        System.out.println("|==============================================================================================================|");
        
        // Create and add treatment
        MedicalTreatment treatment = new MedicalTreatment(treatmentId, consultationId, patientId, doctorId,
                                                        diagnosis, prescription, treatmentDate, cost);
        treatmentControl.addTreatment(treatment);
        
        // Automatically change treatment status to COMPLETED for revenue tracking
        boolean treatmentStatusUpdated = treatmentControl.updateTreatmentStatus(treatmentId, "COMPLETED");
        
        // Automatically change consultation status to COMPLETED
        boolean consultationStatusUpdated = consultationControl.updateConsultationStatus(consultationId, "COMPLETED");
        
        // SUCCESS SUMMARY
        System.out.println("| TREATMENT SUCCESS SUMMARY                                                                                   |");
        System.out.println("|==============================================================================================================|");
        System.out.printf("| Treatment ID: %-15s | Status: %-15s |\n", treatmentId, "COMPLETED");
        System.out.printf("| Cost: RM%-10.2f | Prescription: %-15s |\n", cost, "Ready for dispensing");
        System.out.println("|==============================================================================================================|");
        
        if (treatmentStatusUpdated) {
            System.out.printf("| Treatment cost: RM%-10.2f |\n", cost);
        }
        
        if (consultationStatusUpdated) {
            System.out.printf("| Doctor %-15s is now available |\n", nextPatient.getDoctorName());
            
            // Show next patient in queue
            Consultation nextInQueue = consultationControl.getNextPatient();
            if (nextInQueue != null) {
                System.out.printf("| Next patient: %-15s (%s) |\n", nextInQueue.getPatientName(), nextInQueue.getQueueType());
            }
        }
        System.out.println("================================================================================================================");
        
        // Generate prescription for pharmacy processing
        generatePrescription(diagnosis, treatmentId);
    }
    
    // Generate prescription for pharmacy processing
    private void generatePrescription(String diagnosis, String treatmentId) {
        System.out.println("\n=== PRESCRIPTION GENERATED ===");
        System.out.println("================================================================================================================");
        
        // Get medicines for this diagnosis
        ListInterface<Medicine> medicines = medicineControl.getMedicinesForDiagnosis(diagnosis);
        
        if (medicines.isEmpty()) {
            System.out.println("| No specific medicines found for " + diagnosis + "                                                      |");
            System.out.println("================================================================================================================");
            return;
        }
        
        System.out.println("| PRESCRIPTION DETAILS                                                                                         |");
        System.out.println("|==============================================================================================================|");
        System.out.printf("| Treatment ID: %-15s | Diagnosis: %-15s |\n", treatmentId, diagnosis);
        System.out.println("|==============================================================================================================|");
        
        double totalPrescriptionValue = 0.0;
        
        for (int i = 0; i < medicines.size(); i++) {
            Medicine medicine = medicines.get(i);
            int quantity = calculateQuantityForMedicine(medicine, diagnosis);
            double medicineValue = medicineControl.calculateRevenueFromMedicine(medicine.getMedicineID(), quantity);
            totalPrescriptionValue += medicineValue;
            
            System.out.printf("| %-20s | %-3d units | Price: RM%-8.2f | Stock: %-3d |\n",
                medicine.getName(), quantity, medicineValue, medicine.getStock());
        }
        
        System.out.println("|==============================================================================================================|");
        System.out.printf("| Total prescription value: RM%-10.2f |\n", totalPrescriptionValue);
        System.out.println("| Prescription ready for pharmacy processing |");
        System.out.println("| Please proceed to Pharmacy Management for payment |");
        System.out.println("================================================================================================================");
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

    // Calculate suggested cost based on diagnosis 
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
        // First, show all treatments to see available patients
        ListInterface<MedicalTreatment> allTreatments = treatmentControl.getAllTreatments();
        if (allTreatments.isEmpty()) {
            System.out.println("No treatments found.");
            return;
        }
        
        // Get unique patients from treatments
        MyArrayList<String> uniquePatients = new MyArrayList<>();
        for (int i = 0; i < allTreatments.size(); i++) {
            MedicalTreatment treatment = allTreatments.get(i);
            String patientId = treatment.getPatientId();
            if (!uniquePatients.contains(patientId)) {
                uniquePatients.add(patientId);
            }
        }
        
        System.out.println("\n=== All Patients with Treatments (for reference) ===");
        System.out.println("================================================================================================================");
        System.out.println("| #  | Patient ID | Patient Name        | Total Treatments |");
        System.out.println("================================================================================================================");
        
        for (int i = 0; i < uniquePatients.size(); i++) {
            String patientId = uniquePatients.get(i);
            String patientName = getPatientNameFromId(patientId);
            
            // Count treatments for this patient
            int treatmentCount = 0;
            for (int j = 0; j < allTreatments.size(); j++) {
                if (allTreatments.get(j).getPatientId().equals(patientId)) {
                    treatmentCount++;
                }
            }
            
            System.out.printf("| %-2d | %-10s | %-18s | %-16d |\n",
                (i + 1), patientId, patientName, treatmentCount);
        }
        System.out.println("================================================================================================================");
        
        System.out.print("\nSelect patient to view history (1-" + uniquePatients.size() + "): ");
        int choice = scanner.nextInt(); scanner.nextLine();
        
        if (choice < 1 || choice > uniquePatients.size()) {
            System.out.println("Invalid selection!");
            return;
        }
        
        String patientId = uniquePatients.get(choice - 1);
        String patientName = getPatientNameFromId(patientId);
        
        ListInterface<MedicalTreatment> patientHistory = treatmentControl.getTreatmentHistoryByPatient(patientId);
        if (patientHistory.isEmpty()) {
            System.out.println("No treatment history found for this patient.");
            return;
        }
        
        System.out.println("\n=== Treatment History for Patient " + patientId + " ===");
        System.out.println("================================================================================================================");
        System.out.println("| Treatment ID | Consultation ID | Date       | Diagnosis | Status   | Cost    | Prescription");
        System.out.println("================================================================================================================");
        
        for (int i = 0; i < patientHistory.size(); i++) {
            MedicalTreatment treatment = patientHistory.get(i);
            System.out.printf("| %-12s | %-15s | %-10s | %-9s | %-9s | RM%-6s | %s\n",
                treatment.getTreatmentId(),
                treatment.getConsultationId(),
                treatment.getTreatmentDate(),
                treatment.getDiagnosis(),
                treatment.getStatus(),
                String.format("%.2f", treatment.getCost()),
                treatment.getPrescription());
        }
        System.out.println("================================================================================================================");
    }

    private void viewAllTreatments() {
        ListInterface<MedicalTreatment> treatments = treatmentControl.getAllTreatments();
        if (treatments.isEmpty()) {
            System.out.println("No treatments found.");
            return;
        }
        
        System.out.println("\n=== All Treatments ===");
        System.out.println("================================================================================================================");
        System.out.println("| #  | Treatment ID | Patient ID (Name)        | Doctor ID (Name)         | Date       | Diagnosis | Status   | Cost    |");
        System.out.println("================================================================================================================");
        
        for (int i = 0; i < treatments.size(); i++) {
            MedicalTreatment treatment = treatments.get(i);
            
            String patientInfo = String.format("%s (%s)", treatment.getPatientId(), getPatientNameFromId(treatment.getPatientId()));
            String doctorInfo = String.format("%s (%s)", treatment.getDoctorId(), getDoctorNameFromId(treatment.getDoctorId()));
            
            System.out.printf("| %-2d | %-12s | %-23s | %-24s | %-10s | %-9s | %-9s | RM%-6s |\n",
                (i + 1),
                treatment.getTreatmentId(),
                patientInfo,
                doctorInfo,
                treatment.getTreatmentDate(),
                treatment.getDiagnosis(),
                treatment.getStatus(),
                String.format("%.2f", treatment.getCost()));
        }
        System.out.println("================================================================================================================");
    }

    private void searchByDiagnosis() {
        // First, show all treatments to see available diagnoses
        ListInterface<MedicalTreatment> allTreatments = treatmentControl.getAllTreatments();
        if (allTreatments.isEmpty()) {
            System.out.println("No treatments found.");
            return;
        }
        
        // Get unique diagnoses from treatments
        MyArrayList<String> uniqueDiagnoses = new MyArrayList<>();
        for (int i = 0; i < allTreatments.size(); i++) {
            MedicalTreatment treatment = allTreatments.get(i);
            String diagnosis = treatment.getDiagnosis();
            if (!uniqueDiagnoses.contains(diagnosis)) {
                uniqueDiagnoses.add(diagnosis);
            }
        }
        
        System.out.println("\n=== All Available Diagnoses (for reference) ===");
        System.out.println("================================================================================================================");
        System.out.println("| #  | Diagnosis           | Treatment Count |");
        System.out.println("================================================================================================================");
        
        for (int i = 0; i < uniqueDiagnoses.size(); i++) {
            String diagnosis = uniqueDiagnoses.get(i);
            
            // Count treatments for this diagnosis
            int treatmentCount = 0;
            for (int j = 0; j < allTreatments.size(); j++) {
                if (allTreatments.get(j).getDiagnosis().equals(diagnosis)) {
                    treatmentCount++;
                }
            }
            
            System.out.printf("| %-2d | %-19s | %-15d |\n",
                (i + 1), diagnosis, treatmentCount);
        }
        System.out.println("================================================================================================================");
        
        System.out.print("\nSelect diagnosis to search (1-" + uniqueDiagnoses.size() + "): ");
        int choice = scanner.nextInt(); scanner.nextLine();
        
        if (choice < 1 || choice > uniqueDiagnoses.size()) {
            System.out.println("Invalid selection!");
            return;
        }
        
        String diagnosis = uniqueDiagnoses.get(choice - 1);
        
        // Use linear search with custom iterator
        MyArrayList<LinearSearch.SearchResult<MedicalTreatment>> searchResults = 
            LinearSearch.search(diagnosis, allTreatments);
        
        if (searchResults.isEmpty()) {
            System.out.println("No treatments found with this diagnosis.");
            return;
        }
        
        System.out.println("\n=== Treatments for diagnosis containing '" + diagnosis + "' (Linear Search) ===");
        System.out.println("(Using Linear Search with Custom Iterator)");
        System.out.println("================================================================================================================");
        System.out.println("| Treatment ID | Patient ID (Name)        | Doctor ID (Name)         | Date       | Diagnosis | Status   | Cost    |");
        System.out.println("================================================================================================================");
        
        for (int i = 0; i < searchResults.size(); i++) {
            LinearSearch.SearchResult<MedicalTreatment> result = searchResults.get(i);
            MedicalTreatment treatment = result.item;
            
            String patientInfo = String.format("%s (%s)", treatment.getPatientId(), getPatientNameFromId(treatment.getPatientId()));
            String doctorInfo = String.format("%s (%s)", treatment.getDoctorId(), getDoctorNameFromId(treatment.getDoctorId()));
            
            System.out.printf("| %-12s | %-23s | %-24s | %-10s | %-9s | %-9s | RM%-6s |\n",
                treatment.getTreatmentId(),
                patientInfo,
                doctorInfo,
                treatment.getTreatmentDate(),
                treatment.getDiagnosis(),
                treatment.getStatus(),
                String.format("%.2f", treatment.getCost()));
        }
        System.out.println("================================================================================================================");
    }



    private void updateTreatmentStatus() {
        // First, show all treatments for reference
        ListInterface<MedicalTreatment> treatments = treatmentControl.getAllTreatments();
        if (treatments.isEmpty()) {
            System.out.println("No treatments found to update.");
            return;
        }
        
        System.out.println("\n=== All Treatments (for reference) ===");
        System.out.println("================================================================================================================");
        System.out.println("| #  | Treatment ID | Patient ID (Name)        | Doctor ID (Name)         | Date       | Diagnosis | Status   | Cost    |");
        System.out.println("================================================================================================================");
        
        for (int i = 0; i < treatments.size(); i++) {
            MedicalTreatment treatment = treatments.get(i);
            
            String patientInfo = String.format("%s (%s)", treatment.getPatientId(), getPatientNameFromId(treatment.getPatientId()));
            String doctorInfo = String.format("%s (%s)", treatment.getDoctorId(), getDoctorNameFromId(treatment.getDoctorId()));
            
            System.out.printf("| %-2d | %-12s | %-23s | %-24s | %-10s | %-9s | %-9s | RM%-6s |\n",
                (i + 1),
                treatment.getTreatmentId(),
                patientInfo,
                doctorInfo,
                treatment.getTreatmentDate(),
                treatment.getDiagnosis(),
                treatment.getStatus(),
                String.format("%.2f", treatment.getCost()));
        }
        System.out.println("================================================================================================================");
        
        System.out.print("\nSelect treatment to update (1-" + treatments.size() + "): ");
        int choice = scanner.nextInt(); scanner.nextLine();
        
        if (choice < 1 || choice > treatments.size()) {
            System.out.println("Invalid selection!");
            return;
        }
        
        MedicalTreatment selectedTreatment = treatments.get(choice - 1);
        String treatmentId = selectedTreatment.getTreatmentId();
        
        System.out.println("\nSelected Treatment for Update:");
        System.out.println("   ID: " + treatmentId);
        System.out.println("   Patient: " + selectedTreatment.getPatientId() + " (" + getPatientNameFromId(selectedTreatment.getPatientId()) + ")");
        System.out.println("   Doctor: " + selectedTreatment.getDoctorId() + " (" + getDoctorNameFromId(selectedTreatment.getDoctorId()) + ")");
        System.out.println("   Current Status: " + selectedTreatment.getStatus());
        System.out.println("   Diagnosis: " + selectedTreatment.getDiagnosis());
        System.out.println("   Cost: RM" + String.format("%.2f", selectedTreatment.getCost()));
        
        System.out.println("\nSelect new status:");
        System.out.println("1. PLANNED");
        System.out.println("2. CANCELLED");
        System.out.print("Enter choice: ");
        int statusChoice = scanner.nextInt(); scanner.nextLine();
        
        String newStatus = switch (statusChoice) {
            case 1 -> "PLANNED";
            case 2 -> "CANCELLED";
            default -> "PLANNED";
        };
        
        System.out.println("\nStatus Change Summary:");
        System.out.println("   Current Status: " + selectedTreatment.getStatus());
        System.out.println("   New Status: " + newStatus);
        System.out.print("Confirm status update? (y/n): ");
        String confirm = scanner.nextLine();
        
        if (confirm.equalsIgnoreCase("y") || confirm.equalsIgnoreCase("yes")) {
            boolean success = treatmentControl.updateTreatmentStatus(treatmentId, newStatus);
            if (success) {
                System.out.println("Status updated successfully!");
            } else {
                System.out.println("Treatment not found.");
            }
        } else {
            System.out.println("Status update cancelled.");
        }
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

    private void generateReports() {
        System.out.println("\n=== Generate Reports ===");
        System.out.println("Generating General Treatment Report...");
        treatmentControl.generateTreatmentReport();
    }
}
