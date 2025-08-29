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
import Utility.LinearSearch;
import java.util.Scanner;
import java.util.Random;

/**
 *
 * @author yapji
 */
public class ConsultationUI {
    private final Scanner scanner = new Scanner(System.in);
    private final ConsultationManagement consultationControl;
    private final MyArrayList<Patient> patients;
    private final Random random = new Random();

    public ConsultationUI(ConsultationManagement consultationControl) {
        this.consultationControl = consultationControl;
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
            System.out.println("5. Change Consultation Status");
            System.out.println("6. View Queue by Type");
            System.out.println("7. Search by Patient");
            System.out.println("8. Search by Doctor");
            System.out.println("9. Search by Symptoms");
            System.out.println("10. Remove Consultation");
            System.out.println("11. Generate Queue Report");
            System.out.println("0. Exit");
            System.out.print("Enter choice: ");
            choice = scanner.nextInt(); scanner.nextLine();

            switch (choice) {
                case 1 -> startConsultation();
                case 2 -> addConsultation();
                case 3 -> viewAllConsultations();
                case 4 -> getNextPatient();
                case 5 -> changeConsultationStatus();
                case 6 -> viewQueueByType();
                case 7 -> searchByPatient();
                case 8 -> searchByDoctor();
                case 9 -> searchBySymptoms();
                case 10 -> removeConsultation();
                case 11 -> generateReport();
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
            System.out.println("No patients available for consultation!");
            return;
        }

        System.out.println("\n=== Start Consultation (FIFO) ===");
        
        // Get the first patient in the queue (FIFO)
        Patient nextPatient = patients.get(0);
        String patientId = nextPatient.getId();
        
        System.out.println("Next Patient: " + nextPatient.getName() + " (ID: " + patientId + ")");
        
        // Generate patient dialogue
        String patientDialogue = generatePatientDialogue();
        System.out.println("\nPatient says: \"" + patientDialogue + "\"");
        
        // Auto-assign doctor
        String doctorId = consultationControl.assignDoctor();
        String doctorName = getDoctorName(doctorId);
        System.out.println("Auto-assigned Doctor: " + doctorName + " (" + doctorId + ")");
        
        // Check if doctor is available
        if (!consultationControl.isDoctorAvailable(doctorId)) {
            System.out.println("All doctors are currently at full capacity (2 patients each)!");
            System.out.println("Please wait for a consultation to be completed.");
            return;
        }
        
        // Get next available time slot
        String[] availableSlots = consultationControl.getAvailableTimeSlots();
        if (availableSlots.length == 0) {
            System.out.println("No available time slots today!");
            System.out.println("All slots are booked.");
            return;
        }
        
        // Auto-assign the first available time slot
        String appointmentTime = availableSlots[0];
        System.out.println("Auto-assigned Time Slot: " + appointmentTime);
        
        // Auto-assign queue type based on time (morning = walk-in, afternoon = scheduled)
        String queueType = appointmentTime.compareTo("12:00") < 0 ? "WALK_IN" : "SCHEDULED";
        System.out.println("Queue Type: " + queueType);
        
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
        
        System.out.println("\nConsultation Started Successfully!");
        System.out.println("   Consultation ID: " + consultationId);
        System.out.println("   Symptoms: " + symptoms);
        System.out.println("   Auto-diagnosis: " + consultation.getDiagnosis());
        System.out.println("   Estimated wait: " + consultation.getEstimatedWaitingMinutes() + " minutes");
        
        System.out.println("\nConsultation added to queue successfully!");
        System.out.println("   Status: WAITING (ready for consultation)");
    }

    private void addConsultation() {
        if (patients.isEmpty()) {
            System.out.println("No patients available for consultation!");
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
            System.out.println("Invalid patient selection!");
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
        System.out.println("Auto-assigned Doctor: " + doctorName + " (" + doctorId + ")");
        
        // Ask for queue type 
        System.out.println("\nSelect Queue Type:");
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
        
        // Handle emergency cases immediately
        if (queueType.equals("EMERGENCY")) {
            System.out.println("\nEMERGENCY CASE - Immediate Priority!");
            
            // Check doctor availability even for emergency cases
            if (!consultationControl.isDoctorAvailable(doctorId)) {
                System.out.println("All doctors are currently at full capacity (2 patients each)!");
                System.out.println("   Emergency case cannot be accommodated at this time.");
                System.out.println("   Please wait for a consultation to be completed first.");
                // Return the patient to the list
                patients.add(selectedPatient);
                return;
            }
            
            // Extract symptoms from patient dialogue
            String symptoms = extractSymptomsFromDialogue(patientDialogue);
            
            // Auto-generate consultation ID
            String consultationId = consultationControl.generateConsultationId();
            String doctorName2 = getDoctorName(doctorId);
            Consultation consultation = new Consultation(consultationId, patientId, selectedPatient.getName(), doctorId, doctorName2,
                                                       "17:00", symptoms, queueType); // Temporary time, will be swapped
            
            consultationControl.addConsultation(consultation);
            System.out.println("Emergency consultation added with immediate priority!");
            System.out.println("   Consultation ID: " + consultationId);
            System.out.println("   Patient: " + selectedPatient.getName());
            System.out.println("   Doctor: " + doctorName2);
            System.out.println("   Status: Emergency priority - time slot will be automatically assigned");
            System.out.println("   Symptoms extracted: " + symptoms);
            return;
        }
        
        // For non-emergency cases, check doctor availability and pick time slot
        if (!consultationControl.isDoctorAvailable(doctorId)) {
            System.out.println("All doctors are currently at full capacity (2 patients each)!");
            System.out.println("Please wait for a consultation to be completed.");
            // Return the patient to the list
            patients.add(selectedPatient);
            return;
        }
        
        // Show available time slots
        String[] availableSlots = consultationControl.getAvailableTimeSlots();
        if (availableSlots.length == 0) {
            System.out.println("No available time slots today!");
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
            System.out.println("Invalid time slot selection!");
            // Return the patient to the list
            patients.add(selectedPatient);
            return;
        }
        
        String appointmentTime = availableSlots[slotChoice - 1];
        
        // Extract symptoms from patient dialogue
        String symptoms = extractSymptomsFromDialogue(patientDialogue);
        
        // Auto-generate consultation ID
        String consultationId = consultationControl.generateConsultationId();
        String doctorName2 = getDoctorName(doctorId);
        Consultation consultation = new Consultation(consultationId, patientId, selectedPatient.getName(), doctorId, doctorName2,
                                                   appointmentTime, symptoms, queueType);
        consultationControl.addConsultation(consultation);
                    System.out.println("Consultation added successfully!");
        System.out.println("   Consultation ID: " + consultationId);
        System.out.println("   Patient: " + selectedPatient.getName());
        System.out.println("   Doctor: " + doctorName2);
        System.out.println("   Time: " + appointmentTime);
        System.out.println("   Type: " + queueType);
        System.out.println("   Symptoms extracted: " + symptoms);
        System.out.println("Auto-diagnosis: " + consultation.getDiagnosis());
        
        System.out.println("\nConsultation added to queue successfully!");
        System.out.println("   Status: WAITING (ready for consultation)");
    }

    private void viewAllConsultations() {
        ListInterface<Consultation> consultations = consultationControl.getAllConsultations();
        if (consultations.isEmpty()) {
            System.out.println("No consultations found.");
            return;
        }
        
        System.out.println("\n=== All Consultations ===");
        System.out.println("================================================================================================================");
        System.out.println("| Consultation ID | Patient ID (Name)        | Doctor ID (Name)         | Time  | Type     | Status   | Symptoms");
        System.out.println("================================================================================================================");
        
        for (int i = 0; i < consultations.size(); i++) {
            Consultation consultation = consultations.get(i);
            String patientInfo = String.format("%s (%s)", consultation.getPatientId(), consultation.getPatientName());
            String doctorInfo = String.format("%s (%s)", consultation.getDoctorId(), consultation.getDoctorName());
            String waitInfo = consultation.getStatus().equals("WAITING") ? 
                String.format(" (%d min)", consultation.getEstimatedWaitingMinutes()) : "";
            
            System.out.printf("| %-15s | %-23s | %-24s | %-5s | %-9s | %-9s | %s\n",
                consultation.getConsultationId(),
                patientInfo,
                doctorInfo,
                consultation.getAppointmentTime(),
                consultation.getQueueType(),
                consultation.getStatus() + waitInfo,
                consultation.getSymptoms());
        }
        System.out.println("================================================================================================================");
    }

    private void getNextPatient() {
        Consultation nextPatient = consultationControl.getNextPatient();
        
        if (nextPatient != null) {
            System.out.println("\n=== Next Patient in Queue ===");
            System.out.println("================================================================================================================");
            System.out.println("| PRIORITY PATIENT INFORMATION                                                                                |");
            System.out.println("|==============================================================================================================|");
            System.out.printf("| Queue Type:    %-15s | Priority Level: %-15s |\n", 
                nextPatient.getQueueType(), getPriorityLevel(nextPatient.getQueueType()));
            System.out.printf("| Patient ID:    %-15s | Patient Name:   %-20s |\n", 
                nextPatient.getPatientId(), nextPatient.getPatientName());
            System.out.printf("| Doctor ID:     %-15s | Doctor Name:    %-20s |\n", 
                nextPatient.getDoctorId(), nextPatient.getDoctorName());
            System.out.printf("| Appointment:   %-15s | Status:         %-20s |\n", 
                nextPatient.getAppointmentTime(), nextPatient.getStatus());
            System.out.printf("| Wait Time:     %-15s | Consultation ID: %-20s |\n", 
                nextPatient.getEstimatedWaitingMinutes() + " minutes", nextPatient.getConsultationId());
            System.out.println("|==============================================================================================================|");
            
            if (nextPatient.getSymptoms() != null && !nextPatient.getSymptoms().trim().isEmpty()) {
                System.out.println("| SYMPTOMS                                                                                                   |");
                System.out.println("|==============================================================================================================|");
                System.out.printf("| %s\n", formatSymptoms(nextPatient.getSymptoms()));
                System.out.println("|==============================================================================================================|");
            }
            
            if (nextPatient.getDiagnosis() != null && !nextPatient.getDiagnosis().trim().isEmpty()) {
                System.out.println("| DIAGNOSIS                                                                                                  |");
                System.out.println("|==============================================================================================================|");
                System.out.printf("| %s\n", formatDiagnosis(nextPatient.getDiagnosis()));
                System.out.println("|==============================================================================================================|");
            }
            
            System.out.println("================================================================================================================");
        } else {
            System.out.println("\n=== Queue Status ===");
            System.out.println("================================================================================================================");
            System.out.println("| No patients currently waiting in queue.                                                                      |");
            System.out.println("| All consultations have been completed or are in progress.                                                    |");
            System.out.println("================================================================================================================");
        }
    }
    
    private String getPriorityLevel(String queueType) {
        return switch (queueType.toUpperCase()) {
            case "EMERGENCY" -> "HIGHEST";
            case "SCHEDULED" -> "HIGH";
            case "WALK_IN" -> "NORMAL";
            default -> "UNKNOWN";
        };
    }
    
    private String formatSymptoms(String symptoms) {
        if (symptoms == null || symptoms.trim().isEmpty()) {
            return "";
        }
        
        if (symptoms.length() <= 80) {
            return String.format("%-80s", symptoms);
        } else {
            return symptoms.substring(0, 77) + "...";
        }
    }
    
    private String formatDiagnosis(String diagnosis) {
        if (diagnosis == null || diagnosis.trim().isEmpty()) {
            return "";
        }
        
        if (diagnosis.length() <= 80) {
            return String.format("%-80s", diagnosis);
        } else {
            return diagnosis.substring(0, 77) + "...";
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
        System.out.println("================================================================================================================");
        System.out.println("| Consultation ID | Patient ID (Name)        | Doctor ID (Name)         | Time  | Type     | Status   | Symptoms");
        System.out.println("================================================================================================================");
        
        for (int i = 0; i < queueConsultations.size(); i++) {
            Consultation consultation = queueConsultations.get(i);
            String patientInfo = String.format("%s (%s)", consultation.getPatientId(), consultation.getPatientName());
            String doctorInfo = String.format("%s (%s)", consultation.getDoctorId(), consultation.getDoctorName());
            String waitInfo = consultation.getStatus().equals("WAITING") ? 
                String.format(" (%d min)", consultation.getEstimatedWaitingMinutes()) : "";
            
            System.out.printf("| %-15s | %-23s | %-24s | %-5s | %-9s | %-9s | %s\n",
                consultation.getConsultationId(),
                patientInfo,
                doctorInfo,
                consultation.getAppointmentTime(),
                consultation.getQueueType(),
                consultation.getStatus() + waitInfo,
                consultation.getSymptoms());
        }
        System.out.println("================================================================================================================");
    }

    private void searchByPatient() {
        System.out.print("Enter Patient ID or Name: ");
        String searchQuery = scanner.nextLine();
        
        // Use linear search with custom iterator
        ListInterface<Consultation> allConsultations = consultationControl.getAllConsultations();
        MyArrayList<LinearSearch.SearchResult<Consultation>> searchResults = 
            LinearSearch.search(searchQuery, allConsultations);
        
        if (searchResults.isEmpty()) {
            System.out.println("No consultations found for this patient.");
            return;
        }
        
        System.out.println("\n                                             === Patient Consultations  ===");
       
        System.out.println("=========================================================================================================================================");
        System.out.println("| Consultation ID | Patient ID (Name)        | Doctor ID (Name)         | Time  | Type     | Status   | Symptoms");
        System.out.println("=========================================================================================================================================");
        
        for (int i = 0; i < searchResults.size(); i++) {
            LinearSearch.SearchResult<Consultation> result = searchResults.get(i);
            Consultation consultation = result.item;
            String patientInfo = String.format("%s (%s)", consultation.getPatientId(), consultation.getPatientName());
            String doctorInfo = String.format("%s (%s)", consultation.getDoctorId(), consultation.getDoctorName());
            String waitInfo = consultation.getStatus().equals("WAITING") ? 
                String.format(" (%d min)", consultation.getEstimatedWaitingMinutes()) : "";
            
            System.out.printf("| %-15s | %-23s | %-24s | %-5s | %-9s | %-9s | %s\n",
                consultation.getConsultationId(),
                patientInfo,
                doctorInfo,
                consultation.getAppointmentTime(),
                consultation.getQueueType(),
                consultation.getStatus() + waitInfo,
                consultation.getSymptoms());
        }
        System.out.println("=========================================================================================================================================");
    }

    private void searchByDoctor() {
        System.out.print("Enter Doctor ID or Name: ");
        String searchQuery = scanner.nextLine();
        
        // Use linear search with custom iterator
        ListInterface<Consultation> allConsultations = consultationControl.getAllConsultations();
        MyArrayList<LinearSearch.SearchResult<Consultation>> searchResults = 
            LinearSearch.search(searchQuery, allConsultations);
        
        if (searchResults.isEmpty()) {
            System.out.println("No consultations found for this doctor.");
            return;
        }
        
        System.out.println("\n                                         === Doctor Consultations  ===");

        System.out.println("=========================================================================================================================================");
        System.out.println("| Consultation ID | Patient ID (Name)        | Doctor ID (Name)         | Time  | Type     | Status   | Symptoms");
        System.out.println("=========================================================================================================================================");
        
        for (int i = 0; i < searchResults.size(); i++) {
            LinearSearch.SearchResult<Consultation> result = searchResults.get(i);
            Consultation consultation = result.item;
            String patientInfo = String.format("%s (%s)", consultation.getPatientId(), consultation.getPatientName());
            String doctorInfo = String.format("%s (%s)", consultation.getDoctorId(), consultation.getDoctorName());
            String waitInfo = consultation.getStatus().equals("WAITING") ? 
                String.format(" (%d min)", consultation.getEstimatedWaitingMinutes()) : "";
            
            System.out.printf("| %-15s | %-23s | %-24s | %-5s | %-9s | %-9s | %s\n",
                consultation.getConsultationId(),
                patientInfo,
                doctorInfo,
                consultation.getAppointmentTime(),
                consultation.getQueueType(),
                consultation.getStatus() + waitInfo,
                consultation.getSymptoms());
        }
        System.out.println("=========================================================================================================================================");
    }

    private void searchBySymptoms() {
        System.out.print("Enter symptom to search for: ");
        String symptom = scanner.nextLine();
        
        // Use linear search with custom iterator
        ListInterface<Consultation> allConsultations = consultationControl.getAllConsultations();
        MyArrayList<LinearSearch.SearchResult<Consultation>> searchResults = 
            LinearSearch.search(symptom, allConsultations);
        
        if (searchResults.isEmpty()) {
            System.out.println("No consultations found with this symptom.");
            return;
        }
        
        System.out.println("\n=== Consultations with symptoms containing '" + symptom + "' ===");
        System.out.println("================================================================================================================");
        System.out.println("| Consultation ID | Patient ID (Name)        | Doctor ID (Name)         | Time  | Type     | Status   | Symptoms");
        System.out.println("================================================================================================================");
        
        for (int i = 0; i < searchResults.size(); i++) {
            LinearSearch.SearchResult<Consultation> result = searchResults.get(i);
            Consultation consultation = result.item;
            String patientInfo = String.format("%s (%s)", consultation.getPatientId(), consultation.getPatientName());
            String doctorInfo = String.format("%s (%s)", consultation.getDoctorId(), consultation.getDoctorName());
            String waitInfo = consultation.getStatus().equals("WAITING") ? 
                String.format(" (%d min)", consultation.getEstimatedWaitingMinutes()) : "";
            
            System.out.printf("| %-15s | %-23s | %-24s | %-5s | %-9s | %-9s | %s\n",
                consultation.getConsultationId(),
                patientInfo,
                doctorInfo,
                consultation.getAppointmentTime(),
                consultation.getQueueType(),
                consultation.getStatus() + waitInfo,
                consultation.getSymptoms());
        }
        System.out.println("================================================================================================================");
    }



    private void removeConsultation() {
        // First, show all consultations for reference
        ListInterface<Consultation> allConsultations = consultationControl.getAllConsultations();
        if (allConsultations.isEmpty()) {
            System.out.println("No consultations found to remove.");
            return;
        }
        
        System.out.println("\n=== All Consultations (for reference) ===");
        System.out.println("================================================================================================================");
        System.out.println("| #  | Consultation ID | Patient ID (Name)        | Doctor ID (Name)         | Time  | Type     | Status   |");
        System.out.println("================================================================================================================");
        
        for (int i = 0; i < allConsultations.size(); i++) {
            Consultation consultation = allConsultations.get(i);
            String patientInfo = String.format("%s (%s)", consultation.getPatientId(), consultation.getPatientName());
            String doctorInfo = String.format("%s (%s)", consultation.getDoctorId(), consultation.getDoctorName());
            
            System.out.printf("| %-2d | %-15s | %-23s | %-24s | %-5s | %-9s | %-9s |\n",
                (i + 1), consultation.getConsultationId(), patientInfo, doctorInfo,
                consultation.getAppointmentTime(), consultation.getQueueType(), consultation.getStatus());
        }
        System.out.println("================================================================================================================");
        
        System.out.print("\nSelect consultation to remove (1-" + allConsultations.size() + "): ");
        int choice = scanner.nextInt(); scanner.nextLine();
        
        if (choice < 1 || choice > allConsultations.size()) {
            System.out.println("Invalid selection!");
            return;
        }
        
        Consultation selectedConsultation = allConsultations.get(choice - 1);
        String consultationId = selectedConsultation.getConsultationId();
        
        System.out.println("\nSelected Consultation for Removal:");
        System.out.println("   ID: " + consultationId);
        System.out.println("   Patient: " + selectedConsultation.getPatientName());
        System.out.println("   Doctor: " + selectedConsultation.getDoctorName());
        System.out.println("   Status: " + selectedConsultation.getStatus());
        System.out.println("   Type: " + selectedConsultation.getQueueType());
        System.out.println("   Time: " + selectedConsultation.getAppointmentTime());
        
        System.out.print("Confirm removal? (y/n): ");
        String confirm = scanner.nextLine();
        
        if (confirm.equalsIgnoreCase("y") || confirm.equalsIgnoreCase("yes")) {
            boolean removed = consultationControl.removeConsultation(consultationId);
            if (removed) {
                System.out.println("Consultation removed successfully!");
            } else {
                System.out.println("Failed to remove consultation.");
            }
        } else {
            System.out.println("Removal cancelled.");
        }
    }



    private void changeConsultationStatus() {
        System.out.println("\n=== Change Consultation Status ===");
        
        // Get all consultations
        ListInterface<Consultation> allConsultations = consultationControl.getAllConsultations();
        if (allConsultations.isEmpty()) {
            System.out.println("No consultations found.");
            return;
        }
        
        // Show all consultations
        System.out.println("All Consultations (for reference):");
        System.out.println("================================================================================================================");
        System.out.println("| #  | Consultation ID | Patient ID (Name)        | Doctor ID (Name)         | Time  | Type     | Status   |");
        System.out.println("================================================================================================================");
        
        for (int i = 0; i < allConsultations.size(); i++) {
            Consultation consultation = allConsultations.get(i);
            String patientInfo = String.format("%s (%s)", consultation.getPatientId(), consultation.getPatientName());
            String doctorInfo = String.format("%s (%s)", consultation.getDoctorId(), consultation.getDoctorName());
            
            System.out.printf("| %-2d | %-15s | %-23s | %-24s | %-5s | %-9s | %-9s |\n",
                (i + 1), consultation.getConsultationId(), patientInfo, doctorInfo,
                consultation.getAppointmentTime(), consultation.getQueueType(), consultation.getStatus());
        }
        System.out.println("================================================================================================================");
        
        System.out.print("Select consultation to change (1-" + allConsultations.size() + "): ");
        int choice = scanner.nextInt(); scanner.nextLine();
        
        if (choice < 1 || choice > allConsultations.size()) {
            System.out.println("Invalid selection!");
            return;
        }
        
        Consultation selectedConsultation = allConsultations.get(choice - 1);
        String consultationId = selectedConsultation.getConsultationId();
        
        System.out.println("\nSelected Consultation:");
        System.out.println("   ID: " + consultationId);
        System.out.println("   Patient: " + selectedConsultation.getPatientName());
        System.out.println("   Current Status: " + selectedConsultation.getStatus());
        System.out.println("   Current Type: " + selectedConsultation.getQueueType());
        System.out.println("   Current Time: " + selectedConsultation.getAppointmentTime());
        
        // Special handling for Emergency patients
        if (selectedConsultation.getQueueType().equals("EMERGENCY")) {
            System.out.println("\nEMERGENCY PATIENT - Limited Options Available:");
            System.out.println("Emergency patients cannot change queue type.");
            System.out.println("1. Cancel Emergency Consultation");
            System.out.println("2. Move to Nearest Available Slot");
            System.out.println("3. Cancel Operation");
            System.out.print("Enter choice: ");
            int emergencyChoice = scanner.nextInt(); scanner.nextLine();
            
            switch (emergencyChoice) {
                case 1 -> {
                    // Cancel Emergency
                    boolean success = consultationControl.updateConsultationStatus(consultationId, "CANCELLED");
                    if (success) {
                        System.out.println("Emergency consultation cancelled.");
                        System.out.println("Emergency slot freed up for other patients");
                    } else {
                        System.out.println("Failed to cancel emergency consultation.");
                    }
                }
                case 2 -> {
                    // Move to nearest available slot
                    System.out.println("Moving emergency patient to nearest available slot...");
                    
                    // Remove from current position
                    consultationControl.removeConsultation(consultationId);
                    
                    // Get next available time slot
                    String newTimeSlot = consultationControl.getNextAvailableTimeSlot();
                    
                    // Update the consultation object with new time
                    selectedConsultation.setAppointmentTime(newTimeSlot);
                    
                    // Add back as emergency (maintains emergency priority)
                    consultationControl.addConsultation(selectedConsultation);
                    
                    System.out.println("Emergency patient moved to " + newTimeSlot);
                    System.out.println("Emergency priority maintained");
                    System.out.println("Time slot updated: " + selectedConsultation.getPatientName() + " → " + newTimeSlot);
                }
                case 3 -> System.out.println("Operation cancelled");
                default -> System.out.println("Invalid choice for emergency patient");
            }
            return;
        }
        
        // Regular patients (Scheduled/Walk-in) can change queue type and status
        System.out.println("\nChange Options:");
        System.out.println("1. Change Queue Type (Scheduled ↔ Walk-in)");
        System.out.println("2. Change Status (WAITING ↔ CANCELLED)");
        System.out.println("3. Cancel");
        System.out.print("Enter choice: ");
        int changeChoice = scanner.nextInt(); scanner.nextLine();
        
        switch (changeChoice) {
            case 1 -> {
                // Change queue type (Emergency not allowed for regular patients)
                System.out.println("\nNew Queue Type:");
                System.out.println("1. WALK_IN");
                System.out.println("2. SCHEDULED");
                System.out.print("Enter choice: ");
                int typeChoice = scanner.nextInt(); scanner.nextLine();
                
                String newType = switch (typeChoice) {
                    case 1 -> "WALK_IN";
                    case 2 -> "SCHEDULED";
                    default -> selectedConsultation.getQueueType();
                };
                
                if (!newType.equals(selectedConsultation.getQueueType())) {
                    // Remove from current list and add to appropriate list
                    consultationControl.removeConsultation(consultationId);
                    
                    // Update queue type
                    selectedConsultation.setQueueType(newType);
                    
                    // Add back to appropriate list
                    consultationControl.addConsultation(selectedConsultation);
                    
                    System.out.println("Queue type changed to: " + newType);
                    
                    // If changing to WALK_IN, remove time slot reservation
                    if (newType.equals("WALK_IN")) {
                        System.out.println("Time slot reservation removed (Walk-in patients don't reserve slots)");
                    }
                } else {
                    System.out.println("No change made (same type selected)");
                }
            }
            case 2 -> {
                // Change status
                System.out.println("\nNew Status:");
                System.out.println("1. WAITING");
                System.out.println("2. CANCELLED");
                System.out.print("Enter choice: ");
                int statusChoice = scanner.nextInt(); scanner.nextLine();
                
                String newStatus = switch (statusChoice) {
                    case 1 -> "WAITING";
                    case 2 -> "CANCELLED";
                    default -> selectedConsultation.getStatus();
                };
                
                if (!newStatus.equals(selectedConsultation.getStatus())) {
                    boolean success = consultationControl.updateConsultationStatus(consultationId, newStatus);
                    if (success) {
                        System.out.println("Status changed to: " + newStatus);
                        if (newStatus.equals("CANCELLED")) {
                            System.out.println("   Time slot reservation released");
                        }
                    } else {
                        System.out.println("Failed to update status");
                    }
                } else {
                    System.out.println("No change made (same status selected)");
                }
            }
            case 3 -> System.out.println("Operation cancelled");
            default -> System.out.println("Invalid choice");
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


}
