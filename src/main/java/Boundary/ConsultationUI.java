/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Boundary;

import Control.ConsultationManagement;
import Control.MedicalTreatmentManagement;
import Entity.Consultation;
import Entity.MedicalTreatment;
import Entity.Patient;
import ADT.ListInterface;
import ADT.MyArrayList;
import Utility.patientGenerator;
import java.util.Scanner;
import java.util.Random;

/**
 *
 * @author yapji
 */
public class ConsultationUI {
    private final Scanner scanner = new Scanner(System.in);
    private final ConsultationManagement consultationControl;
    private final MedicalTreatmentManagement treatmentControl;
    private final MyArrayList<Patient> patients;
    private final Random random = new Random();

    public ConsultationUI(ConsultationManagement consultationControl, MedicalTreatmentManagement treatmentControl) {
        this.consultationControl = consultationControl;
        this.treatmentControl = treatmentControl;
        this.patients = new MyArrayList<>();
        generatePatients();
    }

    public void run() {
        int choice;
        do {
            System.out.println("\n=== Consultation Management ===");
            System.out.println("1. Start Consultation (FIFO)");
            System.out.println("2. Add Consultation");
            System.out.println("3. View All Consultations");
            System.out.println("4. Get Next Patient");
            System.out.println("5. View Queue by Type");
            System.out.println("6. Search by Patient ID");
            System.out.println("7. Search by Doctor ID");
            System.out.println("8. Search by Symptoms");
            System.out.println("9. Remove Consultation");
            System.out.println("10. Generate Queue Report");
            System.out.println("0. Exit");
            System.out.print("Enter choice: ");
            choice = scanner.nextInt(); scanner.nextLine();

            switch (choice) {
                case 1 -> startConsultation();
                case 2 -> addConsultation();
                case 3 -> viewAllConsultations();
                case 4 -> getNextPatient();
                case 5 -> viewQueueByType();
                case 6 -> searchByPatient();
                case 7 -> searchByDoctor();
                case 8 -> searchBySymptoms();
                case 9 -> removeConsultation();
                case 10 -> generateReport();
                case 0 -> System.out.println("Exiting...");
                default -> System.out.println("Invalid choice.");
            }
        } while (choice != 0);
    }

    private void generatePatients() {
        patientGenerator generator = new patientGenerator();
        for (int i = 0; i < 10; i++) {
            patients.add(generator.generatePatient());
        }
    }

    private void startConsultation() {
        if (patients.isEmpty()) {
            System.out.println("❌ No patients available for consultation!");
            return;
        }

        System.out.println("\n=== Start Consultation (FIFO) ===");
        
        // Get the first patient in the queue (FIFO)
        Patient nextPatient = patients.get(0);
        String patientId = nextPatient.getId();
        
        System.out.println("👤 Next Patient: " + nextPatient.getName() + " (ID: " + patientId + ")");
        
        // Generate patient dialogue
        String patientDialogue = generatePatientDialogue();
        System.out.println("\nPatient says: \"" + patientDialogue + "\"");
        
        // Auto-assign doctor
        String doctorId = consultationControl.assignDoctor();
        String doctorName = getDoctorName(doctorId);
        System.out.println("👨‍⚕️ Auto-assigned Doctor: " + doctorName + " (" + doctorId + ")");
        
        // Check if doctor is available
        if (!consultationControl.isDoctorAvailable(doctorId)) {
            System.out.println("❌ All doctors are currently at full capacity (2 patients each)!");
            System.out.println("Please wait for a consultation to be completed.");
            return;
        }
        
        // Get next available time slot
        String[] availableSlots = consultationControl.getAvailableTimeSlots();
        if (availableSlots.length == 0) {
            System.out.println("❌ No available time slots today!");
            System.out.println("All slots are booked.");
            return;
        }
        
        // Auto-assign the first available time slot
        String appointmentTime = availableSlots[0];
        System.out.println("⏰ Auto-assigned Time Slot: " + appointmentTime);
        
        // Auto-assign queue type based on time (morning = walk-in, afternoon = scheduled)
        String queueType = appointmentTime.compareTo("12:00") < 0 ? "WALK_IN" : "SCHEDULED";
        System.out.println("📋 Queue Type: " + queueType);
        
        // Extract symptoms from patient dialogue
        String symptoms = extractSymptomsFromDialogue(patientDialogue);
        
        // Auto-generate consultation ID
        String consultationId = consultationControl.generateConsultationId();
        
        // Create consultation
        Consultation consultation = new Consultation(consultationId, patientId, nextPatient.getName(), doctorId, doctorName,
                                                    appointmentTime, symptoms, queueType);
        
        // Add consultation to the system
        consultationControl.addConsultation(consultation);
        
        // Remove patient from available list (FIFO - first in, first out)
        patients.remove(nextPatient);
        
        System.out.println("\n✅ Consultation Started Successfully!");
        System.out.println("   Consultation ID: " + consultationId);
        System.out.println("   Symptoms: " + symptoms);
        System.out.println("   Auto-diagnosis: " + consultation.getDiagnosis());
        System.out.println("   Estimated wait: " + consultation.getEstimatedWaitingMinutes() + " minutes");
        
        // Automatically complete the consultation
        System.out.println("\n🩺 Conducting consultation...");
        consultationControl.updateConsultationStatus(consultationId, "COMPLETED");
        System.out.println("✅ Consultation completed!");
        
        // Prompt for treatment creation
        System.out.println("\n💊 Would you like to create a treatment for this patient?");
        System.out.println("1. Yes - Create treatment now");
        System.out.println("2. No - Skip for now");
        System.out.print("Enter choice: ");
        int treatmentChoice = scanner.nextInt(); scanner.nextLine();
        
        if (treatmentChoice == 1) {
            createTreatmentForConsultation(consultation);
        } else {
            System.out.println("⏭️ Treatment creation skipped. You can create it later from Medical Treatment module.");
        }
    }

    private void addConsultation() {
        if (patients.isEmpty()) {
            System.out.println("❌ No patients available for consultation!");
            return;
        }

        System.out.println("\n=== Add Consultation ===");
        
        // Show available patients
        System.out.println("Available Patients:");
        for (int i = 0; i < patients.size(); i++) {
            Patient patient = patients.get(i);
            System.out.println((i + 1) + ". " + patient.getName() + " (ID: " + patient.getId() + ")");
        }
        
        System.out.print("Select patient (1-" + patients.size() + "): ");
        int patientChoice = scanner.nextInt(); scanner.nextLine();
        
        if (patientChoice < 1 || patientChoice > patients.size()) {
            System.out.println("❌ Invalid patient selection!");
            return;
        }
        
        Patient selectedPatient = patients.get(patientChoice - 1);
        String patientId = selectedPatient.getId();
        
        // Remove patient from available list
        patients.remove(selectedPatient);
        
        // Generate patient dialogue
        String patientDialogue = generatePatientDialogue();
        System.out.println("\nPatient says: \"" + patientDialogue + "\"");
        
        // Auto-assign doctor
        String doctorId = consultationControl.assignDoctor();
        String doctorName = getDoctorName(doctorId);
        System.out.println("👨‍⚕️ Auto-assigned Doctor: " + doctorName + " (" + doctorId + ")");
        
        // Check if doctor is available
        if (!consultationControl.isDoctorAvailable(doctorId)) {
            System.out.println("❌ All doctors are currently at full capacity (2 patients each)!");
            System.out.println("Please wait for a consultation to be completed.");
            // Return the patient to the list
            patients.add(selectedPatient);
            return;
        }
        
        // Show available time slots
        String[] availableSlots = consultationControl.getAvailableTimeSlots();
        if (availableSlots.length == 0) {
            System.out.println("❌ No available time slots today!");
            System.out.println("All slots are booked.");
            // Return the patient to the list
            patients.add(selectedPatient);
            return;
        }
        
        System.out.println("\nAvailable Time Slots:");
        for (int i = 0; i < availableSlots.length; i++) {
            System.out.println((i + 1) + ". " + availableSlots[i]);
        }
        
        System.out.print("Select time slot (1-" + availableSlots.length + "): ");
        int slotChoice = scanner.nextInt(); scanner.nextLine();
        
        if (slotChoice < 1 || slotChoice > availableSlots.length) {
            System.out.println("❌ Invalid time slot selection!");
            // Return the patient to the list
            patients.add(selectedPatient);
            return;
        }
        
        String appointmentTime = availableSlots[slotChoice - 1];
        
        System.out.println("Select Queue Type:");
        System.out.println("1. Emergency");
        System.out.println("2. Walk-in");
        System.out.println("3. Scheduled");
        System.out.print("Enter choice: ");
        int queueChoice = scanner.nextInt(); scanner.nextLine();
        
        String queueType = switch (queueChoice) {
            case 1 -> "EMERGENCY";
            case 2 -> "WALK_IN";
            case 3 -> "SCHEDULED";
            default -> "WALK_IN";
        };
        
        // Extract symptoms from patient dialogue
        String symptoms = extractSymptomsFromDialogue(patientDialogue);
        
        // Auto-generate consultation ID
        String consultationId = consultationControl.generateConsultationId();
        String doctorName2 = getDoctorName(doctorId);
        Consultation consultation = new Consultation(consultationId, patientId, selectedPatient.getName(), doctorId, doctorName2,
                                                   appointmentTime, symptoms, queueType);
        consultationControl.addConsultation(consultation);
        System.out.println("✅ Consultation added successfully!");
        System.out.println("Symptoms extracted: " + symptoms);
        System.out.println("Auto-diagnosis: " + consultation.getDiagnosis());
        
        // Automatically complete the consultation
        System.out.println("\n🩺 Conducting consultation...");
        consultationControl.updateConsultationStatus(consultationId, "COMPLETED");
        System.out.println("✅ Consultation completed!");
        
        // Prompt for treatment creation
        System.out.println("\n💊 Would you like to create a treatment for this patient?");
        System.out.println("1. Yes - Create treatment now");
        System.out.println("2. No - Skip for now");
        System.out.print("Enter choice: ");
        int treatmentChoice = scanner.nextInt(); scanner.nextLine();
        
        if (treatmentChoice == 1) {
            createTreatmentForConsultation(consultation);
        } else {
            System.out.println("⏭️ Treatment creation skipped. You can create it later from Medical Treatment module.");
        }
    }

    private void viewAllConsultations() {
        ListInterface<Consultation> consultations = consultationControl.getAllConsultations();
        if (consultations.isEmpty()) {
            System.out.println("No consultations found.");
            return;
        }
        
        System.out.println("\n=== All Consultations ===");
        for (int i = 0; i < consultations.size(); i++) {
            Consultation consultation = consultations.get(i);
            System.out.println(consultation);
            if (consultation.getStatus().equals("WAITING")) {
                System.out.println("   ⏰ Estimated wait: " + consultation.getEstimatedWaitingMinutes() + " minutes");
            }
        }
    }

    private void getNextPatient() {
        Consultation nextPatient = consultationControl.getNextPatient();
        
        if (nextPatient != null) {
            System.out.println("Next patient to see: " + nextPatient);
            System.out.println("⏰ Estimated wait time: " + nextPatient.getEstimatedWaitingMinutes() + " minutes");
        } else {
            System.out.println("No patients waiting in queue.");
        }
    }

    private void viewQueueByType() {
        System.out.println("Select Queue Type:");
        System.out.println("1. Emergency");
        System.out.println("2. Walk-in");
        System.out.println("3. Scheduled");
        System.out.print("Enter choice: ");
        int choice = scanner.nextInt(); scanner.nextLine();
        
        String queueType = switch (choice) {
            case 1 -> "EMERGENCY";
            case 2 -> "WALK_IN";
            case 3 -> "SCHEDULED";
            default -> "WALK_IN";
        };
        
        ListInterface<Consultation> queueConsultations = consultationControl.getConsultationsByQueueType(queueType);
        if (queueConsultations.isEmpty()) {
            System.out.println("No " + queueType.toLowerCase() + " consultations found.");
            return;
        }
        
        System.out.println("\n=== " + queueType + " Queue ===");
        for (int i = 0; i < queueConsultations.size(); i++) {
            Consultation consultation = queueConsultations.get(i);
            System.out.println(consultation);
            if (consultation.getStatus().equals("WAITING")) {
                System.out.println("   ⏰ Estimated wait: " + consultation.getEstimatedWaitingMinutes() + " minutes");
            }
        }
    }

    private void searchByPatient() {
        System.out.print("Enter Patient ID: ");
        String patientId = scanner.nextLine();
        
        ListInterface<Consultation> patientConsultations = consultationControl.getConsultationsByPatient(patientId);
        if (patientConsultations.isEmpty()) {
            System.out.println("No consultations found for this patient.");
            return;
        }
        
        System.out.println("\n=== Patient Consultations ===");
        for (int i = 0; i < patientConsultations.size(); i++) {
            System.out.println(patientConsultations.get(i));
        }
    }

    private void searchByDoctor() {
        System.out.print("Enter Doctor ID: ");
        String doctorId = scanner.nextLine();
        
        ListInterface<Consultation> doctorConsultations = consultationControl.getConsultationsByDoctor(doctorId);
        if (doctorConsultations.isEmpty()) {
            System.out.println("No consultations found for this doctor.");
            return;
        }
        
        System.out.println("\n=== Doctor Consultations ===");
        for (int i = 0; i < doctorConsultations.size(); i++) {
            System.out.println(doctorConsultations.get(i));
        }
    }

    private void searchBySymptoms() {
        System.out.print("Enter symptom to search for: ");
        String symptom = scanner.nextLine();
        
        ListInterface<Consultation> symptomConsultations = consultationControl.getConsultationsBySymptoms(symptom);
        if (symptomConsultations.isEmpty()) {
            System.out.println("No consultations found with this symptom.");
            return;
        }
        
        System.out.println("\n=== Consultations with '" + symptom + "' ===");
        for (int i = 0; i < symptomConsultations.size(); i++) {
            System.out.println(symptomConsultations.get(i));
        }
    }



    private void removeConsultation() {
        System.out.print("Enter Consultation ID to remove: ");
        String consultationId = scanner.nextLine();
        
        boolean removed = consultationControl.removeConsultation(consultationId);
        if (removed) {
            System.out.println("✅ Consultation removed successfully!");
        } else {
            System.out.println("❌ Consultation not found.");
        }
    }

    private void generateReport() {
        consultationControl.generateQueueReport();
    }

    private String generatePatientDialogue() {
        String[] dialogues = {
            "Hey doctor, I'm feeling feverish and have a terrible headache.",
            "I've been coughing non-stop for the past few days.",
            "My stomach has been hurting since yesterday.",
            "I feel dizzy and nauseous all the time.",
            "I have this sharp pain in my back that won't go away.",
            "My throat is so sore, I can barely swallow.",
            "I'm so tired and fatigued, I can't focus on anything.",
            "I have this constant pain in my joints.",
            "I've been having trouble sleeping due to chest pain.",
            "I feel weak and my muscles are aching."
        };
        return dialogues[random.nextInt(dialogues.length)];
    }

    private String extractSymptomsFromDialogue(String dialogue) {
        String lowerDialogue = dialogue.toLowerCase();
        if (lowerDialogue.contains("fever")) return "Fever";
        if (lowerDialogue.contains("cough")) return "Cough";
        if (lowerDialogue.contains("headache")) return "Headache";
        if (lowerDialogue.contains("stomach")) return "Stomach Pain";
        if (lowerDialogue.contains("dizzy")) return "Dizziness";
        if (lowerDialogue.contains("nausea")) return "Nausea";
        if (lowerDialogue.contains("back")) return "Back Pain";
        if (lowerDialogue.contains("throat")) return "Sore Throat";
        if (lowerDialogue.contains("fatigue") || lowerDialogue.contains("tired")) return "Fatigue";
        if (lowerDialogue.contains("pain")) return "General Pain";
        return "General Symptoms";
    }

    private String getDoctorName(String doctorId) {
        return switch (doctorId) {
            case "D001" -> "Dr. Smith";
            case "D002" -> "Dr. Johnson";
            default -> "Dr. Unknown";
        };
    }

    private void createTreatmentForConsultation(Consultation consultation) {
        System.out.println("\n=== Create Treatment ===");
        
        // Auto-fill data from consultation
        String consultationId = consultation.getConsultationId();
        String patientId = consultation.getPatientId();
        String patientName = consultation.getPatientName();
        String doctorId = consultation.getDoctorId();
        String doctorName = consultation.getDoctorName();
        String diagnosis = consultation.getDiagnosis();
        
        System.out.println("📋 Consultation Details:");
        System.out.println("   Consultation ID: " + consultationId);
        System.out.println("   Patient ID: " + patientId);
        System.out.println("   Patient Name: " + patientName);
        System.out.println("   Doctor ID: " + doctorId);
        System.out.println("   Doctor Name: " + doctorName);
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
        
        // Create and add treatment to the system
        MedicalTreatment treatment = new MedicalTreatment(treatmentId, consultationId, patientId, doctorId,
                                                        diagnosis, prescription, treatmentDate, cost);
        treatmentControl.addTreatment(treatment);
        
        System.out.println("\n✅ Treatment created successfully!");
        System.out.println("   Treatment ID: " + treatmentId);
        System.out.println("   📋 Prescription ready for pharmacy dispensing");
        System.out.println("   💰 Cost: $" + String.format("%.2f", cost));
        System.out.println("   📅 Date: " + treatmentDate);
        
        System.out.println("\n🎉 Complete workflow: Consultation → Treatment created!");
        System.out.println("   You can view and manage treatments in the Medical Treatment module.");
    }

    // Helper methods for treatment creation
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
}
