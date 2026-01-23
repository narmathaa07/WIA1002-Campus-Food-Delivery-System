package com.mycompany.fooddelivery2;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {

    private CardLayout cardLayout;
    private JPanel root;

    public static final String WELCOME = "WELCOME";
    public static final String SYSTEM = "SYSTEM";

    public MainFrame() {
        setTitle("Campus Food Delivery Management System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 800);
        setLocationRelativeTo(null);

        cardLayout = new CardLayout();
        root = new JPanel(cardLayout);

        // 1) Welcome Page
        root.add(new WelcomePanel(this), WELCOME);

        // 2) Full System UI
        root.add(new FoodDeliverySystem(), SYSTEM);

        setContentPane(root);

        showWelcome(); // show welcome first
    }

    public void showWelcome() {
        cardLayout.show(root, WELCOME);
    }

    public void showSystem() {
        cardLayout.show(root, SYSTEM);
    }

    // ✅ WelcomePanel will call this
    public void showSystemPanel() {
        cardLayout.show(root, SYSTEM); // ✅ root, not mainPanel
    }
}
