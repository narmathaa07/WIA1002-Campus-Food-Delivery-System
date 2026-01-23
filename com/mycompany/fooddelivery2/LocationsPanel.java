package com.mycompany.fooddelivery2;

import javax.swing.*;
import java.awt.*;

public class LocationsPanel extends JPanel {

    private final FoodDeliverySystem system;
    private final JTextArea mapArea;

    public LocationsPanel(FoodDeliverySystem system) {
        this.system = system;
        setLayout(new BorderLayout(16, 16));

        JPanel page = UITheme.gradientPage(new BorderLayout(16, 16));
        add(page, BorderLayout.CENTER);

        page.add(header(), BorderLayout.NORTH);

        JPanel card = UITheme.cardPanel(new BorderLayout(12, 12));
        page.add(card, BorderLayout.CENTER);

        JLabel section = new JLabel("📍 CAMPUS MAP (ADJACENCY LIST)");
        section.setFont(UITheme.headingFont(16));
        section.setForeground(UITheme.TEXT_DARK);

        mapArea = new JTextArea();
        mapArea.setEditable(false);
        mapArea.setFont(new Font("Consolas", Font.PLAIN, 14));
        mapArea.setBackground(new Color(245, 250, 246));
        mapArea.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));

        refreshMap();

        card.add(section, BorderLayout.NORTH);
        card.add(new JScrollPane(mapArea), BorderLayout.CENTER);

        JButton refreshBtn = new JButton("Refresh Map");
        UITheme.styleButton(refreshBtn);

        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottom.setOpaque(false);
        bottom.add(refreshBtn);

        card.add(bottom, BorderLayout.SOUTH);

        refreshBtn.addActionListener(e -> refreshMap());
    }

    private JPanel header() {
        JPanel h = UITheme.cardPanel(new BorderLayout());

        JLabel title = new JLabel("🏫 LOCATIONS & ROUTES");
        title.setFont(UITheme.titleFont(22));
        title.setForeground(UITheme.ACCENT);

        JLabel sub = new JLabel("Visual overview of campus food delivery routes");
        sub.setFont(UITheme.bodyFont(14));
        sub.setForeground(UITheme.TEXT_DARK);

        h.add(title, BorderLayout.NORTH);
        h.add(sub, BorderLayout.SOUTH);
        return h;
    }

    private void refreshMap() {
        mapArea.setText(system.campusMap.getGraphInfo());
    }
}
