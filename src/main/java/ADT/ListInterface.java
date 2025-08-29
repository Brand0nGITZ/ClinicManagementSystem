/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ADT;

/**
 * @author yapjinkai
 * @param <T>
 */
public interface ListInterface <T> {
    
    /**
     * Adds the specified item to the end of the list.
     * 
     * @param item the item to add to the list
     * @throws IllegalArgumentException if item is null
     */
    void add(T item);
    
    /**
     * Removes the first occurrence of the specified item from the list.
     * 
     * @param item the item to remove from the list
     * @return true if item was found and removed, false otherwise
     * @throws IllegalArgumentException if item is null
     */
    boolean remove(T item);
    
    /**
     * Retrieves the item at the specified index position in the list.
     * 
     * @param index the index of the item to retrieve
     * @return the item at the specified index
     * @throws IndexOutOfBoundsException if index is out of bounds
     */
    T get(int index);
    
    /**
     * Checks if the specified item exists in the list.
     * 
     * @param item the item to search for
     * @return true if item exists in list, false otherwise
     * @throws IllegalArgumentException if item is null
     */
    boolean contains(T item);
    
    /**
     * Returns the current number of items in the list.
     * 
     * @return the number of items currently in the list (>= 0)
     */
    int size();
    
    /**
     * Checks if the list contains no items.
     * 
     * @return true if list has no items (size == 0), false otherwise
     */
    boolean isEmpty();
    
    /**
     * Checks if the list has reached its maximum capacity.
     * 
     * @return true if list is at maximum capacity, false otherwise
     */
    boolean isFull();
    
    /**
     * Removes all items from the list, making it empty.
     */
    void clear();
    
    /**
     * Finds the first item in the list that satisfies the given predicate condition.
     * 
     * @param predicate the condition to test against each item
     * @return the first item that satisfies the predicate, or null if no such item exists
     * @throws IllegalArgumentException if predicate is null
     */
    T findFirst(Predicate<T> predicate);
    
    /**
     * Creates a new list containing only the items that satisfy the given predicate condition.
     * 
     * @param predicate the condition to test against each item
     * @return a new list containing items that satisfy the predicate
     * @throws IllegalArgumentException if predicate is null
     */
    ListInterface<T> filter(Predicate<T> predicate);
    
    /**
     * Searches for items whose toString() representation contains the specified keyword.
     * 
     * @param keyword the keyword to search for
     * @return a new list containing items whose toString() contains the keyword
     * @throws IllegalArgumentException if keyword is null
     */
    ListInterface<T> searchByKeyword(String keyword);
    
    /**
     * Sorts the list according to the specified comparator.
     * 
     * @param comparator the comparator to use for sorting
     * @throws IllegalArgumentException if comparator is null
     */
    void sort(Comparator<T> comparator);
    
    /**
     * Reverses the order of all items in the list.
     */
    void reverse();
    
    /**
     * Returns the item that appears most frequently in the list.
     * 
     * @return the item that appears most frequently, or null if list is empty
     */
    T getMostFrequent();
    
    /**
     * Calculates the average length of toString() representations of all items in the list.
     * 
     * @return the average length of toString() representations, or 0.0 if list is empty
     */
    double getAverageLength();
    
    /**
     * Counts the number of unique items in the list.
     * 
     * @return the number of unique items in the list (>= 0)
     */
    int getUniqueCount();
    
    /**
     * Checks if the given index is valid for accessing items in the list.
     * 
     * @param index the index to validate
     * @return true if index is valid (0 <= index < size), false otherwise
     */
    boolean isValidIndex(int index);
    
    /**
     * Validates that an item is suitable for addition to the list.
     * 
     * @param item the item to validate
     * @throws IllegalArgumentException if item is invalid
     */
    void validateItem(T item);
    
    /**
     * Checks if the list contains any duplicate items.
     * 
     * @return true if list contains duplicates, false otherwise
     */
    boolean hasDuplicates();
    
    /**
     * Returns an iterator for forward iteration through the list.
     * 
     * @return an iterator positioned at the beginning of the list
     */
    MyIterator iterator();
    
    /**
     * Returns a list iterator for bidirectional iteration through the list.
     * 
     * @return a list iterator positioned at the beginning of the list
     */
    MyListIterator listIterator();
    
    /**
     * Returns a list iterator positioned at the specified index for bidirectional iteration.
     * 
     * @param index the starting position for the iterator
     * @return a list iterator positioned at the specified index
     * @throws IndexOutOfBoundsException if index is out of bounds
     */
    MyListIterator listIterator(int index);
    
    /**
     * Functional interface for predicate operations
     * Used for filtering and searching operations
     */
    @FunctionalInterface
    interface Predicate<T> {
        /**
         * Tests if the given item satisfies a condition
         * @param item the item to test
         * @return true if item satisfies the condition, false otherwise
         */
        boolean test(T item);
    }
    
    /**
     * Functional interface for comparison operations
     * Used for sorting operations
     */
    @FunctionalInterface
    interface Comparator<T> {
        /**
         * Compares two items
         * @param item1 the first item to compare
         * @param item2 the second item to compare
         * @return negative if item1 < item2, 0 if equal, positive if item1 > item2
         */
        int compare(T item1, T item2);
    }
}
