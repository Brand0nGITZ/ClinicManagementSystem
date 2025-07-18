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


public class MyArrayList<T>  implements ListADT<T>  {
    
    private T[] data;
    private int size;
    private static final int DEFAULT_CAPACITY = 10;
    
    
    public MyArrayList() {
        data = (T[]) new Object[DEFAULT_CAPACITY];
        size = 0;
    }
    
    private void expand() { 
        T[] newData = (T[]) new Object[data.length * 2];
        for (int i = 0; i <size ; i++) {
        newData[i] = data[i];
        
    }
        data = newData;
    }
    
    @Override
    public void add(T item) {
        if (size == data.length) {
            expand(); //Expand the array size by twice if it's full
        }
        data[size++] = item; 
    }
    @Override
    public boolean remove(T item) {
        for (int i = 0; i < size; i++) {
            if (data[i].equals(item)) {
                
                for (int j = i; j < size - 1 ; j++) {
                    data[j] = data[j+1];
                }
                data[--size] = null;
                return true;
                
            }
        }
        return false;
    }
    
    @Override
    public T get(int index) {
        if (index >= 0 && index < size) {
            return data[index];
        }
        
        return null;
    }
 
    @Override
    public boolean contains (T item) {
        for (int i = 0 ; i< size; i++) {
            if (data[i].equals(item)){
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
    public void clear() {
        
        for (int i = 0; i< size; i++) {
            data[i] = null;
        }
        size = 0;
        
    }
    
    @Override
     public T findByName(String name) {
        for (int i = 0; i < size; i++) {
            T item = data[i];
            if (item.toString().toLowerCase().contains(name.toLowerCase())) {
                return item;
            }
        }
        return null;
    }
    
     @Override
      public void sortByStockDescending() {
        for (int i = 0; i < size - 1; i++) {
            for (int j = 0; j < size - i - 1; j++) {
                if (getStock(data[j]) < getStock(data[j + 1])) {
                    T temp = data[j];
                    data[j] = data[j + 1];
                    data[j + 1] = temp;
                }
            }
        }
    }

    private int getStock(T item) {
        try {
            return (int) item.getClass().getMethod("getStock").invoke(item);
        } catch (Exception e) {
            return 0; // fallback if item doesn't have getStock()
        }
    }

    
    @Override
    public boolean isEmpty() {
        return size == 0;
    }
    
    @Override
    public boolean isFull() {
        return size == data.length;
    }
    
    
}
