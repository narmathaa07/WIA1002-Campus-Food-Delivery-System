/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.fooddelivery2;

/**
 *
 * @author ASUS
 */


// FoodDeliverySystem.java




import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class FoodDeliverySystem extends JFrame {
    private Graph campusMap;
    private CustomHashMap<String, Order> ordersById;
    private CustomHashMap<String, Rider> ridersById;
    private PriorityQueue pendingOrders;
    private CustomArrayList<Order> allOrders;
    private JTextArea locationsTextArea;
    private DefaultTableModel ordersModel;
    private DefaultTableModel ridersModel;
    
    public FoodDeliverySystem() {
        setTitle("Campus Food Delivery Management System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 800);
        setLocationRelativeTo(null);
        
        initDataStructures();
        setupMenuBar();
        setupMainPanel();
        addSampleData();
    }
    
    private void initDataStructures() {
        campusMap = new Graph();
        ordersById = new CustomHashMap<>(100);
        ridersById = new CustomHashMap<>(50);
        pendingOrders = new PriorityQueue(100);
        allOrders = new CustomArrayList<>(100);
    }
    
    private void addSampleData() {
        // Sample locations
        addLocation("Hostel");
        addLocation("Library");
        addLocation("Cafe");
        addLocation("Block A");
        addLocation("Block B");
        addLocation("Sports Complex");
        
        // Sample routes
        campusMap.addEdge("Hostel", "Library", 5);
        campusMap.addEdge("Hostel", "Cafe", 3);
        campusMap.addEdge("Library", "Cafe", 2);
        campusMap.addEdge("Library", "Block A", 4);
        campusMap.addEdge("Cafe", "Block B", 3);
        campusMap.addEdge("Block A", "Block B", 2);
        campusMap.addEdge("Block B", "Sports Complex", 6);
        
        // Sample riders
        addRider("R001", "Ali", "Hostel");
        addRider("R002", "Siti", "Library");
        addRider("R003", "Ahmad", "Cafe");
    }
    
    private void setupMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        
        // File menu
        JMenu fileMenu = new JMenu("File");
        JMenuItem saveItem = new JMenuItem("Save All");
        JMenuItem loadItem = new JMenuItem("Load All");
        JMenuItem exitItem = new JMenuItem("Exit");
        
        saveItem.addActionListener(e -> saveAllData());
        loadItem.addActionListener(e -> loadAllData());
        exitItem.addActionListener(e -> System.exit(0));
        
        fileMenu.add(saveItem);
        fileMenu.add(loadItem);
        fileMenu.addSeparator();
        fileMenu.add(exitItem);
        
        // Edit menu for Undo
        JMenu editMenu = new JMenu("Edit");
        JMenuItem undoItem = new JMenuItem("Undo Last Action");
        undoItem.addActionListener(e -> undoLastAction());
        editMenu.add(undoItem);
        
        menuBar.add(fileMenu);
        menuBar.add(editMenu);
        setJMenuBar(menuBar);
    }
    
    private void setupMainPanel() {
        JTabbedPane tabbedPane = new JTabbedPane();
        
        tabbedPane.addTab("1. Locations & Routes", createLocationsPanel());
        tabbedPane.addTab("2. Order Management", createOrdersPanel());
        tabbedPane.addTab("3. Rider Management", createRidersPanel());
        tabbedPane.addTab("4. Order Assignment", createAssignmentPanel());
        tabbedPane.addTab("5. Statistics & Reports", createStatisticsPanel());
        
        add(tabbedPane);
    }
    
    // ========== 1️⃣ LOCATIONS & ROUTES PANEL ==========
    private JPanel createLocationsPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Left panel: Add location/route controls
        JPanel controlPanel = new JPanel(new GridLayout(6, 2, 5, 5));
        
        // Add Location section
        controlPanel.add(new JLabel("Add New Location:"));
        controlPanel.add(new JLabel(""));
        JTextField newLocationField = new JTextField();
        controlPanel.add(newLocationField);
        JButton addLocationBtn = new JButton("Add Location");
        addLocationBtn.addActionListener(e -> {
            String location = newLocationField.getText().trim();
            if (!location.isEmpty()) {
                addLocation(location);
                newLocationField.setText("");
                updateLocationsDisplay();
                JOptionPane.showMessageDialog(this, "Location added: " + location);
            }
        });
        controlPanel.add(addLocationBtn);
        
        // Add Route section
        controlPanel.add(new JLabel("Add Route:"));
        controlPanel.add(new JLabel(""));
        JTextField fromField = new JTextField("Hostel");
        JTextField toField = new JTextField("Library");
        JTextField distanceField = new JTextField("5");
        
        JPanel routePanel = new JPanel(new GridLayout(3, 2, 5, 5));
        routePanel.add(new JLabel("From:"));
        routePanel.add(fromField);
        routePanel.add(new JLabel("To:"));
        routePanel.add(toField);
        routePanel.add(new JLabel("Distance:"));
        routePanel.add(distanceField);
        
        controlPanel.add(new JLabel(""));
        controlPanel.add(routePanel);
        
        JButton addRouteBtn = new JButton("Add Route");
        addRouteBtn.addActionListener(e -> {
            try {
                int distance = Integer.parseInt(distanceField.getText());
                campusMap.addEdge(fromField.getText(), toField.getText(), distance);
                updateLocationsDisplay();
                JOptionPane.showMessageDialog(this, "Route added successfully!");
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Please enter valid distance!");
            }
        });
        controlPanel.add(addRouteBtn);
        
        // Shortest Path section
        controlPanel.add(new JLabel("Find Shortest Path:"));
        controlPanel.add(new JLabel(""));
        JTextField startField = new JTextField("Hostel");
        JTextField endField = new JTextField("Library");
        
        JPanel pathPanel = new JPanel(new GridLayout(2, 2, 5, 5));
        pathPanel.add(new JLabel("Start:"));
        pathPanel.add(startField);
        pathPanel.add(new JLabel("End:"));
        pathPanel.add(endField);
        
        controlPanel.add(new JLabel(""));
        controlPanel.add(pathPanel);
        
        JButton findPathBtn = new JButton("Find Shortest Path");
        findPathBtn.addActionListener(e -> {
            String start = startField.getText();
            String end = endField.getText();
            if (!start.isEmpty() && !end.isEmpty()) {
                CustomArrayList<String> path = campusMap.findShortestPath(start, end);
                if (path.size() > 0) {
                    StringBuilder pathStr = new StringBuilder();
                    for (int i = 0; i < path.size(); i++) {
                        pathStr.append(path.get(i));
                        if (i < path.size() - 1) pathStr.append(" → ");
                    }
                    int distance = path.size() - 1; // For unweighted graph
                    JOptionPane.showMessageDialog(this, 
                        "Shortest Path Found!\n\n" +
                        "Path: " + pathStr.toString() + "\n" +
                        "Distance: " + distance + " segments");
                } else {
                    JOptionPane.showMessageDialog(this, "No path found!");
                }
            }
        });
        controlPanel.add(findPathBtn);
        
        // Display area for adjacency list
        locationsTextArea = new JTextArea();
        locationsTextArea.setEditable(false);
        locationsTextArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        updateLocationsDisplay();
        
        JScrollPane scrollPane = new JScrollPane(locationsTextArea);
        scrollPane.setPreferredSize(new Dimension(400, 0));
        
        panel.add(controlPanel, BorderLayout.WEST);
        panel.add(scrollPane, BorderLayout.CENTER);
        
        return panel;
    }
    
    private void addLocation(String location) {
        campusMap.addNode(location);
    }
    
    private void updateLocationsDisplay() {
        locationsTextArea.setText(campusMap.getGraphInfo());
    }
    
    // ========== 2️⃣ ORDER MANAGEMENT PANEL ==========
    private JPanel createOrdersPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Top panel: Create order and filters
        JPanel topPanel = new JPanel(new GridLayout(2, 1, 5, 5));
        
        // Create order panel
        JPanel createPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton createOrderBtn = new JButton("Create New Order");
        createOrderBtn.addActionListener(e -> showCreateOrderDialog());
        createPanel.add(createOrderBtn);
        
        // Filter panel
        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        filterPanel.add(new JLabel("Filter by Status:"));
        JComboBox<String> statusFilter = new JComboBox<>(
            new String[]{"All", "Pending", "Assigned", "Delivered", "Cancelled"});
        statusFilter.addActionListener(e -> filterOrdersByStatus((String) statusFilter.getSelectedItem()));
        filterPanel.add(statusFilter);
        
        topPanel.add(createPanel);
        topPanel.add(filterPanel);
        
        // Orders table
        String[] columnNames = {"Order ID", "Student", "Pickup", "Delivery", "Priority", "Status"};
        ordersModel = new DefaultTableModel(columnNames, 0);
        JTable ordersTable = new JTable(ordersModel);
        refreshOrdersTable();
        
        // Action buttons
        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton cancelOrderBtn = new JButton("Cancel Order");
        JButton completeOrderBtn = new JButton("Complete Order");
        JButton refreshBtn = new JButton("Refresh");
        
        cancelOrderBtn.addActionListener(e -> cancelSelectedOrder(ordersTable));
        completeOrderBtn.addActionListener(e -> completeSelectedOrder(ordersTable));
        refreshBtn.addActionListener(e -> refreshOrdersTable());
        
        actionPanel.add(cancelOrderBtn);
        actionPanel.add(completeOrderBtn);
        actionPanel.add(refreshBtn);
        
        panel.add(topPanel, BorderLayout.NORTH);
        panel.add(new JScrollPane(ordersTable), BorderLayout.CENTER);
        panel.add(actionPanel, BorderLayout.SOUTH);
        
        return panel;
    }
    
    private void showCreateOrderDialog() {
    JDialog dialog = new JDialog(this, "Create New Order", true);
    dialog.setSize(450, 350);
    dialog.setLayout(new GridLayout(8, 2, 10, 10));
    
    JTextField orderIdField = new JTextField("ORD" + (allOrders.size() + 1001));
    JTextField studentField = new JTextField();
    JTextField emailField = new JTextField();
    JComboBox<String> pickupBox = getLocationComboBox();
    JComboBox<String> deliveryBox = getLocationComboBox();
    JComboBox<String> priorityBox = new JComboBox<>(new String[]{"1 - Urgent", "2 - Normal"});
    
    JLabel emailExample = new JLabel("example@gmail.com", SwingConstants.RIGHT);
    emailExample.setForeground(Color.GRAY);
    emailExample.setFont(new Font("Arial", Font.ITALIC, 10));
    
    dialog.add(new JLabel("Order ID:"));
    dialog.add(orderIdField);
    dialog.add(new JLabel("Student Name:"));
    dialog.add(studentField);
    dialog.add(new JLabel("Email Address:*"));
    dialog.add(emailField);
    dialog.add(new JLabel("")); // Empty cell
    dialog.add(emailExample);
    dialog.add(new JLabel("Pickup Location:"));
    dialog.add(pickupBox);
    dialog.add(new JLabel("Delivery Location:"));
    dialog.add(deliveryBox);
    dialog.add(new JLabel("Priority:"));
    dialog.add(priorityBox);
    
    JButton saveBtn = new JButton("Save Order");
    JButton cancelBtn = new JButton("Cancel");
    
    saveBtn.addActionListener(e -> {
        if (studentField.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(dialog, "Please enter student name!");
            return;
        }
        
        String email = emailField.getText().trim();
        if (email.isEmpty() || !isValidEmail(email)) {
            JOptionPane.showMessageDialog(dialog, 
                "Please enter a valid Gmail address!\n\n" +
                "Format: username@gmail.com\n" +
                "Example: ali.ahmad@student.edu.my",
                "Email Required", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        int priority = priorityBox.getSelectedIndex() + 1;
        Order order = new Order(
            orderIdField.getText(),
            studentField.getText(),
            email,
            (String) pickupBox.getSelectedItem(),
            (String) deliveryBox.getSelectedItem(),
            priority
        );
        
        ordersById.put(order.getOrderId(), order);
        pendingOrders.enqueue(order);
        allOrders.add(order);
        
        // Show confirmation AND simulated email
        JOptionPane.showMessageDialog(dialog, 
            "✅ Order created successfully!\n\n" +
            "Order ID: " + order.getOrderId() + "\n" +
            "Confirmation email will be sent to:\n" + order.getStudentEmail(),
            "Order Confirmation", JOptionPane.INFORMATION_MESSAGE);
        
        // Show simulated email notification
        EmailNotification.showOrderConfirmation(order);
        
        refreshOrdersTable();
        dialog.dispose();
    });
    
    cancelBtn.addActionListener(e -> dialog.dispose());
    
    dialog.add(saveBtn);
    dialog.add(cancelBtn);
    dialog.setVisible(true);
}

private boolean isValidEmail(String email) {
    if (email == null || email.isEmpty()) return false;
    
    // Check basic email format
    int atIndex = email.indexOf('@');
    if (atIndex <= 0 || atIndex == email.length() - 1) return false;
    
    // Check for dot after @
    int dotIndex = email.indexOf('.', atIndex);
    if (dotIndex <= atIndex + 1 || dotIndex == email.length() - 1) return false;
    
    // Check for common email domains
    String domain = email.substring(atIndex + 1).toLowerCase();
    return domain.contains("gmail.com") || 
           domain.contains("yahoo.com") || 
           domain.contains("student.") || 
           domain.contains("edu") ||
           domain.contains("outlook.com") ||
           domain.contains("hotmail.com");
}
    
    private void cancelSelectedOrder(JTable table) {
        int selectedRow = table.getSelectedRow();
        if (selectedRow >= 0) {
            String orderId = (String) ordersModel.getValueAt(selectedRow, 0);
            Order order = ordersById.get(orderId);
            if (order != null && !"Delivered".equals(order.getStatus())) {
                order.cancel();
                refreshOrdersTable();
                JOptionPane.showMessageDialog(this, "Order " + orderId + " cancelled!");
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please select an order first!");
        }
    }
    
  private void completeSelectedOrder(JTable table) {
    int selectedRow = table.getSelectedRow();
    if (selectedRow >= 0) {
        String orderId = (String) ordersModel.getValueAt(selectedRow, 0);
        Order order = ordersById.get(orderId);
        
        if (order != null && "Assigned".equals(order.getStatus())) {
            // Find the rider who delivered this order
            Rider deliveringRider = null;
            String riderId = order.getAssignedRiderId();
            
            if (riderId != null) {
                deliveringRider = ridersById.get(riderId);
            }
            
            // If rider not found by ID, find any delivering rider
            if (deliveringRider == null) {
                CustomHashMap.Entry<String, Rider>[] entries = ridersById.entries();
                if (entries != null) {
                    for (int i = 0; i < entries.length; i++) {
                        if (entries[i] != null && entries[i].value != null && 
                            "Delivering".equals(entries[i].value.getStatus())) {
                            deliveringRider = entries[i].value;
                            break;
                        }
                    }
                }
            }
            
            if (deliveringRider == null) {
                JOptionPane.showMessageDialog(this, 
                    "Could not find delivering rider for this order!");
                return;
            }
            
            // Mark order as delivered
            order.markDelivered();
            
            // Update rider status and location
            deliveringRider.completeDelivery(order.getDeliveryLocation());
            
            // Show success message
            JOptionPane.showMessageDialog(this, 
                "✅ Order " + orderId + " marked as delivered!\n\n" +
                "Rider " + deliveringRider.getRiderName() + " is now available.\n" +
                "Delivery confirmation email sent to student.",
                "Delivery Complete", JOptionPane.INFORMATION_MESSAGE);
            
            // Show simulated delivery confirmation email
            EmailNotification.showOrderDelivered(order, deliveringRider);
            
            refreshOrdersTable();
            refreshRidersTable();
            
        } else {
            JOptionPane.showMessageDialog(this, 
                "Order must be in 'Assigned' status!\n" +
                "Current status: " + (order != null ? order.getStatus() : "Not found"));
        }
    } else {
        JOptionPane.showMessageDialog(this, "Please select an order first!");
    }
}
    private void filterOrdersByStatus(String status) {
        ordersModel.setRowCount(0);
        
        for (int i = 0; i < allOrders.size(); i++) {
            Order order = allOrders.get(i);
            if (order != null) {
                if ("All".equals(status) || order.getStatus().equals(status)) {
                    ordersModel.addRow(new Object[]{
                        order.getOrderId(),
                        order.getStudentName(),
                        order.getPickupLocation(),
                        order.getDeliveryLocation(),
                        order.getPriority(),
                        order.getStatus()
                    });
                }
            }
        }
    }
    
    private void refreshOrdersTable() {
        filterOrdersByStatus("All");
    }
    
    // ========== 3️⃣ RIDER MANAGEMENT PANEL ==========
    private JPanel createRidersPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Top panel: Add rider and search
        JPanel topPanel = new JPanel(new GridLayout(2, 1, 5, 5));
        
        JPanel addPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton addRiderBtn = new JButton("Add New Rider");
        addRiderBtn.addActionListener(e -> showAddRiderDialog());
        addPanel.add(addRiderBtn);
        
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        searchPanel.add(new JLabel("Search Rider ID:"));
        JTextField searchField = new JTextField(15);
        JButton searchBtn = new JButton("Search");
        searchBtn.addActionListener(e -> searchRider(searchField.getText()));
        searchPanel.add(searchField);
        searchPanel.add(searchBtn);
        
        topPanel.add(addPanel);
        topPanel.add(searchPanel);
        
        // Riders table
        String[] columnNames = {"Rider ID", "Name", "Current Location", "Status"};
        ridersModel = new DefaultTableModel(columnNames, 0);
        JTable ridersTable = new JTable(ridersModel);
        refreshRidersTable();
        
        panel.add(topPanel, BorderLayout.NORTH);
        panel.add(new JScrollPane(ridersTable), BorderLayout.CENTER);
        
        return panel;
    }
    
    private void showAddRiderDialog() {
        JDialog dialog = new JDialog(this, "Add New Rider", true);
        dialog.setSize(300, 200);
        dialog.setLayout(new GridLayout(4, 2, 10, 10));
        
        JTextField riderIdField = new JTextField("R" + (ridersById.size() + 100));
        JTextField nameField = new JTextField();
        JComboBox<String> locationBox = getLocationComboBox();
        
        dialog.add(new JLabel("Rider ID:"));
        dialog.add(riderIdField);
        dialog.add(new JLabel("Name:"));
        dialog.add(nameField);
        dialog.add(new JLabel("Starting Location:"));
        dialog.add(locationBox);
        
        JButton saveBtn = new JButton("Save");
        JButton cancelBtn = new JButton("Cancel");
        
        saveBtn.addActionListener(e -> {
            if (nameField.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(dialog, "Please enter rider name!");
                return;
            }
            
            Rider rider = new Rider(
                riderIdField.getText(),
                nameField.getText(),
                (String) locationBox.getSelectedItem()
            );
            
            ridersById.put(rider.getRiderId(), rider);
            refreshRidersTable();
            JOptionPane.showMessageDialog(dialog, "Rider added successfully!");
            dialog.dispose();
        });
        
        cancelBtn.addActionListener(e -> dialog.dispose());
        
        dialog.add(saveBtn);
        dialog.add(cancelBtn);
        dialog.setVisible(true);
    }
    
    private void addRider(String id, String name, String location) {
        Rider rider = new Rider(id, name, location);
        ridersById.put(id, rider);
    }
    
    private void searchRider(String riderId) {
        if (riderId.trim().isEmpty()) {
            refreshRidersTable();
            return;
        }
        
        ridersModel.setRowCount(0);
        Rider rider = ridersById.get(riderId);
        if (rider != null) {
            ridersModel.addRow(new Object[]{
                rider.getRiderId(),
                rider.getRiderName(),
                rider.getCurrentLocation(),
                rider.getStatus()
            });
        } else {
            JOptionPane.showMessageDialog(this, "Rider not found!");
            refreshRidersTable();
        }
    }
    
    private void refreshRidersTable() {
        ridersModel.setRowCount(0);
        CustomHashMap.Entry<String, Rider>[] entries = ridersById.entries();
        if (entries != null) {
            for (int i = 0; i < entries.length; i++) {
                if (entries[i] != null && entries[i].value != null) {
                    Rider rider = entries[i].value;
                    ridersModel.addRow(new Object[]{
                        rider.getRiderId(),
                        rider.getRiderName(),
                        rider.getCurrentLocation(),
                        rider.getStatus()
                    });
                }
            }
        }
    }
    
    // ========== 4️⃣ ORDER ASSIGNMENT PANEL ==========
    private JPanel createAssignmentPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        JPanel contentPanel = new JPanel(new GridLayout(3, 1, 20, 20));
        
        // Manual Assignment
        JPanel manualPanel = new JPanel(new BorderLayout());
        manualPanel.setBorder(BorderFactory.createTitledBorder("Manual Assignment"));
        JTextArea manualInfo = new JTextArea(5, 50);
        manualInfo.setText("1. Select a pending order\n" +
                          "2. Select an available rider\n" +
                          "3. System calculates: Rider → Pickup → Delivery\n" +
                          "4. Updates status automatically");
        manualInfo.setEditable(false);
        JButton manualAssignBtn = new JButton("Open Manual Assignment");
        manualAssignBtn.addActionListener(e -> showManualAssignmentDialog());
        manualPanel.add(new JScrollPane(manualInfo), BorderLayout.CENTER);
        manualPanel.add(manualAssignBtn, BorderLayout.SOUTH);
        
        // Automatic Assignment
        JPanel autoPanel = new JPanel(new BorderLayout());
        autoPanel.setBorder(BorderFactory.createTitledBorder("Automatic Assignment"));
        JTextArea autoInfo = new JTextArea(5, 50);
        autoInfo.setText("System automatically:\n" +
                        "1. Picks highest priority pending order\n" +
                        "2. Finds nearest available rider\n" +
                        "3. Calculates shortest path\n" +
                        "4. Shows full route and distance");
        autoInfo.setEditable(false);
        JButton autoAssignBtn = new JButton("Auto Assign Next Order");
        autoAssignBtn.addActionListener(e -> autoAssignOrder());
        autoPanel.add(new JScrollPane(autoInfo), BorderLayout.CENTER);
        autoPanel.add(autoAssignBtn, BorderLayout.SOUTH);
        
        // Assignment Results
        JPanel resultPanel = new JPanel(new BorderLayout());
        resultPanel.setBorder(BorderFactory.createTitledBorder("Assignment Results"));
        JTextArea resultArea = new JTextArea(10, 50);
        resultArea.setEditable(false);
        resultArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        resultPanel.add(new JScrollPane(resultArea), BorderLayout.CENTER);
        
        contentPanel.add(manualPanel);
        contentPanel.add(autoPanel);
        contentPanel.add(resultPanel);
        
        panel.add(contentPanel, BorderLayout.CENTER);
        
        return panel;
    }
    
    private void showManualAssignmentDialog() {
        JDialog dialog = new JDialog(this, "Manual Order Assignment", true);
        dialog.setSize(500, 400);
        dialog.setLayout(new BorderLayout(10, 10));
        
        JPanel mainPanel = new JPanel(new GridLayout(3, 1, 10, 10));
        
        // Select Order
        JPanel orderPanel = new JPanel(new BorderLayout());
        orderPanel.setBorder(BorderFactory.createTitledBorder("1. Select Pending Order"));
        JComboBox<String> orderCombo = getPendingOrdersCombo();
        orderPanel.add(orderCombo, BorderLayout.CENTER);
        
        // Select Rider
        JPanel riderPanel = new JPanel(new BorderLayout());
        riderPanel.setBorder(BorderFactory.createTitledBorder("2. Select Available Rider"));
        JComboBox<String> riderCombo = getAvailableRidersCombo();
        riderPanel.add(riderCombo, BorderLayout.CENTER);
        
        // Assignment Info
        JPanel infoPanel = new JPanel(new BorderLayout());
        JTextArea infoArea = new JTextArea(8, 40);
        infoArea.setEditable(false);
        infoPanel.add(new JScrollPane(infoArea), BorderLayout.CENTER);
        
        mainPanel.add(orderPanel);
        mainPanel.add(riderPanel);
        mainPanel.add(infoPanel);
        
        JButton assignBtn = new JButton("Assign Order");
        assignBtn.addActionListener(e -> {
            String orderId = (String) orderCombo.getSelectedItem();
            String riderId = (String) riderCombo.getSelectedItem();
            
            if (orderId != null && riderId != null) {
                Order order = ordersById.get(orderId);
                Rider rider = ridersById.get(riderId);
                
                if (order != null && rider != null) {
                    // Calculate paths
                    CustomArrayList<String> path1 = campusMap.findShortestPath(
                        rider.getCurrentLocation(), order.getPickupLocation());
                    CustomArrayList<String> path2 = campusMap.findShortestPath(
                        order.getPickupLocation(), order.getDeliveryLocation());
                    
                    // Update status
                    order.assignToRider();
                    rider.startDelivery();
                    
                    // Display results
                    StringBuilder sb = new StringBuilder();
                    sb.append("ASSIGNMENT COMPLETE!\n\n");
                    sb.append("Order: ").append(orderId).append(" (").append(order.getStudentName()).append(")\n");
                    sb.append("Rider: ").append(riderId).append(" (").append(rider.getRiderName()).append(")\n\n");
                    sb.append("PATH:\n");
                    sb.append("Rider → Pickup (").append(path1.size() - 1).append(" segments):\n");
                    for (int i = 0; i < path1.size(); i++) {
                        sb.append("  ").append(path1.get(i));
                        if (i < path1.size() - 1) sb.append(" → ");
                    }
                    sb.append("\n\nPickup → Delivery (").append(path2.size() - 1).append(" segments):\n");
                    for (int i = 0; i < path2.size(); i++) {
                        sb.append("  ").append(path2.get(i));
                        if (i < path2.size() - 1) sb.append(" → ");
                    }
                    
                    infoArea.setText(sb.toString());
                    refreshOrdersTable();
                    refreshRidersTable();
                }
            }
        });
        
        dialog.add(mainPanel, BorderLayout.CENTER);
        dialog.add(assignBtn, BorderLayout.SOUTH);
        dialog.setVisible(true);
    }
    
    private JComboBox<String> getPendingOrdersCombo() {
        JComboBox<String> combo = new JComboBox<>();
        for (int i = 0; i < allOrders.size(); i++) {
            Order order = allOrders.get(i);
            if (order != null && "Pending".equals(order.getStatus())) {
                combo.addItem(order.getOrderId() + " - " + order.getStudentName());
            }
        }
        return combo;
    }
    
    private JComboBox<String> getAvailableRidersCombo() {
        JComboBox<String> combo = new JComboBox<>();
        CustomHashMap.Entry<String, Rider>[] entries = ridersById.entries();
        if (entries != null) {
            for (int i = 0; i < entries.length; i++) {
                if (entries[i] != null && entries[i].value != null && 
                    "Available".equals(entries[i].value.getStatus())) {
                    Rider rider = entries[i].value;
                    combo.addItem(rider.getRiderId() + " - " + rider.getRiderName());
                }
            }
        }
        return combo;
    }
    
    private JComboBox<String> getLocationComboBox() {
        JComboBox<String> combo = new JComboBox<>();
        // Get all locations from graph (simplified - in real app, get from graph)
        String[] sampleLocations = {"Hostel", "Library", "Cafe", "Block A", "Block B", "Sports Complex"};
        for (String loc : sampleLocations) {
            combo.addItem(loc);
        }
        return combo;
    }
    
private void autoAssignOrder() {
    if (pendingOrders.isEmpty()) {
        JOptionPane.showMessageDialog(this, "No pending orders!");
        return;
    }
    
    Order order = pendingOrders.peek();
    if (order == null) {
        JOptionPane.showMessageDialog(this, "No valid order to assign!");
        return;
    }
    
    // Find nearest available rider
    Rider nearestRider = null;
    int minDistance = Integer.MAX_VALUE;
    CustomArrayList<String> bestPath = null;
    
    CustomHashMap.Entry<String, Rider>[] entries = ridersById.entries();
    if (entries != null) {
        for (int i = 0; i < entries.length; i++) {
            if (entries[i] != null && entries[i].value != null && 
                "Available".equals(entries[i].value.getStatus())) {
                Rider rider = entries[i].value;
                CustomArrayList<String> path = campusMap.findShortestPath(
                    rider.getCurrentLocation(), order.getPickupLocation());
                if (path.size() > 0 && path.size() - 1 < minDistance) {
                    minDistance = path.size() - 1;
                    nearestRider = rider;
                    bestPath = path; // Store the best path
                }
            }
        }
    }
    
    if (nearestRider == null) {
        JOptionPane.showMessageDialog(this, "No available riders!");
        return;
    }
    
    // Calculate paths
    CustomArrayList<String> path1 = bestPath != null ? bestPath : 
        campusMap.findShortestPath(nearestRider.getCurrentLocation(), order.getPickupLocation());
    CustomArrayList<String> path2 = campusMap.findShortestPath(
        order.getPickupLocation(), order.getDeliveryLocation());
    
    // Update status - STORE RIDER ID IN ORDER
    order.assignToRider();
    order.setAssignedRiderId(nearestRider.getRiderId());  // NEW: Store rider ID
    nearestRider.startDelivery();
    pendingOrders.dequeue();
    
    // Show results with email information
    StringBuilder sb = new StringBuilder();
    sb.append("🚴 AUTO ASSIGNMENT COMPLETE!\n\n");
    sb.append("📋 ORDER DETAILS:\n");
    sb.append("────────────────────────\n");
    sb.append("Order ID:     ").append(order.getOrderId()).append("\n");
    sb.append("Student:      ").append(order.getStudentName()).append("\n");
    sb.append("Student Email:").append(order.getStudentEmail()).append("\n");
    sb.append("Priority:     ").append(order.getPriority() == 1 ? "🔴 URGENT" : "🟢 Normal").append("\n");
    sb.append("Pickup:       ").append(order.getPickupLocation()).append("\n");
    sb.append("Delivery:     ").append(order.getDeliveryLocation()).append("\n\n");
    
    sb.append("🚴 RIDER ASSIGNED:\n");
    sb.append("────────────────────────\n");
    sb.append("Rider Name:   ").append(nearestRider.getRiderName()).append("\n");
    sb.append("Rider ID:     ").append(nearestRider.getRiderId()).append("\n");
    sb.append("From Location:").append(nearestRider.getCurrentLocation()).append("\n");
    sb.append("Status:       ").append(nearestRider.getStatus()).append("\n\n");
    
    sb.append("🗺️ DELIVERY ROUTE:\n");
    sb.append("────────────────────────\n");
    sb.append("Total Distance: ").append((path1.size() - 1) + (path2.size() - 1)).append(" segments\n\n");
    
    sb.append("1. RIDER → PICKUP (").append(path1.size() - 1).append(" segments):\n");
    sb.append("   ");
    for (int i = 0; i < path1.size(); i++) {
        if (i == 0) {
            sb.append("🏍️ ").append(path1.get(i));
        } else {
            sb.append(" → ").append(path1.get(i));
        }
    }
    
    sb.append("\n\n2. PICKUP → DELIVERY (").append(path2.size() - 1).append(" segments):\n");
    sb.append("   ");
    for (int i = 0; i < path2.size(); i++) {
        if (i == 0) {
            sb.append("📦 ").append(path2.get(i));
        } else if (i == path2.size() - 1) {
            sb.append(" → 🎯 ").append(path2.get(i));
        } else {
            sb.append(" → ").append(path2.get(i));
        }
    }
    
    sb.append("\n\n⏱️ Estimated Delivery Time:\n");
    sb.append("────────────────────────\n");
    int totalSegments = (path1.size() - 1) + (path2.size() - 1);
    int estimatedMinutes = totalSegments * 3; // 3 minutes per segment
    sb.append("Total: ").append(totalSegments).append(" segments × 3 min/segment\n");
    sb.append("= ").append(estimatedMinutes).append(" minutes\n");
    
    // Calculate estimated delivery time
    int currentHour = (int) ((System.currentTimeMillis() / 3600000) % 24) + 8;
    int currentMinute = (int) ((System.currentTimeMillis() / 60000) % 60);
    int deliveryHour = currentHour + ((currentMinute + estimatedMinutes) / 60);
    int deliveryMinute = (currentMinute + estimatedMinutes) % 60;
    sb.append("Estimated Arrival: ").append(String.format("%02d:%02d", deliveryHour % 24, deliveryMinute)).append("\n");
    
    sb.append("\n📧 Notification:\n");
    sb.append("────────────────────────\n");
    sb.append("Rider assignment notification would be sent to:\n");
    sb.append(order.getStudentEmail()).append("\n");
    sb.append("\nStudent can track delivery in the Orders tab.");
    
    // Show assignment confirmation
    int result = JOptionPane.showConfirmDialog(this, sb.toString(), 
        "🚴 Auto Assignment Complete", JOptionPane.OK_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE);
    
    if (result == JOptionPane.OK_OPTION) {
        // Show simulated rider assignment notification
        JOptionPane.showMessageDialog(this,
            "📧 Rider Assignment Notification (Simulated)\n\n" +
            "An email notification would be sent to:\n" +
            order.getStudentEmail() + "\n\n" +
            "Subject: \"🚴 Rider " + nearestRider.getRiderName() + 
            " assigned to your order " + order.getOrderId() + "\"\n\n" +
            "Content: Includes rider details and estimated delivery time.",
            "Email Notification", JOptionPane.INFORMATION_MESSAGE);
    }
    
    refreshOrdersTable();
    refreshRidersTable();
}
    
    // ========== 5️⃣ STATISTICS PANEL ==========
    private JPanel createStatisticsPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        JTextArea statsArea = new JTextArea();
        statsArea.setEditable(false);
        statsArea.setFont(new Font("Monospaced", Font.PLAIN, 14));
        updateStatistics(statsArea);
        
        JButton refreshBtn = new JButton("Refresh Statistics");
        refreshBtn.addActionListener(e -> updateStatistics(statsArea));
        
        panel.add(new JScrollPane(statsArea), BorderLayout.CENTER);
        panel.add(refreshBtn, BorderLayout.SOUTH);
        
        return panel;
    }
    
    private void updateStatistics(JTextArea area) {
        int totalOrders = allOrders.size();
        int pendingOrders = 0;
        int assignedOrders = 0;
        int deliveredOrders = 0;
        int urgentOrders = 0;
        
        for (int i = 0; i < allOrders.size(); i++) {
            Order order = allOrders.get(i);
            if (order != null) {
                switch (order.getStatus()) {
                    case "Pending": pendingOrders++; break;
                    case "Assigned": assignedOrders++; break;
                    case "Delivered": deliveredOrders++; break;
                }
                if (order.getPriority() == 1) urgentOrders++;
            }
        }
        
        int availableRiders = 0;
        CustomHashMap.Entry<String, Rider>[] entries = ridersById.entries();
        if (entries != null) {
            for (int i = 0; i < entries.length; i++) {
                if (entries[i] != null && entries[i].value != null && 
                    "Available".equals(entries[i].value.getStatus())) {
                    availableRiders++;
                }
            }
        }
        
        StringBuilder sb = new StringBuilder();
        sb.append("CAMPUS FOOD DELIVERY SYSTEM - STATISTICS\n");
        sb.append("══════════════════════════════════════════════════\n\n");
        
        sb.append("ORDER STATISTICS:\n");
        sb.append("──────────────────────────────────────────────────\n");
        sb.append(String.format("Total Orders:          %d\n", totalOrders));
        sb.append(String.format("Pending Orders:        %d\n", pendingOrders));
        sb.append(String.format("Assigned Orders:       %d\n", assignedOrders));
        sb.append(String.format("Delivered Orders:      %d\n", deliveredOrders));
        sb.append(String.format("Urgent Orders:         %d\n", urgentOrders));
        sb.append(String.format("Delivery Rate:         %.1f%%\n", 
            totalOrders > 0 ? (deliveredOrders * 100.0 / totalOrders) : 0));
        
        sb.append("\nRIDER STATISTICS:\n");
        sb.append("──────────────────────────────────────────────────\n");
        sb.append(String.format("Total Riders:          %d\n", ridersById.size()));
        sb.append(String.format("Available Riders:      %d\n", availableRiders));
        sb.append(String.format("Busy Riders:           %d\n", ridersById.size() - availableRiders));
        
        sb.append("\nLOCATION STATISTICS:\n");
        sb.append("──────────────────────────────────────────────────\n");
        sb.append(String.format("Total Locations:       %d\n", campusMap.getNodeCount()));
        sb.append(String.format("Total Routes:          %d\n", campusMap.getEdgeCount()));
        
        sb.append("\nSYSTEM PERFORMANCE:\n");
        sb.append("──────────────────────────────────────────────────\n");
        sb.append("Shortest Path Algorithm: BFS (Breadth-First Search)\n");
        sb.append("Time Complexity: O(V + E)\n");
        sb.append("Data Structures Used: Graph, Priority Queue, HashMap, ArrayList\n");
        
        area.setText(sb.toString());
    }
    
    private void saveAllData() {
    // Since java.io is not allowed, we cannot implement file saving
    // In a real application, this would save data to files
    JOptionPane.showMessageDialog(this,
        "FILE SAVING DISABLED\n\n" +
        "Project restrictions prohibit using java.io package.\n" +
        "In a real implementation, data would be saved to:\n" +
        "• locations.txt - Graph nodes and edges\n" +
        "• orders.txt - All order records\n" +
        "• riders.txt - Rider information\n\n" +
        "Current implementation uses in-memory data structures only.",
        "Save Feature (Conceptual)", JOptionPane.INFORMATION_MESSAGE);
}

private void loadAllData() {
    JOptionPane.showMessageDialog(this,
        "FILE LOADING DISABLED\n\n" +
        "Project restrictions prohibit using java.io package.\n" +
        "System starts with sample data for demonstration.",
        "Load Feature (Conceptual)", JOptionPane.INFORMATION_MESSAGE);
}

private void undoLastAction() {
    // Undo feature would require tracking operations
    // Could be implemented with a CustomStack
    JOptionPane.showMessageDialog(this,
        "UNDO FEATURE (Conceptual)\n\n" +
        "This would be implemented using a Stack data structure:\n" +
        "1. Push each operation onto stack\n" +
        "2. Pop to undo last action\n" +
        "3. Time complexity: O(1) for push/pop\n\n" +
        "Required: CustomStack class (not implemented due to scope)",
        "Undo Feature", JOptionPane.INFORMATION_MESSAGE);
}
}