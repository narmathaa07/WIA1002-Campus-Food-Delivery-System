/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.fooddelivery;

/**
 *
 * @author ASUS
 */




public class DispatchService {

    private Graph graph;
    private OrderManager orderManager;
    private RiderManager riderManager;

    public DispatchService(Graph graph, OrderManager om, RiderManager rm) {
        this.graph = graph;
        this.orderManager = om;
        this.riderManager = rm;
    }

    // ============ MANUAL ASSIGNMENT ============
    public void assignOrderManually(int orderId, int riderId) {
        Order order = orderManager.getOrderById(orderId);
        Rider rider = riderManager.getRiderById(riderId);

        if (order == null) {
            System.out.println("Order not found.");
            return;
        }

        if (rider == null) {
            System.out.println("Rider not found.");
            return;
        }

        if (order.getStatus() != OrderStatus.PENDING) {
            System.out.println("Order is not pending.");
            return;
        }

        if (rider.getStatus() != RiderStatus.AVAILABLE) {
            System.out.println("Rider is not available.");
            return;
        }

        // Calculate path
        PathResult p1 = graph.shortestPath(rider.getCurrentLocation(), order.getPickupLocation());
        PathResult p2 = graph.shortestPath(order.getPickupLocation(), order.getDeliveryLocation());

        int totalDistance = p1.getTotalDistance() + p2.getTotalDistance();

        System.out.println("Rider → Pickup Path: " + p1.getPath());
        System.out.println("Pickup → Delivery Path: " + p2.getPath());
        System.out.println("Total Distance: " + totalDistance);

        // Update statuses
        order.setStatus(OrderStatus.ASSIGNED);
        rider.setStatus(RiderStatus.BUSY);

        System.out.println("Order successfully assigned to rider!");
    }

    // ============ COMPLETE ORDER ============
    public void completeOrder(int orderId, int riderId) {
        Order order = orderManager.getOrderById(orderId);
        Rider rider = riderManager.getRiderById(riderId);

        if (order == null || rider == null) {
            System.out.println("Invalid order or rider.");
            return;
        }

        if (order.getStatus() != OrderStatus.ASSIGNED) {
            System.out.println("Order is not assigned yet.");
            return;
        }

        // Update statuses
        order.setStatus(OrderStatus.DELIVERED);
        rider.setStatus(RiderStatus.AVAILABLE);
        rider.setCurrentLocation(order.getDeliveryLocation());

        System.out.println("Order delivered successfully!");
        System.out.println("Rider is now available.");
        System.out.println("Rider new location: " + rider.getCurrentLocation());
    }
}
