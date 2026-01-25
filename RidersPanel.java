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
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class RidersPanel extends JPanel {

    private final FoodDeliverySystem system;
    private final DefaultTableModel model;
    private final JTable table;
    private final JTextField searchField;
    private TableRowSorter<DefaultTableModel> sorter;

    public RidersPanel(FoodDeliverySystem system) {
        this.system = system;
        setLayout(new BorderLayout(16, 16));

        JPanel page = UITheme.gradientPage(new BorderLayout(16, 16));
        add(page, BorderLayout.CENTER);

        page.add(header(), BorderLayout.NORTH);

        // Main Card
        JPanel card = UITheme.cardPanel(new BorderLayout(12, 12));
        page.add(card, BorderLayout.CENTER);

        // Search Panel
        JPanel searchPanel = new JPanel(new BorderLayout(10, 0));
        searchPanel.setOpaque(false);
        searchPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
        
        JLabel searchLabel = new JLabel("🔍 Search Riders:");
        searchLabel.setFont(UITheme.headingFont(14));
        
        searchField = new JTextField();
        searchField.setFont(UITheme.bodyFont(13));
        searchField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(180, 220, 195), 1),
            BorderFactory.createEmptyBorder(8, 10, 8, 10)
        ));
        searchField.setToolTipText("Search by ID, Name, Location, or Status");
        
        JButton clearSearchBtn = new JButton("Clear");
        UITheme.styleButtonSecondary(clearSearchBtn);
        clearSearchBtn.setPreferredSize(new Dimension(80, 35));
        
        searchPanel.add(searchLabel, BorderLayout.WEST);
        searchPanel.add(searchField, BorderLayout.CENTER);
        searchPanel.add(clearSearchBtn, BorderLayout.EAST);
        
        card.add(searchPanel, BorderLayout.NORTH);

        // Table
        String[] cols = { "Rider ID", "Name", "Current Location", "Status" };
        model = new DefaultTableModel(cols, 0) {
            @Override
            public boolean isCellEditable(int row, int col) {
                return false;
            }
            
            @Override
            public Class<?> getColumnClass(int column) {
                return String.class;
            }
        };

        table = new JTable(model);
        table.setRowHeight(32);
        table.setFont(UITheme.bodyFont(14));
        table.getTableHeader().setFont(UITheme.headingFont(13));
        
        // Enable sorting
        sorter = new TableRowSorter<>(model);
        table.setRowSorter(sorter);
        
        // Status pill renderer (Status = col index 3)
        table.getColumnModel().getColumn(3).setCellRenderer(new RiderStatusCellRenderer());

        JScrollPane sp = new JScrollPane(table);
        sp.setBorder(BorderFactory.createEmptyBorder());
        card.add(sp, BorderLayout.CENTER);

        // Buttons Row
        JPanel actions = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        actions.setOpaque(false);

        JButton addBtn = new JButton("➕ Add Rider");
        JButton deleteBtn = new JButton("🗑️ Delete Rider");
        JButton setAvailableBtn = new JButton("🟢 Set Available");
        JButton setOfflineBtn = new JButton("⚫ Set Offline");
        JButton refreshBtn = new JButton("🔄 Refresh");
        JButton exportBtn = new JButton("📋 Export List");

        UITheme.styleButtonSecondary(addBtn);
        UITheme.styleButtonSecondary(deleteBtn);
        UITheme.styleButtonSecondary(setAvailableBtn);
        UITheme.styleButtonSecondary(setOfflineBtn);
        UITheme.styleButtonSecondary(refreshBtn);
        UITheme.styleButtonSecondary(exportBtn);

        actions.add(addBtn);
        actions.add(deleteBtn);
        actions.add(setAvailableBtn);
        actions.add(setOfflineBtn);
        actions.add(refreshBtn);
        actions.add(exportBtn);

        card.add(actions, BorderLayout.SOUTH);

        // Load data
        refreshTable();

        // ==================== ACTION LISTENERS ====================
        
        // Search functionality
        searchField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                String searchText = searchField.getText().trim();
                if (searchText.isEmpty()) {
                    sorter.setRowFilter(null);
                } else {
                    sorter.setRowFilter(RowFilter.regexFilter("(?i)" + searchText));
                }
            }
        });
        
        clearSearchBtn.addActionListener(e -> {
            searchField.setText("");
            sorter.setRowFilter(null);
        });
        
        // Add rider
        addBtn.addActionListener(e -> {
            system.showAddRiderDialog();
            refreshTable();
        });
        
        // Delete rider
        deleteBtn.addActionListener(e -> deleteSelectedRider());
        
        // Refresh
        refreshBtn.addActionListener(e -> refreshTable());
        
        // Set Available
        setAvailableBtn.addActionListener(e -> {
            Rider r = getSelectedRider();
            if (r == null) return;
            
            if ("Delivering".equals(r.getStatus())) {
                JOptionPane.showMessageDialog(this,
                        "This rider is currently Delivering.\nComplete the order before changing status.",
                        "Cannot Change Status",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            r.setStatus("Available");
            refreshTable();
        });
        
        // Set Offline
        setOfflineBtn.addActionListener(e -> {
            Rider r = getSelectedRider();
            if (r == null) return;
            
            if ("Delivering".equals(r.getStatus())) {
                JOptionPane.showMessageDialog(this,
                        "This rider is currently Delivering.\nComplete the order before setting Offline.",
                        "Cannot Change Status",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            r.setStatus("Offline");
            refreshTable();
        });
        
        // Export List
        exportBtn.addActionListener(e -> exportRiderList());
        
        // Double-click to view rider details
        table.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                if (evt.getClickCount() == 2) {
                    showRiderDetails();
                }
            }
        });
    }

    private JPanel header() {
        JPanel h = UITheme.cardPanel(new BorderLayout());

        JLabel title = new JLabel("🚴 RIDER MANAGEMENT");
        title.setFont(UITheme.titleFont(22));
        title.setForeground(UITheme.ACCENT);

        JLabel sub = new JLabel("Manage riders, search, and toggle availability");
        sub.setFont(UITheme.bodyFont(14));
        sub.setForeground(UITheme.TEXT_DARK);

        h.add(title, BorderLayout.NORTH);
        h.add(sub, BorderLayout.SOUTH);
        return h;
    }

    private Rider getSelectedRider() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Select a rider row first.");
            return null;
        }
        
        // Convert view row index to model row index (for sorting)
        int modelRow = table.convertRowIndexToModel(row);
        String riderId = model.getValueAt(modelRow, 0).toString();
        Rider r = system.ridersById.get(riderId);

        if (r == null) {
            JOptionPane.showMessageDialog(this, "Rider not found in system!");
            return null;
        }

        return r;
    }
    
    // NEW: Delete selected rider
    private void deleteSelectedRider() {
        Rider r = getSelectedRider();
        if (r == null) return;
        
        system.deleteRider(r.getRiderId());
        refreshTable();
    }
    
    // NEW: Show rider details
    private void showRiderDetails() {
        Rider r = getSelectedRider();
        if (r == null) return;
        
        StringBuilder sb = new StringBuilder();
        sb.append("🚴 RIDER DETAILS\n");
        sb.append("══════════════════════════════════════\n\n");
        
        sb.append("ID:            ").append(r.getRiderId()).append("\n");
        sb.append("Name:          ").append(r.getRiderName()).append("\n");
        sb.append("Location:      ").append(r.getCurrentLocation()).append("\n");
        sb.append("Status:        ").append(r.getStatus()).append("\n\n");
        
        // Show assigned orders
        sb.append("📦 ASSIGNED ORDERS:\n");
        sb.append("────────────────────\n");
        
        int assignedCount = 0;
        for (int i = 0; i < system.allOrders.size(); i++) {
            Order order = system.allOrders.get(i);
            if (order != null && r.getRiderId().equals(order.getAssignedRiderId())) {
                sb.append("• Order ").append(order.getOrderId())
                  .append(" - ").append(order.getStatus())
                  .append(" (").append(order.getStudentName()).append(")\n");
                assignedCount++;
            }
        }
        
        if (assignedCount == 0) {
            sb.append("No orders currently assigned\n");
        }
        
        sb.append("\n📊 STATS:\n");
        sb.append("────────────────────\n");
        sb.append("Total orders assigned: ").append(assignedCount).append("\n");
        
        JTextArea area = new JTextArea(sb.toString());
        area.setEditable(false);
        area.setFont(new Font("Monospaced", Font.PLAIN, 12));
        area.setMargin(new Insets(10, 10, 10, 10));
        
        JOptionPane.showMessageDialog(
            this, new JScrollPane(area),
            "Rider Details - " + r.getRiderName(),
            JOptionPane.INFORMATION_MESSAGE);
    }
    
    // NEW: Export rider list
    private void exportRiderList() {
        StringBuilder sb = new StringBuilder();
        sb.append("CAMPUS FOOD DELIVERY - RIDER LIST\n");
        sb.append("Generated: ").append(java.time.LocalDateTime.now()).append("\n");
        sb.append("══════════════════════════════════════\n\n");
        
        CustomHashMap.Entry<String, Rider>[] entries = system.ridersById.entries();
        if (entries == null || entries.length == 0) {
            sb.append("No riders in system.\n");
        } else {
            sb.append(String.format("%-10s %-20s %-20s %-15s\n", 
                "ID", "Name", "Location", "Status"));
            sb.append(String.format("%-10s %-20s %-20s %-15s\n", 
                "──", "────", "────────", "──────"));
            
            for (int i = 0; i < entries.length; i++) {
                if (entries[i] != null && entries[i].value != null) {
                    Rider r = entries[i].value;
                    sb.append(String.format("%-10s %-20s %-20s %-15s\n", 
                        r.getRiderId(), 
                        r.getRiderName(), 
                        r.getCurrentLocation(), 
                        r.getStatus()));
                }
            }
            
            sb.append("\n📊 SUMMARY:\n");
            sb.append("Total Riders: ").append(system.ridersById.size()).append("\n");
            
            // Count by status
            int available = 0, delivering = 0, offline = 0;
            for (int i = 0; i < entries.length; i++) {
                if (entries[i] != null && entries[i].value != null) {
                    String status = entries[i].value.getStatus();
                    if ("Available".equals(status)) available++;
                    else if ("Delivering".equals(status)) delivering++;
                    else if ("Offline".equals(status)) offline++;
                }
            }
            
            sb.append("Available:    ").append(available).append("\n");
            sb.append("Delivering:   ").append(delivering).append("\n");
            sb.append("Offline:      ").append(offline).append("\n");
        }
        
        JTextArea area = new JTextArea(sb.toString());
        area.setEditable(false);
        area.setFont(new Font("Monospaced", Font.PLAIN, 12));
        area.setMargin(new Insets(10, 10, 10, 10));
        
        JOptionPane.showMessageDialog(
            this, new JScrollPane(area),
            "Rider List Export",
            JOptionPane.INFORMATION_MESSAGE);
    }

    private void refreshTable() {
        model.setRowCount(0);

        CustomHashMap.Entry<String, Rider>[] entries = system.ridersById.entries();
        if (entries == null)
            return;

        for (int i = 0; i < entries.length; i++) {
            if (entries[i] != null && entries[i].value != null) {
                Rider r = entries[i].value;
                model.addRow(new Object[] {
                        r.getRiderId(),
                        r.getRiderName(),
                        r.getCurrentLocation(),
                        r.getStatus()
                });
            }
        }
        
        // Update search if there's text
        String searchText = searchField.getText().trim();
        if (!searchText.isEmpty()) {
            sorter.setRowFilter(RowFilter.regexFilter("(?i)" + searchText));
        }
    }
}