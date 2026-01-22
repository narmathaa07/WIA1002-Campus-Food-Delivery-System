/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.fooddelivery2;

/**
 *
 * @author ASUS
 */
// CustomHashMap.java
// CustomHashMap.java
// CustomHashMap.java


public class CustomHashMap<K, V> {
    public static class Entry<K, V> {
        public K key;
        public V value;
        public Entry<K, V> next;
        
        public Entry(K key, V value) {
            this.key = key;
            this.value = value;
            this.next = null;
        }
    }
    
    private Entry<K, V>[] table;
    private int size;
    
    @SuppressWarnings("unchecked")
    public CustomHashMap(int capacity) {
        table = new Entry[capacity];
        size = 0;
    }
    
    public void put(K key, V value) {
        int index = hash(key);
        Entry<K, V> newEntry = new Entry<>(key, value);
        
        if (table[index] == null) {
            table[index] = newEntry;
        } else {
            Entry<K, V> current = table[index];
            while (current.next != null) {
                if (current.key.equals(key)) {
                    current.value = value;
                    return;
                }
                current = current.next;
            }
            if (current.key.equals(key)) {
                current.value = value;
            } else {
                current.next = newEntry;
            }
        }
        size++;
    }
    
    public V get(K key) {
        int index = hash(key);
        Entry<K, V> current = table[index];
        
        while (current != null) {
            if (current.key.equals(key)) {
                return current.value;
            }
            current = current.next;
        }
        return null;
    }
    
    public boolean containsKey(K key) {
        return get(key) != null;
    }
    
    public void remove(K key) {
        int index = hash(key);
        
        if (table[index] == null) {
            return;
        }
        
        if (table[index].key.equals(key)) {
            table[index] = table[index].next;
            size--;
            return;
        }
        
        Entry<K, V> prev = table[index];
        Entry<K, V> current = prev.next;
        
        while (current != null) {
            if (current.key.equals(key)) {
                prev.next = current.next;
                size--;
                return;
            }
            prev = current;
            current = current.next;
        }
    }
    
    public int size() {
        return size;
    }
    
    @SuppressWarnings("unchecked")
    public Entry<K, V>[] entries() {
        Entry<K, V>[] allEntries = new Entry[size];
        int count = 0;
        
        for (int i = 0; i < table.length; i++) {
            Entry<K, V> current = table[i];
            while (current != null) {
                allEntries[count++] = current;
                current = current.next;
            }
        }
        
        return allEntries;
    }
    
    private int hash(K key) {
        if (key == null) return 0;
        return Math.abs(key.hashCode()) % table.length;
    }
    
    public void clear() {
        for (int i = 0; i < table.length; i++) {
            table[i] = null;
        }
        size = 0;
    }
    
    public boolean isEmpty() {
        return size == 0;
    }
}