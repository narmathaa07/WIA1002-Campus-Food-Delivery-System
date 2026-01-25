/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.fooddelivery3;

/**
 *
 * @author ASUS
 */


import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * FileManager handles saving and loading all system data to/from CSV files
 */
public class FileManager {
    
    // File paths - now with .csv extension
    public static final String DATA_FOLDER = "food_delivery_data/";
    public static final String LOCATIONS_FILE = DATA_FOLDER + "locations.csv";
    public static final String ROUTES_FILE = DATA_FOLDER + "routes.csv";
    public static final String ORDERS_FILE = DATA_FOLDER + "orders.csv";
    public static final String RIDERS_FILE = DATA_FOLDER + "riders.csv";
    public static final String BACKUP_FOLDER = DATA_FOLDER + "backups/";
    
    // Save all system data
    public static boolean saveAllData(FoodDeliverySystem system) {
        try {
            // Create data and backup folders if they don't exist
            new File(DATA_FOLDER).mkdirs();
            new File(BACKUP_FOLDER).mkdirs();
            
            // Save backup with timestamp
            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
            
            // Save current data
            boolean success = saveCurrentData(system);
            
            if (success) {
                // Create backup copies with .bak extension
                copyFile(LOCATIONS_FILE, BACKUP_FOLDER + "locations_" + timestamp + ".csv");
                copyFile(ROUTES_FILE, BACKUP_FOLDER + "routes_" + timestamp + ".csv");
                copyFile(ORDERS_FILE, BACKUP_FOLDER + "orders_" + timestamp + ".csv");
                copyFile(RIDERS_FILE, BACKUP_FOLDER + "riders_" + timestamp + ".csv");
                
                return true;
            }
            return false;
            
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
    // Save current data (without backup)
    public static boolean saveCurrentData(FoodDeliverySystem system) {
        try {
            return saveLocationsCSV(system.campusMap) &&
                   saveRoutesCSV(system.campusMap) &&
                   saveOrdersCSV(system.ordersById, system.allOrders) &&
                   saveRidersCSV(system.ridersById);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
    // Load all data
    public static boolean loadAllData(FoodDeliverySystem system) {
        try {
            // Clear current data
            system.campusMap = new Graph();
            system.ordersById.clear();
            system.ridersById.clear();
            system.pendingOrders = new PriorityQueue(100);
            system.allOrders.clear();
            
            // Load data from CSV files
            boolean locationsLoaded = loadLocationsCSV(system.campusMap);
            boolean routesLoaded = loadRoutesCSV(system.campusMap);
            boolean ordersLoaded = loadOrdersCSV(system);
            boolean ridersLoaded = loadRidersCSV(system);
            
            // Rebuild priority queue from loaded orders
            rebuildPriorityQueue(system);
            
            return locationsLoaded || routesLoaded || ordersLoaded || ridersLoaded;
            
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
    // ==================== LOCATIONS CSV SAVE/LOAD ====================
    
    private static boolean saveLocationsCSV(Graph graph) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(LOCATIONS_FILE))) {
            // CSV Header
            writer.println("LocationID,LocationName");
            writer.println("# Generated: " + LocalDateTime.now());
            
            CustomArrayList<String> locations = graph.getAllNodes();
            int id = 1;
            for (int i = 0; i < locations.size(); i++) {
                String location = locations.get(i);
                if (location != null) {
                    writer.println(id + "," + escapeCSV(location));
                    id++;
                }
            }
            
            return true;
        } catch (IOException e) {
            return false;
        }
    }
    
    private static boolean loadLocationsCSV(Graph graph) {
        File file = new File(LOCATIONS_FILE);
        if (!file.exists()) return false;
        
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            boolean isFirstLine = true;
            
            while ((line = reader.readLine()) != null) {
                // Skip empty lines and comments
                if (line.trim().isEmpty() || line.startsWith("#")) {
                    continue;
                }
                
                // Skip header
                if (isFirstLine && line.toLowerCase().contains("locationid")) {
                    isFirstLine = false;
                    continue;
                }
                isFirstLine = false;
                
                String[] parts = line.split(",", -1);
                if (parts.length >= 2) {
                    // parts[0] is LocationID, parts[1] is LocationName
                    String locationName = unescapeCSV(parts[1].trim());
                    graph.addNode(locationName);
                }
            }
            return true;
        } catch (IOException e) {
            return false;
        }
    }
    
    // ==================== ROUTES CSV SAVE/LOAD ====================
    
    private static boolean saveRoutesCSV(Graph graph) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(ROUTES_FILE))) {
            // CSV Header
            writer.println("RouteID,FromLocation,ToLocation,Distance(minutes)");
            writer.println("# Generated: " + LocalDateTime.now());
            
            CustomArrayList<String> locations = graph.getAllNodes();
            int routeId = 1;
            
            for (int i = 0; i < locations.size(); i++) {
                String from = locations.get(i);
                if (from == null) continue;
                
                CustomArrayList<Graph.Edge> edges = graph.getEdges(from);
                if (edges != null) {
                    for (int j = 0; j < edges.size(); j++) {
                        Graph.Edge edge = edges.get(j);
                        if (edge != null) {
                            // Avoid duplicate entries (undirected graph)
                            if (from.compareTo(edge.destination) < 0) {
                                writer.println(routeId + "," + 
                                        escapeCSV(from) + "," + 
                                        escapeCSV(edge.destination) + "," + 
                                        edge.distance);
                                routeId++;
                            }
                        }
                    }
                }
            }
            
            return true;
        } catch (IOException e) {
            return false;
        }
    }
    
    private static boolean loadRoutesCSV(Graph graph) {
        File file = new File(ROUTES_FILE);
        if (!file.exists()) return false;
        
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            boolean isFirstLine = true;
            
            while ((line = reader.readLine()) != null) {
                // Skip empty lines and comments
                if (line.trim().isEmpty() || line.startsWith("#")) {
                    continue;
                }
                
                // Skip header
                if (isFirstLine && line.toLowerCase().contains("routeid")) {
                    isFirstLine = false;
                    continue;
                }
                isFirstLine = false;
                
                String[] parts = line.split(",", -1);
                if (parts.length >= 4) {
                    try {
                        String from = unescapeCSV(parts[1].trim());
                        String to = unescapeCSV(parts[2].trim());
                        int distance = Integer.parseInt(parts[3].trim());
                        graph.addEdge(from, to, distance);
                    } catch (NumberFormatException e) {
                        // Skip invalid line
                    }
                }
            }
            return true;
        } catch (IOException e) {
            return false;
        }
    }
    
    // ==================== ORDERS CSV SAVE/LOAD ====================
    
    private static boolean saveOrdersCSV(CustomHashMap<String, Order> ordersById, 
                                         CustomArrayList<Order> allOrders) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(ORDERS_FILE))) {
            // CSV Header
            writer.println("OrderID,StudentName,StudentEmail,PickupLocation,DeliveryLocation,Priority,Status,AssignedRiderID,OrderTime");
            writer.println("# Priority: 1=URGENT, 2=Normal");
            writer.println("# Status: Pending,Assigned,Delivered,Cancelled");
            writer.println("# Generated: " + LocalDateTime.now());
            
            for (int i = 0; i < allOrders.size(); i++) {
                Order order = allOrders.get(i);
                if (order != null) {
                    writer.printf("%s,%s,%s,%s,%s,%d,%s,%s,%d%n",
                            escapeCSV(order.getOrderId()),
                            escapeCSV(order.getStudentName()),
                            escapeCSV(order.getStudentEmail()),
                            escapeCSV(order.getPickupLocation()),
                            escapeCSV(order.getDeliveryLocation()),
                            order.getPriority(),
                            escapeCSV(order.getStatus()),
                            order.getAssignedRiderId() != null ? escapeCSV(order.getAssignedRiderId()) : "",
                            order.getOrderTime());
                }
            }
            
            return true;
        } catch (IOException e) {
            return false;
        }
    }
    
    private static boolean loadOrdersCSV(FoodDeliverySystem system) {
        File file = new File(ORDERS_FILE);
        if (!file.exists()) return false;
        
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            boolean isFirstLine = true;
            
            while ((line = reader.readLine()) != null) {
                // Skip empty lines and comments
                if (line.trim().isEmpty() || line.startsWith("#")) {
                    continue;
                }
                
                // Skip header
                if (isFirstLine && line.toLowerCase().contains("orderid")) {
                    isFirstLine = false;
                    continue;
                }
                isFirstLine = false;
                
                String[] parts = line.split(",", -1);
                if (parts.length >= 8) {
                    try {
                        String orderId = unescapeCSV(parts[0].trim());
                        String studentName = unescapeCSV(parts[1].trim());
                        String studentEmail = unescapeCSV(parts[2].trim());
                        String pickup = unescapeCSV(parts[3].trim());
                        String delivery = unescapeCSV(parts[4].trim());
                        int priority = Integer.parseInt(parts[5].trim());
                        String status = unescapeCSV(parts[6].trim());
                        String assignedRiderId = parts[7].trim().isEmpty() ? null : unescapeCSV(parts[7].trim());
                        int orderTime = parts.length > 8 ? Integer.parseInt(parts[8].trim()) : (int) (System.currentTimeMillis() % 1000000);
                        
                        // Create order
                        Order order = new Order(orderId, studentName, studentEmail, pickup, delivery, priority);
                        order.setStatus(status);
                        if (assignedRiderId != null && !assignedRiderId.isEmpty()) {
                            order.setAssignedRiderId(assignedRiderId);
                        }
                        
                        // Add to system
                        system.ordersById.put(orderId, order);
                        system.allOrders.add(order);
                        
                        // Note: Priority queue will be rebuilt separately
                        
                    } catch (Exception e) {
                        System.err.println("Error loading order: " + e.getMessage());
                        // Skip invalid line
                    }
                }
            }
            return true;
        } catch (IOException e) {
            return false;
        }
    }
    
    // ==================== RIDERS CSV SAVE/LOAD ====================
    
    private static boolean saveRidersCSV(CustomHashMap<String, Rider> ridersById) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(RIDERS_FILE))) {
            // CSV Header
            writer.println("RiderID,RiderName,CurrentLocation,Status");
            writer.println("# Generated: " + LocalDateTime.now());
            
            CustomHashMap.Entry<String, Rider>[] entries = ridersById.entries();
            if (entries != null) {
                for (int i = 0; i < entries.length; i++) {
                    if (entries[i] != null && entries[i].value != null) {
                        Rider rider = entries[i].value;
                        writer.printf("%s,%s,%s,%s%n",
                                escapeCSV(rider.getRiderId()),
                                escapeCSV(rider.getRiderName()),
                                escapeCSV(rider.getCurrentLocation()),
                                escapeCSV(rider.getStatus()));
                    }
                }
            }
            
            return true;
        } catch (IOException e) {
            return false;
        }
    }
    
    private static boolean loadRidersCSV(FoodDeliverySystem system) {
        File file = new File(RIDERS_FILE);
        if (!file.exists()) return false;
        
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            boolean isFirstLine = true;
            
            while ((line = reader.readLine()) != null) {
                // Skip empty lines and comments
                if (line.trim().isEmpty() || line.startsWith("#")) {
                    continue;
                }
                
                // Skip header
                if (isFirstLine && line.toLowerCase().contains("riderid")) {
                    isFirstLine = false;
                    continue;
                }
                isFirstLine = false;
                
                String[] parts = line.split(",", -1);
                if (parts.length >= 4) {
                    try {
                        String riderId = unescapeCSV(parts[0].trim());
                        String riderName = unescapeCSV(parts[1].trim());
                        String location = unescapeCSV(parts[2].trim());
                        String status = unescapeCSV(parts[3].trim());
                        
                        Rider rider = new Rider(riderId, riderName, location);
                        rider.setStatus(status);
                        system.ridersById.put(riderId, rider);
                        
                    } catch (Exception e) {
                        // Skip invalid line
                    }
                }
            }
            return true;
        } catch (IOException e) {
            return false;
        }
    }
    
    // ==================== HELPER METHODS ====================
    
    private static void rebuildPriorityQueue(FoodDeliverySystem system) {
        // Clear existing priority queue
        system.pendingOrders = new PriorityQueue(100);
        
        // Add pending orders to priority queue
        for (int i = 0; i < system.allOrders.size(); i++) {
            Order order = system.allOrders.get(i);
            if (order != null && "Pending".equals(order.getStatus())) {
                system.pendingOrders.enqueue(order);
            }
        }
    }
    
    // Proper CSV escaping (handles commas, quotes, and newlines)
    private static String escapeCSV(String text) {
        if (text == null) return "";
        
        // If text contains comma, quote, or newline, wrap in quotes
        if (text.contains(",") || text.contains("\"") || text.contains("\n")) {
            // Escape quotes by doubling them
            text = text.replace("\"", "\"\"");
            return "\"" + text + "\"";
        }
        return text;
    }
    
    private static String unescapeCSV(String text) {
        if (text == null || text.isEmpty()) return "";
        
        // Remove surrounding quotes if present
        if (text.startsWith("\"") && text.endsWith("\"")) {
            text = text.substring(1, text.length() - 1);
            // Unescape doubled quotes
            text = text.replace("\"\"", "\"");
        }
        return text;
    }
    
    private static void copyFile(String sourcePath, String destPath) throws IOException {
        File source = new File(sourcePath);
        File dest = new File(destPath);
        
        if (source.exists()) {
            try (InputStream in = new FileInputStream(source);
                 OutputStream out = new FileOutputStream(dest)) {
                byte[] buffer = new byte[1024];
                int length;
                while ((length = in.read(buffer)) > 0) {
                    out.write(buffer, 0, length);
                }
            }
        }
    }
    
    // ==================== UI METHODS ====================
    
    public static void showSaveDialog(FoodDeliverySystem system, Component parent) {
        int choice = JOptionPane.showConfirmDialog(parent,
                "Save all data to CSV files?\n\n" +
                "Will save:\n" +
                "• locations.csv - All campus locations\n" +
                "• routes.csv - All routes with distances\n" +
                "• orders.csv - All orders with details\n" +
                "• riders.csv - All riders with status\n\n" +
                "Previous files will be backed up automatically.",
                "Save Data to CSV",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE);
        
        if (choice == JOptionPane.YES_OPTION) {
            boolean success = FileManager.saveAllData(system);
            
            if (success) {
                JOptionPane.showMessageDialog(parent,
                        "✅ Data saved successfully to CSV files!\n\n" +
                        "Files saved in: " + new File(DATA_FOLDER).getAbsolutePath() + "\n" +
                        "• locations.csv\n" +
                        "• routes.csv\n" +
                        "• orders.csv\n" +
                        "• riders.csv\n\n" +
                        "Backups created in: " + new File(BACKUP_FOLDER).getAbsolutePath(),
                        "Save Successful",
                        JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(parent,
                        "❌ Failed to save data!\nCheck file permissions.",
                        "Save Failed",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    public static void showLoadDialog(FoodDeliverySystem system, Component parent) {
        // Check if any CSV files exist
        boolean filesExist = new File(LOCATIONS_FILE).exists() ||
                            new File(ROUTES_FILE).exists() ||
                            new File(ORDERS_FILE).exists() ||
                            new File(RIDERS_FILE).exists();
        
        if (!filesExist) {
            JOptionPane.showMessageDialog(parent,
                    "No CSV data files found!\n\n" +
                    "Looking in: " + new File(DATA_FOLDER).getAbsolutePath() + "\n" +
                    "Expected files:\n" +
                    "• locations.csv\n" +
                    "• routes.csv\n" +
                    "• orders.csv\n" +
                    "• riders.csv\n\n" +
                    "Please save data first before loading.",
                    "No CSV Files Found",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        int choice = JOptionPane.showConfirmDialog(parent,
                "Load data from CSV files?\n\n" +
                "⚠️ Warning: This will replace ALL current data!\n\n" +
                "Files to load from " + DATA_FOLDER + ":\n" +
                (new File(LOCATIONS_FILE).exists() ? "✓ locations.csv\n" : "✗ locations.csv\n") +
                (new File(ROUTES_FILE).exists() ? "✓ routes.csv\n" : "✗ routes.csv\n") +
                (new File(ORDERS_FILE).exists() ? "✓ orders.csv\n" : "✗ orders.csv\n") +
                (new File(RIDERS_FILE).exists() ? "✓ riders.csv\n" : "✗ riders.csv\n"),
                "Load Data from CSV",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);
        
        if (choice == JOptionPane.YES_OPTION) {
            boolean success = FileManager.loadAllData(system);
            
            if (success) {
                JOptionPane.showMessageDialog(parent,
                        "✅ Data loaded successfully from CSV files!\n" +
                        "System has been updated with saved data.",
                        "Load Successful",
                        JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(parent,
                        "❌ Failed to load data!\n" +
                        "CSV files might be corrupted or in wrong format.\n" +
                        "Check the file format matches the expected headers.",
                        "Load Failed",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    public static void showBackupInfoDialog(Component parent) {
        File backupDir = new File(BACKUP_FOLDER);
        if (!backupDir.exists()) {
            JOptionPane.showMessageDialog(parent,
                    "No backup folder exists yet.\n" +
                    "Backups are created automatically when you save data.\n\n" +
                    "Backup location: " + backupDir.getAbsolutePath(),
                    "Backup Information",
                    JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        
        File[] backups = backupDir.listFiles((dir, name) -> name.toLowerCase().endsWith(".csv"));
        if (backups == null || backups.length == 0) {
            JOptionPane.showMessageDialog(parent,
                    "Backup folder exists but no CSV backups found.\n\n" +
                    "Location: " + backupDir.getAbsolutePath(),
                    "Backup Information",
                    JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        
        // Sort by date (newest first)
        java.util.Arrays.sort(backups, (f1, f2) -> Long.compare(f2.lastModified(), f1.lastModified()));
        
        StringBuilder sb = new StringBuilder();
        sb.append("📂 CSV BACKUP FILES\n");
        sb.append("══════════════════════════════════════\n\n");
        sb.append("Location: ").append(backupDir.getAbsolutePath()).append("\n\n");
        
        for (File backup : backups) {
            if (backup.isFile()) {
                String filename = backup.getName();
                String fileType = filename.contains("locations") ? "Locations" :
                                 filename.contains("routes") ? "Routes" :
                                 filename.contains("orders") ? "Orders" :
                                 filename.contains("riders") ? "Riders" : "Unknown";
                
                sb.append("• ").append(filename).append("\n");
                sb.append("  Type: ").append(fileType).append("\n");
                sb.append("  Size: ").append(backup.length()).append(" bytes\n");
                sb.append("  Date: ").append(
                        new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
                        .format(backup.lastModified())).append("\n\n");
            }
        }
        
        sb.append("Total CSV backups: ").append(backups.length).append("\n");
        
        JTextArea area = new JTextArea(sb.toString());
        area.setEditable(false);
        area.setFont(new Font("Monospaced", Font.PLAIN, 12));
        area.setMargin(new Insets(10, 10, 10, 10));
        
        JOptionPane.showMessageDialog(parent,
                new JScrollPane(area),
                "CSV Backup Files",
                JOptionPane.INFORMATION_MESSAGE);
    }
    
    public static void showFileLocationsDialog(Component parent) {
        StringBuilder sb = new StringBuilder();
        sb.append("📁 CSV FILE LOCATIONS\n");
        sb.append("══════════════════════════════════════\n\n");
        
        sb.append("Data Folder:\n");
        sb.append(new File(DATA_FOLDER).getAbsolutePath()).append("\n\n");
        
        sb.append("CSV Files:\n");
        String[] files = {LOCATIONS_FILE, ROUTES_FILE, ORDERS_FILE, RIDERS_FILE};
        String[] descriptions = {"Locations", "Routes", "Orders", "Riders"};
        
        for (int i = 0; i < files.length; i++) {
            File file = new File(files[i]);
            sb.append(i+1).append(". ").append(descriptions[i]).append(" (");
            sb.append(file.getName()).append("):\n");
            
            if (file.exists()) {
                sb.append("   ✓ EXISTS\n");
                sb.append("   Path: ").append(file.getAbsolutePath()).append("\n");
                sb.append("   Size: ").append(file.length()).append(" bytes\n");
                
                try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                    String firstLine = reader.readLine();
                    if (firstLine != null && !firstLine.startsWith("#")) {
                        sb.append("   Header: ").append(firstLine).append("\n");
                    }
                } catch (IOException e) {
                    // Ignore
                }
            } else {
                sb.append("   ✗ NOT FOUND\n");
                sb.append("   Will be created at: ").append(file.getAbsolutePath()).append("\n");
            }
            sb.append("\n");
        }
        
        // Check backup folder
        File backupDir = new File(BACKUP_FOLDER);
        sb.append("Backup Folder:\n");
        sb.append("• ").append(BACKUP_FOLDER).append(": ");
        if (backupDir.exists()) {
            File[] csvBackups = backupDir.listFiles((dir, name) -> name.toLowerCase().endsWith(".csv"));
            sb.append("✓ EXISTS\n");
            sb.append("  Path: ").append(backupDir.getAbsolutePath()).append("\n");
            sb.append("  CSV Backups: ").append(csvBackups != null ? csvBackups.length : 0).append(" files\n");
        } else {
            sb.append("✗ NOT FOUND (will be created on first save)\n");
        }
        
        JTextArea area = new JTextArea(sb.toString());
        area.setEditable(false);
        area.setFont(new Font("Monospaced", Font.PLAIN, 12));
        area.setMargin(new Insets(10, 10, 10, 10));
        
        JOptionPane.showMessageDialog(parent,
                new JScrollPane(area),
                "CSV File Locations",
                JOptionPane.INFORMATION_MESSAGE);
    }
    
    // ==================== EXPORT TO EXCEL-READY CSV ====================
    
    public static void exportToExcelFormat(FoodDeliverySystem system, Component parent) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Export All Data to Single CSV File");
        fileChooser.setSelectedFile(new File("food_delivery_export.csv"));
        
        if (fileChooser.showSaveDialog(parent) == JFileChooser.APPROVE_OPTION) {
            File exportFile = fileChooser.getSelectedFile();
            
            try (PrintWriter writer = new PrintWriter(new FileWriter(exportFile))) {
                // Write Excel-compatible CSV with proper headers
                writer.println("CAMPUS FOOD DELIVERY SYSTEM - DATA EXPORT");
                writer.println("Exported:," + LocalDateTime.now());
                writer.println();
                
                // 1. LOCATIONS
                writer.println("LOCATIONS");
                writer.println("LocationID,LocationName");
                CustomArrayList<String> locations = system.campusMap.getAllNodes();
                int locId = 1;
                for (int i = 0; i < locations.size(); i++) {
                    String location = locations.get(i);
                    if (location != null) {
                        writer.println(locId + "," + escapeCSV(location));
                        locId++;
                    }
                }
                writer.println();
                
                // 2. ROUTES
                writer.println("ROUTES");
                writer.println("RouteID,FromLocation,ToLocation,Distance(minutes)");
                int routeId = 1;
                for (int i = 0; i < locations.size(); i++) {
                    String from = locations.get(i);
                    if (from == null) continue;
                    
                    CustomArrayList<Graph.Edge> edges = system.campusMap.getEdges(from);
                    if (edges != null) {
                        for (int j = 0; j < edges.size(); j++) {
                            Graph.Edge edge = edges.get(j);
                            if (edge != null && from.compareTo(edge.destination) < 0) {
                                writer.println(routeId + "," + 
                                        escapeCSV(from) + "," + 
                                        escapeCSV(edge.destination) + "," + 
                                        edge.distance);
                                routeId++;
                            }
                        }
                    }
                }
                writer.println();
                
                // 3. ORDERS
                writer.println("ORDERS");
                writer.println("OrderID,StudentName,StudentEmail,PickupLocation,DeliveryLocation,Priority,Status,AssignedRiderID,OrderTime");
                for (int i = 0; i < system.allOrders.size(); i++) {
                    Order order = system.allOrders.get(i);
                    if (order != null) {
                        writer.printf("%s,%s,%s,%s,%s,%d,%s,%s,%d%n",
                                escapeCSV(order.getOrderId()),
                                escapeCSV(order.getStudentName()),
                                escapeCSV(order.getStudentEmail()),
                                escapeCSV(order.getPickupLocation()),
                                escapeCSV(order.getDeliveryLocation()),
                                order.getPriority(),
                                escapeCSV(order.getStatus()),
                                order.getAssignedRiderId() != null ? escapeCSV(order.getAssignedRiderId()) : "",
                                order.getOrderTime());
                    }
                }
                writer.println();
                
                // 4. RIDERS
                writer.println("RIDERS");
                writer.println("RiderID,RiderName,CurrentLocation,Status");
                CustomHashMap.Entry<String, Rider>[] entries = system.ridersById.entries();
                if (entries != null) {
                    for (int i = 0; i < entries.length; i++) {
                        if (entries[i] != null && entries[i].value != null) {
                            Rider rider = entries[i].value;
                            writer.printf("%s,%s,%s,%s%n",
                                    escapeCSV(rider.getRiderId()),
                                    escapeCSV(rider.getRiderName()),
                                    escapeCSV(rider.getCurrentLocation()),
                                    escapeCSV(rider.getStatus()));
                        }
                    }
                }
                
                JOptionPane.showMessageDialog(parent,
                        "✅ Data exported successfully to:\n" + 
                        exportFile.getAbsolutePath() + "\n\n" +
                        "This CSV file can be opened in Excel, Google Sheets, or any spreadsheet software.",
                        "Export Successful",
                        JOptionPane.INFORMATION_MESSAGE);
                
            } catch (IOException e) {
                JOptionPane.showMessageDialog(parent,
                        "❌ Failed to export data: " + e.getMessage(),
                        "Export Failed",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}