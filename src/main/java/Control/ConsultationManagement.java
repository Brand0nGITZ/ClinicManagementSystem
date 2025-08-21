/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Control;

import Entity.Consultation;
import ADT.ListInterface;
import ADT.MyArrayList;

/**
 *
 * @author yapji
 */
public class ConsultationManagement {
    private final MyArrayList<Consultation> consultationList;
    private int nextConsultationId = 1;
    
    // Symptom categorization dictionary
    private final String[][] symptomDiagnosisMap = {
        {"fever", "Fever"},
        {"cough", "Flu"},
        {"stomach ache", "Gastritis"},
        {"headache", "Migraine"},
        {"vomiting", "Food Poisoning"},
        {"dizziness", "Vertigo"},
        {"chest pain", "Heart Condition"},
        {"back pain", "Muscle Strain"},
        {"sore throat", "Tonsillitis"},
        {"fatigue", "Anemia"}
    };

    public ConsultationManagement() {
        this.consultationList = new MyArrayList<>();
    }

    // Auto-generate consultation ID
    public String generateConsultationId() {
        return String.format("C%03d", nextConsultationId++);
    }

    // Symptom categorization
    public String categorizeSymptoms(String symptoms) {
        String lowerSymptoms = symptoms.toLowerCase();
        for (String[] mapping : symptomDiagnosisMap) {
            if (lowerSymptoms.contains(mapping[0])) {
                return mapping[1];
            }
        }
        return "General Check-up";
    }

    // Check doctor availability (max 2 consultations per doctor)
    public boolean isDoctorAvailable(String doctorId) {
        int patientCount = 0;
        for (int i = 0; i < consultationList.size(); i++) {
            Consultation consultation = consultationList.get(i);
            if (consultation.getDoctorId().equals(doctorId) && 
                !consultation.getStatus().equals("CANCELLED") &&
                !consultation.getStatus().equals("COMPLETED")) {
                patientCount++;
            }
        }
        return patientCount < 2;
    }

    // Auto-assign doctor (round-robin between 2 doctors)
    public String assignDoctor() {
        int doctor1Count = 0, doctor2Count = 0;
        
        for (int i = 0; i < consultationList.size(); i++) {
            Consultation consultation = consultationList.get(i);
            if (!consultation.getStatus().equals("CANCELLED") && 
                !consultation.getStatus().equals("COMPLETED")) {
                if (consultation.getDoctorId().equals("D001")) {
                    doctor1Count++;
                } else if (consultation.getDoctorId().equals("D002")) {
                    doctor2Count++;
                }
            }
        }
        
        // Assign to doctor with fewer patients
        if (doctor1Count <= doctor2Count) {
            return "D001";
        } else {
            return "D002";
        }
    }

    // Get available time slots (30-minute increments from 9 AM to 5 PM)
    public String[] getAvailableTimeSlots() {
        String[] timeSlots = {
            "09:00", "09:30", "10:00", "10:30", "11:00", "11:30",
            "12:00", "12:30", "13:00", "13:30", "14:00", "14:30",
            "15:00", "15:30", "16:00", "16:30", "17:00"
        };
        
        // Filter out occupied time slots
        MyArrayList<String> availableSlots = new MyArrayList<>();
        for (String slot : timeSlots) {
            boolean slotAvailable = true;
            for (int i = 0; i < consultationList.size(); i++) {
                Consultation consultation = consultationList.get(i);
                if (consultation.getAppointmentTime().equals(slot) && 
                    !consultation.getStatus().equals("CANCELLED")) {
                    slotAvailable = false;
                    break;
                }
            }
            if (slotAvailable) {
                availableSlots.add(slot);
            }
        }
        
        String[] result = new String[availableSlots.size()];
        for (int i = 0; i < availableSlots.size(); i++) {
            result[i] = availableSlots.get(i);
        }
        return result;
    }

    // Check if time slot is available
    public boolean isTimeSlotAvailable(String timeSlot) {
        for (int i = 0; i < consultationList.size(); i++) {
            Consultation consultation = consultationList.get(i);
            if (consultation.getAppointmentTime().equals(timeSlot) && 
                !consultation.getStatus().equals("CANCELLED")) {
                return false;
            }
        }
        return true;
    }

    // Calculate estimated waiting time
    public int calculateWaitingTime(String queueType) {
        int baseTime = 15; // 15 minutes per consultation
        int queueLength = 0;
        
        // Count waiting patients in same queue type
        for (int i = 0; i < consultationList.size(); i++) {
            Consultation consultation = consultationList.get(i);
            if (consultation.getStatus().equals("WAITING") && 
                consultation.getQueueType().equalsIgnoreCase(queueType)) {
                queueLength++;
            }
        }
        
        // Emergency gets priority
        if (queueType.equalsIgnoreCase("EMERGENCY")) {
            return Math.max(5, queueLength * 10); // 5-10 minutes for emergency
        }
        
        return queueLength * baseTime;
    }

    public void addConsultation(Consultation consultation) {
        // Auto-categorize symptoms
        String diagnosis = categorizeSymptoms(consultation.getSymptoms());
        consultation.setDiagnosis(diagnosis);
        
        // Calculate waiting time
        int waitingTime = calculateWaitingTime(consultation.getQueueType());
        consultation.setEstimatedWaitingMinutes(waitingTime);
        
        consultationList.add(consultation);
    }

    public boolean removeConsultation(String consultationId) {
        for (int i = 0; i < consultationList.size(); i++) {
            Consultation consultation = consultationList.get(i);
            if (consultation.getConsultationId().equalsIgnoreCase(consultationId)) {
                return consultationList.remove(consultation);
            }
        }
        return false;
    }

    // Creative ADT Usage: Filter by Queue Type
    public ListInterface<Consultation> getConsultationsByQueueType(String queueType) {
        ListInterface<Consultation> filteredList = new MyArrayList<>();
        for (int i = 0; i < consultationList.size(); i++) {
            Consultation consultation = consultationList.get(i);
            if (consultation.getQueueType().equalsIgnoreCase(queueType)) {
                filteredList.add(consultation);
            }
        }
        return filteredList;
    }

    // Creative ADT Usage: Get Next Patient by Priority
    public Consultation getNextPatient() {
        // Priority: EMERGENCY > WALK_IN > SCHEDULED
        for (int i = 0; i < consultationList.size(); i++) {
            Consultation consultation = consultationList.get(i);
            if (consultation.getStatus().equals("WAITING") && 
                consultation.getQueueType().equalsIgnoreCase("EMERGENCY")) {
                return consultation;
            }
        }
        
        for (int i = 0; i < consultationList.size(); i++) {
            Consultation consultation = consultationList.get(i);
            if (consultation.getStatus().equals("WAITING") && 
                consultation.getQueueType().equalsIgnoreCase("WALK_IN")) {
                return consultation;
            }
        }
        
        for (int i = 0; i < consultationList.size(); i++) {
            Consultation consultation = consultationList.get(i);
            if (consultation.getStatus().equals("WAITING") && 
                consultation.getQueueType().equalsIgnoreCase("SCHEDULED")) {
                return consultation;
            }
        }
        
        return null;
    }

    // Creative ADT Usage: Search by Patient ID
    public ListInterface<Consultation> getConsultationsByPatient(String patientId) {
        ListInterface<Consultation> patientConsultations = new MyArrayList<>();
        for (int i = 0; i < consultationList.size(); i++) {
            Consultation consultation = consultationList.get(i);
            if (consultation.getPatientId().equalsIgnoreCase(patientId)) {
                patientConsultations.add(consultation);
            }
        }
        return patientConsultations;
    }

    // Creative ADT Usage: Search by Doctor ID
    public ListInterface<Consultation> getConsultationsByDoctor(String doctorId) {
        ListInterface<Consultation> doctorConsultations = new MyArrayList<>();
        for (int i = 0; i < consultationList.size(); i++) {
            Consultation consultation = consultationList.get(i);
            if (consultation.getDoctorId().equalsIgnoreCase(doctorId)) {
                doctorConsultations.add(consultation);
            }
        }
        return doctorConsultations;
    }

    // Creative ADT Usage: Search by Symptoms
    public ListInterface<Consultation> getConsultationsBySymptoms(String symptom) {
        ListInterface<Consultation> symptomConsultations = new MyArrayList<>();
        for (int i = 0; i < consultationList.size(); i++) {
            Consultation consultation = consultationList.get(i);
            if (consultation.getSymptoms().toLowerCase().contains(symptom.toLowerCase())) {
                symptomConsultations.add(consultation);
            }
        }
        return symptomConsultations;
    }

    public boolean updateConsultationStatus(String consultationId, String newStatus) {
        for (int i = 0; i < consultationList.size(); i++) {
            Consultation consultation = consultationList.get(i);
            if (consultation.getConsultationId().equalsIgnoreCase(consultationId)) {
                consultation.setStatus(newStatus);
                return true;
            }
        }
        return false;
    }

    // Creative ADT Usage: Generate Queue Report
    public void generateQueueReport() {
        System.out.println("=== Multi-Level Queue Report ===");
        
        if (consultationList.isEmpty()) {
            System.out.println("No consultations in queue.");
            return;
        }

        // Count by queue type
        int emergency = 0, walkIn = 0, scheduled = 0;
        for (int i = 0; i < consultationList.size(); i++) {
            Consultation consultation = consultationList.get(i);
            switch (consultation.getQueueType().toUpperCase()) {
                case "EMERGENCY" -> emergency++;
                case "WALK_IN" -> walkIn++;
                case "SCHEDULED" -> scheduled++;
            }
        }

        System.out.println("Emergency Queue: " + emergency + " patients");
        System.out.println("Walk-in Queue: " + walkIn + " patients");
        System.out.println("Scheduled Queue: " + scheduled + " patients");
        System.out.println("Total: " + consultationList.size() + " consultations");
        
        // Show estimated waiting times
        if (emergency > 0) {
            System.out.println("Emergency waiting time: ~" + Math.max(5, emergency * 10) + " minutes");
        }
        if (walkIn > 0) {
            System.out.println("Walk-in waiting time: ~" + (walkIn * 15) + " minutes");
        }
        if (scheduled > 0) {
            System.out.println("Scheduled waiting time: ~" + (scheduled * 15) + " minutes");
        }
        
        // Show doctor workload
        System.out.println("\n=== Doctor Workload ===");
        int doctor1Count = 0, doctor2Count = 0;
        for (int i = 0; i < consultationList.size(); i++) {
            Consultation consultation = consultationList.get(i);
            if (!consultation.getStatus().equals("CANCELLED") && 
                !consultation.getStatus().equals("COMPLETED")) {
                if (consultation.getDoctorId().equals("D001")) {
                    doctor1Count++;
                } else if (consultation.getDoctorId().equals("D002")) {
                    doctor2Count++;
                }
            }
        }
        System.out.println("Dr. Smith (D001): " + doctor1Count + "/2 patients");
        System.out.println("Dr. Johnson (D002): " + doctor2Count + "/2 patients");
    }

    public ListInterface<Consultation> getAllConsultations() {
        return consultationList;
    }
} 
