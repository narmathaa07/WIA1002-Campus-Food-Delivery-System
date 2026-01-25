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
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;

public class PriorityCellRenderer extends DefaultTableCellRenderer {
    
    @Override
    public Component getTableCellRendererComponent(
            JTable table, Object value, boolean isSelected,
            boolean hasFocus, int row, int column) {
        
        // Call parent method
        super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        
        setHorizontalAlignment(SwingConstants.CENTER);
        setFont(UITheme.headingFont(12));
        setOpaque(true);

        String priority = value == null ? "" : value.toString();

        if ("URGENT".equals(priority)) {
            setBackground(new Color(255, 220, 220));
            setForeground(new Color(180, 0, 0));
            setText("⚡ " + priority);
        } else {
            setBackground(new Color(220, 245, 232));
            setForeground(new Color(16, 122, 62));
            setText("✓ " + priority);
        }

        return this;
    }
}