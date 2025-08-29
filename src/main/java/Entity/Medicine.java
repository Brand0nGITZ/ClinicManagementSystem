/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Entity;

/**
 *
 * @author yapji
 */
public class Medicine {
    private String medicineID;
    private String name;
    private String category;
    private int stock;
    private String expiryDate; // 

    public Medicine(String medicineID, String name, String category, int stock, String expiryDate) {
        this.medicineID = medicineID;
        this.name = name;
        this.category = category;
        this.stock = stock;
        this.expiryDate = expiryDate;
    }

    // Getters
    public String getMedicineID() {
        return medicineID;
    }

    public String getName() {
        return name;
    }

    public String getCategory() {
        return category;
    }

    public int getStock() {
        return stock;
    }

    public String getExpiryDate() {
        return expiryDate;
    }

    // Setters
    public void setStock(int stock) {
        this.stock = stock;
    }

    public void reduceStock(int quantity) {
        if (quantity <= stock) {
            stock -= quantity;
        }
    }

    @Override
    public String toString() {
        return String.format("ID: %s | Name: %s | Category: %s | Stock: %d | Expiry: %s",
                medicineID, name, category, stock, expiryDate);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Medicine)) return false;
        Medicine other = (Medicine) obj;
        return this.medicineID.equalsIgnoreCase(other.medicineID);
    }
}