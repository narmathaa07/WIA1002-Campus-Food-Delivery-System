/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.fooddelivery;

/**
 *
 * @author ASUS
 */


public class Order implements Comparable<Order> {
    private int orderId;
    private String studentName;
    private String pickupLocation;
    private String deliveryLocation;
    private int priority; // 1 = urgent, 2 = normal
    private OrderStatus status;
    private long timeCreated;

    public Order(int orderId, String studentName, String pickupLocation, String deliveryLocation, int priority) {
        this.orderId = orderId;
        this.studentName = studentName;
        this.pickupLocation = pickupLocation;
        this.deliveryLocation = deliveryLocation;
        this.priority = priority;
        this.status = OrderStatus.PENDING;
        this.timeCreated = System.currentTimeMillis();
    }

    // ===== GETTERS =====
    public int getOrderId() {
        return orderId;
    }

    public String getStudentName() {
        return studentName;
    }

    public String getPickupLocation() {
        return pickupLocation;
    }

    public String getDeliveryLocation() {
        return deliveryLocation;
    }

    public int getPriority() {
        return priority;
    }

    public OrderStatus getStatus() {
        return status;
    }

    // ===== SETTER =====
    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    @Override
    public int compareTo(Order o) {
        if (this.priority != o.priority) {
            return Integer.compare(this.priority, o.priority); // urgent first
        }
        return Long.compare(this.timeCreated, o.timeCreated);
    }

    @Override
    public String toString() {
        return "Order{" +
                "ID=" + orderId +
                ", Student='" + studentName + '\'' +
                ", Pickup='" + pickupLocation + '\'' +
                ", Delivery='" + deliveryLocation + '\'' +
                ", Priority=" + priority +
                ", Status=" + status +
                '}';
    }
}
