package com.mycompany.fooddelivery2;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;

public class UITheme {

    // Main theme colors
    public static final Color GREEN_DARK = new Color(16, 122, 62);
    public static final Color GREEN = new Color(24, 160, 88);
    public static final Color GREEN_LIGHT = new Color(220, 245, 232);
    public static final Color WHITE = Color.WHITE;

    public static final Color ACCENT = GREEN_DARK;
    public static final Color TEXT_DARK = new Color(40, 40, 40);

    private UITheme() {
    }

    // ---------- PAGE BACKGROUND (GRADIENT) ----------
    public static JPanel gradientPage(LayoutManager layout) {
        return new JPanel(layout) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);

                int w = getWidth();
                int h = getHeight();

                GradientPaint gp = new GradientPaint(
                        0, 0, WHITE,
                        w, h, GREEN_LIGHT);

                g2.setPaint(gp);
                g2.fillRect(0, 0, w, h);
                g2.dispose();
            }
        };
    }

    // ---------- CARD PANEL ----------
    public static JPanel cardPanel(LayoutManager layout) {
        JPanel p = new JPanel(layout);
        p.setOpaque(true);
        p.setBackground(new Color(255, 255, 255, 235));
        p.setBorder(compoundCardBorder());
        return p;
    }

    private static Border compoundCardBorder() {
        Border outer = BorderFactory.createLineBorder(new Color(210, 235, 220), 1);
        Border inner = BorderFactory.createEmptyBorder(14, 14, 14, 14);
        return BorderFactory.createCompoundBorder(outer, inner);
    }

    // ---------- BUTTON STYLES ----------
    public static void styleButton(JButton b) {
        b.setFocusPainted(false);
        b.setBackground(GREEN);
        b.setForeground(Color.WHITE);
        b.setFont(headingFont(13));
        b.setBorder(BorderFactory.createEmptyBorder(10, 16, 10, 16));
        b.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    public static void styleButtonSecondary(JButton b) {
        b.setFocusPainted(false);
        b.setBackground(new Color(240, 250, 244));
        b.setForeground(GREEN_DARK);
        b.setFont(headingFont(13));
        b.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(180, 220, 195), 1),
                BorderFactory.createEmptyBorder(10, 16, 10, 16)));
        b.setCursor(new Cursor(Cursor.HAND_CURSOR));
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
