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
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class WelcomePanel extends JPanel {

    private final MainFrame frame;

    public WelcomePanel(MainFrame frame) {
        this.frame = frame;
        setOpaque(true);

        // Use YOUR gradient helper (white -> green)
        JPanel bg = UITheme.gradientPage(new GridBagLayout());
        bg.setOpaque(true);

        JPanel center = new JPanel();
        center.setOpaque(false);
        center.setLayout(new BoxLayout(center, BoxLayout.Y_AXIS));

        JLabel title = new JLabel("CAMPUS FOOD DELIVERY SYSTEM");
        title.setFont(UITheme.titleFont(40));
        title.setForeground(UITheme.ACCENT);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel subtitle = new JLabel("FAST • RELIABLE • DELICIOUS FOOD DELIVERED TO YOU");
        subtitle.setFont(UITheme.bodyFont(16));
        subtitle.setForeground(UITheme.TEXT_DARK);
        subtitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        center.add(title);
        center.add(Box.createVerticalStrut(6));
        center.add(subtitle);
        center.add(Box.createVerticalStrut(26));

        // BIG + visible arrow
        ArrowCircleButton arrow = new ArrowCircleButton();
        arrow.setAlignmentX(Component.CENTER_ALIGNMENT);

        arrow.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                frame.showSystem();
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                arrow.setHover(true);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                arrow.setHover(false);
            }
        });

        center.add(arrow);
        bg.add(center);
        setLayout(new BorderLayout());
        add(bg, BorderLayout.CENTER);
    }

    // ===== Circular wrapped arrow component =====
    static class ArrowCircleButton extends JComponent {

        private boolean hover = false;

        public ArrowCircleButton() {
            setPreferredSize(new Dimension(150, 150));
            setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            setOpaque(false);
        }

        public void setHover(boolean h) {
            hover = h;
            repaint();
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            int size = Math.min(getWidth(), getHeight());
            int pad = 10;

            int x = (getWidth() - size) / 2 + pad;
            int y = (getHeight() - size) / 2 + pad;
            int d = size - pad * 2;

            // Shadow (so it pops on gradient)
            g2.setColor(new Color(0, 0, 0, 40));
            g2.fillOval(x + 5, y + 7, d, d);

            // Main circle
            g2.setColor(hover ? UITheme.GREEN_DARK : UITheme.GREEN);
            g2.fillOval(x, y, d, d);

            // Inner ring highlight
            g2.setColor(new Color(255, 255, 255, 60));
            g2.setStroke(new BasicStroke(5f));
            g2.drawOval(x + 10, y + 10, d - 20, d - 20);

            // Wrapped arrow arc
            g2.setColor(Color.WHITE);
            g2.setStroke(new BasicStroke(8f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));

            int arcPad = 26;
            int ax = x + arcPad;
            int ay = y + arcPad;
            int ad = d - arcPad * 2;

            // Big arc around circle
            g2.drawArc(ax, ay, ad, ad, 210, 260);

            // Arrow head near the right side of arc
            double angle = Math.toRadians(25);
            int cx = x + d / 2;
            int cy = y + d / 2;
            int r = ad / 2;

            int tipX = (int) (cx + r * Math.cos(angle));
            int tipY = (int) (cy - r * Math.sin(angle));

            g2.drawLine(tipX, tipY, tipX - 22, tipY - 10);
            g2.drawLine(tipX, tipY, tipX - 6, tipY - 28);

            // Optional NEXT label to make it obvious
            g2.setFont(UITheme.headingFont(14));
            FontMetrics fm = g2.getFontMetrics();
            String txt = "NEXT";
            int tw = fm.stringWidth(txt);
            g2.setColor(Color.WHITE);
            g2.drawString(txt, x + (d - tw) / 2, y + d - 18);

            g2.dispose();
        }
    }
}