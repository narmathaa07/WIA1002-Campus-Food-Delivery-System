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
import java.awt.*;

public class MenuPanel extends JPanel {

    public MenuPanel(JTabbedPane tabbedPane) {
        setLayout(new GridLayout(6, 1, 10, 10));
        setBackground(new Color(45, 62, 80));
        setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));

        JButton welcomeBtn = createButton("Welcome", new Color(0, 173, 181));
        JButton locationsBtn = createButton("Locations & Routes", new Color(0, 148, 50));
        JButton ordersBtn = createButton("Orders", new Color(255, 87, 34));
        JButton ridersBtn = createButton("Riders", new Color(156, 39, 176));
        JButton assignmentBtn = createButton("Assignment", new Color(255, 193, 7));
        JButton statisticsBtn = createButton("Statistics", new Color(33, 150, 243));

        welcomeBtn.addActionListener(e -> tabbedPane.setSelectedIndex(0));
        locationsBtn.addActionListener(e -> tabbedPane.setSelectedIndex(1));
        ordersBtn.addActionListener(e -> tabbedPane.setSelectedIndex(2));
        ridersBtn.addActionListener(e -> tabbedPane.setSelectedIndex(3));
        assignmentBtn.addActionListener(e -> tabbedPane.setSelectedIndex(4));
        statisticsBtn.addActionListener(e -> tabbedPane.setSelectedIndex(5));

        add(welcomeBtn);
        add(locationsBtn);
        add(ordersBtn);
        add(ridersBtn);
        add(assignmentBtn);
        add(statisticsBtn);
    }

    private JButton createButton(String text, Color bgColor) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 16));
        button.setFocusPainted(false);
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setPreferredSize(new Dimension(180, 50));

        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(button.getBackground().darker());
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(bgColor);
            }
        });

        return button;
    }
}