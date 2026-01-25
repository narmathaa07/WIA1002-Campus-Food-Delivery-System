/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.fooddelivery3;

/**
 *
 * @author ASUS
 */



public class Order {
    private String orderId;
    private String studentName;
    private String studentEmail;  // NEW FIELD
    private String pickupLocation;
    private String deliveryLocation;
    private int orderTime;
    private int priority;
    private String status;
    private String assignedRiderId;  // Track which rider is assigned
    
    public Order(String orderId, String studentName, String studentEmail, 
                 String pickupLocation, String deliveryLocation, int priority) {
        this.orderId = orderId;
        this.studentName = studentName;
        this.studentEmail = studentEmail;
        this.pickupLocation = pickupLocation;
        this.deliveryLocation = deliveryLocation;
        this.priority = priority;
        this.status = "Pending";
        this.orderTime = (int) (System.currentTimeMillis() % 1000000);
        this.assignedRiderId = null;
    }
    
    // Getters and Setters
    public String getStudentEmail() { return studentEmail; }
    public String getAssignedRiderId() { return assignedRiderId; }
    public void setAssignedRiderId(String riderId) { this.assignedRiderId = riderId; }
    
    // ... rest of existing methods

    
    // Getters and Setters
    public String getOrderId() { return orderId; }
    public String getStudentName() { return studentName; }
    public String getPickupLocation() { return pickupLocation; }
    public String getDeliveryLocation() { return deliveryLocation; }
    public int getOrderTime() { return orderTime; }
    public int getPriority() { return priority; }
    public String getStatus() { return status; }
    
    public void setStatus(String status) { this.status = status; }
    public void assignToRider() { this.status = "Assigned"; }
    public void markDelivered() { this.status = "Delivered"; }
    public void cancel() { this.status = "Cancelled"; }
    
    // NEW: Check if order matches search term
    public boolean matchesSearch(String searchTerm) {
        if (searchTerm == null || searchTerm.trim().isEmpty()) {
            return true;
        }
        
        String term = searchTerm.toLowerCase().trim();
        return orderId.toLowerCase().contains(term) ||
               studentName.toLowerCase().contains(term) ||
               studentEmail.toLowerCase().contains(term) ||
               pickupLocation.toLowerCase().contains(term) ||
               deliveryLocation.toLowerCase().contains(term) ||
               status.toLowerCase().contains(term) ||
               (priority == 1 && "urgent".contains(term)) ||
               (priority == 2 && "normal".contains(term));
    }
    
    @Override
    public String toString() {
        return "Order[" + orderId + ", " + studentName + ", " + status + "]";
    }
}