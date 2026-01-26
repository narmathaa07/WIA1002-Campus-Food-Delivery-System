/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.fooddelivery3;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class OrdersPanel extends JPanel {

    private final FoodDeliverySystem system;
    private final JTable table;
    private final DefaultTableModel model;
    private final JTextField searchField;
    private final JComboBox<String> filterCombo;
    private TableRowSorter<DefaultTableModel> sorter;

    public OrdersPanel(FoodDeliverySystem system) {
        this.system = system;
        setLayout(new BorderLayout(16, 16));
        setOpaque(true);

        JPanel page = UITheme.gradientPage(new BorderLayout(16, 16));
        page.setOpaque(true);
        add(page, BorderLayout.CENTER);

        page.add(header(), BorderLayout.NORTH);

        JPanel card = UITheme.cardPanel(new BorderLayout(12, 12));
        card.setOpaque(true);
        page.add(card, BorderLayout.CENTER);

        // ==================== SEARCH AND FILTER PANEL ====================
        JPanel controlPanel = new JPanel(new BorderLayout(10, 0));
        controlPanel.setOpaque(true);
        controlPanel.setBackground(Color.WHITE);
        controlPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));

        // Left side: Search
        JPanel searchPanel = new JPanel(new BorderLayout(5, 0));
        searchPanel.setOpaque(true);
        searchPanel.setBackground(Color.WHITE);

        JLabel searchLabel = new JLabel("Search:");
        searchLabel.setFont(UITheme.headingFont(14));
        searchLabel.setForeground(Color.BLACK);

        searchField = new JTextField();
        searchField.setFont(UITheme.bodyFont(13));
        searchField.setForeground(Color.BLACK);
        searchField.setBackground(Color.WHITE);
        searchField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(180, 220, 195), 2),
                BorderFactory.createEmptyBorder(8, 10, 8, 10)
        ));
        searchField.setToolTipText("Search by Order ID, Student Name, Email, or Location");

        JButton clearSearchBtn = new JButton("Clear");
        styleRedTextButton(clearSearchBtn);
        clearSearchBtn.setPreferredSize(new Dimension(80, 35));

        searchPanel.add(searchLabel, BorderLayout.WEST);
        searchPanel.add(searchField, BorderLayout.CENTER);
        searchPanel.add(clearSearchBtn, BorderLayout.EAST);

        // Right side: Filter
        JPanel filterPanel = new JPanel(new BorderLayout(5, 0));
        filterPanel.setOpaque(true);
        filterPanel.setBackground(Color.WHITE);

        JLabel filterLabel = new JLabel("Filter:");
        filterLabel.setFont(UITheme.headingFont(14));
        filterLabel.setForeground(Color.BLACK);

        String[] filterOptions = {"All Orders", "Pending", "Assigned", "Delivered", "Cancelled",
                "URGENT Only", "Normal Only"};
        filterCombo = new JComboBox<>(filterOptions);
        filterCombo.setFont(UITheme.bodyFont(13));
        filterCombo.setBackground(Color.WHITE);
        filterCombo.setForeground(Color.BLACK);

        filterPanel.add(filterLabel, BorderLayout.WEST);
        filterPanel.add(filterCombo, BorderLayout.CENTER);

        // Add both panels to control panel
        controlPanel.add(searchPanel, BorderLayout.CENTER);
        controlPanel.add(filterPanel, BorderLayout.EAST);

        card.add(controlPanel, BorderLayout.NORTH);

        // ==================== ORDERS TABLE ====================
        model = new DefaultTableModel(
                new String[] { "Order ID", "Student", "Priority", "Status", "Pickup", "Delivery" }, 0) {
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
        table.getColumnModel().getColumn(3).setCellRenderer(new StatusCellRenderer());
        table.getColumnModel().getColumn(2).setCellRenderer(new PriorityCellRenderer()); // ← USING THE SEPARATE CLASS

        table.setFont(UITheme.bodyFont(13));
        table.setForeground(Color.BLACK);
        table.setBackground(Color.WHITE);
        table.setGridColor(new Color(220, 220, 220));
        table.setRowHeight(30);
        table.getTableHeader().setFont(UITheme.headingFont(13));
        table.getTableHeader().setBackground(new Color(240, 240, 240));
        table.getTableHeader().setForeground(Color.BLACK);

        // Enable sorting
        sorter = new TableRowSorter<>(model);
        table.setRowSorter(sorter);

        // Set column widths
        table.getColumnModel().getColumn(0).setPreferredWidth(100);
        table.getColumnModel().getColumn(1).setPreferredWidth(120);
        table.getColumnModel().getColumn(2).setPreferredWidth(80);
        table.getColumnModel().getColumn(3).setPreferredWidth(100);
        table.getColumnModel().getColumn(4).setPreferredWidth(100);
        table.getColumnModel().getColumn(5).setPreferredWidth(100);

        refreshTable();

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.getViewport().setBackground(Color.WHITE);
        card.add(scrollPane, BorderLayout.CENTER);

        // ==================== ACTION BUTTONS ====================
        JPanel actions = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        actions.setOpaque(true);
        actions.setBackground(Color.WHITE);

        JButton createBtn = new JButton("Create Order");
        JButton cancelBtn = new JButton("Cancel Order");
        JButton completeBtn = new JButton("Complete Order");
        JButton viewDetailsBtn = new JButton("View Details");
        JButton refreshBtn = new JButton("Refresh");

        styleRedTextButton(createBtn);
        styleRedTextButton(cancelBtn);
        styleRedTextButton(completeBtn);
        styleRedTextButton(viewDetailsBtn);
        styleRedTextButton(refreshBtn);

        actions.add(createBtn);
        actions.add(cancelBtn);
        actions.add(completeBtn);
        actions.add(viewDetailsBtn);
        actions.add(refreshBtn);

        card.add(actions, BorderLayout.SOUTH);

        // ==================== EVENT LISTENERS ====================

        // Search functionality
        searchField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                applyFilters();
            }
        });

        clearSearchBtn.addActionListener(e -> {
            searchField.setText("");
            applyFilters();
        });

        // Filter functionality
        filterCombo.addActionListener(e -> applyFilters());

        // Create order
        createBtn.addActionListener(e -> {
            system.showCreateOrderDialog();
            refreshTable();
        });

        // Cancel order
        cancelBtn.addActionListener(e -> {
            system.cancelSelectedOrder(table);
            refreshTable();
        });

        // Complete order
        completeBtn.addActionListener(e -> {
            system.completeSelectedOrder(table);
            refreshTable();
        });

        // View order details
        viewDetailsBtn.addActionListener(e -> showOrderDetails());

        // Refresh table
        refreshBtn.addActionListener(e -> refreshTable());

        // Double-click to view details
        table.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                if (evt.getClickCount() == 2) {
                    showOrderDetails();
                }
            }
        });
    }

    private JPanel header() {
        JPanel h = UITheme.cardPanel(new BorderLayout());
        h.setOpaque(true);

        JLabel title = new JLabel("ORDER MANAGEMENT");
        title.setFont(UITheme.titleFont(22));
        title.setForeground(UITheme.ACCENT);

        JLabel sub = new JLabel("Create, cancel, complete and filter student orders");
        sub.setFont(UITheme.bodyFont(14));
        sub.setForeground(UITheme.TEXT_DARK);

        h.add(title, BorderLayout.NORTH);
        h.add(sub, BorderLayout.SOUTH);
        return h;
    }

    // Helper method to style buttons with red text, white background, red border
    private void styleRedTextButton(JButton button) {
        button.setOpaque(true);
        button.setContentAreaFilled(true);
        button.setFocusPainted(false);
        button.setBackground(Color.WHITE);
        button.setForeground(UITheme.ACCENT);
        button.setFont(UITheme.headingFont(14));
        button.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(UITheme.ACCENT, 2),
                BorderFactory.createEmptyBorder(10, 15, 10, 15)
        ));
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    }

    // NEW: Apply both search and filter
    private void applyFilters() {
        final String searchText = searchField.getText().trim().toLowerCase();
        final String filterOption = (String) filterCombo.getSelectedItem();

        sorter.setRowFilter(new RowFilter<DefaultTableModel, Integer>() {
            @Override
            public boolean include(Entry<? extends DefaultTableModel, ? extends Integer> entry) {
                // Search filter
                if (!searchText.isEmpty()) {
                    boolean found = false;
                    for (int i = 0; i < entry.getValueCount(); i++) {
                        String value = (String) entry.getValue(i);
                        if (value != null && value.toLowerCase().contains(searchText)) {
                            found = true;
                            break;
                        }
                    }
                    if (!found) return false;
                }

                // Status/Priority filter
                if ("All Orders".equals(filterOption)) {
                    return true;
                }

                String status = (String) entry.getValue(3);
                String priority = (String) entry.getValue(2);

                if (filterOption.equals("Pending")) return "Pending".equals(status);
                if (filterOption.equals("Assigned")) return "Assigned".equals(status);
                if (filterOption.equals("Delivered")) return "Delivered".equals(status);
                if (filterOption.equals("Cancelled")) return "Cancelled".equals(status);
                if (filterOption.equals("URGENT Only")) return "URGENT".equals(priority);
                if (filterOption.equals("Normal Only")) return "Normal".equals(priority);

                return true;
            }
        });
    }

    // NEW: Show order details
    private void showOrderDetails() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Select an order first.");
            return;
        }

        // Convert view row index to model row index (for sorting/filtering)
        int modelRow = table.convertRowIndexToModel(row);
        String orderId = model.getValueAt(modelRow, 0).toString();
        Order order = system.ordersById.get(orderId);

        if (order == null) {
            JOptionPane.showMessageDialog(this, "Order not found!");
            return;
        }

        StringBuilder sb = new StringBuilder();
        sb.append("ORDER DETAILS\n");
        sb.append("══════════════════════════════════════\n\n");

        sb.append("Order ID:      ").append(order.getOrderId()).append("\n");
        sb.append("Student:       ").append(order.getStudentName()).append("\n");
        sb.append("Email:         ").append(order.getStudentEmail()).append("\n");
        sb.append("Pickup:        ").append(order.getPickupLocation()).append("\n");
        sb.append("Delivery:      ").append(order.getDeliveryLocation()).append("\n");
        sb.append("Priority:      ").append(order.getPriority() == 1 ? "URGENT" : "Normal").append("\n");
        sb.append("Status:        ").append(order.getStatus()).append("\n");
        sb.append("Order Time:    ").append(order.getOrderTime()).append("\n\n");

        // Show assigned rider if any
        if (order.getAssignedRiderId() != null) {
            Rider rider = system.ridersById.get(order.getAssignedRiderId());
            if (rider != null) {
                sb.append("ASSIGNED RIDER:\n");
                sb.append("────────────────────\n");
                sb.append("Rider ID:    ").append(rider.getRiderId()).append("\n");
                sb.append("Name:        ").append(rider.getRiderName()).append("\n");
                sb.append("Location:    ").append(rider.getCurrentLocation()).append("\n");
                sb.append("Status:      ").append(rider.getStatus()).append("\n\n");
            }
        }

        sb.append("PATH INFORMATION:\n");
        sb.append("────────────────────\n");

        CustomArrayList<String> path = system.campusMap.findShortestPath(
                order.getPickupLocation(),
                order.getDeliveryLocation()
        );

        if (path != null && path.size() > 0) {
            sb.append("Shortest path (").append(path.size() - 1).append(" segments):\n");
            for (int i = 0; i < path.size(); i++) {
                sb.append("  ").append(i + 1).append(". ").append(path.get(i));
                if (i < path.size() - 1) sb.append(" →\n");
            }
            sb.append("\n\nEstimated time: ").append((path.size() - 1) * 3).append(" minutes\n");
        } else {
            sb.append("No path found between pickup and delivery locations.\n");
        }

        JTextArea area = new JTextArea(sb.toString());
        area.setEditable(false);
        area.setFont(new Font("Monospaced", Font.PLAIN, 12));
        area.setForeground(Color.BLACK);
        area.setBackground(Color.WHITE);
        area.setMargin(new Insets(10, 10, 10, 10));

        JOptionPane.showMessageDialog(
                this, new JScrollPane(area),
                "Order Details - " + orderId,
                JOptionPane.INFORMATION_MESSAGE);
    }

    private void refreshTable() {
        model.setRowCount(0);
        for (int i = 0; i < system.allOrders.size(); i++) {
            Order o = system.allOrders.get(i);
            if (o != null) {
                model.addRow(new Object[] {
                        o.getOrderId(),
                        o.getStudentName(),
                        o.getPriority() == 1 ? "URGENT" : "Normal",
                        o.getStatus(),
                        o.getPickupLocation(),
                        o.getDeliveryLocation()
                });
            }
        }
        applyFilters(); // Reapply filters after refresh
    }
}