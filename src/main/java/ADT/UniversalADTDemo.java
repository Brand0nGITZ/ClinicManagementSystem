/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ADT;

import Entity.Medicine;
import Entity.Patient;

/**
 * Universal ADT Demonstration - Shows One-to-Many Reusability
 * Same ADT used across multiple systems and modules
 * @author yapji
 */
public class UniversalADTDemo {
    
    public static void main(String[] args) {
        System.out.println("🌍 UNIVERSAL ADT DEMONSTRATION");
        System.out.println("One-to-Many Reusability Across Systems");
        System.out.println("=".repeat(60));
        
        // === 1. HEALTHCARE SYSTEM (Your Clinic) ===
        demoHealthcareSystem();
        
        // === 2. BANKING SYSTEM ===
        demoBankingSystem();
        
        // === 3. E-COMMERCE SYSTEM ===
        demoEcommerceSystem();
        
        // === 4. EDUCATION SYSTEM ===
        demoEducationSystem();
        
        // === 5. GAMING SYSTEM ===
        demoGamingSystem();
        
        System.out.println("\n🎯 REUSABILITY PROVEN!");
        System.out.println("Same ADT works across 5 different systems!");
    }
    
    private static void demoHealthcareSystem() {
        System.out.println("\n🏥 1. HEALTHCARE SYSTEM (Your Clinic)");
        System.out.println("-".repeat(40));
        
        MyArrayList<Medicine> medicineList = new MyArrayList<>();
        
        // Add medicines
        medicineList.add(new Medicine("M001", "Paracetamol", "Painkiller", 50, "2025-12-31"));
        medicineList.add(new Medicine("M002", "Amoxicillin", "Antibiotic", 30, "2025-06-30"));
        medicineList.add(new Medicine("M003", "Ibuprofen", "Painkiller", 25, "2025-08-15"));
        
        // Universal search - find painkillers
        ListInterface<Medicine> painkillers = medicineList.filter(med -> med.getCategory().equals("Painkiller"));
        System.out.println("✅ Painkillers found: " + painkillers.size());
        
        // Universal sort - by stock descending
        medicineList.sort((med1, med2) -> Integer.compare(med2.getStock(), med1.getStock()));
        System.out.println("✅ Sorted by stock (highest first)");
        
        // Universal statistics
        System.out.println("✅ Most frequent category: " + medicineList.getMostFrequent());
        System.out.println("✅ Average medicine name length: " + String.format("%.2f", medicineList.getAverageLength()));
    }
    
    private static void demoBankingSystem() {
        System.out.println("\n🏦 2. BANKING SYSTEM");
        System.out.println("-".repeat(40));
        
        MyArrayList<BankAccount> accountList = new MyArrayList<>();
        
        // Add bank accounts
        accountList.add(new BankAccount("ACC001", "John Doe", 5000.0, "Savings"));
        accountList.add(new BankAccount("ACC002", "Jane Smith", 15000.0, "Current"));
        accountList.add(new BankAccount("ACC003", "Bob Johnson", 2500.0, "Savings"));
        accountList.add(new BankAccount("ACC004", "Alice Brown", 8000.0, "Current"));
        
        // Universal search - find high balance accounts
        ListInterface<BankAccount> highBalance = accountList.filter(acc -> acc.getBalance() > 5000);
        System.out.println("✅ High balance accounts (>$5000): " + highBalance.size());
        
        // Universal sort - by balance descending
        accountList.sort((acc1, acc2) -> Double.compare(acc2.getBalance(), acc1.getBalance()));
        System.out.println("✅ Sorted by balance (highest first)");
        
        // Universal statistics
        System.out.println("✅ Most common account type: " + accountList.getMostFrequent());
        System.out.println("✅ Unique account types: " + accountList.getUniqueCount());
    }
    
    private static void demoEcommerceSystem() {
        System.out.println("\n🛒 3. E-COMMERCE SYSTEM");
        System.out.println("-".repeat(40));
        
        MyArrayList<Product> productList = new MyArrayList<>();
        
        // Add products
        productList.add(new Product("P001", "Laptop", 999.99, "Electronics"));
        productList.add(new Product("P002", "T-Shirt", 19.99, "Clothing"));
        productList.add(new Product("P003", "Book", 12.99, "Books"));
        productList.add(new Product("P004", "Phone", 699.99, "Electronics"));
        productList.add(new Product("P005", "Jeans", 49.99, "Clothing"));
        
        // Universal search - find electronics
        ListInterface<Product> electronics = productList.filter(prod -> prod.getCategory().equals("Electronics"));
        System.out.println("✅ Electronics products: " + electronics.size());
        
        // Universal sort - by price ascending
        productList.sort((prod1, prod2) -> Double.compare(prod1.getPrice(), prod2.getPrice()));
        System.out.println("✅ Sorted by price (lowest first)");
        
        // Universal random - get random product for promotion
        Product randomProduct = productList.getRandom();
        System.out.println("✅ Random product for promotion: " + randomProduct.getName());
    }
    
    private static void demoEducationSystem() {
        System.out.println("\n🎓 4. EDUCATION SYSTEM");
        System.out.println("-".repeat(40));
        
        MyArrayList<Student> studentList = new MyArrayList<>();
        
        // Add students
        studentList.add(new Student("S001", "Alex Chen", "Computer Science", 3.8));
        studentList.add(new Student("S002", "Maria Garcia", "Mathematics", 3.9));
        studentList.add(new Student("S003", "David Kim", "Computer Science", 3.5));
        studentList.add(new Student("S004", "Sarah Wilson", "Physics", 3.7));
        studentList.add(new Student("S005", "Mike Brown", "Computer Science", 3.6));
        
        // Universal search - find CS students
        ListInterface<Student> csStudents = studentList.filter(student -> student.getMajor().equals("Computer Science"));
        System.out.println("✅ Computer Science students: " + csStudents.size());
        
        // Universal sort - by GPA descending
        studentList.sort((stud1, stud2) -> Double.compare(stud2.getGpa(), stud1.getGpa()));
        System.out.println("✅ Sorted by GPA (highest first)");
        
        // Universal statistics
        System.out.println("✅ Most common major: " + studentList.getMostFrequent());
        System.out.println("✅ Average GPA: " + String.format("%.2f", studentList.getAverageLength()));
    }
    
    private static void demoGamingSystem() {
        System.out.println("\n🎮 5. GAMING SYSTEM");
        System.out.println("-".repeat(40));
        
        MyArrayList<GameItem> itemList = new MyArrayList<>();
        
        // Add game items
        itemList.add(new GameItem("SWORD", "Iron Sword", 100, "Weapon"));
        itemList.add(new GameItem("POTION", "Health Potion", 50, "Consumable"));
        itemList.add(new GameItem("SHIELD", "Wooden Shield", 75, "Armor"));
        itemList.add(new GameItem("SCROLL", "Fire Scroll", 200, "Magic"));
        itemList.add(new GameItem("POTION", "Mana Potion", 50, "Consumable"));
        
        // Universal search - find weapons
        ListInterface<GameItem> weapons = itemList.filter(item -> item.getType().equals("Weapon"));
        System.out.println("✅ Weapons found: " + weapons.size());
        
        // Universal sort - by value descending
        itemList.sort((item1, item2) -> Integer.compare(item2.getValue(), item1.getValue()));
        System.out.println("✅ Sorted by value (highest first)");
        
        // Universal shuffle - randomize inventory
        itemList.shuffle();
        System.out.println("✅ Inventory shuffled for random encounter");
        
        // Universal statistics
        System.out.println("✅ Most common item: " + itemList.getMostFrequent());
        System.out.println("✅ Has duplicates: " + itemList.hasDuplicates());
    }
    
    // === SAMPLE ENTITY CLASSES FOR DIFFERENT SYSTEMS ===
    
    static class BankAccount {
        private String accountId, accountHolder, accountType;
        private double balance;
        
        public BankAccount(String id, String holder, double bal, String type) {
            this.accountId = id;
            this.accountHolder = holder;
            this.balance = bal;
            this.accountType = type;
        }
        
        public double getBalance() { return balance; }
        public String getAccountType() { return accountType; }
        
        @Override
        public String toString() {
            return accountId + " - " + accountHolder + " ($" + balance + ")";
        }
        
        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (!(obj instanceof BankAccount)) return false;
            BankAccount other = (BankAccount) obj;
            return this.accountId.equals(other.accountId);
        }
        
        @Override
        public int hashCode() {
            return accountId.hashCode();
        }
    }
    
    static class Product {
        private String productId, name, category;
        private double price;
        
        public Product(String id, String name, double price, String category) {
            this.productId = id;
            this.name = name;
            this.price = price;
            this.category = category;
        }
        
        public String getName() { return name; }
        public double getPrice() { return price; }
        public String getCategory() { return category; }
        
        @Override
        public String toString() {
            return name + " - $" + price + " (" + category + ")";
        }
        
        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (!(obj instanceof Product)) return false;
            Product other = (Product) obj;
            return this.productId.equals(other.productId);
        }
        
        @Override
        public int hashCode() {
            return productId.hashCode();
        }
    }
    
    static class Student {
        private String studentId, name, major;
        private double gpa;
        
        public Student(String id, String name, String major, double gpa) {
            this.studentId = id;
            this.name = name;
            this.major = major;
            this.gpa = gpa;
        }
        
        public String getMajor() { return major; }
        public double getGpa() { return gpa; }
        
        @Override
        public String toString() {
            return name + " - " + major + " (GPA: " + gpa + ")";
        }
        
        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (!(obj instanceof Student)) return false;
            Student other = (Student) obj;
            return this.studentId.equals(other.studentId);
        }
        
        @Override
        public int hashCode() {
            return studentId.hashCode();
        }
    }
    
    static class GameItem {
        private String itemId, name, type;
        private int value;
        
        public GameItem(String id, String name, int value, String type) {
            this.itemId = id;
            this.name = name;
            this.value = value;
            this.type = type;
        }
        
        public String getName() { return name; }
        public int getValue() { return value; }
        public String getType() { return type; }
        
        @Override
        public String toString() {
            return name + " - " + value + " gold (" + type + ")";
        }
        
        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (!(obj instanceof GameItem)) return false;
            GameItem other = (GameItem) obj;
            return this.itemId.equals(other.itemId);
        }
        
        @Override
        public int hashCode() {
            return itemId.hashCode();
        }
    }
}

