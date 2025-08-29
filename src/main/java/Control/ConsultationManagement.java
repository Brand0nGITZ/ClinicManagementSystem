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
    private final MyArrayList<Consultation> consultationList; // For WALK_IN and EMERGENCY only
    private final MyArrayList<Consultation> scheduledConsultations; // For SCHEDULED appointments only
    private int nextConsultationId = 1;
    
    // Symptom to diagnosis mapping
    private final String[][] symptomDiagnosisMap = {
        {"fever", "Fever"},
        {"cough", "Common Cold"},
        {"headache", "Headache"},
        {"stomach", "Gastritis"},
        {"pain", "Pain Management"},
        {"dizzy", "Dizziness"},
        {"nausea", "Nausea"},
        {"fatigue", "Fatigue"},
        {"sore throat", "Sore Throat"},
        {"back pain", "Back Pain"}
    };

    public ConsultationManagement() {
        this.consultationList = new MyArrayList<>();
        this.scheduledConsultations = new MyArrayList<>();
    }

    // Auto-generate consultation ID
    public String generateConsultationId() {
        return String.format("C%03d", nextConsultationId++);
    }

    // Categorize symptoms to diagnosis
    public String categorizeSymptoms(String symptoms) {
        String lowerSymptoms = symptoms.toLowerCase();
        for (String[] mapping : symptomDiagnosisMap) {
            if (lowerSymptoms.contains(mapping[0])) {
                return mapping[1];
            }
        }
        return "General Consultation";
    }

    // Check doctor availability (max 2 consultations per doctor)
    public boolean isDoctorAvailable(String doctorId) {
        int patientCount = 0;
        
        // Check regular consultations (walk-in and emergency)
        for (int i = 0; i < consultationList.size(); i++) {
            Consultation consultation = consultationList.get(i);
            if (consultation.getDoctorId().equals(doctorId) && 
                !consultation.getStatus().equals("CANCELLED") &&
                !consultation.getStatus().equals("CONSULTED") &&
                !consultation.getStatus().equals("COMPLETED")) {
                patientCount++;
            }
        }
        
        // Check scheduled consultations
        for (int i = 0; i < scheduledConsultations.size(); i++) {
            Consultation consultation = scheduledConsultations.get(i);
            if (consultation.getDoctorId().equals(doctorId) && 
                !consultation.getStatus().equals("CANCELLED") &&
                !consultation.getStatus().equals("CONSULTED") &&
                !consultation.getStatus().equals("COMPLETED")) {
                patientCount++;
            }
        }
        
        return patientCount < 2;
    }

    // Auto-assign doctor (round-robin between 2 doctors)
    private int lastAssignedDoctor = 0; // 0 for D001, 1 for D002
    
    public String assignDoctor() {
        int doctor1Count = 0, doctor2Count = 0;
        
        // Count regular consultations (walk-in and emergency)
        for (int i = 0; i < consultationList.size(); i++) {
            Consultation consultation = consultationList.get(i);
            if (!consultation.getStatus().equals("CANCELLED") && 
                !consultation.getStatus().equals("CONSULTED") &&
                !consultation.getStatus().equals("COMPLETED")) {
                if (consultation.getDoctorId().equals("D001")) {
                    doctor1Count++;
                } else if (consultation.getDoctorId().equals("D002")) {
                    doctor2Count++;
                }
            }
        }
        
        // Count scheduled consultations
        for (int i = 0; i < scheduledConsultations.size(); i++) {
            Consultation consultation = scheduledConsultations.get(i);
            if (!consultation.getStatus().equals("CANCELLED") && 
                !consultation.getStatus().equals("CONSULTED") &&
                !consultation.getStatus().equals("COMPLETED")) {
                if (consultation.getDoctorId().equals("D001")) {
                    doctor1Count++;
                } else if (consultation.getDoctorId().equals("D002")) {
                    doctor2Count++;
                }
            }
        }
        
        // If both doctors have equal load, use round-robin alternation
        if (doctor1Count == doctor2Count) {
            lastAssignedDoctor = (lastAssignedDoctor + 1) % 2; // Alternate between 0 and 1
            return lastAssignedDoctor == 0 ? "D001" : "D002";
        }
        
        // Otherwise, assign to doctor with fewer patients
        if (doctor1Count < doctor2Count) {
            lastAssignedDoctor = 0;
            return "D001";
        } else {
            lastAssignedDoctor = 1;
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
        
        // Filter out occupied time slots (real-life scheduling simulation)
        MyArrayList<String> availableSlots = new MyArrayList<>();
        for (String slot : timeSlots) {
            boolean slotAvailable = true;
            
            for (int i = 0; i < consultationList.size(); i++) {
                Consultation consultation = consultationList.get(i);
                
                // Skip cancelled consultations
                if (consultation.getStatus().equals("CANCELLED")) {
                    continue;
                }
                
                // For SCHEDULED appointments: block the exact time slot
                if (consultation.getQueueType().equals("SCHEDULED") && 
                    consultation.getAppointmentTime().equals(slot)) {
                    slotAvailable = false;
                    break;
                }
                
                // For SCHEDULED appointments: block all time slots before the appointment
                if (consultation.getQueueType().equals("SCHEDULED") && 
                    slot.compareTo(consultation.getAppointmentTime()) < 0) {
                    slotAvailable = false;
                    break;
                }
                
                // For WALK_IN and EMERGENCY: only block the exact time slot
                if ((consultation.getQueueType().equals("WALK_IN") || 
                     consultation.getQueueType().equals("EMERGENCY")) && 
                    consultation.getAppointmentTime().equals(slot)) {
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

    // Check if time slot is available (real-life scheduling simulation)
    public boolean isTimeSlotAvailable(String timeSlot) {
        for (int i = 0; i < consultationList.size(); i++) {
            Consultation consultation = consultationList.get(i);
            
            // Skip cancelled consultations
            if (consultation.getStatus().equals("CANCELLED")) {
                continue;
            }
            
            // For SCHEDULED appointments: block the exact time slot
            if (consultation.getQueueType().equals("SCHEDULED") && 
                consultation.getAppointmentTime().equals(timeSlot)) {
                return false;
            }
            
            // For SCHEDULED appointments: block all time slots before the appointment
            if (consultation.getQueueType().equals("SCHEDULED") && 
                timeSlot.compareTo(consultation.getAppointmentTime()) < 0) {
                return false;
            }
            
            // For WALK_IN and EMERGENCY: only block the exact time slot
            if ((consultation.getQueueType().equals("WALK_IN") || 
                 consultation.getQueueType().equals("EMERGENCY")) && 
                consultation.getAppointmentTime().equals(timeSlot)) {
                return false;
            }
        }
        return true;
    }

    public void addConsultation(Consultation consultation) {
        // Auto-categorize symptoms
        String diagnosis = categorizeSymptoms(consultation.getSymptoms());
        consultation.setDiagnosis(diagnosis);
        
        // Calculate waiting time
        int waitingTime = calculateWaitingTime(consultation.getQueueType());
        consultation.setEstimatedWaitingMinutes(waitingTime);
        
        // Separate scheduled appointments from walk-in/emergency
        if (consultation.getQueueType().equals("SCHEDULED")) {
            scheduledConsultations.add(consultation);
            System.out.println("Scheduled appointment added to priority queue");
        } else if (consultation.getQueueType().equals("EMERGENCY")) {
            // Emergency patients swap with the earliest walk-in patient
            String swappedSlot = swapWithEarliestWalkIn(consultation);
            consultation.setAppointmentTime(swappedSlot);
            
            consultationList.add(consultation);
            
            System.out.println("| EMERGENCY TIME SLOT ASSIGNMENT                                                                              |");
            System.out.println("|==============================================================================================================|");
            System.out.printf("| Emergency patient: %-15s | Assigned Time: %-15s |\n", 
                consultation.getPatientName(), swappedSlot);
            System.out.println("| Priority: HIGHEST - Time slot automatically assigned with immediate priority!        |");
            System.out.println("================================================================================================================");
        } else {
            // Walk-in patients get next available slot
            String nextSlot = getNextAvailableTimeSlot();
            consultation.setAppointmentTime(nextSlot);
            consultationList.add(consultation);
            System.out.println("Walk-in patient added at " + nextSlot);
        }
    }

    // Calculate waiting time based on queue type
    private int calculateWaitingTime(String queueType) {
        switch (queueType.toUpperCase()) {
            case "EMERGENCY" -> {
                return 5; // Emergency patients get priority
            }
            case "WALK_IN" -> {
                int walkInCount = 0;
                for (int i = 0; i < consultationList.size(); i++) {
                    Consultation c = consultationList.get(i);
                    if (c.getQueueType().equals("WALK_IN") && c.getStatus().equals("WAITING")) {
                        walkInCount++;
                    }
                }
                return walkInCount * 15; // 15 minutes per walk-in patient
            }
            case "SCHEDULED" -> {
                // Scheduled appointments have time-based priority, not queue-based
                return 0; // They wait until their scheduled time
            }
            default -> {
                return 30;
            }
        }
    }

    public boolean removeConsultation(String consultationId) {
        // Check regular consultations first
        for (int i = 0; i < consultationList.size(); i++) {
            Consultation consultation = consultationList.get(i);
            if (consultation.getConsultationId().equalsIgnoreCase(consultationId)) {
                return consultationList.remove(consultation);
            }
        }
        
        // Check scheduled consultations
        for (int i = 0; i < scheduledConsultations.size(); i++) {
            Consultation consultation = scheduledConsultations.get(i);
            if (consultation.getConsultationId().equalsIgnoreCase(consultationId)) {
                return scheduledConsultations.remove(consultation);
            }
        }
        
        return false;
    }

    //  Filter by Queue Type
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

    //  Get Next Patient by Priority (Emergency > Scheduled > Walk-in)
    public Consultation getNextPatient() {
        // First, check for emergency patients (highest priority)
        for (int i = 0; i < consultationList.size(); i++) {
            Consultation consultation = consultationList.get(i);
            if (consultation.getStatus().equals("WAITING") && 
                consultation.getQueueType().equalsIgnoreCase("EMERGENCY")) {
                return consultation;
            }
        }
        
        // Second, check if any scheduled appointments are due
        Consultation scheduledPatient = getNextScheduledPatient();
        if (scheduledPatient != null) {
            return scheduledPatient;
        }
        
        // Finally, check walk-in patients (earliest time first)
        Consultation earliestWalkIn = null;
        String earliestTime = "17:30"; // Default to after clinic hours
        
        for (int i = 0; i < consultationList.size(); i++) {
            Consultation consultation = consultationList.get(i);
            if (consultation.getStatus().equals("WAITING") && 
                consultation.getQueueType().equalsIgnoreCase("WALK_IN") &&
                consultation.getAppointmentTime().compareTo(earliestTime) < 0) {
                earliestWalkIn = consultation;
                earliestTime = consultation.getAppointmentTime();
            }
        }
        
        return earliestWalkIn;
    }
    
    // Check if scheduled appointment is due (previous slot completed)
    private Consultation getNextScheduledPatient() {
        for (int i = 0; i < scheduledConsultations.size(); i++) {
            Consultation scheduled = scheduledConsultations.get(i);
            
            if (scheduled.getStatus().equals("WAITING")) {
                String appointmentTime = scheduled.getAppointmentTime();
                String previousSlot = getPreviousTimeSlot(appointmentTime);
                
                // Check if previous slot is completed AND it's time for the appointment
                if (isPreviousSlotCompleted(previousSlot, scheduled.getDoctorId()) && 
                    isTimeForScheduledAppointment(appointmentTime)) {
                    System.out.println("Scheduled appointment due: " + scheduled.getPatientName() + 
                                     " at " + appointmentTime + " with Dr. " + scheduled.getDoctorName());
                    return scheduled;
                } else {
                    System.out.println("Scheduled appointment waiting: " + scheduled.getPatientName() + 
                                     " at " + appointmentTime + " (not yet time for appointment)");
                }
            }
        }
        return null;
    }
    
    // Get the previous time slot
    private String getPreviousTimeSlot(String currentTime) {
        String[] timeSlots = {
            "09:00", "09:30", "10:00", "10:30", "11:00", "11:30",
            "12:00", "12:30", "13:00", "13:30", "14:00", "14:30",
            "15:00", "15:30", "16:00", "16:30", "17:00"
        };
        
        for (int i = 1; i < timeSlots.length; i++) {
            if (timeSlots[i].equals(currentTime)) {
                return timeSlots[i - 1];
            }
        }
        return "09:00"; // Default to first slot
    }
    
    // Check if previous slot is completed for the same doctor
    private boolean isPreviousSlotCompleted(String previousSlot, String doctorId) {
        // Check regular consultations
        for (int i = 0; i < consultationList.size(); i++) {
            Consultation consultation = consultationList.get(i);
            if (consultation.getAppointmentTime().equals(previousSlot) && 
                consultation.getDoctorId().equals(doctorId) && 
                consultation.getStatus().equals("COMPLETED")) {
                return true;
            }
        }
        
        // Check scheduled consultations
        for (int i = 0; i < scheduledConsultations.size(); i++) {
            Consultation consultation = scheduledConsultations.get(i);
            if (consultation.getAppointmentTime().equals(previousSlot) && 
                consultation.getDoctorId().equals(doctorId) && 
                consultation.getStatus().equals("COMPLETED")) {
                return true;
            }
        }
        
        // If no consultation found for previous slot, consider it available
        return true;
    }
    
    // Check if it's time for a scheduled appointment
    private boolean isTimeForScheduledAppointment(String appointmentTime) {
        // For now, we'll use a simple approach: check if there are no earlier walk-in/emergency patients
        // This ensures scheduled patients only get priority when it's actually their time
        
        // Get the current "clinic time" based on the earliest waiting patient
        String currentClinicTime = getCurrentClinicTime();
        
        // Scheduled appointment should only be prioritized if current time >= appointment time
        return currentClinicTime.compareTo(appointmentTime) >= 0;
    }
    
    // Get current clinic time based on the earliest waiting patient
    private String getCurrentClinicTime() {
        String earliestTime = "17:30"; // Default to after clinic hours
        
        // Check walk-in and emergency patients
        for (int i = 0; i < consultationList.size(); i++) {
            Consultation consultation = consultationList.get(i);
            if (consultation.getStatus().equals("WAITING") && 
                consultation.getAppointmentTime().compareTo(earliestTime) < 0) {
                earliestTime = consultation.getAppointmentTime();
            }
        }
        
        // If no waiting patients, return 09:00 (clinic opening time)
        if (earliestTime.equals("17:30")) {
            return "09:00";
        }
        
        return earliestTime;
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

    // Update consultation status (check both lists)
    public boolean updateConsultationStatus(String consultationId, String newStatus) {
        // Check regular consultations first
        for (int i = 0; i < consultationList.size(); i++) {
            Consultation consultation = consultationList.get(i);
            if (consultation.getConsultationId().equalsIgnoreCase(consultationId)) {
                consultation.setStatus(newStatus);
                return true;
            }
        }
        
        // Check scheduled consultations
        for (int i = 0; i < scheduledConsultations.size(); i++) {
            Consultation consultation = scheduledConsultations.get(i);
            if (consultation.getConsultationId().equalsIgnoreCase(consultationId)) {
                consultation.setStatus(newStatus);
                return true;
            }
        }
        
        return false;
    }

    // Creative ADT Usage: Generate Queue Report
    public void generateQueueReport() {
        System.out.println("\n=== Consultation Queue Report ===");
        System.out.println("================================================================================================================");
        
        if (consultationList.isEmpty() && scheduledConsultations.isEmpty()) {
            System.out.println("| No consultations in queue.                                                                                |");
            System.out.println("================================================================================================================");
            return;
        }

        // Count by queue type (regular consultations)
        int emergency = 0, walkIn = 0;
        for (int i = 0; i < consultationList.size(); i++) {
            Consultation consultation = consultationList.get(i);
            if (consultation.getStatus().equals("WAITING")) {
                switch (consultation.getQueueType().toUpperCase()) {
                    case "EMERGENCY" -> emergency++;
                    case "WALK_IN" -> walkIn++;
                }
            }
        }
        
        // Count scheduled consultations
        int scheduled = 0;
        for (int i = 0; i < scheduledConsultations.size(); i++) {
            Consultation consultation = scheduledConsultations.get(i);
            if (consultation.getStatus().equals("WAITING")) {
                scheduled++;
            }
        }

        // Queue Summary
        System.out.println("| QUEUE SUMMARY                                                                                                |");
        System.out.println("|==============================================================================================================|");
        System.out.printf("| Scheduled Queue: %-3d patients (Priority Queue)                                                          |\n", scheduled);
        System.out.printf("| Emergency Queue: %-3d patients                                                                              |\n", emergency);
        System.out.printf("| Walk-in Queue:   %-3d patients                                                                              |\n", walkIn);
        System.out.printf("| Total:           %-3d consultations                                                                         |\n", (consultationList.size() + scheduledConsultations.size()));
        System.out.println("|==============================================================================================================|");
        
        // Waiting Times
        System.out.println("| ESTIMATED WAITING TIMES                                                                                     |");
        System.out.println("|==============================================================================================================|");
        if (emergency > 0) {
            System.out.printf("| Emergency: ~%-3d minutes                                                                                    |\n", Math.max(5, emergency * 10));
        } else {
            System.out.println("| Emergency: No patients waiting                                                                              |");
        }
        if (walkIn > 0) {
            System.out.printf("| Walk-in:   ~%-3d minutes                                                                                    |\n", (walkIn * 15));
        } else {
            System.out.println("| Walk-in:   No patients waiting                                                                              |");
        }
        if (scheduled > 0) {
            System.out.println("| Scheduled: Time-based priority                                                                              |");
        } else {
            System.out.println("| Scheduled: No appointments waiting                                                                          |");
        }
        System.out.println("|==============================================================================================================|");
        
        // Show scheduled appointments
        if (scheduled > 0) {
            System.out.println("| SCHEDULED APPOINTMENTS                                                                                    |");
            System.out.println("|==============================================================================================================|");
            for (int i = 0; i < scheduledConsultations.size(); i++) {
                Consultation consultation = scheduledConsultations.get(i);
                if (consultation.getStatus().equals("WAITING")) {
                    System.out.printf("| %-20s | %-8s | Dr. %-15s | %-8s |\n",
                        consultation.getPatientName(),
                        consultation.getAppointmentTime(),
                        consultation.getDoctorName(),
                        consultation.getStatus());
                }
            }
            System.out.println("|==============================================================================================================|");
        }
        
        // Show doctor workload (both lists)
        System.out.println("| DOCTOR WORKLOAD                                                                                             |");
        System.out.println("|==============================================================================================================|");
        int doctor1Count = 0, doctor2Count = 0;
        
        // Count from regular consultations
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
        
        // Count from scheduled consultations
        for (int i = 0; i < scheduledConsultations.size(); i++) {
            Consultation consultation = scheduledConsultations.get(i);
            if (!consultation.getStatus().equals("CANCELLED") && 
                !consultation.getStatus().equals("COMPLETED")) {
                if (consultation.getDoctorId().equals("D001")) {
                    doctor1Count++;
                } else if (consultation.getDoctorId().equals("D002")) {
                    doctor2Count++;
                }
            }
        }
        
        System.out.printf("| Dr. Smith (D001):   %d/2 patients                                                                          |\n", doctor1Count);
        System.out.printf("| Dr. Johnson (D002): %d/2 patients                                                                          |\n", doctor2Count);
        System.out.println("================================================================================================================");
    }

    public ListInterface<Consultation> getAllConsultations() {
        // Combine both lists: scheduled consultations first, then regular consultations
        ListInterface<Consultation> allConsultations = new MyArrayList<>();
        
        // Add scheduled consultations first (they have priority)
        for (int i = 0; i < scheduledConsultations.size(); i++) {
            allConsultations.add(scheduledConsultations.get(i));
        }
        
        // Add regular consultations (walk-in and emergency)
        for (int i = 0; i < consultationList.size(); i++) {
            allConsultations.add(consultationList.get(i));
        }
        
        return allConsultations;
    }
    
    // Get only scheduled consultations
    public ListInterface<Consultation> getScheduledConsultations() {
        return scheduledConsultations;
    }
    
    // Get only regular consultations (walk-in and emergency)
    public ListInterface<Consultation> getRegularConsultations() {
        return consultationList;
    }
    
    // Get the earliest available time slot
    private String getEarliestAvailableTimeSlot() {
        String[] timeSlots = {
            "09:00", "09:30", "10:00", "10:30", "11:00", "11:30",
            "12:00", "12:30", "13:00", "13:30", "14:00", "14:30",
            "15:00", "15:30", "16:00", "16:30", "17:00"
        };
        
        for (String slot : timeSlots) {
            if (isTimeSlotAvailable(slot)) {
                return slot;
            }
        }
        return "17:00"; // Default to last slot if all are taken
    }
    
    // Get the next available time slot after a specific time
    private String getNextAvailableTimeSlotAfter(String afterTime) {
        String[] timeSlots = {
            "09:00", "09:30", "10:00", "10:30", "11:00", "11:30",
            "12:00", "12:30", "13:00", "13:30", "14:00", "14:30",
            "15:00", "15:30", "16:00", "16:30", "17:00"
        };
        
        // Find the next available slot after the specified time
        for (String slot : timeSlots) {
            if (slot.compareTo(afterTime) > 0 && isTimeSlotAvailable(slot)) {
                return slot;
            }
        }
        
        return getEarliestAvailableTimeSlot();
    }
    
    // Get the next available time slot
    public String getNextAvailableTimeSlot() {
        String[] timeSlots = {
            "09:00", "09:30", "10:00", "10:30", "11:00", "11:30",
            "12:00", "12:30", "13:00", "13:30", "14:00", "14:30",
            "15:00", "15:30", "16:00", "16:30", "17:00"
        };
        
        // Find the latest occupied slot
        String latestOccupied = "08:30";
        for (int i = 0; i < consultationList.size(); i++) {
            Consultation consultation = consultationList.get(i);
            if (consultation.getAppointmentTime().compareTo(latestOccupied) > 0) {
                latestOccupied = consultation.getAppointmentTime();
            }
        }
        
        // Find the next available slot after the latest occupied
        for (String slot : timeSlots) {
            if (slot.compareTo(latestOccupied) > 0 && isTimeSlotAvailable(slot)) {
                return slot;
            }
        }
        
        return getEarliestAvailableTimeSlot();
    }
    
    // Emergency patients swap with the earliest walk-in patient
    private String swapWithEarliestWalkIn(Consultation emergencyPatient) {
        String[] timeSlots = {
            "09:00", "09:30", "10:00", "10:30", "11:00", "11:30",
            "12:00", "12:30", "13:00", "13:30", "14:00", "14:30",
            "15:00", "15:30", "16:00", "16:30", "17:00"
        };
        
        // Find the earliest walk-in patient
        Consultation earliestWalkIn = null;
        String earliestTime = "17:30"; // Default to after clinic hours
        
        for (int i = 0; i < consultationList.size(); i++) {
            Consultation consultation = consultationList.get(i);
            if (consultation.getQueueType().equals("WALK_IN") && 
                consultation.getStatus().equals("WAITING") &&
                consultation.getAppointmentTime().compareTo(earliestTime) < 0) {
                earliestWalkIn = consultation;
                earliestTime = consultation.getAppointmentTime();
            }
        }
        
        if (earliestWalkIn != null) {
            // Get the walk-in's time slot
            String walkInTime = earliestWalkIn.getAppointmentTime();
            
            // Emergency patient gets the walk-in's time slot
            emergencyPatient.setAppointmentTime(walkInTime);
            
            // Find the next available time slot for the walk-in patient
            String nextAvailableSlot = getNextAvailableTimeSlotAfter(walkInTime);
            earliestWalkIn.setAppointmentTime(nextAvailableSlot);
            
            System.out.println("| TIME SLOT SWAP DETAILS                                                                                      |");
            System.out.println("|==============================================================================================================|");
            System.out.printf("| Emergency patient %-15s gets %-5s (swapped with %-15s) |\n", 
                emergencyPatient.getPatientName(), walkInTime, earliestWalkIn.getPatientName());
            System.out.printf("| Walk-in patient %-15s moved to %-5s                                    |\n", 
                earliestWalkIn.getPatientName(), nextAvailableSlot);
            System.out.println("|==============================================================================================================|");
            
            return walkInTime;
        } else {
            // If no walk-in patients, get the earliest available slot
            return getEarliestAvailableTimeSlot();
        }
    }
} 


