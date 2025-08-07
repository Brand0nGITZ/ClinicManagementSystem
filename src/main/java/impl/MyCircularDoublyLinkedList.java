/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package impl;
import ADT.ListADT;
import Entity.Medicine;

/**
 *
 * @author yapji
 * @param <T>
 */

public class MyCircularDoublyLinkedList<T> implements ListADT<T> {
    private Node<T> head;
    private int size;
    
    public MyCircularDoublyLinkedList() {
        head = null;
        size = 0;
    }
    
    @Override
    public void add(T item) {
        Node<T> newNode = new Node<>(item);
        
        if (head == null) {
            head = newNode;  
      } else {
            Node<T> tail = head.prev;
            tail.next = newNode;
            newNode.prev = tail;
            newNode.next = head;
            head.prev = newNode;           
        }
        size++;
    }
    
     @Override
    public boolean remove(T item) {
        if (head == null) return false;
        Node<T> current = head;
        int i = 0;
        do {
            if (current.data.equals(item)) {
                if (size == 1) {
                    head = null;
                } else {
                    current.prev.next = current.next;
                    current.next.prev = current.prev;
                    if (current == head) {
                        head = current.next;
                    }
                }
                size--;
                return true;
            }
            current = current.next;
            i++;
        } while (current != head && i < size);
        return false;
    }
    
    @Override
    public T get(int index) {
        if (index < 0 || index >= size) throw new IndexOutOfBoundsException();
        Node<T> current = head;
        for (int i = 0; i < index; i++) current = current.next;
        return current.data;
    }
    
    @Override
    public boolean contains(T item) {
        if (head == null) return false;
        Node<T> current = head;
        int i = 0;
        do {
            if (current.data.equals(item)) return true;
            current = current.next;
            i++;
        } while (current != head && i < size);
        return false;
    }
    
    @Override
    public int size() { return size; }

    @Override
    public boolean isEmpty() { return size == 0; }

    @Override
    public void clear() {
        head = null;
        size = 0;
    }

    @Override
    public void sortByStockDescending() {
        if (size <= 1 || !(head.data instanceof Medicine)) return;

        for (int i = 0; i < size - 1; i++) {
            Node<T> current = head;
            for (int j = 0; j < size - 1; j++) {
                Medicine m1 = (Medicine) current.data;
                Medicine m2 = (Medicine) current.next.data;
                if (m1.getStock() < m2.getStock()) {
                    T temp = current.data;
                    current.data = current.next.data;
                    current.next.data = temp;
                }
                current = current.next;
            }
        }
    }
    
     private static class Node<T> {
        T data;
        Node<T> next;
        Node<T> prev;
        
        Node(T data) {
            this.data = data;
            this.next = null;
            this.prev = null;
        }
    }
    
}
