/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.fooddelivery3;

import javax.swing.*;
import java.awt.*;

public class LocationsPanel extends JPanel {

    private final FoodDeliverySystem system;
    private final JTextArea mapArea;

    public LocationsPanel(FoodDeliverySystem system) {
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

        JLabel section = new JLabel("CAMPUS MAP (ADJACENCY LIST)");
        section.setFont(UITheme.headingFont(16));
        section.setForeground(UITheme.TEXT_DARK);

        mapArea = new JTextArea();
        mapArea.setEditable(false);
        mapArea.setFont(new Font("Consolas", Font.PLAIN, 14));
        mapArea.setBackground(Color.WHITE);
        mapArea.setForeground(Color.BLACK);
        mapArea.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
                BorderFactory.createEmptyBorder(12, 12, 12, 12)
        ));

        refreshMap();

        card.add(section, BorderLayout.NORTH);
        card.add(new JScrollPane(mapArea), BorderLayout.CENTER);

        // Control buttons - 2 rows
        JPanel buttonPanel = new JPanel(new GridLayout(2, 4, 10, 10));
        buttonPanel.setOpaque(true);
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));

        // First row - Basic operations
        JButton addLocationBtn = new JButton("Add Location");
        JButton addRouteBtn = new JButton("Add Route");
        JButton findPathBtn = new JButton("Find Shortest Path");
        JButton refreshBtn = new JButton("Refresh Map");

        // Second row - Delete and management operations
        JButton deleteLocationBtn = new JButton("Delete Location");
        JButton deleteRouteBtn = new JButton("Delete Route");
        JButton viewDetailsBtn = new JButton("View Details");
        JButton saveDataBtn = new JButton("Save Data");

        // Style ALL buttons to have red text, white background, red border
        styleRedTextButton(addLocationBtn);
        styleRedTextButton(addRouteBtn);
        styleRedTextButton(findPathBtn);
        styleRedTextButton(refreshBtn);
        styleRedTextButton(deleteLocationBtn);
        styleRedTextButton(deleteRouteBtn);
        styleRedTextButton(viewDetailsBtn);
        styleRedTextButton(saveDataBtn);

        // Add buttons to panel
        buttonPanel.add(addLocationBtn);
        buttonPanel.add(addRouteBtn);
        buttonPanel.add(findPathBtn);
        buttonPanel.add(refreshBtn);
        buttonPanel.add(deleteLocationBtn);
        buttonPanel.add(deleteRouteBtn);
        buttonPanel.add(viewDetailsBtn);
        buttonPanel.add(saveDataBtn);

        card.add(buttonPanel, BorderLayout.SOUTH);

        // Action listeners
        addLocationBtn.addActionListener(e -> {
            system.showAddLocationDialog();
            refreshMap();
        });

        addRouteBtn.addActionListener(e -> {
            system.showAddRouteDialog();
            refreshMap();
        });

        findPathBtn.addActionListener(e -> system.showFindShortestPathDialog());

        refreshBtn.addActionListener(e -> refreshMap());

        deleteLocationBtn.addActionListener(e -> {
            system.showDeleteLocationDialog();
            refreshMap();
        });

        deleteRouteBtn.addActionListener(e -> {
            system.showDeleteRouteDialog();
            refreshMap();
        });

        viewDetailsBtn.addActionListener(e -> showLocationDetailsDialog());

        saveDataBtn.addActionListener(e -> FileManager.showSaveDialog(system, LocationsPanel.this));
    }

    private JPanel header() {
        JPanel h = UITheme.cardPanel(new BorderLayout());
        h.setOpaque(true);

        JLabel title = new JLabel("LOCATIONS & ROUTES");
        title.setFont(UITheme.titleFont(22));
        title.setForeground(UITheme.ACCENT); // Red text

        JLabel sub = new JLabel("Manage campus locations, routes and find shortest paths");
        sub.setFont(UITheme.bodyFont(14));
        sub.setForeground(UITheme.TEXT_DARK);

        h.add(title, BorderLayout.NORTH);
        h.add(sub, BorderLayout.SOUTH);
        return h;
    }

    private void refreshMap() {
        mapArea.setText(system.campusMap.getGraphInfo());
        mapArea.setForeground(UITheme.TEXT_DARK);
        mapArea.setBackground(Color.WHITE);
        mapArea.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(UITheme.BORDER_COLOR, 1),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
    }

    // ✅ NEW: Style button to have red text, white background, red border
    private void styleRedTextButton(JButton button) {
        button.setOpaque(true);
        button.setContentAreaFilled(true);
        button.setFocusPainted(false);
        button.setBackground(Color.WHITE);           // White background
        button.setForeground(UITheme.ACCENT);       // Red text
        button.setFont(UITheme.headingFont(14));
        button.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(UITheme.ACCENT, 2), // Red border
                BorderFactory.createEmptyBorder(10, 15, 10, 15)
        ));
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    }

    // Helper method to show location details
    private void showLocationDetailsDialog() {
        CustomArrayList<String> locations = system.campusMap.getAllNodes();

        if (locations.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No locations in the map!");
            return;
        }

        StringBuilder sb = new StringBuilder();
        sb.append("LOCATION DETAILS\n");
        sb.append("══════════════════════════════════════\n\n");

        sb.append("Total Locations: ").append(locations.size()).append("\n");
        sb.append("Total Routes: ").append(system.campusMap.getEdgeCount()).append("\n\n");

        sb.append("Location Connectivity:\n");
        sb.append("──────────────────────────\n");

        for (int i = 0; i < locations.size(); i++) {
            String location = locations.get(i);
            sb.append("\n").append(i + 1).append(". ").append(location).append(":\n");

            CustomArrayList<Graph.Edge> edges = system.campusMap.getEdges(location);
            if (edges != null && edges.size() > 0) {
                for (int j = 0; j < edges.size(); j++) {
                    Graph.Edge edge = edges.get(j);
                    if (edge != null) {
                        sb.append("   → ").append(edge.destination)
                                .append(" (").append(edge.distance).append(" min)\n");
                    }
                }
            } else {
                sb.append("   (isolated - no connections)\n");
            }
        }

        JTextArea area = new JTextArea(sb.toString());
        area.setEditable(false);
        area.setFont(new Font("Monospaced", Font.PLAIN, 12));
        area.setForeground(Color.BLACK);
        area.setBackground(Color.WHITE);
        area.setMargin(new Insets(10, 10, 10, 10));

        JOptionPane.showMessageDialog(
                this, new JScrollPane(area),
                "Location Details",
                JOptionPane.INFORMATION_MESSAGE);
    }
}