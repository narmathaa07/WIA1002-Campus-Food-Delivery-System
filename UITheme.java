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
import javax.swing.border.Border;
import java.awt.*;

public class UITheme {
    // REDWOOD LIGHT THEME (warm, professional, accessible)
    public static final Color ACCENT = new Color(165, 42, 42);        // Redwood (dark red-brown)
    public static final Color ACCENT_LIGHT = new Color(220, 180, 180); // Soft redwood tint
    public static final Color ACCENT_HOVER = new Color(139, 35, 35);   // Darker on hover

    public static final Color BACKGROUND = new Color(253, 245, 240);   // Warm off-white (redwood light base)
    public static final Color CARD_BG = Color.WHITE;                   // Clean white cards
    public static final Color TEXT_DARK = new Color(40, 30, 30);       // Near-black for readability
    public static final Color TEXT_LIGHT = Color.WHITE;

    // Status colors (updated to match redwood theme)
    public static final Color STATUS_PENDING = new Color(255, 230, 200);
    public static final Color STATUS_ASSIGNED = new Color(220, 235, 250);
    public static final Color STATUS_DELIVERED = new Color(220, 245, 232);
    public static final Color STATUS_CANCELLED = new Color(240, 240, 240);

    private UITheme() {}

    public static JPanel gradientPage(LayoutManager layout) {
        JPanel p = new JPanel(layout);
        p.setOpaque(true);
        p.setBackground(BACKGROUND); // Warm background
        return p;
    }

    public static JPanel cardPanel(LayoutManager layout) {
        JPanel p = new JPanel(layout);
        p.setOpaque(true);
        p.setBackground(CARD_BG);
        p.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 210, 200), 1),
                BorderFactory.createEmptyBorder(16, 16, 16, 16)
        ));
        return p;
    }

    public static void styleButton(JButton b) {
        b.setFocusPainted(false);
        b.setBackground(ACCENT);
        b.setForeground(TEXT_LIGHT);
        b.setFont(new Font("Segoe UI", Font.BOLD, 14));
        b.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(ACCENT_HOVER, 2),
                BorderFactory.createEmptyBorder(12, 20, 12, 20)
        ));
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        b.setOpaque(true);
        b.setContentAreaFilled(true);

        // Hover effect
        b.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                b.setBackground(ACCENT_HOVER);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                b.setBackground(ACCENT);
            }
        });
    }

    public static void styleButtonSecondary(JButton b) {
        b.setFocusPainted(false);
        b.setBackground(ACCENT_LIGHT);
        b.setForeground(ACCENT);
        b.setFont(new Font("Segoe UI", Font.BOLD, 14));
        b.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(ACCENT, 1),
                BorderFactory.createEmptyBorder(12, 20, 12, 20)
        ));
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        b.setOpaque(true);
        b.setContentAreaFilled(true);

        b.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                b.setBackground(ACCENT);
                b.setForeground(TEXT_LIGHT);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                b.setBackground(ACCENT_LIGHT);
                b.setForeground(ACCENT);
            }
        });
    }

    public static Font titleFont(int size) { return new Font("Segoe UI", Font.BOLD, size); }
    public static Font headingFont(int size) { return new Font("Segoe UI", Font.BOLD, size); }
    public static Font bodyFont(int size) { return new Font("Segoe UI", Font.PLAIN, size); }
}