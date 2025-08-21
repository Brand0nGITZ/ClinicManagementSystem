/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Utility;
import Entity.Patient;
import java.util.Random;
/**
 *
 * @author yapji
 */
public class patientGenerator {
    
     private static final String[] FIRST_NAMES = {
        "Alex", "Jordan", "Taylor", "Sam", "Casey", "Jamie", "Morgan", "Dylan"
    };

    private static final String[] LAST_NAMES = {
        "Lee", "Smith", "Ng", "Tan", "Kumar", "Lim", "Chong", "Ali"
    };

    private static int patientCount = 1;
    private static Random rand = new Random();

    public static Patient generatePatient() {
        String first = FIRST_NAMES[rand.nextInt(FIRST_NAMES.length)];
        String last = LAST_NAMES[rand.nextInt(LAST_NAMES.length)];
        String name = first + " " + last;
        String id = "P" + String.format("%03d", patientCount++);
        return new Patient(id, name);
    }
    
    
    public static void main(String[] args) {
        System.out.println(generatePatient());
    }
    
}
