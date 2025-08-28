package ADT;

/**
 * Custom Iterator interface for the MyArrayList ADT
 * Provides methods for traversing collections
 * 
 * @param <T> The type of elements in the collection
 */
public interface MyIterator<T> {
    
    /**
     * Returns true if the iteration has more elements
     * @return true if the iterator has more elements, false otherwise
     */
    boolean hasNext();
    
    /**
     * Returns the next element in the iteration
     * @return the next element
     * @throws IllegalStateException if there are no more elements
     */
    T next();
    
    /**
     * Removes the last element returned by next() from the underlying collection
     * @throws IllegalStateException if next() has not been called yet
     */
    void remove();
}

