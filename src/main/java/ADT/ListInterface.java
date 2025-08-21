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
    
    // === UNIVERSAL BASIC OPERATIONS ===
    void add(T item);                     // Add any item
    boolean remove(T item);              // Remove by value
    T get(int index);                    // Get item at index
    boolean contains(T item);            // Check if value exists
    int size();                          // Get current size
    boolean isEmpty();                   // True if list has no elements
    boolean isFull(); 
    void clear();
    
    // === UNIVERSAL SEARCH OPERATIONS ===
    T findFirst(Predicate<T> predicate); // Find first item matching condition
    ListInterface<T> filter(Predicate<T> predicate); // Filter items by condition
    ListInterface<T> searchByKeyword(String keyword); // Search by keyword in toString()
    
    // === UNIVERSAL SORTING OPERATIONS ===
    void sort(Comparator<T> comparator); // Sort with custom comparator
    void reverse();                      // Reverse the list order
    
    // === UNIVERSAL STATISTICS OPERATIONS ===
    int getFrequency(T item);            // Count occurrences of item
    T getMostFrequent();                 // Get most common item
    T getLeastFrequent();                // Get least common item
    double getAverageLength();           // Average toString() length
    int getUniqueCount();                // Count unique items
    
    // === UNIVERSAL UTILITY OPERATIONS ===
    T getRandom();                       // Get random item
    void shuffle();                      // Shuffle the list
    ListInterface<T> getRandomSample(int count); // Get random sample
    ListInterface<T> clone();            // Create a copy
    void addAll(ListInterface<T> other); // Add all items from another list
    boolean equals(ListInterface<T> other); // Compare with another list
    
    // === UNIVERSAL VALIDATION OPERATIONS ===
    boolean isValidIndex(int index);     // Check if index is valid
    void validateItem(T item);           // Validate item before adding
    boolean hasDuplicates();             // Check for duplicate items
    
    // === FUNCTIONAL INTERFACES FOR UNIVERSAL USE ===
    @FunctionalInterface
    interface Predicate<T> {
        boolean test(T item);
    }
    
    @FunctionalInterface
    interface Comparator<T> {
        int compare(T item1, T item2);
    }
}
