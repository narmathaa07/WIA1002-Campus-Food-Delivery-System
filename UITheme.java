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

    // Main theme colors - HIGH CONTRAST
    public static final Color GREEN_DARK = new Color(0, 100, 0);        // Darker green
    public static final Color GREEN = new Color(0, 150, 0);             // Medium green
    public static final Color GREEN_LIGHT = new Color(230, 255, 230);   // Light green
    
    public static final Color WHITE = Color.WHITE;
    public static final Color BLACK = Color.BLACK;
    public static final Color DARK_GRAY = new Color(50, 50, 50);        // Very dark gray
    
    public static final Color ACCENT = GREEN_DARK;
    public static final Color TEXT_DARK = DARK_GRAY;                    // Almost black
    public static final Color TEXT_LIGHT = Color.WHITE;

    private UITheme() {
    }

    // ---------- PAGE BACKGROUND (SOLID WHITE FOR MAX CONTRAST) ----------
    public static JPanel gradientPage(LayoutManager layout) {
        JPanel p = new JPanel(layout);
        p.setOpaque(true);
        p.setBackground(Color.WHITE);  // SOLID WHITE - NO GRADIENT
        return p;
    }

    // ---------- CARD PANEL ----------
    public static JPanel cardPanel(LayoutManager layout) {
        JPanel p = new JPanel(layout);
        p.setOpaque(true);
        p.setBackground(Color.WHITE);  // SOLID WHITE
        p.setBorder(compoundCardBorder());
        return p;
    }

    private static Border compoundCardBorder() {
        Border outer = BorderFactory.createLineBorder(new Color(200, 200, 200), 2);
        Border inner = BorderFactory.createEmptyBorder(16, 16, 16, 16);
        return BorderFactory.createCompoundBorder(outer, inner);
    }

    // ---------- BUTTON STYLES ----------
    public static void styleButton(JButton b) {
        b.setFocusPainted(true);
        b.setBackground(GREEN);
        b.setForeground(TEXT_LIGHT);
        b.setFont(headingFont(14));
        b.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(GREEN_DARK, 2),
            BorderFactory.createEmptyBorder(12, 20, 12, 20)
        ));
        b.setCursor(new Cursor(Cursor.HAND_CURSOR));
        b.setOpaque(true);
        b.setContentAreaFilled(true);
        
        // Add bold font
        b.setFont(new Font("Segoe UI", Font.BOLD, 14));
    }
    
    public static void styleButtonSecondary(JButton b) {
        b.setFocusPainted(true);
        b.setBackground(WHITE);
        b.setForeground(GREEN_DARK);
        b.setFont(headingFont(14));
        b.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(GREEN, 2),
            BorderFactory.createEmptyBorder(12, 20, 12, 20)
        ));
        b.setCursor(new Cursor(Cursor.HAND_CURSOR));
        b.setOpaque(true);
        b.setContentAreaFilled(true);
        
        // Add bold font
        b.setFont(new Font("Segoe UI", Font.BOLD, 14));
    }

    // ---------- FONTS ----------
    public static Font titleFont(int size) {
        return new Font("Segoe UI", Font.BOLD, size);
    }

    public static Font headingFont(int size) {
        return new Font("Segoe UI", Font.BOLD, size);
    }

    public static Font bodyFont(int size) {
        return new Font("Segoe UI", Font.PLAIN, size);
    }
}