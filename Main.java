/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.fooddelivery3;


import javax.swing.*;
import java.awt.*;

public class Main {
    public static void main(String[] args) {

        try {
            // Use system look and feel
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            
            // FORCE ALL TEXT TO BLACK FOR MAXIMUM VISIBILITY
            UIManager.put("Button.foreground", Color.BLACK);
            UIManager.put("Button.font", new Font("Segoe UI", Font.BOLD, 14));
            
            UIManager.put("Label.foreground", Color.BLACK);
            UIManager.put("Label.font", new Font("Segoe UI", Font.PLAIN, 14));
            
            UIManager.put("TextField.foreground", Color.BLACK);
            UIManager.put("TextField.font", new Font("Segoe UI", Font.PLAIN, 14));
            
            UIManager.put("TextArea.foreground", Color.BLACK);
            UIManager.put("TextArea.font", new Font("Consolas", Font.PLAIN, 14));
            
            UIManager.put("ComboBox.foreground", Color.BLACK);
            UIManager.put("ComboBox.font", new Font("Segoe UI", Font.PLAIN, 14));
            
            UIManager.put("Table.foreground", Color.BLACK);
            UIManager.put("Table.font", new Font("Segoe UI", Font.PLAIN, 13));
            
            UIManager.put("TableHeader.foreground", Color.BLACK);
            UIManager.put("TableHeader.font", new Font("Segoe UI", Font.BOLD, 13));
            
            // Force solid backgrounds
            UIManager.put("Button.background", Color.WHITE);
            UIManager.put("Panel.background", Color.WHITE);
            UIManager.put("TextField.background", Color.WHITE);
            UIManager.put("TextArea.background", Color.WHITE);
            UIManager.put("ComboBox.background", Color.WHITE);
            UIManager.put("Table.background", Color.WHITE);
            
        } catch (Exception ignored) {
        }

        SwingUtilities.invokeLater(() -> {
            MainFrame frame = new MainFrame();
            frame.setVisible(true);
        });
    }
}