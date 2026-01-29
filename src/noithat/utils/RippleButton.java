package noithat.utils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Ellipse2D;

/**
 * Material Design Ripple Button with gradient and animations
 */
public class RippleButton extends JButton {
    private Color baseColor;
    private Color gradientEndColor;
    private Point rippleCenter;
    private float rippleRadius = 0;
    private Timer rippleTimer;
    private boolean isHovered = false;
    private int cornerRadius = 10;
    
    public RippleButton(String text, Color baseColor) {
        super(text);
        this.baseColor = baseColor;
        this.gradientEndColor = ColorTheme.darken(baseColor, 0.15f);
        
        setFocusPainted(false);
        setBorderPainted(false);
        setContentAreaFilled(false);
        setForeground(Color.WHITE);
        setFont(new Font("Segoe UI", Font.BOLD, 13));
        setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Hover effect
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                isHovered = true;
                repaint();
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                isHovered = false;
                repaint();
            }
            
            @Override
            public void mousePressed(MouseEvent e) {
                rippleCenter = e.getPoint();
                startRipple();
            }
        });
    }
    
    public void setCornerRadius(int radius) {
        this.cornerRadius = radius;
    }
    
    private void startRipple() {
        rippleRadius = 0;
        if (rippleTimer != null && rippleTimer.isRunning()) {
            rippleTimer.stop();
        }
        
        rippleTimer = new Timer(10, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                rippleRadius += getWidth() / 15f;
                if (rippleRadius > getWidth() * 1.5) {
                    rippleTimer.stop();
                    rippleRadius = 0;
                }
                repaint();
            }
        });
        rippleTimer.start();
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        int width = getWidth();
        int height = getHeight();
        
        // Gradient background
        GradientPaint gradient;
        if (isHovered) {
            Color brighterBase = ColorTheme.brighten(baseColor, 0.1f);
            Color brighterEnd = ColorTheme.brighten(gradientEndColor, 0.1f);
            gradient = new GradientPaint(0, 0, brighterBase, width, height, brighterEnd);
        } else {
            gradient = new GradientPaint(0, 0, baseColor, width, height, gradientEndColor);
        }
        
        g2.setPaint(gradient);
        g2.fillRoundRect(0, 0, width, height, cornerRadius, cornerRadius);
        
        // Ripple effect
        if (rippleRadius > 0 && rippleCenter != null) {
            g2.setColor(ColorTheme.HOVER_OVERLAY);
            Ellipse2D ripple = new Ellipse2D.Double(
                rippleCenter.x - rippleRadius,
                rippleCenter.y - rippleRadius,
                rippleRadius * 2,
                rippleRadius * 2
            );
            g2.fill(ripple);
        }
        
        // Hover overlay
        if (isHovered) {
            g2.setColor(ColorTheme.HOVER_OVERLAY);
            g2.fillRoundRect(0, 0, width, height, cornerRadius, cornerRadius);
        }
        
        g2.dispose();
        super.paintComponent(g);
    }
}
