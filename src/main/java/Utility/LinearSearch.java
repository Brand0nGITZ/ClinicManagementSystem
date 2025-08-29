package Utility;

import ADT.ListInterface;
import ADT.MyArrayList;
import ADT.MyIterator;

/**
 * Linear Search Utility for Universal Search
 * Can be used across all modules: Consultation, Medical Treatment, Pharmacy
 * 
 * @author yapjinkai
 */
public class LinearSearch {
    
    /**
     * Search result with match information
     */
    public static class SearchResult<T> {
        public final T item;
        public final int index;
        public final String matchedField;
        
        public SearchResult(T item, int index, String matchedField) {
            this.item = item;
            this.index = index;
            this.matchedField = matchedField;
        }
        
        @Override
        public String toString() {
            return String.format("%s (index: %d, field: %s)", item.toString(), index, matchedField);
        }
    }
    
    /**
     * Linear search for exact matches
     * @param query search query
     * @param items list of items to search
     * @return list of search results
     */
    public static <T> MyArrayList<SearchResult<T>> search(String query, ListInterface<T> items) {
        MyArrayList<SearchResult<T>> results = new MyArrayList<>();
        
        if (query == null || query.trim().isEmpty() || items == null || items.isEmpty()) {
            return results;
        }
        
        query = query.toLowerCase().trim();
        
        
        MyIterator<T> iterator = items.iterator();
        int index = 0;
        
        while (iterator.hasNext()) {
            T item = iterator.next();
            if (item != null) {
                // Check if item contains the query in its string representation
                String itemText = item.toString().toLowerCase();
                if (itemText.contains(query)) {
                    results.add(new SearchResult<>(item, index, "toString"));
                }
            }
            index++;
        }
        
        return results;
    }
    
    /**
     * Linear search with case-insensitive matching
     * @param query search query
     * @param items list of items to search
     * @param caseSensitive whether to perform case-sensitive search
     * @return list of search results
     */
    public static <T> MyArrayList<SearchResult<T>> search(String query, ListInterface<T> items, boolean caseSensitive) {
        MyArrayList<SearchResult<T>> results = new MyArrayList<>();
        
        if (query == null || query.trim().isEmpty() || items == null || items.isEmpty()) {
            return results;
        }
        
        String searchQuery = caseSensitive ? query.trim() : query.toLowerCase().trim();
        
        // Use custom iterator for linear search
        MyIterator<T> iterator = items.iterator();
        int index = 0;
        
        while (iterator.hasNext()) {
            T item = iterator.next();
            if (item != null) {
                String itemText = caseSensitive ? item.toString() : item.toString().toLowerCase();
                if (itemText.contains(searchQuery)) {
                    results.add(new SearchResult<>(item, index, "toString"));
                }
            }
            index++;
        }
        
        return results;
    }
    
    /**
     * Linear search for partial matches (starts with)
     * @param query search query
     * @param items list of items to search
     * @return list of search results
     */
    public static <T> MyArrayList<SearchResult<T>> searchStartsWith(String query, ListInterface<T> items) {
        MyArrayList<SearchResult<T>> results = new MyArrayList<>();
        
        if (query == null || query.trim().isEmpty() || items == null || items.isEmpty()) {
            return results;
        }
        
        query = query.toLowerCase().trim();
        
        // Use custom iterator for linear search
        MyIterator<T> iterator = items.iterator();
        int index = 0;
        
        while (iterator.hasNext()) {
            T item = iterator.next();
            if (item != null) {
                String itemText = item.toString().toLowerCase();
                if (itemText.startsWith(query)) {
                    results.add(new SearchResult<>(item, index, "startsWith"));
                }
            }
            index++;
        }
        
        return results;
    }
    
    /**
     * Linear search for exact matches only
     * @param query search query
     * @param items list of items to search
     * @return list of search results
     */
    public static <T> MyArrayList<SearchResult<T>> searchExact(String query, ListInterface<T> items) {
        MyArrayList<SearchResult<T>> results = new MyArrayList<>();
        
        if (query == null || query.trim().isEmpty() || items == null || items.isEmpty()) {
            return results;
        }
        
        query = query.trim();
        
        // Use custom iterator for linear search
        MyIterator<T> iterator = items.iterator();
        int index = 0;
        
        while (iterator.hasNext()) {
            T item = iterator.next();
            if (item != null) {
                String itemText = item.toString();
                if (itemText.equals(query)) {
                    results.add(new SearchResult<>(item, index, "exact"));
                }
            }
            index++;
        }
        
        return results;
    }
    
    /**
     * Linear search with multiple search terms (OR logic)
     * @param queries array of search queries
     * @param items list of items to search
     * @return list of search results
     */
    public static <T> MyArrayList<SearchResult<T>> searchMultiple(String[] queries, ListInterface<T> items) {
        MyArrayList<SearchResult<T>> results = new MyArrayList<>();
        
        if (queries == null || queries.length == 0 || items == null || items.isEmpty()) {
            return results;
        }
        
    
        MyIterator<T> iterator = items.iterator();
        int index = 0;
        
        while (iterator.hasNext()) {
            T item = iterator.next();
            if (item != null) {
                String itemText = item.toString().toLowerCase();
                
                // Check if item matches any of the queries
                for (String query : queries) {
                    if (query != null && !query.trim().isEmpty()) {
                        String searchQuery = query.toLowerCase().trim();
                        if (itemText.contains(searchQuery)) {
                            results.add(new SearchResult<>(item, index, "multiple: " + query));
                            break; // Only add once per item
                        }
                    }
                }
            }
            index++;
        }
        
        return results;
    }
    
    /**
     * Linear search with field-specific matching
     * @param query search query
     * @param items list of items to search
     * @param fieldExtractor function to extract specific field from item
     * @return list of search results
     */
    public static <T> MyArrayList<SearchResult<T>> searchByField(String query, ListInterface<T> items, FieldExtractor<T> fieldExtractor) {
        MyArrayList<SearchResult<T>> results = new MyArrayList<>();
        
        if (query == null || query.trim().isEmpty() || items == null || items.isEmpty()) {
            return results;
        }
        
        query = query.toLowerCase().trim();
        
      
        MyIterator<T> iterator = items.iterator();
        int index = 0;
        
        while (iterator.hasNext()) {
            T item = iterator.next();
            if (item != null) {
                String fieldValue = fieldExtractor.extract(item);
                if (fieldValue != null && fieldValue.toLowerCase().contains(query)) {
                    results.add(new SearchResult<>(item, index, fieldExtractor.getFieldName()));
                }
            }
            index++;
        }
        
        return results;
    }
    
    /**
     * Field extractor interface for field-specific search
     */
    public interface FieldExtractor<T> {
        /**
         * Extract a field value from an item
         * @param item the item to extract from
         * @return the field value as string
         */
        String extract(T item);
        
        /**
         * Get the name of this field
         * @return field name
         */
        String getFieldName();
    }
    
    /**
     * Print search results in a formatted way
     */
    public static <T> void printSearchResults(String query, MyArrayList<SearchResult<T>> results) {
        System.out.println("\n=== Linear Search Results for: \"" + query + "\" ===");
        
        if (results.isEmpty()) {
            System.out.println("No matches found.");
            return;
        }
        
        System.out.println("Found " + results.size() + " match(es):");
        System.out.println("-".repeat(60));
        
        for (int i = 0; i < results.size(); i++) {
            SearchResult<T> result = results.get(i);
            System.out.printf("%d. %s\n", i + 1, result.toString());
        }
    }
    
    /**
     * Print search results with custom formatter
     */
    public static <T> void printSearchResults(String query, MyArrayList<SearchResult<T>> results, ResultFormatter<T> formatter) {
        System.out.println("\n=== Linear Search Results for: \"" + query + "\" ===");
        
        if (results.isEmpty()) {
            System.out.println("No matches found.");
            return;
        }
        
        System.out.println("Found " + results.size() + " match(es):");
        System.out.println("-".repeat(60));
        
        for (int i = 0; i < results.size(); i++) {
            SearchResult<T> result = results.get(i);
            System.out.printf("%d. %s\n", i + 1, formatter.format(result));
        }
    }
    
    /**
     * Result formatter interface for custom output formatting
     */
    public interface ResultFormatter<T> {
        /**
         * Format a search result for display
         * @param result the search result to format
         * @return formatted string
         */
        String format(SearchResult<T> result);
    }
}




