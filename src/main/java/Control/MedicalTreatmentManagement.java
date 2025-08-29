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
 * @author yapjinkai
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

    //  Get Treatment History by Patient
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

    //  Search by Diagnosis
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

    //  Search by Doctor
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

    //  Filter by Status
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

    //  Generate Treatment Reports
    public void generateTreatmentReport() {
        System.out.println("\n=== Medical Treatment Report ===");
        System.out.println("================================================================================================================");
        
        if (treatmentList.isEmpty()) {
            System.out.println("| No treatments found.                                                                                        |");
            System.out.println("================================================================================================================");
            return;
        }

        // Count by status
        int planned = 0, completed = 0, cancelled = 0, paid = 0;
        for (int i = 0; i < treatmentList.size(); i++) {
            MedicalTreatment treatment = treatmentList.get(i);
            switch (treatment.getStatus().toUpperCase()) {
                case "PLANNED" -> planned++;
                case "COMPLETED" -> completed++;
                case "CANCELLED" -> cancelled++;
                case "PAID" -> paid++;
            }
        }

        // Treatment Status Summary
        System.out.println("| TREATMENT STATUS SUMMARY                                                                                      |");
        System.out.println("|==============================================================================================================|");
        System.out.printf("| Planned:     %-3d treatments                                                                                |\n", planned);
        System.out.printf("| Completed:   %-3d treatments                                                                                |\n", completed);
        System.out.printf("| Cancelled:   %-3d treatments                                                                                |\n", cancelled);
        System.out.printf("| Paid:        %-3d treatments                                                                                |\n", paid);
        System.out.printf("| Total:       %-3d treatments                                                                                |\n", treatmentList.size());
        System.out.println("|==============================================================================================================|");

        // Financial summary
        System.out.println("| FINANCIAL SUMMARY                                                                                            |");
        System.out.println("|==============================================================================================================|");
        System.out.printf("| Total Revenue: RM%-10.2f                                                                                    |\n", calculateTotalRevenue());
        System.out.println("|==============================================================================================================|");
        
        // Most common diagnoses
        System.out.println("| DIAGNOSIS ANALYSIS                                                                                          |");
        System.out.println("|==============================================================================================================|");
        String[] commonDiagnoses = {"Fever", "Common Cold", "Headache", "Gastritis", "Pain Management"};
        for (String diagnosis : commonDiagnoses) {
            ListInterface<MedicalTreatment> diagnosisTreatments = getTreatmentsByDiagnosis(diagnosis);
            if (!diagnosisTreatments.isEmpty()) {
                System.out.printf("| %-15s | %-3d treatments | Revenue: RM%-10.2f |\n", 
                    diagnosis, diagnosisTreatments.size(), calculateRevenueByDiagnosis(diagnosis));
            }
        }
        System.out.println("================================================================================================================");
    }



   

    public ListInterface<MedicalTreatment> getAllTreatments() {
        return treatmentList;
    }
}
