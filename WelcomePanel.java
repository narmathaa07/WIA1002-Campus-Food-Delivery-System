/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.fooddelivery3;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class WelcomePanel extends JPanel {

    private final MainFrame frame;

    public WelcomePanel(MainFrame frame) {
        this.frame = frame;
        setLayout(new BorderLayout());
        setBackground(UITheme.BACKGROUND);

        JPanel center = new JPanel();
        center.setLayout(new BoxLayout(center, BoxLayout.Y_AXIS));
        center.setOpaque(false);
        center.setBorder(BorderFactory.createEmptyBorder(100, 0, 100, 0));

        JLabel title = new JLabel("CAMPUS FOOD DELIVERY SYSTEM");
        title.setFont(UITheme.titleFont(40));
        title.setForeground(UITheme.ACCENT); // Red title
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel subtitle = new JLabel("FAST • RELIABLE • DELICIOUS FOOD DELIVERED TO YOU");
        subtitle.setFont(UITheme.bodyFont(16));
        subtitle.setForeground(UITheme.TEXT_DARK);
        subtitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        // START SYSTEM button - red text, white background, red border
        JButton startBtn = new JButton("START SYSTEM");
        startBtn.setFont(UITheme.titleFont(20));
        startBtn.setForeground(UITheme.ACCENT); // Red text
        startBtn.setBackground(Color.WHITE);    // White background
        startBtn.setFocusPainted(false);
        startBtn.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(UITheme.ACCENT, 2), // Red border
                BorderFactory.createEmptyBorder(15, 40, 15, 40)
        ));
        startBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        startBtn.setAlignmentX(Component.CENTER_ALIGNMENT);

        startBtn.addActionListener(e -> frame.showSystem());

        center.add(title);
        center.add(Box.createVerticalStrut(10));
        center.add(subtitle);
        center.add(Box.createVerticalStrut(40));
        center.add(startBtn);

        add(center, BorderLayout.CENTER);
    }
}