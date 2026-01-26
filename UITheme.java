/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.fooddelivery3;

import java.awt.*;
import javax.swing.*;

public class UITheme {

    public static final Color ACCENT = new Color(165, 42, 42);
    public static final Color BACKGROUND = new Color(253, 245, 240);
    public static final Color TEXT_DARK = new Color(40, 30, 30);
    public static final Color BORDER_COLOR = new Color(220, 210, 200);

    private UITheme() {}

    public static JPanel gradientPage(LayoutManager layout) {
        JPanel p = new JPanel(layout);
        p.setOpaque(true);
        p.setBackground(BACKGROUND);
        return p;
    }

    public static JPanel cardPanel(LayoutManager layout) {
        JPanel p = new JPanel(layout);
        p.setOpaque(true);
        p.setBackground(Color.WHITE);
        p.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER_COLOR, 1),
                BorderFactory.createEmptyBorder(16, 16, 16, 16)
        ));
        return p;
    }

    // Primary button (dark redwood, white text)
    public static void styleButton(JButton button) {
        button.setFocusPainted(false);
        button.setBackground(ACCENT);
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(ACCENT.darker(), 2),
                BorderFactory.createEmptyBorder(12, 20, 12, 20)
        ));
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        button.setOpaque(true);
        button.setContentAreaFilled(true);
    }

    // Secondary button — NOW DARK WITH WHITE TEXT
    public static void styleButtonSecondary(JButton button) {
        button.setFocusPainted(false);
        button.setBackground(new Color(80, 20, 20)); // Dark redwood
        button.setForeground(Color.WHITE);           // White text
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(100, 30, 30), 2), // Dark border
                BorderFactory.createEmptyBorder(12, 20, 12, 20)
        ));
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        button.setOpaque(true);
        button.setContentAreaFilled(true);
    }

    public static JTable styleTableHeader(JTable table) {
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        table.getTableHeader().setBackground(new Color(240, 240, 240));
        table.getTableHeader().setForeground(TEXT_DARK);
        table.getTableHeader().setBorder(BorderFactory.createLineBorder(BORDER_COLOR, 1));
        return table;
    }

    public static Font titleFont(int size) { return new Font("Segoe UI", Font.BOLD, size); }
    public static Font headingFont(int size) { return new Font("Segoe UI", Font.BOLD, size); }
    public static Font bodyFont(int size) { return new Font("Segoe UI", Font.PLAIN, size); }
}