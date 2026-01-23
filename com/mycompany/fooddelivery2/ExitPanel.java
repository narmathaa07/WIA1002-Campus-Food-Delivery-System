package com.mycompany.fooddelivery2;

import javax.swing.*;
import java.awt.*;

public class ExitPanel extends JPanel {

    public ExitPanel() {
        setLayout(new BorderLayout());

        JPanel bg = UITheme.gradientPage(new GridBagLayout());

        JPanel card = UITheme.cardPanel(new GridLayout(5, 1, 10, 10));

        JLabel title = new JLabel("EXIT APPLICATION", SwingConstants.CENTER);
        title.setFont(UITheme.titleFont(28));
        title.setForeground(UITheme.ACCENT);

        JLabel msg = new JLabel("Are you sure you want to exit?", SwingConstants.CENTER);
        msg.setFont(UITheme.bodyFont(16));
        msg.setForeground(UITheme.TEXT_DARK);

        JButton exitBtn = new JButton("Exit Now");
        UITheme.styleButton(exitBtn);

        JButton cancelBtn = new JButton("Cancel");
        UITheme.styleButtonSecondary(cancelBtn);

        exitBtn.addActionListener(e -> {
            int choice = JOptionPane.showConfirmDialog(
                    this,
                    "Confirm exit?",
                    "Exit",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE);

            if (choice == JOptionPane.YES_OPTION) {
                Window window = SwingUtilities.getWindowAncestor(this);
                if (window != null)
                    window.dispose(); // close main window
                System.exit(0); // make sure app ends
            }
        });

        cancelBtn.addActionListener(e -> {
            // Go back to previous tab (Statistics) if possible
            Container parent = getParent();
            while (parent != null && !(parent instanceof JTabbedPane)) {
                parent = parent.getParent();
            }
            if (parent instanceof JTabbedPane tabs) {
                int idx = tabs.getSelectedIndex();
                if (idx > 0)
                    tabs.setSelectedIndex(idx - 1);
            }
        });

        card.add(title);
        card.add(msg);
        card.add(new JLabel(" ", SwingConstants.CENTER));
        card.add(exitBtn);
        card.add(cancelBtn);

        bg.add(card);
        add(bg, BorderLayout.CENTER);
    }
}
