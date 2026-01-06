/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package ordermanager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.PriorityQueue;
import java.io.*;
import java.util.Scanner;
/**
 *
 * @author Harshini
 */
public class OrderManager {

    private final PriorityQueue<Order> pendingOrders = new PriorityQueue<>();
    private final HashMap<Integer, Order> orderMap = new HashMap<>();
    private final ArrayList<Order> allOrders = new ArrayList<>();
    private int nextOrderId = 1; // Auto-increment

    // 4.2 Create New Order
    public void createOrder(String studentName, String pickupLocation, String deliveryLocation, int priority) {
        if (priority != 1 && priority != 2) {
            throw new IllegalArgumentException("Priority must be 1 (Urgent) or 2 (Normal)");
        }
        Order newOrder = new Order(nextOrderId++, studentName, pickupLocation, deliveryLocation, priority);
        allOrders.add(newOrder);
        orderMap.put(newOrder.getOrderId(), newOrder);
        pendingOrders.add(newOrder);
        System.out.println("Order created: " + newOrder);
    }

    // 4.3 View All Orders (in order of creation)
    public void viewAllOrders() {
        if (allOrders.isEmpty()) {
            System.out.println("No orders available.");
            return;
        }
        System.out.println("All Orders:");
        for (Order order : allOrders) {
            System.out.println(order);
        }
    }

    // 4.3 View Orders by Status
    public void viewOrdersByStatus(Status status) {
        boolean found = false;
        System.out.println("Orders with status " + status + ":");
        for (Order order : allOrders) {
            if (order.getStatus() == status) {
                System.out.println(order);
                found = true;
            }
        }
        if (!found) {
            System.out.println("No orders with status " + status);
        }
    }

    // 4.6 Cancel/Delete Order
    public void cancelOrder(int orderId) {
        Order order = orderMap.get(orderId);
        if (order == null) {
            System.out.println("Order ID " + orderId + " not found.");
            return;
        }
        if (order.getStatus() == Status.PENDING) {
            pendingOrders.remove(order); // O(n) but acceptable for small n
        }
        order.setStatus(Status.CANCELLED);
        // Delete: Remove from map and allOrders (but keep if history needed; adjust as per group)
        orderMap.remove(orderId);
        allOrders.remove(order);
        System.out.println("Order " + orderId + " cancelled/deleted.");
        // Note: If assigned, Member 3 should free rider before calling this
    }

    // Helper for other members: Get next pending order (for dispatch)
    public Order getNextPendingOrder() {
        return pendingOrders.poll(); // Dequeues highest priority
    }

    // Helper: Get order by ID (for search/integration)
    public Order getOrderById(int orderId) {
        return orderMap.get(orderId);
    }

    // Helper: Update status (for dispatch/complete)
    public void updateOrderStatus(int orderId, Status newStatus) {
        Order order = orderMap.get(orderId);
        if (order != null) {
            if (newStatus == Status.ASSIGNED && order.getStatus() == Status.PENDING) {
                pendingOrders.remove(order);
            }
            order.setStatus(newStatus);
        }
    }
    
    // Save (append to file)
public void saveOrdersToFile(String filename) throws IOException {
    try (PrintWriter writer = new PrintWriter(new FileWriter(filename))) {
        for (Order order : allOrders) {
            writer.println(order.getOrderId() + "," + order.getStudentName() + "," + order.getPickupLocation() + "," +
                    order.getDeliveryLocation() + "," + order.getOrderTime() + "," + order.getPriority() + "," + order.getStatus());
        }
    }
}

// Load (rebuild structures)
public void loadOrdersFromFile(String filename) throws FileNotFoundException {
    try (Scanner scanner = new Scanner(new File(filename))) {
        while (scanner.hasNextLine()) {
            String[] parts = scanner.nextLine().split(",");
            int id = Integer.parseInt(parts[0]);
            String name = parts[1];
            String pickup = parts[2];
            String delivery = parts[3];
            long time = Long.parseLong(parts[4]);
            int prio = Integer.parseInt(parts[5]);
            Status status = Status.valueOf(parts[6]);

            Order order = new Order(id, name, pickup, delivery, prio);
            order.setStatus(status); // Override default
            // Override time (since constructor sets current)
            try {
                java.lang.reflect.Field timeField = Order.class.getDeclaredField("orderTime");
                timeField.setAccessible(true);
                timeField.set(order, time);
            } catch (Exception e) {
                e.printStackTrace();
            }

            allOrders.add(order);
            orderMap.put(id, order);
            if (status == Status.PENDING) {
                pendingOrders.add(order);
            }
            nextOrderId = Math.max(nextOrderId, id + 1); // Update ID counter
        }
    }
}

public void displayStatistics() {
    int totalOrders = allOrders.size();
    int urgentOrders = 0;
    int deliveredOrders = 0;
    for (Order order : allOrders) {
        if (order.getPriority() == 1) urgentOrders++;
        if (order.getStatus() == Status.DELIVERED) deliveredOrders++;
    }
    System.out.println("Total Orders: " + totalOrders);
    System.out.println("Urgent Orders: " + urgentOrders);
    System.out.println("Delivered Orders: " + deliveredOrders);
}
    
}
