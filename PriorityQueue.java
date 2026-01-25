/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.fooddelivery3;

/**
 *
 * @author ASUS
 */

public class PriorityQueue {
    private Order[] heap;
    private int size;
    private int capacity;
    
    public PriorityQueue(int capacity) {
        this.capacity = capacity;
        this.size = 0;
        this.heap = new Order[capacity];
    }
    
    public void enqueue(Order order) {
        if (size >= capacity) {
            resize();
        }
        
        heap[size] = order;
        size++;
        heapifyUp(size - 1);
    }
    
    public Order dequeue() {
        if (isEmpty()) {
            return null;
        }
        
        Order highestPriority = heap[0];
        heap[0] = heap[size - 1];
        size--;
        heapifyDown(0);
        
        return highestPriority;
    }
    
    public Order peek() {
        if (isEmpty()) {
            return null;
        }
        return heap[0];
    }
    
    public boolean isEmpty() {
        return size == 0;
    }
    
    public int size() {
        return size;
    }
    
    private void heapifyUp(int index) {
        int parent = (index - 1) / 2;
        
        while (index > 0 && compareOrders(heap[index], heap[parent]) > 0) {
            swap(index, parent);
            index = parent;
            parent = (index - 1) / 2;
        }
    }
    
    private void heapifyDown(int index) {
        int largest = index;
        int left = 2 * index + 1;
        int right = 2 * index + 2;
        
        if (left < size && compareOrders(heap[left], heap[largest]) > 0) {
            largest = left;
        }
        
        if (right < size && compareOrders(heap[right], heap[largest]) > 0) {
            largest = right;
        }
        
        if (largest != index) {
            swap(index, largest);
            heapifyDown(largest);
        }
    }
    
    private int compareOrders(Order a, Order b) {
        if (a == null || b == null) return 0;
        
        // Higher priority (1 > 2) comes first
        if (a.getPriority() != b.getPriority()) {
            return b.getPriority() - a.getPriority(); // Lower number = higher priority
        }
        // If same priority, earlier order time comes first
        return b.getOrderTime() - a.getOrderTime();
    }
    
    private void swap(int i, int j) {
        Order temp = heap[i];
        heap[i] = heap[j];
        heap[j] = temp;
    }
    
    private void resize() {
        capacity *= 2;
        Order[] newHeap = new Order[capacity];
        for (int i = 0; i < size; i++) {
            newHeap[i] = heap[i];
        }
        heap = newHeap;
    }
    
    public void display() {
        System.out.print("PriorityQueue: ");
        for (int i = 0; i < size; i++) {
            if (heap[i] != null) {
                System.out.print(heap[i].getOrderId() + "(" + heap[i].getPriority() + ") ");
            }
        }
        System.out.println();
    }
}