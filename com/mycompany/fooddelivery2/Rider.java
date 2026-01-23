/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.fooddelivery2;

/**
 *
 * @author ASUS
 */

// Rider.java


public class Rider {
    private String riderId;
    private String riderName;
    private String currentLocation;
    private String status; // Available, Delivering, Offline
    
    public Rider(String riderId, String riderName, String currentLocation) {
        this.riderId = riderId;
        this.riderName = riderName;
        this.currentLocation = currentLocation;
        this.status = "Available";
    }
    
    // Getters and Setters
    public String getRiderId() { return riderId; }
    public String getRiderName() { return riderName; }
    public String getCurrentLocation() { return currentLocation; }
    public String getStatus() { return status; }
    
    public void setCurrentLocation(String location) { 
        this.currentLocation = location; 
    }
    
    public void setStatus(String status) { 
        this.status = status; 
    }
    
    public void startDelivery() { 
        this.status = "Delivering"; 
    }
    
    public void completeDelivery(String newLocation) { 
        this.status = "Available"; 
        this.currentLocation = newLocation;
    }
    
    public void goOffline() { 
        this.status = "Offline"; 
    }
    
    @Override
    public String toString() {
        return "Rider[" + riderId + ", " + riderName + ", " + status + "]";
    }
}