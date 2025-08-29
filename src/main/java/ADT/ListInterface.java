/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ADT;

/**
 * Universal Reusable List Interface
 * Can be used by ANY system: Banking, E-commerce, Education, Healthcare, etc.
 * @author yapji
 * @param <T>
 */
public interface ListInterface <T> {
    
    
    void add(T item);                     // Add any item
    boolean remove(T item);              // Remove by value
    T get(int index);                    // Get item at index
    boolean contains(T item);            // Check if value exists
    int size();                          // Get current size
    boolean isEmpty();                   // True if list has no elements
    boolean isFull();                    // True if list is at capacity
    void clear();                        // Remove all elements from list
    
    
    T findFirst(Predicate<T> predicate); // Find first item matching condition
    ListInterface<T> filter(Predicate<T> predicate); // Filter items by condition
    ListInterface<T> searchByKeyword(String keyword); // Search by keyword in toString()
    
    
    void sort(Comparator<T> comparator); // Sort with custom comparator
    void reverse();                      // Reverse the list order
    
    
    T getMostFrequent();                 // Get most common item
    double getAverageLength();           // Average toString() length
    int getUniqueCount();                // Count unique items
        
    
    boolean isValidIndex(int index);     // Check if index is valid
    void validateItem(T item);           // Validate item before adding
    boolean hasDuplicates();             // Check for duplicate items
    
    
    MyIterator iterator();            // Get iterator for forward iteration
    MyListIterator listIterator();    // Get list iterator for bidirectional iteration
    MyListIterator listIterator(int index); // Get list iterator starting from index
    
    
    @FunctionalInterface
    interface Predicate<T> {
        boolean test(T item);
    }
    
    @FunctionalInterface
    interface Comparator<T> {
        int compare(T item1, T item2);
    }
}
