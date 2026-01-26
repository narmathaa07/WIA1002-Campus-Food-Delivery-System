/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.fooddelivery3;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {

    private CardLayout cardLayout;
    private JPanel root;
    private FoodDeliverySystem systemPanel;

    public static final String WELCOME = "WELCOME";
    public static final String SYSTEM = "SYSTEM";

    public MainFrame() {
        setTitle("Campus Food Delivery Management System");
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setSize(1200, 800);
        setLocationRelativeTo(null);

        createMenuBar();

        cardLayout = new CardLayout();
        root = new JPanel(cardLayout);
        root.setOpaque(true);

        // 1) Welcome Page
        root.add(new WelcomePanel(this), WELCOME);

        // 2) Create system panel BUT DON'T INITIALIZE DATA YET
        systemPanel = new FoodDeliverySystem();

        // 👇 CRITICAL: Load data BEFORE adding to UI
        boolean loaded = FileManager.loadAllData(systemPanel);

        root.add(systemPanel, SYSTEM);

        setContentPane(root);

        showWelcome();

        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                exitApplication();
            }
        });
    }

    private void createMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        menuBar.setBackground(UITheme.ACCENT);
        menuBar.setBorder(BorderFactory.createLineBorder(UITheme.ACCENT, 1));

        // File Menu
        JMenu fileMenu = new JMenu("File");
        fileMenu.setFont(UITheme.bodyFont(13));
        fileMenu.setForeground(Color.WHITE);
        fileMenu.setBackground(UITheme.ACCENT);
        fileMenu.setOpaque(true);

        JMenuItem saveItem = new JMenuItem("Save All Data (CSV)");
        JMenuItem openExcelItem = new JMenuItem("Open in Excel");
        JMenuItem exportItem = new JMenuItem("Export to CSV");
        JMenuItem fileLocationsItem = new JMenuItem("View File Locations");
        JMenuItem backupInfoItem = new JMenuItem("View Backups");
        JMenuItem exitItem = new JMenuItem("Exit");

        // Style menu items
        styleMenuItem(saveItem);
        styleMenuItem(openExcelItem);
        styleMenuItem(exportItem);
        styleMenuItem(fileLocationsItem);
        styleMenuItem(backupInfoItem);
        styleMenuItem(exitItem);

        saveItem.addActionListener(e -> FileManager.showSaveDialog(systemPanel, this));
        openExcelItem.addActionListener(e -> {
            JOptionPane.showMessageDialog(this,
                    "To open in Excel:\n" +
                            "1. Save data first (File → Save All Data)\n" +
                            "2. Go to food_delivery_data folder\n" +
                            "3. Double-click any .csv file",
                    "Open in Excel",
                    JOptionPane.INFORMATION_MESSAGE);
        });
        exportItem.addActionListener(e -> FileManager.exportToExcelFormat(systemPanel, this));
        fileLocationsItem.addActionListener(e -> FileManager.showFileLocationsDialog(this));
        backupInfoItem.addActionListener(e -> FileManager.showBackupInfoDialog(this));
        exitItem.addActionListener(e -> exitApplication());

        fileMenu.add(saveItem);
        fileMenu.addSeparator();
        fileMenu.add(openExcelItem);
        fileMenu.add(exportItem);
        fileMenu.addSeparator();
        fileMenu.add(fileLocationsItem);
        fileMenu.add(backupInfoItem);
        fileMenu.addSeparator();
        fileMenu.add(exitItem);

        // Help Menu
        JMenu helpMenu = new JMenu("Help");
        helpMenu.setFont(UITheme.bodyFont(13));
        helpMenu.setForeground(Color.WHITE);
        helpMenu.setBackground(UITheme.ACCENT);
        helpMenu.setOpaque(true);

        JMenuItem aboutItem = new JMenuItem("About");
        styleMenuItem(aboutItem);
        aboutItem.addActionListener(e -> showAboutDialog());

        helpMenu.add(aboutItem);

        menuBar.add(fileMenu);
        menuBar.add(helpMenu);

        setJMenuBar(menuBar);
    }

    private void styleMenuItem(JMenuItem item) {
        item.setFont(UITheme.bodyFont(12));
        item.setBackground(Color.WHITE);
        item.setForeground(UITheme.TEXT_DARK);
        item.setOpaque(true);
        item.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
    }

    private void refreshUI() {
        CardLayout cl = (CardLayout) root.getLayout();
        cl.show(root, SYSTEM);
    }

    private void exitApplication() {
        int choice = JOptionPane.showConfirmDialog(this,
                "Would you like to save before exiting?\n\n" +
                        "Yes - Save and exit\n" +
                        "No - Exit without saving\n" +
                        "Cancel - Return to application",
                "Exit Application",
                JOptionPane.YES_NO_CANCEL_OPTION,
                JOptionPane.QUESTION_MESSAGE);

        if (choice == JOptionPane.YES_OPTION) {
            boolean saved = FileManager.saveAllData(systemPanel);
            if (saved) {
                // JOptionPane.showMessageDialog(this, "Data saved. Goodbye!", "Exit", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Failed to save data!", "Error", JOptionPane.ERROR_MESSAGE);
            }
            System.exit(0);
        } else if (choice == JOptionPane.NO_OPTION) {
            System.exit(0);
        }
        // Cancel - do nothing, window closing will be cancelled
    }

    private void showAboutDialog() {
        JOptionPane.showMessageDialog(this,
                "Campus Food Delivery Management System\n" +
                        "Version 2.0\n\n" +
                        "Features:\n" +
                        "• Location and Route Management\n" +
                        "• Order Processing with Priority Queue\n" +
                        "• Rider Assignment and Tracking\n" +
                        "• File Persistence (Save/Load)\n\n" +
                        "© 2024 Campus Food Delivery",
                "About",
                JOptionPane.INFORMATION_MESSAGE);
    }

    public void showWelcome() {
        cardLayout.show(root, WELCOME);
    }

    public void showSystem() {
        cardLayout.show(root, SYSTEM);
    }

    public void showSystemPanel() {
        cardLayout.show(root, SYSTEM);
    }
}