/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.fooddelivery2;

/**
 *
 * @author ASUS
 */


import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        // Set look and feel to system default
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        SwingUtilities.invokeLater(() -> {
            FoodDeliverySystem system = new FoodDeliverySystem();
            system.setVisible(true);
        });
    }
}