package com.mycompany.fooddelivery2;

import javax.swing.*;
import java.awt.*;

public class AssignmentPanel extends JPanel {

    public AssignmentPanel(FoodDeliverySystem system) {
        setLayout(new BorderLayout(16, 16));

        JPanel page = UITheme.gradientPage(new BorderLayout(16, 16));
        add(page, BorderLayout.CENTER);

        page.add(header(), BorderLayout.NORTH);

        JPanel card = UITheme.cardPanel(new GridLayout(2, 1, 16, 16));
        page.add(card, BorderLayout.CENTER);

        JButton manualBtn = new JButton("Manual Assignment");
        JButton autoBtn = new JButton("Auto Assign (Priority Queue)");

        UITheme.styleButton(manualBtn);
        UITheme.styleButtonSecondary(autoBtn);

        manualBtn.setFont(UITheme.headingFont(16));
        autoBtn.setFont(UITheme.headingFont(16));

        card.add(manualBtn);
        card.add(autoBtn);

        manualBtn.addActionListener(e -> system.showManualAssignmentDialog());
        autoBtn.addActionListener(e -> system.autoAssignOrder());
    }

    private JPanel header() {
        JPanel h = UITheme.cardPanel(new BorderLayout());
        JLabel title = new JLabel("🛵 ORDER ASSIGNMENT");
        title.setFont(UITheme.titleFont(22));
        title.setForeground(UITheme.ACCENT);

        JLabel sub = new JLabel("Assign orders to riders manually or automatically");
        sub.setFont(UITheme.bodyFont(14));

        h.add(title, BorderLayout.NORTH);
        h.add(sub, BorderLayout.SOUTH);
        return h;
    }
}
