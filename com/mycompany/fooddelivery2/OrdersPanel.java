package com.mycompany.fooddelivery2;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class OrdersPanel extends JPanel {

    private final FoodDeliverySystem system;
    private final JTable table;
    private final DefaultTableModel model;

    public OrdersPanel(FoodDeliverySystem system) {
        this.system = system;
        setLayout(new BorderLayout(16, 16));

        JPanel page = UITheme.gradientPage(new BorderLayout(16, 16));
        add(page, BorderLayout.CENTER);

        page.add(header(), BorderLayout.NORTH);

        JPanel card = UITheme.cardPanel(new BorderLayout(12, 12));
        page.add(card, BorderLayout.CENTER);

        model = new DefaultTableModel(
                new String[] { "Order ID", "Student", "Priority", "Status" }, 0);
        table = new JTable(model);
        table.getColumnModel().getColumn(3)
                .setCellRenderer(new StatusCellRenderer());
        table.setFont(UITheme.bodyFont(13));
        table.setRowHeight(26);
        table.getTableHeader().setFont(UITheme.headingFont(13));

        refreshTable();

        card.add(new JScrollPane(table), BorderLayout.CENTER);

        JPanel actions = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        actions.setOpaque(false);

        JButton createBtn = new JButton("Create Order");
        JButton cancelBtn = new JButton("Cancel Order");
        JButton completeBtn = new JButton("Complete Order");

        UITheme.styleButton(createBtn);
        UITheme.styleButtonSecondary(cancelBtn);
        UITheme.styleButtonSecondary(completeBtn);

        actions.add(createBtn);
        actions.add(cancelBtn);
        actions.add(completeBtn);

        card.add(actions, BorderLayout.SOUTH);

        createBtn.addActionListener(e -> {
            system.showCreateOrderDialog();
            refreshTable();
        });

        cancelBtn.addActionListener(e -> {
            system.cancelSelectedOrder(table);
            refreshTable();
        });

        completeBtn.addActionListener(e -> {
            system.completeSelectedOrder(table);
            refreshTable();
        });
    }

    private JPanel header() {
        JPanel h = UITheme.cardPanel(new BorderLayout());
        JLabel title = new JLabel("📦 ORDER MANAGEMENT");
        title.setFont(UITheme.titleFont(22));
        title.setForeground(UITheme.ACCENT);

        JLabel sub = new JLabel("Create, cancel and complete student orders");
        sub.setFont(UITheme.bodyFont(14));

        h.add(title, BorderLayout.NORTH);
        h.add(sub, BorderLayout.SOUTH);
        return h;
    }

    private void refreshTable() {
        model.setRowCount(0);
        for (int i = 0; i < system.allOrders.size(); i++) {
            Order o = system.allOrders.get(i);
            model.addRow(new Object[] {
                    o.getOrderId(),
                    o.getStudentName(),
                    o.getPriority() == 1 ? "URGENT" : "Normal",
                    o.getStatus()
            });
        }
    }
}
