/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.fooddelivery3;

/**
 *
 * @author ASUS
 */


/**
 *
 * @author ASUS
 */



import javax.swing.*;
import java.awt.*;

public class FoodDeliverySystem extends JPanel {

    public Graph campusMap;
    public CustomHashMap<String, Order> ordersById;
    public CustomHashMap<String, Rider> ridersById;
    public PriorityQueue pendingOrders;
    public CustomArrayList<Order> allOrders;

    public FoodDeliverySystem() {
        setLayout(new BorderLayout());

        // Init structures (keep logic)
        campusMap = new Graph();
        ordersById = new CustomHashMap<>(100);
        ridersById = new CustomHashMap<>(50);
        pendingOrders = new PriorityQueue(100);
        allOrders = new CustomArrayList<>(100);

        addSampleData();
        initGUI();
    }

    private void addSampleData() {
        String[] locs = { "Hostel", "Library", "Cafe", "Block A", "Block B", "Sports Complex" };
        for (String loc : locs) {
            campusMap.addNode(loc);
        }

        campusMap.addEdge("Hostel", "Library", 5);
        campusMap.addEdge("Hostel", "Cafe", 3);
        campusMap.addEdge("Library", "Cafe", 2);
        campusMap.addEdge("Library", "Block A", 4);
        campusMap.addEdge("Cafe", "Block B", 3);
        campusMap.addEdge("Block A", "Block B", 2);
        campusMap.addEdge("Block B", "Sports Complex", 6);

        addRider("R001", "Ali", "Hostel");
        addRider("R002", "Siti", "Library");
        addRider("R003", "Ahmad", "Cafe");
    }

    private void addRider(String id, String name, String location) {
        ridersById.put(id, new Rider(id, name, location));
    }

    private void initGUI() {
        JTabbedPane tabs = new JTabbedPane();
        tabs.setFont(UITheme.bodyFont(14));

        tabs.addTab("Locations", new LocationsPanel(this));
        tabs.addTab("Orders", new OrdersPanel(this));
        tabs.addTab("Riders", new RidersPanel(this));
        tabs.addTab("Assignment", new AssignmentPanel(this));
        tabs.addTab("Statistics", new StatisticsPanel(this));
        tabs.addTab("Exit", new ExitPanel());

        add(tabs, BorderLayout.CENTER);
    }

    // ======================= METHODS CALLED BY PANELS =======================

    public void updateStatistics(JTextArea area) {
        int totalOrders = allOrders.size();
        int pending = 0, assigned = 0, delivered = 0, urgent = 0;

        for (int i = 0; i < allOrders.size(); i++) {
            Order o = allOrders.get(i);
            if (o != null) {
                switch (o.getStatus()) {
                    case "Pending":
                        pending++;
                        break;
                    case "Assigned":
                        assigned++;
                        break;
                    case "Delivered":
                        delivered++;
                        break;
                }
                if (o.getPriority() == 1)
                    urgent++;
            }
        }

        int availableRiders = 0;
        CustomHashMap.Entry<String, Rider>[] entries = ridersById.entries();
        if (entries != null) {
            for (CustomHashMap.Entry<String, Rider> e : entries) {
                if (e != null && e.value != null && "Available".equals(e.value.getStatus())) {
                    availableRiders++;
                }
            }
        }

        StringBuilder sb = new StringBuilder();
        sb.append("CAMPUS FOOD DELIVERY STATISTICS\n");
        sb.append("=================================\n\n");
        sb.append("Total Orders: ").append(totalOrders).append("\n");
        sb.append("Pending Orders: ").append(pending).append("\n");
        sb.append("Assigned Orders: ").append(assigned).append("\n");
        sb.append("Delivered Orders: ").append(delivered).append("\n");
        sb.append("Urgent Orders: ").append(urgent).append("\n\n");
        sb.append("Total Riders: ").append(ridersById.size()).append("\n");
        sb.append("Available Riders: ").append(availableRiders).append("\n");
        sb.append("Busy Riders: ").append(ridersById.size() - availableRiders).append("\n\n");
        sb.append("Locations: ").append(campusMap.getNodeCount()).append("\n");
        sb.append("Routes: ").append(campusMap.getEdgeCount()).append("\n");

        area.setText(sb.toString());
    }

    public void showAddRiderDialog() {
        JTextField idField = new JTextField();
        JTextField nameField = new JTextField();
        JTextField locationField = new JTextField();

        Object[] message = {
                "Rider ID:", idField,
                "Rider Name:", nameField,
                "Current Location:", locationField
        };

        int option = JOptionPane.showConfirmDialog(
                this, message, "Add New Rider", JOptionPane.OK_CANCEL_OPTION);

        if (option == JOptionPane.OK_OPTION) {
            String id = idField.getText().trim();
            String name = nameField.getText().trim();
            String location = locationField.getText().trim();

            if (id.isEmpty() || name.isEmpty() || location.isEmpty()) {
                JOptionPane.showMessageDialog(this, "All fields are required!");
                return;
            }

            if (ridersById.containsKey(id)) {
                JOptionPane.showMessageDialog(this, "Rider ID already exists!");
                return;
            }

            ridersById.put(id, new Rider(id, name, location));
            JOptionPane.showMessageDialog(this, " Rider added successfully!");
        }
    }

    public void showManualAssignmentDialog() {
        if (pendingOrders.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No pending orders available.");
            return;
        }

        String orderId = JOptionPane.showInputDialog(this, "Enter Order ID to assign:");
        if (orderId == null || orderId.trim().isEmpty())
            return;

        Order order = ordersById.get(orderId);
        if (order == null || !"Pending".equals(order.getStatus())) {
            JOptionPane.showMessageDialog(this, "Invalid or non-pending order.");
            return;
        }

        String riderId = JOptionPane.showInputDialog(this, "Enter Available Rider ID:");
        if (riderId == null || riderId.trim().isEmpty())
            return;

        Rider rider = ridersById.get(riderId);
        if (rider == null || !"Available".equals(rider.getStatus())) {
            JOptionPane.showMessageDialog(this, "Rider not available.");
            return;
        }

        order.setStatus("Assigned");
        order.setAssignedRiderId(rider.getRiderId());
        rider.setStatus("Delivering");

        JOptionPane.showMessageDialog(this,
                " Order " + order.getOrderId() + " assigned to Rider " + rider.getRiderName());
    }

    // ✅ AUTO ASSIGN using NEAREST rider (shortest path by BFS hops)
    public void autoAssignOrder() {

        if (pendingOrders.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No pending orders to assign.");
            return;
        }

        // 1) Take highest priority order
        Order order = pendingOrders.dequeue();
        if (order == null) {
            JOptionPane.showMessageDialog(this, "No valid order found.");
            return;
        }

        // 2) Find NEAREST available rider using shortest path (BFS hops)
        Rider nearestRider = null;
        int bestDistance = Integer.MAX_VALUE;
        CustomArrayList<String> bestPathToPickup = null;

        CustomHashMap.Entry<String, Rider>[] entries = ridersById.entries();
        if (entries != null) {
            for (int i = 0; i < entries.length; i++) {

                if (entries[i] == null || entries[i].value == null)
                    continue;

                Rider rider = entries[i].value;
                if (!"Available".equals(rider.getStatus()))
                    continue;

                CustomArrayList<String> path = campusMap.findShortestPath(
                        rider.getCurrentLocation(),
                        order.getPickupLocation());

                if (path == null || path.size() == 0)
                    continue;

                int distance = path.size() - 1; // hops/segments

                if (distance < bestDistance) {
                    bestDistance = distance;
                    nearestRider = rider;
                    bestPathToPickup = path;
                }
            }
        }

        if (nearestRider == null) {
            JOptionPane.showMessageDialog(this,
                    "No available rider can reach the pickup location.\n" +
                            "Either no riders are Available or no path exists in the graph.");
            return;
        }

        // 3) Path pickup -> delivery
        CustomArrayList<String> pathPickupToDelivery = campusMap.findShortestPath(
                order.getPickupLocation(),
                order.getDeliveryLocation());

        if (pathPickupToDelivery == null || pathPickupToDelivery.size() == 0) {
            JOptionPane.showMessageDialog(this,
                    "No path exists from pickup to delivery location.\n" +
                            "Please check routes in Locations tab.");
            return;
        }

        // 4) Assign order to nearest rider
        order.setStatus("Assigned");
        order.setAssignedRiderId(nearestRider.getRiderId());
        nearestRider.setStatus("Delivering");

        // 5) Show BOTH routes in a nice dialog (proof for demo)
        int segments1 = bestPathToPickup.size() - 1;
        int segments2 = pathPickupToDelivery.size() - 1;
        int totalSegments = segments1 + segments2;

        StringBuilder sb = new StringBuilder();
        sb.append(" AUTO ASSIGNMENT (Nearest Rider + Shortest Path)\n");
        sb.append("══════════════════════════════════════\n\n");

        sb.append(" ORDER:\n");
        sb.append("Order ID: ").append(order.getOrderId()).append("\n");
        sb.append("Student: ").append(order.getStudentName()).append("\n");
        sb.append("Pickup: ").append(order.getPickupLocation()).append("\n");
        sb.append("Delivery: ").append(order.getDeliveryLocation()).append("\n");
        sb.append("Priority: ").append(order.getPriority() == 1 ? "URGENT" : "Normal").append("\n\n");

        sb.append(" NEAREST RIDER CHOSEN:\n");
        sb.append("Rider: ").append(nearestRider.getRiderName())
                .append(" (").append(nearestRider.getRiderId()).append(")\n");
        sb.append("From: ").append(nearestRider.getCurrentLocation()).append("\n");
        sb.append("Distance to pickup: ").append(segments1).append(" segments\n\n");

        sb.append(" ROUTE 1: Rider → Pickup (").append(segments1).append(" segments)\n");
        sb.append(formatPath(bestPathToPickup)).append("\n\n");

        sb.append(" ROUTE 2: Pickup → Delivery (").append(segments2).append(" segments)\n");
        sb.append(formatPath(pathPickupToDelivery)).append("\n\n");

        sb.append(" ESTIMATION:\n");
        sb.append("Total segments: ").append(totalSegments).append("\n");
        sb.append("Estimated time: ").append(totalSegments * 3).append(" minutes (3 min/segment)\n");

        JTextArea area = new JTextArea(sb.toString());
        area.setEditable(false);
        area.setFont(new Font("Monospaced", Font.PLAIN, 12));
        area.setMargin(new Insets(10, 10, 10, 10));

        JOptionPane.showMessageDialog(
                this, new JScrollPane(area),
                "Auto Assignment Result",
                JOptionPane.INFORMATION_MESSAGE);
    }

    // helper to format path list
    private String formatPath(CustomArrayList<String> path) {
        StringBuilder p = new StringBuilder("   ");
        for (int i = 0; i < path.size(); i++) {
            p.append(path.get(i));
            if (i < path.size() - 1)
                p.append(" → ");
        }
        return p.toString();
    }

    // ======================= RIDER DELETE METHOD =======================

public void deleteRider(String riderId) {
    if (riderId == null || riderId.trim().isEmpty()) {
        JOptionPane.showMessageDialog(this, "Invalid rider ID!");
        return;
    }
    
    Rider rider = ridersById.get(riderId);
    if (rider == null) {
        JOptionPane.showMessageDialog(this, "Rider not found!");
        return;
    }
    
    // Check if rider is currently delivering
    if ("Delivering".equals(rider.getStatus())) {
        JOptionPane.showMessageDialog(this, 
            "Cannot delete rider " + riderId + "!\n" +
            "This rider is currently delivering an order.\n" +
            "Complete the order first or reassign it.");
        return;
    }
    
    int confirm = JOptionPane.showConfirmDialog(
        this,
        "Are you sure you want to delete rider " + riderId + "?\n" +
        "Name: " + rider.getRiderName() + "\n" +
        "Location: " + rider.getCurrentLocation() + "\n" +
        "This action cannot be undone!",
        "Confirm Rider Deletion",
        JOptionPane.YES_NO_OPTION,
        JOptionPane.WARNING_MESSAGE);
    
    if (confirm == JOptionPane.YES_OPTION) {
        ridersById.remove(riderId);
        JOptionPane.showMessageDialog(this, 
            " Rider " + riderId + " deleted successfully!");
    }
}
    
    
    public void showCreateOrderDialog() {
        JTextField idField = new JTextField();
        JTextField nameField = new JTextField();
        JTextField emailField = new JTextField();
        JTextField pickupField = new JTextField();
        JTextField deliveryField = new JTextField();

        JComboBox<String> priorityBox = new JComboBox<>(new String[] { "1 - Urgent", "2 - Normal" });

        Object[] message = {
                "Order ID:", idField,
                "Student Name:", nameField,
                "Student Email:", emailField,
                "Pickup Location:", pickupField,
                "Delivery Location:", deliveryField,
                "Priority:", priorityBox
        };

        int option = JOptionPane.showConfirmDialog(
                this, message, "Create New Order", JOptionPane.OK_CANCEL_OPTION);
        if (option != JOptionPane.OK_OPTION)
            return;

        String id = idField.getText().trim();
        String name = nameField.getText().trim();
        String email = emailField.getText().trim();
        String pickup = pickupField.getText().trim();
        String delivery = deliveryField.getText().trim();
        int priority = priorityBox.getSelectedIndex() + 1;

        if (id.isEmpty() || name.isEmpty() || email.isEmpty() || pickup.isEmpty() || delivery.isEmpty()) {
            JOptionPane.showMessageDialog(this, "All fields are required!");
            return;
        }

        if (ordersById.containsKey(id)) {
            JOptionPane.showMessageDialog(this, "Order ID already exists!");
            return;
        }

        Order order = new Order(id, name, email, pickup, delivery, priority);

        ordersById.put(id, order);
        allOrders.add(order);
        pendingOrders.enqueue(order);

        JOptionPane.showMessageDialog(this, " Order created successfully!");
    }

 public void cancelSelectedOrder(JTable table) {
    int row = table.getSelectedRow();
    if (row == -1) {
        JOptionPane.showMessageDialog(this, "Select an order first.");
        return;
    }

    // Convert view row index to model row index (for sorting/filtering)
    int modelRow = table.convertRowIndexToModel(row);
    String orderId = table.getModel().getValueAt(modelRow, 0).toString();
    Order order = ordersById.get(orderId);
    if (order == null)
        return;

    order.setStatus("Cancelled");
    JOptionPane.showMessageDialog(this, "✖ Order " + orderId + " cancelled.");
}

public void completeSelectedOrder(JTable table) {
    int row = table.getSelectedRow();
    if (row == -1) {
        JOptionPane.showMessageDialog(this, "Select an order first.");
        return;
    }

    // Convert view row index to model row index (for sorting/filtering)
    int modelRow = table.convertRowIndexToModel(row);
    String orderId = table.getModel().getValueAt(modelRow, 0).toString();
    Order order = ordersById.get(orderId);
    if (order == null)
        return;

    order.setStatus("Delivered");

    // Update rider status if assigned
    if (order.getAssignedRiderId() != null) {
        Rider rider = ridersById.get(order.getAssignedRiderId());
        if (rider != null) {
            rider.setStatus("Available");
            rider.setCurrentLocation(order.getDeliveryLocation());
        }
    }

    JOptionPane.showMessageDialog(this, "✔ Order " + orderId + " marked Delivered.");
}

    // ======================= LOCATION MANAGEMENT METHODS =======================

    public void showAddLocationDialog() {
        JTextField locationField = new JTextField();
        
        Object[] message = {
            "Enter new location name:",
            locationField
        };
        
        int option = JOptionPane.showConfirmDialog(
                this, message, "Add New Location", JOptionPane.OK_CANCEL_OPTION);
        
        if (option == JOptionPane.OK_OPTION) {
            String location = locationField.getText().trim();
            if (location.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Location name cannot be empty!");
                return;
            }
            
            campusMap.addNode(location);
            JOptionPane.showMessageDialog(this, " Location '" + location + "' added successfully!");
        }
    }

    public void showAddRouteDialog() {
        JTextField fromField = new JTextField();
        JTextField toField = new JTextField();
        JTextField distanceField = new JTextField("1");
        
        Object[] message = {
            "From Location:", fromField,
            "To Location:", toField,
            "Distance (in minutes):", distanceField
        };
        
        int option = JOptionPane.showConfirmDialog(
                this, message, "Add New Route", JOptionPane.OK_CANCEL_OPTION);
        
        if (option == JOptionPane.OK_OPTION) {
            String from = fromField.getText().trim();
            String to = toField.getText().trim();
            String distanceStr = distanceField.getText().trim();
            
            if (from.isEmpty() || to.isEmpty() || distanceStr.isEmpty()) {
                JOptionPane.showMessageDialog(this, "All fields are required!");
                return;
            }
            
            try {
                int distance = Integer.parseInt(distanceStr);
                if (distance <= 0) {
                    JOptionPane.showMessageDialog(this, "Distance must be positive!");
                    return;
                }
                
                campusMap.addEdge(from, to, distance);
                JOptionPane.showMessageDialog(this, 
                    " Route added: " + from + " ↔ " + to + " (" + distance + " min)");
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Distance must be a number!");
            }
        }
    }

    public void showFindShortestPathDialog() {
        // Get all locations from the graph
        CustomArrayList<String> locations = campusMap.getAllNodes();
        
        if (locations == null || locations.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No locations found! Add locations first.");
            return;
        }

        // Create combo boxes with locations
        String[] locArray = new String[locations.size()];
        for (int i = 0; i < locations.size(); i++) {
            locArray[i] = locations.get(i);
        }

        JComboBox<String> fromCombo = new JComboBox<>(locArray);
        JComboBox<String> toCombo = new JComboBox<>(locArray);

        Object[] message = {
            "From Location:", fromCombo,
            "To Location:", toCombo
        };

        int option = JOptionPane.showConfirmDialog(
                this, message, "Find Shortest Path", JOptionPane.OK_CANCEL_OPTION);

        if (option == JOptionPane.OK_OPTION) {
            String from = (String) fromCombo.getSelectedItem();
            String to = (String) toCombo.getSelectedItem();

            if (from.equals(to)) {
                JOptionPane.showMessageDialog(this, "Start and end locations cannot be the same!");
                return;
            }

            CustomArrayList<String> path = campusMap.findShortestPath(from, to);

            if (path == null || path.isEmpty()) {
                JOptionPane.showMessageDialog(this, 
                    " No path found between " + from + " and " + to + "!");
                return;
            }

            // Display path in a nice dialog
            StringBuilder sb = new StringBuilder();
            sb.append(" SHORTEST PATH: ").append(from).append(" → ").append(to).append("\n");
            sb.append("══════════════════════════════════════\n\n");

            sb.append("Path (").append(path.size() - 1).append(" segments):\n");
            for (int i = 0; i < path.size(); i++) {
                sb.append("  ").append(i + 1).append(". ").append(path.get(i));
                if (i < path.size() - 1) {
                    sb.append(" →\n");
                }
            }

            sb.append("\n\n Path Details:\n");
            sb.append("• Total locations: ").append(path.size()).append("\n");
            sb.append("• Segments to travel: ").append(path.size() - 1).append("\n");
            sb.append("• Estimated time: ").append((path.size() - 1) * 3).append(" minutes (3 min/segment)\n");

            JTextArea area = new JTextArea(sb.toString());
            area.setEditable(false);
            area.setFont(new Font("Monospaced", Font.PLAIN, 13));
            area.setMargin(new Insets(10, 10, 10, 10));

            JOptionPane.showMessageDialog(
                this, new JScrollPane(area),
                "Shortest Path Result",
                JOptionPane.INFORMATION_MESSAGE);
        }
    }

    // ======================= DELETE LOCATION/ROUTE METHODS =======================
    // THESE ARE THE MISSING METHODS THAT ARE CAUSING THE ERROR

    public void showDeleteLocationDialog() {
        // Get all locations
        CustomArrayList<String> locations = campusMap.getAllNodes();
        
        if (locations.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No locations to delete!");
            return;
        }
        
        // Create combo box with locations
        String[] locArray = new String[locations.size()];
        for (int i = 0; i < locations.size(); i++) {
            locArray[i] = locations.get(i);
        }
        
        JComboBox<String> locationCombo = new JComboBox<>(locArray);
        
        // Warning message
        JLabel warning = new JLabel("<html><font color='red'><b>Warning:</b> This will delete ALL routes connected to this location!</font></html>");
        
        Object[] message = {
            "Select location to delete:",
            locationCombo,
            warning
        };
        
        int option = JOptionPane.showConfirmDialog(
                this, message, "Delete Location", JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.WARNING_MESSAGE);
        
        if (option == JOptionPane.OK_OPTION) {
            String location = (String) locationCombo.getSelectedItem();
            
            // Ask for confirmation
            int confirm = JOptionPane.showConfirmDialog(
                this,
                "Are you sure you want to delete '" + location + "' and ALL connected routes?\nThis action cannot be undone!",
                "Confirm Deletion",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);
            
            if (confirm == JOptionPane.YES_OPTION) {
                boolean deleted = campusMap.deleteNode(location);
                if (deleted) {
                    JOptionPane.showMessageDialog(this, 
                        " Location '" + location + "' deleted successfully!");
                    
                    // Also update any riders at this location
                    updateRidersAfterLocationDeletion(location);
                } else {
                    JOptionPane.showMessageDialog(this, 
                        " Failed to delete location '" + location + "'!");
                }
            }
        }
    }

    public void showDeleteRouteDialog() {
        // Get all locations
        CustomArrayList<String> locations = campusMap.getAllNodes();
        
        if (locations.size() < 2) {
            JOptionPane.showMessageDialog(this, "Need at least 2 locations to delete a route!");
            return;
        }
        
        // Create combo boxes
        String[] locArray = new String[locations.size()];
        for (int i = 0; i < locations.size(); i++) {
            locArray[i] = locations.get(i);
        }
        
        JComboBox<String> fromCombo = new JComboBox<>(locArray);
        JComboBox<String> toCombo = new JComboBox<>(locArray);
        
        Object[] message = {
            "From Location:", fromCombo,
            "To Location:", toCombo
        };
        
        int option = JOptionPane.showConfirmDialog(
                this, message, "Delete Route", JOptionPane.OK_CANCEL_OPTION);
        
        if (option == JOptionPane.OK_OPTION) {
            String from = (String) fromCombo.getSelectedItem();
            String to = (String) toCombo.getSelectedItem();
            
            if (from.equals(to)) {
                JOptionPane.showMessageDialog(this, "Cannot delete route to same location!");
                return;
            }
            
            // Check if route exists
            if (!routeExists(from, to)) {
                JOptionPane.showMessageDialog(this, 
                    "No route exists between " + from + " and " + to + "!");
                return;
            }
            
            boolean deleted = campusMap.deleteEdge(from, to);
            if (deleted) {
                JOptionPane.showMessageDialog(this, 
                    " Route deleted: " + from + " ↔ " + to);
            } else {
                JOptionPane.showMessageDialog(this, 
                    " Failed to delete route!");
            }
        }
    }

    // Helper method to check if route exists
    private boolean routeExists(String from, String to) {
        CustomArrayList<Graph.Edge> edges = campusMap.getEdges(from);
        if (edges != null) {
            for (int i = 0; i < edges.size(); i++) {
                Graph.Edge edge = edges.get(i);
                if (edge != null && edge.destination.equals(to)) {
                    return true;
                }
            }
        }
        return false;
    }

    // Update riders when a location is deleted
    private void updateRidersAfterLocationDeletion(String deletedLocation) {
        CustomHashMap.Entry<String, Rider>[] entries = ridersById.entries();
        if (entries != null) {
            int movedCount = 0;
            for (int i = 0; i < entries.length; i++) {
                if (entries[i] != null && entries[i].value != null) {
                    Rider rider = entries[i].value;
                    if (rider.getCurrentLocation().equals(deletedLocation)) {
                        // Move rider to first available location
                        CustomArrayList<String> locations = campusMap.getAllNodes();
                        if (locations != null && locations.size() > 0) {
                            String newLocation = locations.get(0);
                            rider.setCurrentLocation(newLocation);
                            movedCount++;
                        }
                    }
                }
            }
            
            if (movedCount > 0) {
                JOptionPane.showMessageDialog(this,
                    " " + movedCount + " rider(s) were at deleted location.\n" +
                    "They have been moved to another location.");
            }
        }
    }
}