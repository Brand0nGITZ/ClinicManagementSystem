/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Boundary;

import Control.ConsultationManagement;
import Entity.Consultation;
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
    private ListInterface<Patient> patients;

    public ConsultationUI() {
        this.consultationControl = new ConsultationManagement();
        this.patients = new MyArrayList<>();
        generatePatients(); // Generate 10 patients using patientGenerator
    }

    public void run() {
        int choice;
        do {
            System.out.println("\n=== Consultation Management ===");
            System.out.println("1. Add New Consultation");
            System.out.println("2. View All Consultations");
            System.out.println("3. Get Next Patient (Priority Queue)");
            System.out.println("4. View Queue by Type");
            System.out.println("5. Search by Patient ID");
            System.out.println("6. Search by Doctor ID");
            System.out.println("7. Search by Symptoms");
            System.out.println("8. Update Consultation Status");
            System.out.println("9. Remove Consultation");
            System.out.println("10. Generate Queue Report");
            System.out.println("0. Exit");
            System.out.print("Enter choice: ");
            choice = scanner.nextInt(); scanner.nextLine();

            switch (choice) {
                case 1 -> addConsultation();
                case 2 -> viewAllConsultations();
                case 3 -> getNextPatient();
                case 4 -> viewQueueByType();
                case 5 -> searchByPatient();
                case 6 -> searchByDoctor(); 
                case 7 -> searchBySymptoms();
                case 8 -> updateStatus();
                case 9 -> removeConsultation();
                case 10 -> generateReport();
                case 0 -> System.out.println("Exiting...");
                default -> System.out.println("Invalid choice.");
            }
        } while (choice != 0);
    }

    private void addConsultation() {
        System.out.println("\n=== Add New Consultation ===");
        
        // Show available patients
        System.out.println("Available Patients:");
        for (int i = 0; i < patients.size(); i++) {
            Patient patient = patients.get(i);
            System.out.println(patient.getId() + " - " + patient.getName());
        }
        
        System.out.print("\nEnter Patient ID: ");
        String patientId = scanner.nextLine();
        
        // Find and remove the selected patient
        Patient selectedPatient = null;
        for (int i = 0; i < patients.size(); i++) {
            Patient patient = patients.get(i);
            if (patient.getId().equals(patientId)) {
                selectedPatient = patient;
                patients.remove(patient);
                break;
            }
        }
        
        if (selectedPatient == null) {
            System.out.println("❌ Patient not found or already selected!");
            return;
        }
        
        // Generate patient dialogue based on random symptoms
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
        
        // Extract symptoms from dialogue for medical treatment
        String symptoms = extractSymptomsFromDialogue(patientDialogue);
        
        // Auto-generate consultation ID
        String consultationId = consultationControl.generateConsultationId();
        
        Consultation consultation = new Consultation(consultationId, patientId, doctorId, doctorName,
                                                   appointmentTime, symptoms, queueType);
        consultationControl.addConsultation(consultation);
        
        System.out.println("✅ Consultation added successfully!");
        System.out.println("Symptoms extracted: " + symptoms);
        System.out.println("Auto-diagnosis: " + consultation.getDiagnosis());
        System.out.println("Estimated waiting time: " + consultation.getEstimatedWaitingMinutes() + " minutes");
        System.out.println("Appointment time: " + appointmentTime);
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
        System.out.println("\n=== Get Next Patient (Priority Queue) ===");
        Consultation nextPatient = consultationControl.getNextPatient();
        
        if (nextPatient != null) {
            System.out.println("Next patient to see: " + nextPatient);
            System.out.println("⏰ Estimated wait time: " + nextPatient.getEstimatedWaitingMinutes() + " minutes");
        } else {
            System.out.println("No patients waiting in queue.");
        }
    }

    private void viewQueueByType() {
        System.out.println("\n=== View Queue by Type ===");
        System.out.println("1. Emergency Queue");
        System.out.println("2. Walk-in Queue");
        System.out.println("3. Scheduled Queue");
        System.out.print("Enter choice: ");
        int choice = scanner.nextInt(); 
        scanner.nextLine();
        
        String queueType = switch (choice) {
            case 1 -> "EMERGENCY";
            case 2 -> "WALK_IN";
            case 3 -> "SCHEDULED";
            default -> "WALK_IN";
        };
        
        ListInterface<Consultation> queueConsultations = consultationControl.getConsultationsByQueueType(queueType);
        if (queueConsultations.isEmpty()) {
            System.out.println("No consultations in " + queueType + " queue.");
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

    private void updateStatus() {
        System.out.print("Enter Consultation ID: ");
        String consultationId = scanner.nextLine();
        
        System.out.println("Select new status:");
        System.out.println("1. WAITING");
        System.out.println("2. IN_PROGRESS");
        System.out.println("3. COMPLETED");
        System.out.println("4. CANCELLED");
        System.out.println("5. NO_SHOW");
        System.out.print("Enter choice: ");
        int statusChoice = scanner.nextInt(); scanner.nextLine();
        
        String newStatus = switch (statusChoice) {
            case 1 -> "WAITING";
            case 2 -> "IN_PROGRESS";
            case 3 -> "COMPLETED";
            case 4 -> "CANCELLED";
            case 5 -> "NO_SHOW";
            default -> "WAITING";
        };
        
        boolean success = consultationControl.updateConsultationStatus(consultationId, newStatus);
        if (success) {
            System.out.println("✅ Status updated successfully!");
        } else {
            System.out.println("❌ Consultation not found.");
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

    private void generatePatients() {
        for (int i = 0; i < 10; i++) {
            Patient patient = patientGenerator.generatePatient();
            patients.add(patient);
        }
        System.out.println("✅ Clinic initialized with 10 patients ready for consultation!");
    }
    
    private String generatePatientDialogue() {
        String[] dialogues = {
            "Hey doctor, I am feeling frail and feverish",
            "Doctor, I have a terrible headache and I'm feeling dizzy",
            "I've been coughing non-stop and my chest hurts",
            "My stomach is killing me and I can't keep anything down",
            "I have this sharp pain in my back that won't go away",
            "I feel like I'm burning up with fever",
            "My throat is so sore, I can barely swallow",
            "I'm having trouble breathing and my chest feels tight",
            "I've been vomiting all morning and feel weak",
            "I have this pounding headache that's making me nauseous"
        };
        Random rand = new Random();
        return dialogues[rand.nextInt(dialogues.length)];
    }
    
    private String getDoctorName(String doctorId) {
        return switch (doctorId) {
            case "D001" -> "Dr. Smith";
            case "D002" -> "Dr. Johnson";
            default -> "Dr. Unknown";
        };
    }
    
    private String extractSymptomsFromDialogue(String dialogue) {
        if (dialogue.toLowerCase().contains("fever") || dialogue.toLowerCase().contains("feverish")) {
            return "Fever";
        } else if (dialogue.toLowerCase().contains("headache")) {
            return "Headache";
        } else if (dialogue.toLowerCase().contains("cough")) {
            return "Cough";
        } else if (dialogue.toLowerCase().contains("chest") && dialogue.toLowerCase().contains("pain")) {
            return "Chest pain";
        } else if (dialogue.toLowerCase().contains("stomach") || dialogue.toLowerCase().contains("vomiting")) {
            return "Stomach ache";
        } else if (dialogue.toLowerCase().contains("back") && dialogue.toLowerCase().contains("pain")) {
            return "Back pain";
        } else if (dialogue.toLowerCase().contains("throat") || dialogue.toLowerCase().contains("sore")) {
            return "Sore throat";
        } else if (dialogue.toLowerCase().contains("breathing") || dialogue.toLowerCase().contains("tight")) {
            return "Shortness of breath";
        } else if (dialogue.toLowerCase().contains("dizzy") || dialogue.toLowerCase().contains("nauseous")) {
            return "Dizziness";
        } else {
            return "General discomfort";
        }
    }
}
