/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ADT;

import java.util.Random;

/**
 * Universal Reusable ArrayList - Can be used by ANY system
 * Banking, E-commerce, Education, Healthcare, Gaming, etc.
 * @author yapji
 * @param <T>
 */
public class MyArrayList<T> implements ListInterface<T> {
    
    // === CORE DATA STRUCTURES ===
    private T[] array;
    private int size;
    private static final int DEFAULT_CAPACITY = 10;
    
    // === STATISTICS TRACKING ===
    private T[] frequencyKeys;
    private int[] frequencyValues;
    private int frequencySize;
    private Random random;
    
    // === CONSTRUCTORS ===
    public MyArrayList() {
        this(DEFAULT_CAPACITY);
    }
    
    public MyArrayList(int initialCapacity) {
        array = (T[]) new Object[initialCapacity];
        size = 0;
        frequencyKeys = (T[]) new Object[initialCapacity];
        frequencyValues = new int[initialCapacity];
        frequencySize = 0;
        random = new Random();
    }
    
    // === UNIVERSAL BASIC OPERATIONS ===
    @Override
    public void add(T item) {
        validateItem(item);
        
        if (size == array.length) {
            expand();
        }
        array[size++] = item;
        updateFrequencyMap(item, 1);
    }
    
    @Override
    public boolean remove(T item) {
        for (int i = 0; i < size; i++) {
            if (array[i].equals(item)) {
                for (int j = i; j < size - 1; j++) {
                    array[j] = array[j + 1];
                }
                array[--size] = null;
                updateFrequencyMap(item, -1);
                return true;
            }
        }
        return false;
    }
    
    @Override
    public T get(int index) {
        if (isValidIndex(index)) {
            return array[index];
        }
        return null;
    }
    
    @Override
    public boolean contains(T item) {
        for (int i = 0; i < size; i++) {
            if (array[i].equals(item)) {
                return true;
            }
        }
        return false;
    }
    
    @Override
    public int size() {
        return size;
    }
    
    @Override
    public boolean isEmpty() {
        return size == 0;
    }
    
    @Override
    public boolean isFull() {
        return size == array.length;
    }
    
    @Override
    public void clear() {
        for (int i = 0; i < size; i++) {
            array[i] = null;
        }
        size = 0;
        frequencySize = 0;
    }
    
    // === UNIVERSAL SEARCH OPERATIONS ===
    @Override
    public T findFirst(Predicate<T> predicate) {
        for (int i = 0; i < size; i++) {
            if (predicate.test(array[i])) {
                return array[i];
            }
        }
        return null;
    }
    
    @Override
    public ListInterface<T> filter(Predicate<T> predicate) {
        MyArrayList<T> filtered = new MyArrayList<>();
        for (int i = 0; i < size; i++) {
            if (predicate.test(array[i])) {
                filtered.add(array[i]);
            }
        }
        return filtered;
    }
    
    @Override
    public ListInterface<T> searchByKeyword(String keyword) {
        return filter(item -> item.toString().toLowerCase().contains(keyword.toLowerCase()));
    }
    
    // === UNIVERSAL SORTING OPERATIONS ===
    @Override
    public void sort(Comparator<T> comparator) {
        for (int i = 0; i < size - 1; i++) {
            for (int j = 0; j < size - i - 1; j++) {
                if (comparator.compare(array[j], array[j + 1]) > 0) {
                    T temp = array[j];
                    array[j] = array[j + 1];
                    array[j + 1] = temp;
                }
            }
        }
    }
    
    @Override
    public void reverse() {
        for (int i = 0; i < size / 2; i++) {
            T temp = array[i];
            array[i] = array[size - 1 - i];
            array[size - 1 - i] = temp;
        }
    }
    
    // === UNIVERSAL STATISTICS OPERATIONS ===
    @Override
    public int getFrequency(T item) {
        for (int i = 0; i < frequencySize; i++) {
            if (frequencyKeys[i].equals(item)) {
                return frequencyValues[i];
            }
        }
        return 0;
    }
    
    @Override
    public T getMostFrequent() {
        T mostFrequent = null;
        int maxFreq = 0;
        
        for (int i = 0; i < frequencySize; i++) {
            if (frequencyValues[i] > maxFreq) {
                maxFreq = frequencyValues[i];
                mostFrequent = frequencyKeys[i];
            }
        }
        return mostFrequent;
    }
    
    @Override
    public T getLeastFrequent() {
        T leastFrequent = null;
        int minFreq = Integer.MAX_VALUE;
        
        for (int i = 0; i < frequencySize; i++) {
            if (frequencyValues[i] < minFreq) {
                minFreq = frequencyValues[i];
                leastFrequent = frequencyKeys[i];
            }
        }
        return leastFrequent;
    }
    
    @Override
    public double getAverageLength() {
        if (isEmpty()) return 0.0;
        
        int totalLength = 0;
        for (int i = 0; i < size; i++) {
            totalLength += array[i].toString().length();
        }
        return (double) totalLength / size;
    }
    
    @Override
    public int getUniqueCount() {
        return frequencySize;
    }
    
    // === UNIVERSAL UTILITY OPERATIONS ===
    @Override
    public T getRandom() {
        if (isEmpty()) return null;
        return array[random.nextInt(size)];
    }
    
    @Override
    public void shuffle() {
        for (int i = size - 1; i > 0; i--) {
            int j = random.nextInt(i + 1);
            T temp = array[i];
            array[i] = array[j];
            array[j] = temp;
        }
    }
    
    @Override
    public ListInterface<T> getRandomSample(int count) {
        MyArrayList<T> sample = new MyArrayList<>();
        int sampleSize = Math.min(count, size);
        
        for (int i = 0; i < sampleSize; i++) {
            T randomItem = getRandom();
            if (randomItem != null) {
                sample.add(randomItem);
            }
        }
        return sample;
    }
    
    @Override
    public ListInterface<T> clone() {
        MyArrayList<T> clone = new MyArrayList<>(array.length);
        for (int i = 0; i < size; i++) {
            clone.add(array[i]);
        }
        return clone;
    }
    
    @Override
    public void addAll(ListInterface<T> other) {
        for (int i = 0; i < other.size(); i++) {
            add(other.get(i));
        }
    }
    
    @Override
    public boolean equals(ListInterface<T> other) {
        if (other == null || this.size() != other.size()) {
            return false;
        }
        
        for (int i = 0; i < size; i++) {
            if (!array[i].equals(other.get(i))) {
                return false;
            }
        }
        return true;
    }
    
    // === UNIVERSAL VALIDATION OPERATIONS ===
    @Override
    public boolean isValidIndex(int index) {
        return index >= 0 && index < size;
    }
    
    @Override
    public void validateItem(T item) {
        if (item == null) {
            throw new IllegalArgumentException("Cannot add null item to list");
        }
    }
    
    @Override
    public boolean hasDuplicates() {
        for (int i = 0; i < size; i++) {
            for (int j = i + 1; j < size; j++) {
                if (array[i].equals(array[j])) {
                    return true;
                }
            }
        }
        return false;
    }
    
    // === PRIVATE HELPER METHODS ===
    private void expand() {
        T[] currentArray = array;
        T[] newArray = (T[]) new Object[array.length * 2];
        for (int i = 0; i < currentArray.length; i++) {
            newArray[i] = currentArray[i];
        }
        array = newArray;
    }
    
    private void updateFrequencyMap(T item, int change) {
        // Find existing item in frequency array
        for (int i = 0; i < frequencySize; i++) {
            if (frequencyKeys[i].equals(item)) {
                int newFreq = frequencyValues[i] + change;
                if (newFreq <= 0) {
                    // Remove item by shifting remaining elements
                    for (int j = i; j < frequencySize - 1; j++) {
                        frequencyKeys[j] = frequencyKeys[j + 1];
                        frequencyValues[j] = frequencyValues[j + 1];
                    }
                    frequencySize--;
                } else {
                    frequencyValues[i] = newFreq;
                }
                return;
            }
        }
        
        // Item not found, add it if change is positive
        if (change > 0) {
            if (frequencySize == frequencyKeys.length) {
                // Expand frequency arrays
                T[] newKeys = (T[]) new Object[frequencyKeys.length * 2];
                int[] newValues = new int[frequencyValues.length * 2];
                for (int i = 0; i < frequencySize; i++) {
                    newKeys[i] = frequencyKeys[i];
                    newValues[i] = frequencyValues[i];
                }
                frequencyKeys = newKeys;
                frequencyValues = newValues;
            }
            frequencyKeys[frequencySize] = item;
            frequencyValues[frequencySize] = change;
            frequencySize++;
        }
    }
    
    // === ENHANCED TOSTRING ===
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("MyArrayList[Size: ").append(size).append(", Capacity: ").append(array.length).append("]\n");
        
        for (int i = 0; i < size; i++) {
            sb.append(i + 1).append(". ").append(array[i]).append("\n");
        }
        
        if (!isEmpty()) {
            sb.append("\nStatistics:\n");
            sb.append("- Most Frequent: ").append(getMostFrequent()).append("\n");
            sb.append("- Unique Items: ").append(getUniqueCount()).append("\n");
            sb.append("- Average Length: ").append(String.format("%.2f", getAverageLength())).append("\n");
        }
        
        return sb.toString();
    }
}

