package com.mycompany.fooddelivery2;

import javax.swing.*;
import java.awt.*;

public class StatisticsPanel extends JPanel {

    public StatisticsPanel(FoodDeliverySystem system) {
        setLayout(new BorderLayout(16, 16));

        JPanel page = UITheme.gradientPage(new BorderLayout(16, 16));
        add(page, BorderLayout.CENTER);

        page.add(header(), BorderLayout.NORTH);

        JPanel card = UITheme.cardPanel(new BorderLayout(12, 12));
        page.add(card, BorderLayout.CENTER);

        JTextArea statsArea = new JTextArea();
        statsArea.setEditable(false);
        statsArea.setFont(new Font("Consolas", Font.PLAIN, 14));
        statsArea.setBackground(new Color(248, 255, 248));
        statsArea.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        system.updateStatistics(statsArea);

        card.add(new JScrollPane(statsArea), BorderLayout.CENTER);

        JButton refreshBtn = new JButton("Refresh Statistics");
        UITheme.styleButton(refreshBtn);

        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottom.setOpaque(false);
        bottom.add(refreshBtn);

        card.add(bottom, BorderLayout.SOUTH);

        refreshBtn.addActionListener(e -> system.updateStatistics(statsArea));
    }

    private JPanel header() {
        JPanel h = UITheme.cardPanel(new BorderLayout());
        JLabel title = new JLabel("📊 SYSTEM STATISTICS");
        title.setFont(UITheme.titleFont(22));
        title.setForeground(UITheme.ACCENT);

        JLabel sub = new JLabel("Overview of orders, riders and campus routes");
        sub.setFont(UITheme.bodyFont(14));

        h.add(title, BorderLayout.NORTH);
        h.add(sub, BorderLayout.SOUTH);
        return h;
    }
}
