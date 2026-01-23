package com.mycompany.fooddelivery2;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;

public class StatusCellRenderer extends DefaultTableCellRenderer {

    @Override
    public Component getTableCellRendererComponent(
            JTable table, Object value, boolean isSelected,
            boolean hasFocus, int row, int column) {

        JLabel label = (JLabel) super.getTableCellRendererComponent(
                table, value, isSelected, hasFocus, row, column);

        label.setHorizontalAlignment(SwingConstants.CENTER);
        label.setFont(UITheme.headingFont(12));
        label.setOpaque(true);

        String status = value.toString();

        switch (status) {
            case "Pending":
                label.setBackground(new Color(255, 239, 213));
                label.setForeground(new Color(204, 102, 0));
                break;
            case "Assigned":
                label.setBackground(new Color(220, 235, 250));
                label.setForeground(new Color(0, 70, 160));
                break;
            case "Delivered":
                label.setBackground(new Color(220, 245, 232));
                label.setForeground(new Color(16, 122, 62));
                break;
            case "Cancelled":
                label.setBackground(new Color(235, 235, 235));
                label.setForeground(Color.DARK_GRAY);
                break;
            default:
                label.setBackground(Color.WHITE);
                label.setForeground(Color.BLACK);
        }

        return label;
    }
}
