/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.fooddelivery;

/**
 *
 * @author ASUS
 */




import java.io.*;
import java.util.*;

public class OrderManager {

    private PriorityQueue<Order> pendingOrders = new PriorityQueue<>();
    private HashMap<Integer, Order> orderMap = new HashMap<>();
    private ArrayList<Order> allOrders = new ArrayList<>();
    private Stack<UndoAction> undoStack = new Stack<>();

    private int nextOrderId = 1;

    // ============ CREATE ORDER ============
    public void createOrder(String name, String pickup, String delivery, int priority) {
        Order order = new Order(nextOrderId++, name, pickup, delivery, priority);
        pendingOrders.add(order);
        orderMap.put(order.getOrderId(), order);
        allOrders.add(order);

        undoStack.push(new UndoAction(UndoAction.ActionType.CREATE, order));

        System.out.println("Order created: " + order);
    }

    // ============ GET ORDER BY ID (IMPORTANT FOR RIDER DISPATCH) ============
    public Order getOrderById(int id) {
        return orderMap.get(id);
    }

    // ============ VIEW ALL ============
    public void viewAllOrders() {
        if (allOrders.isEmpty()) {
            System.out.println("No orders available.");
            return;
        }
        for (Order o : allOrders) {
            System.out.println(o);
        }
    }

    // ============ VIEW BY STATUS ============
    public void viewByStatus(OrderStatus status) {
        boolean found = false;
        for (Order o : allOrders) {
            if (o.getStatus() == status) {
                System.out.println(o);
                found = true;
            }
        }
        if (!found) {
            System.out.println("No orders with status " + status);
        }
    }

    // ============ CANCEL ORDER ============
    public void cancelOrder(int id) {
        Order o = orderMap.get(id);
        if (o == null) {
            System.out.println("Order not found.");
            return;
        }

        pendingOrders.remove(o);
        o.setStatus(OrderStatus.CANCELLED);
        orderMap.remove(id);
        allOrders.remove(o);

        undoStack.push(new UndoAction(UndoAction.ActionType.CANCEL, o));

        System.out.println("Order cancelled.");
    }

    // ============ GET NEXT PENDING (FOR AUTO ASSIGN) ============
    public Order getNextPendingOrder() {
        return pendingOrders.poll();
    }

    // ============ UNDO ============
    public void undo() {
        if (undoStack.isEmpty()) {
            System.out.println("Nothing to undo.");
            return;
        }

        UndoAction action = undoStack.pop();

        if (action.getType() == UndoAction.ActionType.CREATE) {
            Order o = action.getOrder();
            pendingOrders.remove(o);
            orderMap.remove(o.getOrderId());
            allOrders.remove(o);
            System.out.println("Undo: Order creation reverted.");
        }
        else if (action.getType() == UndoAction.ActionType.CANCEL) {
            Order o = action.getOrder();
            o.setStatus(OrderStatus.PENDING);
            pendingOrders.add(o);
            orderMap.put(o.getOrderId(), o);
            allOrders.add(o);
            System.out.println("Undo: Order cancellation reverted.");
        }
    }

    // ============ STATISTICS ============
    public void showStats() {
        int urgent = 0, delivered = 0;

        for (Order o : allOrders) {
            if (o.getPriority() == 1) urgent++;
            if (o.getStatus() == OrderStatus.DELIVERED) delivered++;
        }

        System.out.println("Total Orders: " + allOrders.size());
        System.out.println("Urgent Orders: " + urgent);
        System.out.println("Delivered Orders: " + delivered);
    }

    // ============ SAVE ============
    public void saveToFile(String filename) throws IOException {
        PrintWriter pw = new PrintWriter(new FileWriter(filename));
        for (Order o : allOrders) {
            pw.println(o.getOrderId() + "," + o.getStudentName() + "," +
                    o.getPickupLocation() + "," + o.getDeliveryLocation() + "," +
                    o.getPriority() + "," + o.getStatus());
        }
        pw.close();
    }

    // ============ LOAD ============
    public void loadFromFile(String filename) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(filename));
        String line;

        while ((line = br.readLine()) != null) {
            String[] p = line.split(",");
            Order o = new Order(Integer.parseInt(p[0]), p[1], p[2], p[3], Integer.parseInt(p[4]));
            o.setStatus(OrderStatus.valueOf(p[5]));
            allOrders.add(o);
            orderMap.put(o.getOrderId(), o);
            if (o.getStatus() == OrderStatus.PENDING) {
                pendingOrders.add(o);
            }
            nextOrderId = Math.max(nextOrderId, o.getOrderId() + 1);
        }
        br.close();
    }
}
