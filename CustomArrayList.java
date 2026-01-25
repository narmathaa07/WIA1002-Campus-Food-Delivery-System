/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.fooddelivery3;

/**
 *
 * @author ASUS
 */

public class CustomArrayList<E> {
    private Object[] elements;
    private int size;
    private int capacity;
    
    public CustomArrayList(int initialCapacity) {
        capacity = initialCapacity;
        size = 0;
        elements = new Object[capacity];
    }
    
    public void add(E element) {
        if (size >= capacity) {
            resize();
        }
        elements[size] = element;
        size++;
    }
    
    @SuppressWarnings("unchecked")
    public E get(int index) {
        if (index < 0 || index >= size) {
            return null;
        }
        return (E) elements[index];
    }
    
    public void set(int index, E element) {
        if (index >= 0 && index < size) {
            elements[index] = element;
        }
    }
    
    public void remove(int index) {
        if (index < 0 || index >= size) {
            return;
        }
        
        for (int i = index; i < size - 1; i++) {
            elements[i] = elements[i + 1];
        }
        elements[size - 1] = null;
        size--;
    }
    
    public int size() {
        return size;
    }
    
    public boolean isEmpty() {
        return size == 0;
    }
    
    private void resize() {
        capacity *= 2;
        Object[] newElements = new Object[capacity];
        for (int i = 0; i < size; i++) {
            newElements[i] = elements[i];
        }
        elements = newElements;
    }
    
    public void insertAt(int index, E element) {
        if (size >= capacity) {
            resize();
        }
        
        for (int i = size; i > index; i--) {
            elements[i] = elements[i - 1];
        }
        elements[index] = element;
        size++;
    }
    
    public boolean contains(E element) {
        for (int i = 0; i < size; i++) {
            if (elements[i] != null && elements[i].equals(element)) {
                return true;
            }
        }
        return false;
    }
    
    public void clear() {
        for (int i = 0; i < size; i++) {
            elements[i] = null;
        }
        size = 0;
    }

}
