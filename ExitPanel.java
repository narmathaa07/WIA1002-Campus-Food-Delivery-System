/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.fooddelivery3;

import javax.swing.*;
import java.awt.*;

public class ExitPanel extends JPanel {

    public ExitPanel() {
        setLayout(new BorderLayout(16, 16));

        JPanel page = UITheme.gradientPage(new BorderLayout(16, 16));
        add(page, BorderLayout.CENTER);

        page.add(header(), BorderLayout.NORTH);

        JPanel card = UITheme.cardPanel(new GridBagLayout());
        page.add(card, BorderLayout.CENTER);

        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setOpaque(false);
        contentPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Title
        JLabel titleLabel = new JLabel("Thank You for Using Our System!");
        titleLabel.setFont(UITheme.titleFont(24));
        titleLabel.setForeground(UITheme.ACCENT);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Subtitle
        JLabel subtitleLabel = new JLabel("Campus Food Delivery Management System");
        subtitleLabel.setFont(UITheme.bodyFont(16));
        subtitleLabel.setForeground(UITheme.TEXT_DARK);
        subtitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Info text
        JTextArea infoArea = new JTextArea();
        infoArea.setText("This system was developed to efficiently manage\n" +
                "food delivery operations across campus locations.\n\n" +
                "Features:\n" +
                "• Location & Route Management\n" +
                "• Order Processing with Priority Queue\n" +
                "• Rider Assignment & Tracking\n" +
                "• Real-time Statistics\n\n" +
                "Click the Exit button below to close the application.");
        infoArea.setEditable(false);
        infoArea.setFont(UITheme.bodyFont(13));
        infoArea.setBackground(new Color(0, 0, 0, 0));
        infoArea.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));
        infoArea.setAlignmentX(Component.CENTER_ALIGNMENT);
        infoArea.setLineWrap(true);
        infoArea.setWrapStyleWord(true);

        // EXIT BUTTON - red text, white background, red border
        JButton exitButton = new JButton("Exit Application");
        exitButton.setFont(UITheme.headingFont(16));
        exitButton.setForeground(UITheme.ACCENT); // Red text
        exitButton.setBackground(Color.WHITE);    // White background
        exitButton.setFocusPainted(false);
        exitButton.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(UITheme.ACCENT, 2), // Red border
                BorderFactory.createEmptyBorder(12, 20, 12, 20)
        ));
        exitButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        exitButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        exitButton.setPreferredSize(new Dimension(200, 50));

        exitButton.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(
                    this,
                    "Are you sure you want to exit the application?",
                    "Confirm Exit",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE
            );

            if (confirm == JOptionPane.YES_OPTION) {
                System.exit(0);
            }
        });

        contentPanel.add(Box.createVerticalStrut(20));
        contentPanel.add(titleLabel);
        contentPanel.add(Box.createVerticalStrut(10));
        contentPanel.add(subtitleLabel);
        contentPanel.add(Box.createVerticalStrut(30));
        contentPanel.add(infoArea);
        contentPanel.add(Box.createVerticalStrut(30));
        contentPanel.add(exitButton);
        contentPanel.add(Box.createVerticalStrut(20));

        card.add(contentPanel);

        // Footer
        JPanel footer = new JPanel(new FlowLayout(FlowLayout.CENTER));
        footer.setOpaque(false);
        JLabel footerLabel = new JLabel("© 2024 Campus Food Delivery System - All Rights Reserved");
        footerLabel.setFont(UITheme.bodyFont(11));
        footerLabel.setForeground(Color.GRAY);
        footer.add(footerLabel);

        page.add(footer, BorderLayout.SOUTH);
    }

    private JPanel header() {
        JPanel h = UITheme.cardPanel(new BorderLayout());

        JLabel title = new JLabel("EXIT");
        title.setFont(UITheme.titleFont(22));
        title.setForeground(UITheme.TEXT_DARK);

        JLabel sub = new JLabel("Close the application safely");
        sub.setFont(UITheme.bodyFont(14));
        sub.setForeground(UITheme.TEXT_DARK);

        h.add(title, BorderLayout.NORTH);
        h.add(sub, BorderLayout.SOUTH);
        return h;
    }
}