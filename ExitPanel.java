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

public class ExitPanel extends JPanel {

    public ExitPanel() {
        setLayout(new BorderLayout(16, 16));

        JPanel page = UITheme.gradientPage(new BorderLayout(16, 16));
        add(page, BorderLayout.CENTER);

        page.add(header(), BorderLayout.NORTH);

        JPanel card = UITheme.cardPanel(new GridBagLayout());
        page.add(card, BorderLayout.CENTER);

        // Create a panel for the content
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setOpaque(false);
        contentPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Icon/Emoji
        JLabel iconLabel = new JLabel("👋");
        iconLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 80));
        iconLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

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

        // Info text - FIXED: Using regular string concatenation instead of text blocks
        JTextArea infoArea = new JTextArea();
        infoArea.setText("This system was developed to efficiently manage food delivery\n" +
                        "operations across campus locations.\n\n" +
                        "Features:\n" +
                        "• Location & Route Management\n" +
                        "• Order Processing with Priority Queue\n" +
                        "• Rider Assignment & Tracking\n" +
                        "• Automated Email Notifications\n" +
                        "• Real-time Statistics\n\n" +
                        "Click the Exit button below to close the application.");
        infoArea.setEditable(false);
        infoArea.setFont(UITheme.bodyFont(13));
        infoArea.setBackground(new Color(0, 0, 0, 0)); // Transparent
        infoArea.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));
        infoArea.setAlignmentX(Component.CENTER_ALIGNMENT);
        infoArea.setLineWrap(true);
        infoArea.setWrapStyleWord(true);

        // Exit button
        JButton exitButton = new JButton("Exit Application");
        UITheme.styleButton(exitButton);
        exitButton.setFont(UITheme.headingFont(16));
        exitButton.setPreferredSize(new Dimension(200, 50));
        exitButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        // Add action listener to exit the application
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

        // Add all components to content panel
        contentPanel.add(Box.createVerticalStrut(20));
        contentPanel.add(iconLabel);
        contentPanel.add(Box.createVerticalStrut(20));
        contentPanel.add(titleLabel);
        contentPanel.add(Box.createVerticalStrut(10));
        contentPanel.add(subtitleLabel);
        contentPanel.add(Box.createVerticalStrut(30));
        contentPanel.add(infoArea);
        contentPanel.add(Box.createVerticalStrut(30));
        contentPanel.add(exitButton);
        contentPanel.add(Box.createVerticalStrut(20));

        // Add content panel to card
        card.add(contentPanel);

        // Footer with copyright
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
        
        JLabel title = new JLabel("🚪 EXIT");
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