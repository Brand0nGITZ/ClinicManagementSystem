/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Entity;

/**
 *
 * @author yapji
 */
public class Consultation {
    private String consultationId;
    private String patientId;
    private String doctorId;
    private String doctorName;
    private String appointmentTime;
    private String symptoms;
    private String diagnosis;
    private String queueType; // WALK_IN, SCHEDULED, EMERGENCY
    private String status; // WAITING, IN_PROGRESS, COMPLETED, CANCELLED, NO_SHOW
    private int estimatedWaitingMinutes;

    public Consultation(String consultationId, String patientId, String doctorId, String doctorName,
                       String appointmentTime, String symptoms, String queueType) {
        this.consultationId = consultationId;
        this.patientId = patientId;
        this.doctorId = doctorId;
        this.doctorName = doctorName;
        this.appointmentTime = appointmentTime;
        this.symptoms = symptoms;
        this.diagnosis = "Pending";
        this.queueType = queueType;
        this.status = "WAITING";
        this.estimatedWaitingMinutes = 0;
    }

    // Getters
    public String getConsultationId() { return consultationId; }
    public String getPatientId() { return patientId; }
    public String getDoctorId() { return doctorId; }
    public String getDoctorName() { return doctorName; }
    public String getAppointmentTime() { return appointmentTime; }
    public String getSymptoms() { return symptoms; }
    public String getDiagnosis() { return diagnosis; }
    public String getQueueType() { return queueType; }
    public String getStatus() { return status; }
    public int getEstimatedWaitingMinutes() { return estimatedWaitingMinutes; }

    // Setters
    public void setStatus(String status) { this.status = status; }
    public void setDiagnosis(String diagnosis) { this.diagnosis = diagnosis; }
    public void setEstimatedWaitingMinutes(int minutes) { this.estimatedWaitingMinutes = minutes; }

    @Override
    public String toString() {
        return String.format("ID: %s | Patient: %s | Doctor: %s | Time: %s | Type: %s | Status: %s | Symptoms: %s | Diagnosis: %s",    
                consultationId, patientId, doctorName, appointmentTime, queueType, status, symptoms, diagnosis);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Consultation)) return false;
        Consultation other = (Consultation) obj;
        return this.consultationId.equalsIgnoreCase(other.consultationId);
    }
}

