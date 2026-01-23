/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.fooddelivery2;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import javax.swing.BorderFactory;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;

/**
 *
 * @author ASUS
 */
public class EmailNotification {
    
    
    // Simulated email sending - shows what would be sent
    public static void showOrderConfirmation(Order order) {
        String subject = "✅ Order Confirmed - Campus Food Delivery";
        String content = generateOrderConfirmationEmail(order);
        displayEmailDialog("Order Confirmation", order.getStudentEmail(), subject, content);
    }
    
    public static void showOrderDelivered(Order order, Rider rider) {
        String subject = "🎉 Order Delivered Successfully!";
        String content = generateDeliveryConfirmationEmail(order, rider);
        displayEmailDialog("Delivery Confirmation", order.getStudentEmail(), subject, content);
    }
    
    private static String generateOrderConfirmationEmail(Order order) {
        StringBuilder sb = new StringBuilder();
        sb.append("Dear ").append(order.getStudentName()).append(",\n\n");
        sb.append("Thank you for using Campus Food Delivery System!\n");
        sb.append("Your order has been confirmed.\n\n");
        sb.append("📋 ORDER DETAILS:\n");
        sb.append("────────────────────────\n");
        sb.append("Order ID:      ").append(order.getOrderId()).append("\n");
        sb.append("Pickup:        ").append(order.getPickupLocation()).append("\n");
        sb.append("Delivery:      ").append(order.getDeliveryLocation()).append("\n");
        sb.append("Priority:      ").append(order.getPriority() == 1 ? "URGENT" : "Normal").append("\n");
        sb.append("Status:        ").append(order.getStatus()).append("\n");
        sb.append("Order Time:    ").append(getFormattedTime()).append("\n\n");
        sb.append("⏱️ Estimated Delivery: 30-45 minutes\n\n");
        sb.append("You will receive another email when your rider is assigned.\n");
        sb.append("────────────────────────\n");
        sb.append("Campus Food Delivery System\n");
        sb.append("University Campus\n");
        return sb.toString();
    }
    
    private static String generateDeliveryConfirmationEmail(Order order, Rider rider) {
        StringBuilder sb = new StringBuilder();
        sb.append("Dear ").append(order.getStudentName()).append(",\n\n");
        sb.append("🎉 GREAT NEWS! Your food has been delivered!\n\n");
        sb.append("✅ ORDER DELIVERED SUCCESSFULLY\n");
        sb.append("────────────────────────\n");
        sb.append("Order ID:      ").append(order.getOrderId()).append("\n");
        sb.append("Delivered to:  ").append(order.getDeliveryLocation()).append("\n");
        sb.append("Delivery Time: ").append(getFormattedTime()).append("\n");
        sb.append("Rider:         ").append(rider.getRiderName()).append("\n");
        sb.append("Rider ID:      ").append(rider.getRiderId()).append("\n\n");
        
        sb.append("👨‍🍳 RIDER FEEDBACK:\n");
        sb.append("────────────────────────\n");
        sb.append("Rider ").append(rider.getRiderName()).append(" has successfully\n");
        sb.append("delivered your order from ").append(order.getPickupLocation());
        sb.append(" to ").append(order.getDeliveryLocation()).append(".\n\n");
        
        sb.append("📱 NEED HELP?\n");
        sb.append("────────────────────────\n");
        sb.append("• Contact Support: food.delivery@campus.edu\n");
        sb.append("• Phone: 03-1234 5678\n");
        sb.append("• Hours: 8AM - 10PM Daily\n\n");
        
        sb.append("Thank you for choosing Campus Food Delivery!\n");
        sb.append("We hope you enjoy your meal! 🍽️\n");
        sb.append("────────────────────────\n");
        sb.append("Campus Food Delivery System\n");
        sb.append("University Campus\n");
        return sb.toString();
    }
    
    private static String getFormattedTime() {
        // Simple time formatting without java.util
        long millis = System.currentTimeMillis();
        int hours = (int) ((millis / 3600000) % 24) + 8; // Adjust for Malaysia time
        int minutes = (int) ((millis / 60000) % 60);
        return String.format("%02d:%02d", hours, minutes);
    }
    
    private static void displayEmailDialog(String title, String toEmail, String subject, String content) {
        JDialog emailDialog = new JDialog();
        emailDialog.setTitle(title);
        emailDialog.setSize(500, 400);
        emailDialog.setLayout(new BorderLayout());
        
        JPanel headerPanel = new JPanel(new GridLayout(3, 1));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        headerPanel.setBackground(new Color(220, 240, 255));
        
        JLabel titleLabel = new JLabel("📧 EMAIL NOTIFICATION (Simulated)");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 14));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        JLabel toLabel = new JLabel("To: " + toEmail);
        toLabel.setFont(new Font("Monospaced", Font.PLAIN, 12));
        
        JLabel subjectLabel = new JLabel("Subject: " + subject);
        subjectLabel.setFont(new Font("Arial", Font.BOLD, 12));
        
        headerPanel.add(titleLabel);
        headerPanel.add(toLabel);
        headerPanel.add(subjectLabel);
        
        JTextArea emailContent = new JTextArea(content);
        emailContent.setEditable(false);
        emailContent.setFont(new Font("Monospaced", Font.PLAIN, 11));
        emailContent.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        JScrollPane scrollPane = new JScrollPane(emailContent);
        
        JPanel footerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JLabel footerLabel = new JLabel("Note: In a real system, this email would be sent automatically");
        footerLabel.setFont(new Font("Arial", Font.ITALIC, 10));
        footerLabel.setForeground(Color.GRAY);
        footerPanel.add(footerLabel);
        
        emailDialog.add(headerPanel, BorderLayout.NORTH);
        emailDialog.add(scrollPane, BorderLayout.CENTER);
        emailDialog.add(footerPanel, BorderLayout.SOUTH);
        
        emailDialog.setLocationRelativeTo(null);
        emailDialog.setVisible(true);
    }
}

