/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Boundary;

import Control.MedicalTreatmentManagement;
import Control.ConsultationManagement;
import Entity.MedicalTreatment;
import Entity.Consultation;
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

    public MedicalTreatmentUI(MedicalTreatmentManagement treatmentControl, ConsultationManagement consultationControl) {
        this.treatmentControl = treatmentControl;
        this.consultationControl = consultationControl;
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
        System.out.println("\n=== Add New Treatment ===");
        
        // Get consultations that need treatments (completed consultations)
        ListInterface<Consultation> completedConsultations = getConsultationsNeedingTreatment();
        
        if (completedConsultations.isEmpty()) {
            System.out.println("❌ No consultations found that need treatments.");
            System.out.println("   (Only completed consultations can have treatments)");
            return;
        }
        
        // Show available consultations
        System.out.println("Select Consultation to Create Treatment For:");
        for (int i = 0; i < completedConsultations.size(); i++) {
            Consultation consultation = completedConsultations.get(i);
            System.out.printf("%d. %s - Patient: %s - Diagnosis: %s - Dr. %s%n", 
                (i + 1), consultation.getConsultationId(), consultation.getPatientId(), 
                consultation.getDiagnosis(), consultation.getDoctorName());
        }
        
        System.out.print("Enter choice (1-" + completedConsultations.size() + "): ");
        int consultationChoice = scanner.nextInt(); scanner.nextLine();
        
        if (consultationChoice < 1 || consultationChoice > completedConsultations.size()) {
            System.out.println("❌ Invalid consultation selection!");
            return;
        }
        
        // Get selected consultation
        Consultation selectedConsultation = completedConsultations.get(consultationChoice - 1);
        
        // Check if treatment already exists for this consultation
        if (treatmentExistsForConsultation(selectedConsultation.getConsultationId())) {
            System.out.println("❌ Treatment already exists for consultation " + selectedConsultation.getConsultationId());
            return;
        }
        
        // Auto-fill data from consultation
        String consultationId = selectedConsultation.getConsultationId();
        String patientId = selectedConsultation.getPatientId();
        String doctorId = selectedConsultation.getDoctorId();
        String diagnosis = selectedConsultation.getDiagnosis();
        
        System.out.println("\n📋 Consultation Details:");
        System.out.println("   Consultation ID: " + consultationId);
        System.out.println("   Patient ID: " + patientId);
        System.out.println("   Patient Name: " + selectedConsultation.getPatientName());
        System.out.println("   Doctor ID: " + doctorId);
        System.out.println("   Doctor Name: " + selectedConsultation.getDoctorName());
        System.out.println("   Diagnosis: " + diagnosis);
        
        // Suggest prescription based on diagnosis
        String suggestedPrescription = suggestPrescription(diagnosis);
        System.out.println("\n💊 Suggested Prescription for " + diagnosis + ":");
        System.out.println("   " + suggestedPrescription);
        
        System.out.print("Enter Prescription Details (or press Enter to use suggestion): ");
        String prescription = scanner.nextLine();
        
        if (prescription.trim().isEmpty()) {
            prescription = suggestedPrescription;
            System.out.println("✅ Using suggested prescription: " + prescription);
        }
        
        // Auto-calculate cost based on diagnosis
        double suggestedCost = calculateSuggestedCost(diagnosis);
        System.out.println("\n💰 Suggested Cost: $" + String.format("%.2f", suggestedCost));
        
        System.out.print("Enter Treatment Cost (or press Enter to use suggestion): $");
        String costInput = scanner.nextLine();
        double cost;
        
        if (costInput.trim().isEmpty()) {
            cost = suggestedCost;
            System.out.println("✅ Using suggested cost: $" + String.format("%.2f", cost));
        } else {
            try {
                cost = Double.parseDouble(costInput);
            } catch (NumberFormatException e) {
                System.out.println("❌ Invalid cost format. Using suggested cost.");
                cost = suggestedCost;
            }
        }
        
        // Auto-generate treatment date (today)
        String treatmentDate = java.time.LocalDate.now().format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        System.out.println("📅 Treatment Date: " + treatmentDate);
        
        // Auto-generate treatment ID
        String treatmentId = treatmentControl.generateTreatmentId();
        
        // Create and add treatment
        MedicalTreatment treatment = new MedicalTreatment(treatmentId, consultationId, patientId, doctorId,
                                                        diagnosis, prescription, treatmentDate, cost);
        treatmentControl.addTreatment(treatment);
        
        System.out.println("\n✅ Treatment added successfully!");
        System.out.println("   Treatment ID: " + treatmentId);
        System.out.println("   📋 Prescription ready for pharmacy dispensing");
        System.out.println("   💰 Cost: $" + String.format("%.2f", cost));
    }

    // Get consultations that need treatments (completed consultations)
    private ListInterface<Consultation> getConsultationsNeedingTreatment() {
        ListInterface<Consultation> allConsultations = consultationControl.getAllConsultations();
        ListInterface<Consultation> completedConsultations = new MyArrayList<>();
        
        for (int i = 0; i < allConsultations.size(); i++) {
            Consultation consultation = allConsultations.get(i);
            if (consultation.getStatus().equals("COMPLETED")) {
                completedConsultations.add(consultation);
            }
        }
        
        return completedConsultations;
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

    // Suggest prescription based on diagnosis
    private String suggestPrescription(String diagnosis) {
        return switch (diagnosis.toLowerCase()) {
            case "fever" -> "Paracetamol 500mg - 2 tablets every 6 hours for 3 days";
            case "common cold" -> "Decongestant syrup - 10ml every 8 hours for 5 days";
            case "headache" -> "Ibuprofen 400mg - 1 tablet every 8 hours as needed";
            case "gastritis" -> "Antacid tablets - 2 tablets after meals for 7 days";
            case "pain management" -> "Pain relief cream - Apply 3 times daily";
            case "dizziness" -> "Anti-vertigo medication - 1 tablet daily for 3 days";
            case "nausea" -> "Anti-nausea tablets - 1 tablet before meals";
            case "fatigue" -> "Multivitamin supplements - 1 tablet daily";
            case "sore throat" -> "Throat lozenges - 1 every 4 hours";
            case "back pain" -> "Muscle relaxant - 1 tablet at night for 5 days";
            default -> "General medication - Follow doctor's instructions";
        };
    }

    // Calculate suggested cost based on diagnosis
    private double calculateSuggestedCost(String diagnosis) {
        return switch (diagnosis.toLowerCase()) {
            case "fever" -> 25.00;
            case "common cold" -> 30.00;
            case "headache" -> 20.00;
            case "gastritis" -> 35.00;
            case "pain management" -> 40.00;
            case "dizziness" -> 45.00;
            case "nausea" -> 25.00;
            case "fatigue" -> 50.00;
            case "sore throat" -> 15.00;
            case "back pain" -> 60.00;
            default -> 30.00;
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
            System.out.println("  Cost: $" + String.format("%.2f", treatment.getCost()));
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
            System.out.println("✅ Status updated successfully!");
            if (newStatus.equals("COMPLETED")) {
                System.out.println("💰 Treatment completed - Revenue recorded");
            }
        } else {
            System.out.println("❌ Treatment not found.");
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
                System.out.println("\n📊 Generating General Treatment Report...");
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
