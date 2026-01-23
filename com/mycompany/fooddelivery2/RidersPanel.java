package com.mycompany.fooddelivery2;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class RidersPanel extends JPanel {

    private final FoodDeliverySystem system;
    private final DefaultTableModel model;
    private final JTable table;

    public RidersPanel(FoodDeliverySystem system) {
        this.system = system;
        setLayout(new BorderLayout(16, 16));

        JPanel page = UITheme.gradientPage(new BorderLayout(16, 16));
        add(page, BorderLayout.CENTER);

        page.add(header(), BorderLayout.NORTH);

        // Main Card
        JPanel card = UITheme.cardPanel(new BorderLayout(12, 12));
        page.add(card, BorderLayout.CENTER);

        // Table
        String[] cols = { "Rider ID", "Name", "Current Location", "Status" };
        model = new DefaultTableModel(cols, 0) {
            @Override
            public boolean isCellEditable(int row, int col) {
                return false;
            }
        };

        table = new JTable(model);
        table.setRowHeight(32);
        table.setFont(UITheme.bodyFont(14));
        table.getTableHeader().setFont(UITheme.headingFont(13));

        // Status pill renderer (Status = col index 3)
        table.getColumnModel().getColumn(3).setCellRenderer(new RiderStatusCellRenderer());

        JScrollPane sp = new JScrollPane(table);
        sp.setBorder(BorderFactory.createEmptyBorder());
        card.add(sp, BorderLayout.CENTER);

        // Buttons Row
        JPanel actions = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        actions.setOpaque(false);

        JButton addBtn = new JButton("Add Rider");
        JButton setAvailableBtn = new JButton("Set Available");
        JButton setOfflineBtn = new JButton("Set Offline");
        JButton refreshBtn = new JButton("Refresh");

        UITheme.styleButton(addBtn);
        UITheme.styleButton(setAvailableBtn);
        UITheme.styleButton(setOfflineBtn);
        UITheme.styleButton(refreshBtn);

        actions.add(addBtn);
        actions.add(setAvailableBtn);
        actions.add(setOfflineBtn);
        actions.add(refreshBtn);

        card.add(actions, BorderLayout.SOUTH);

        // Load data
        refreshTable();

        // Actions
        addBtn.addActionListener(e -> {
            system.showAddRiderDialog();
            refreshTable();
        });

        refreshBtn.addActionListener(e -> refreshTable());

        setAvailableBtn.addActionListener(e -> {
            Rider r = getSelectedRider();
            if (r == null)
                return;

            // If delivering, we block to keep your logic consistent
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

        setOfflineBtn.addActionListener(e -> {
            Rider r = getSelectedRider();
            if (r == null)
                return;

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
    }

    private JPanel header() {
        JPanel h = UITheme.cardPanel(new BorderLayout());

        JLabel title = new JLabel("🚴 RIDER MANAGEMENT");
        title.setFont(UITheme.titleFont(22));
        title.setForeground(UITheme.ACCENT);

        JLabel sub = new JLabel("Manage riders and toggle availability instantly");
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

        String riderId = model.getValueAt(row, 0).toString();
        Rider r = system.ridersById.get(riderId);

        if (r == null) {
            JOptionPane.showMessageDialog(this, "Rider not found in system!");
            return null;
        }

        return r;
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
    }
}
