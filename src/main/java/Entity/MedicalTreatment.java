/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Entity;

/**
 *
 * @author yapji
 */
public class MedicalTreatment {
    private String treatmentId;
    private String consultationId;
    private String patientId;
    private String doctorId;
    private String diagnosis;
    private String prescription; // Medicine names, dosage, instructions
    private String treatmentDate;
    private String status; // PLANNED, IN_PROGRESS, COMPLETED, CANCELLED
    private double cost;

    public MedicalTreatment(String treatmentId, String consultationId, String patientId, String doctorId,
                           String diagnosis, String prescription, String treatmentDate, double cost) {
        this.treatmentId = treatmentId;
        this.consultationId = consultationId;
        this.patientId = patientId;
        this.doctorId = doctorId;
        this.diagnosis = diagnosis;
        this.prescription = prescription;
        this.treatmentDate = treatmentDate;
        this.status = "PLANNED";
        this.cost = cost;
    }

    // Getters
    public String getTreatmentId() { return treatmentId; }
    public String getConsultationId() { return consultationId; }
    public String getPatientId() { return patientId; }
    public String getDoctorId() { return doctorId; }
    public String getDiagnosis() { return diagnosis; }
    public String getPrescription() { return prescription; }
    public String getTreatmentDate() { return treatmentDate; }
    public String getStatus() { return status; }
    public double getCost() { return cost; }

    // Setters
    public void setStatus(String status) { this.status = status; }
    public void setDiagnosis(String diagnosis) { this.diagnosis = diagnosis; }
    public void setPrescription(String prescription) { this.prescription = prescription; }
    public void setCost(double cost) { this.cost = cost; }

    @Override
    public String toString() {
        return String.format("ID: %s | Consultation: %s | Patient: %s | Doctor: %s | Diagnosis: %s | Status: %s | Cost: $%.2f",    
                treatmentId, consultationId, patientId, doctorId, diagnosis, status, cost);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof MedicalTreatment)) return false;
        MedicalTreatment other = (MedicalTreatment) obj;
        return this.treatmentId.equalsIgnoreCase(other.treatmentId);
    }
}
