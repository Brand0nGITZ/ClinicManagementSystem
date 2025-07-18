/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ADT;

/**
 *
 * @author yapji
 * @param <T>
 */
public interface ListADT <T> {
    
    void add(T item);                     // Add medicine
    boolean remove(T item);              // Remove by value
    T get(int index);                    // Get item at index
    boolean contains(T item);            // Check if value exists
    int size();                          // Get current size
    boolean isEmpty();  // True if list has no elements
    boolean isFull(); 
    void clear();     
    T findByName(String name);           // Search item by name (for Pharmacy)
    void sortByStockDescending();
    
}
