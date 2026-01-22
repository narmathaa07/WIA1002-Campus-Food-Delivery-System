/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.fooddelivery2;

/**
 *
 * @author ASUS
 */
// CustomQueue.java


public class CustomQueue<E> {
    private Object[] elements;
    private int front;
    private int rear;
    private int size;
    private int capacity;
    
    public CustomQueue(int capacity) {
        this.capacity = capacity;
        elements = new Object[capacity];
        front = 0;
        rear = -1;
        size = 0;
    }
    
    public void enqueue(E element) {
        if (isFull()) {
            return;
        }
        rear = (rear + 1) % capacity;
        elements[rear] = element;
        size++;
    }
    
    @SuppressWarnings("unchecked")
    public E dequeue() {
        if (isEmpty()) {
            return null;
        }
        E element = (E) elements[front];
        front = (front + 1) % capacity;
        size--;
        return element;
    }
    
    @SuppressWarnings("unchecked")
    public E peek() {
        if (isEmpty()) {
            return null;
        }
        return (E) elements[front];
    }
    
    public boolean isEmpty() {
        return size == 0;
    }
    
    public boolean isFull() {
        return size == capacity;
    }
    
    public int size() {
        return size;
    }
    
    public void clear() {
        for (int i = 0; i < capacity; i++) {
            elements[i] = null;
        }
        front = 0;
        rear = -1;
        size = 0;
    }
}