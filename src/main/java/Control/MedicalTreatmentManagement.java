/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Control;

import Entity.MedicalTreatment;
import ADT.ListInterface;
import ADT.MyArrayList;

/**
 *
 * @author yapji
 */
public class MedicalTreatmentManagement {
    private final MyArrayList<MedicalTreatment> treatmentList;
    private int nextTreatmentId = 1;

    public MedicalTreatmentManagement() {
        this.treatmentList = new MyArrayList<>();
    }

    // Auto-generate treatment ID
    public String generateTreatmentId() {
        return String.format("T%03d", nextTreatmentId++);
    }

    // Add new treatment
    public void addTreatment(MedicalTreatment treatment) {
        treatmentList.add(treatment);
    }

    // Remove treatment
    public boolean removeTreatment(String treatmentId) {
        for (int i = 0; i < treatmentList.size(); i++) {
            MedicalTreatment treatment = treatmentList.get(i);
            if (treatment.getTreatmentId().equalsIgnoreCase(treatmentId)) {
                return treatmentList.remove(treatment);
            }
        }
        return false;
    }

    // Creative ADT Usage: Get Treatment History by Patient
    public ListInterface<MedicalTreatment> getTreatmentHistoryByPatient(String patientId) {
        ListInterface<MedicalTreatment> patientHistory = new MyArrayList<>();
        for (int i = 0; i < treatmentList.size(); i++) {
            MedicalTreatment treatment = treatmentList.get(i);
            if (treatment.getPatientId().equalsIgnoreCase(patientId)) {
                patientHistory.add(treatment);
            }
        }
        return patientHistory;
    }

    // Creative ADT Usage: Search by Diagnosis
    public ListInterface<MedicalTreatment> getTreatmentsByDiagnosis(String diagnosis) {
        ListInterface<MedicalTreatment> diagnosisTreatments = new MyArrayList<>();
        for (int i = 0; i < treatmentList.size(); i++) {
            MedicalTreatment treatment = treatmentList.get(i);
            if (treatment.getDiagnosis().toLowerCase().contains(diagnosis.toLowerCase())) {
                diagnosisTreatments.add(treatment);
            }
        }
        return diagnosisTreatments;
    }

    // Creative ADT Usage: Search by Doctor
    public ListInterface<MedicalTreatment> getTreatmentsByDoctor(String doctorId) {
        ListInterface<MedicalTreatment> doctorTreatments = new MyArrayList<>();
        for (int i = 0; i < treatmentList.size(); i++) {
            MedicalTreatment treatment = treatmentList.get(i);
            if (treatment.getDoctorId().equalsIgnoreCase(doctorId)) {
                doctorTreatments.add(treatment);
            }
        }
        return doctorTreatments;
    }

    // Creative ADT Usage: Filter by Status
    public ListInterface<MedicalTreatment> getTreatmentsByStatus(String status) {
        ListInterface<MedicalTreatment> statusTreatments = new MyArrayList<>();
        for (int i = 0; i < treatmentList.size(); i++) {
            MedicalTreatment treatment = treatmentList.get(i);
            if (treatment.getStatus().equalsIgnoreCase(status)) {
                statusTreatments.add(treatment);
            }
        }
        return statusTreatments;
    }

    // Update treatment status
    public boolean updateTreatmentStatus(String treatmentId, String newStatus) {
        for (int i = 0; i < treatmentList.size(); i++) {
            MedicalTreatment treatment = treatmentList.get(i);
            if (treatment.getTreatmentId().equalsIgnoreCase(treatmentId)) {
                treatment.setStatus(newStatus);
                return true;
            }
        }
        return false;
    }

    // Get treatment by ID
    public MedicalTreatment getTreatmentById(String treatmentId) {
        for (int i = 0; i < treatmentList.size(); i++) {
            MedicalTreatment treatment = treatmentList.get(i);
            if (treatment.getTreatmentId().equalsIgnoreCase(treatmentId)) {
                return treatment;
            }
        }
        return null;
    }

    // Calculate total revenue
    public double calculateTotalRevenue() {
        double totalRevenue = 0.0;
        for (int i = 0; i < treatmentList.size(); i++) {
            MedicalTreatment treatment = treatmentList.get(i);
            if (treatment.getStatus().equals("COMPLETED")) {
                totalRevenue += treatment.getCost();
            }
        }
        return totalRevenue;
    }

    // Calculate revenue by diagnosis
    public double calculateRevenueByDiagnosis(String diagnosis) {
        double revenue = 0.0;
        for (int i = 0; i < treatmentList.size(); i++) {
            MedicalTreatment treatment = treatmentList.get(i);
            if (treatment.getDiagnosis().toLowerCase().contains(diagnosis.toLowerCase()) && 
                treatment.getStatus().equals("COMPLETED")) {
                revenue += treatment.getCost();
            }
        }
        return revenue;
    }

    // Creative ADT Usage: Generate Treatment Reports
    public void generateTreatmentReport() {
        System.out.println("=== Medical Treatment Report ===");
        
        if (treatmentList.isEmpty()) {
            System.out.println("No treatments found.");
            return;
        }

        // Count by status
        int planned = 0, inProgress = 0, completed = 0, cancelled = 0;
        for (int i = 0; i < treatmentList.size(); i++) {
            MedicalTreatment treatment = treatmentList.get(i);
            switch (treatment.getStatus().toUpperCase()) {
                case "PLANNED" -> planned++;
                case "IN_PROGRESS" -> inProgress++;
                case "COMPLETED" -> completed++;
                case "CANCELLED" -> cancelled++;
            }
        }

        System.out.println("Treatment Status:");
        System.out.println("  Planned: " + planned + " treatments");
        System.out.println("  In Progress: " + inProgress + " treatments");
        System.out.println("  Completed: " + completed + " treatments");
        System.out.println("  Cancelled: " + cancelled + " treatments");
        System.out.println("Total: " + treatmentList.size() + " treatments");

        // Financial summary
        System.out.println("\nFinancial Summary:");
        System.out.println("  Total Revenue: $" + String.format("%.2f", calculateTotalRevenue()));
        
        // Most common diagnoses
        System.out.println("\nMost Common Diagnoses:");
        String[] commonDiagnoses = {"Fever", "Common Cold", "Headache", "Gastritis", "Pain Management"};
        for (String diagnosis : commonDiagnoses) {
            ListInterface<MedicalTreatment> diagnosisTreatments = getTreatmentsByDiagnosis(diagnosis);
            if (!diagnosisTreatments.isEmpty()) {
                System.out.println("  " + diagnosis + ": " + diagnosisTreatments.size() + " treatments");
                System.out.println("    Revenue: $" + String.format("%.2f", calculateRevenueByDiagnosis(diagnosis)));
            }
        }
    }

    // Creative ADT Usage: Generate Patient Treatment Summary
    public void generatePatientTreatmentSummary(String patientId) {
        ListInterface<MedicalTreatment> patientHistory = getTreatmentHistoryByPatient(patientId);
        
        if (patientHistory.isEmpty()) {
            System.out.println("No treatment history found for patient " + patientId);
            return;
        }

        System.out.println("=== Patient Treatment Summary ===");
        System.out.println("Patient ID: " + patientId);
        System.out.println("Total Treatments: " + patientHistory.size());

        double totalCost = 0.0;
        int completedTreatments = 0;
        
        for (int i = 0; i < patientHistory.size(); i++) {
            MedicalTreatment treatment = patientHistory.get(i);
            System.out.println("\nTreatment " + (i + 1) + ":");
            System.out.println("  ID: " + treatment.getTreatmentId());
            System.out.println("  Date: " + treatment.getTreatmentDate());
            System.out.println("  Diagnosis: " + treatment.getDiagnosis());
            System.out.println("  Status: " + treatment.getStatus());
            System.out.println("  Cost: $" + String.format("%.2f", treatment.getCost()));
            System.out.println("  Prescription: " + treatment.getPrescription());
            
            totalCost += treatment.getCost();
            if (treatment.getStatus().equals("COMPLETED")) {
                completedTreatments++;
            }
        }

        System.out.println("\nSummary:");
        System.out.println("  Total Cost: $" + String.format("%.2f", totalCost));
        System.out.println("  Completed Treatments: " + completedTreatments);
        System.out.println("  Pending Treatments: " + (patientHistory.size() - completedTreatments));
    }

    public ListInterface<MedicalTreatment> getAllTreatments() {
        return treatmentList;
    }
}
