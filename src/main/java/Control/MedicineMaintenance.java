/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Control;
import Entity.Medicine;
import ADT.ListADT;
import ADT.MyArrayList;
/**
 *
 * @author yapji
 */
public class MedicineMaintenance {
    private final MyArrayList<Medicine> medicineList;

    public MedicineMaintenance(MyArrayList<Medicine> medicineList) {
        this.medicineList = medicineList;
    }

    public void addMedicine(Medicine med) {
        medicineList.add(med);
    }

    public boolean removeMedicine(String id) {
        for (int i = 0; i < medicineList.size(); i++) {
            Medicine med = medicineList.get(i);
            if (med.getMedicineID().equalsIgnoreCase(id)) {
                return medicineList.remove(med);
            }
        }
        return false;
    }

    public boolean dispenseMedicine(String id, int quantity) {
        for (int i = 0; i < medicineList.size(); i++) {
            Medicine med = medicineList.get(i);
            if (med.getMedicineID().equalsIgnoreCase(id)) {
                if (med.getStock() >= quantity) {
                    med.reduceStock(quantity);
                    return true;
                }
                return false;
            }
        }
        return false;
    }

    public Medicine findByName(String name) {
        for (int i = 0; i < medicineList.size(); i++) {
            Medicine med = medicineList.get(i);
            if (med.getName().equalsIgnoreCase(name)) {
                return med;
            }
        }
        return null;
    }

    public void sortMedicinesByStock() {
        medicineList.sortByStockDescending();
    }

    public void generateLowStockReport(int threshold) {
        System.out.println("=== Low Stock Report ===");
        boolean found = false;
        for (int i = 0; i < medicineList.size(); i++) {
            Medicine med = medicineList.get(i);
            if (med.getStock() <= threshold) {
                System.out.println(med);
                found = true;
            }
        }
        if (!found) {
            System.out.println("✅ All medicine stock is above threshold.");
        }
    }

    public MyArrayList<Medicine> getAllMedicines() {
        return medicineList;
    }
}