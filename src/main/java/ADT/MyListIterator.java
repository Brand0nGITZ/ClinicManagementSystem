package ADT;

/**
 * Custom ListIterator interface for the MyArrayList ADT
 * Extends MyIterator to provide bidirectional iteration capabilities
 * 
 * @param <T> The type of elements in the collection
 */
public interface MyListIterator<T> extends MyIterator<T> {
    
    /**
     * Returns true if the iteration has more elements when traversing forward
     * @return true if the iterator has more elements when traversing forward
     */
    boolean hasNext();
    
    /**
     * Returns the next element in the iteration
     * @return the next element
     * @throws IllegalStateException if there are no more elements
     */
    T next();
    
    /**
     * Returns true if the iteration has more elements when traversing backward
     * @return true if the iterator has more elements when traversing backward
     */
    boolean hasPrevious();
    
    /**
     * Returns the previous element in the iteration
     * @return the previous element
     * @throws IllegalStateException if there are no more elements
     */
    T previous();
    
    /**
     * Returns the index of the element that would be returned by a subsequent call to next()
     * @return the index of the next element
     */
    int nextIndex();
    
    /**
     * Returns the index of the element that would be returned by a subsequent call to previous()
     * @return the index of the previous element
     */
    int previousIndex();
    
    /**
     * Replaces the last element returned by next() or previous() with the specified element
     * @param element the element to replace the last returned element
     * @throws IllegalStateException if neither next() nor previous() have been called
     */
    void set(T element);
    
    /**
     * Inserts the specified element into the list at the current position
     * @param element the element to insert
     */
    void add(T element);
    
    /**
     * Removes the last element returned by next() or previous() from the underlying collection
     * @throws IllegalStateException if neither next() nor previous() have been called
     */
    void remove();
}




