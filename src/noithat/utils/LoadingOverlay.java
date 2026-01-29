package noithat.utils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Arc2D;

/**
 * Loading overlay with animated spinner
 */
public class LoadingOverlay extends JPanel {
    private Timer rotationTimer;
    private int rotationAngle = 0;
    private String message = "Đang tải...";
    
    public LoadingOverlay() {
        this("Đang tải...");
    }
    
    public LoadingOverlay(String message) {
        this.message = message;
        setOpaque(false);
        setLayout(new GridBagLayout());
        
        // Start rotation animation
        rotationTimer = new Timer(30, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                rotationAngle += 5;
                if (rotationAngle >= 360) {
                    rotationAngle = 0;
                }
                repaint();
            }
        });
        rotationTimer.start();
    }
    
    public void setMessage(String message) {
        this.message = message;
        repaint();
    }
    
    public void stopAnimation() {
        if (rotationTimer != null) {
            rotationTimer.stop();
        }
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        int width = getWidth();
        int height = getHeight();
        
        // Semi-transparent overlay
        g2.setColor(ColorTheme.OVERLAY_LIGHT);
        g2.fillRect(0, 0, width, height);
        
        // Spinner
        int spinnerSize = 50;
        int centerX = width / 2;
        int centerY = height / 2;
        
        g2.setStroke(new BasicStroke(4, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        
        // Draw animated arc
        g2.setColor(ColorTheme.PRIMARY);
        Arc2D.Double arc = new Arc2D.Double(
            centerX - spinnerSize / 2,
            centerY - spinnerSize / 2 - 20,
            spinnerSize,
            spinnerSize,
            rotationAngle,
            270,
            Arc2D.OPEN
        );
        g2.draw(arc);
        
        // Loading text
        g2.setColor(ColorTheme.TEXT_PRIMARY);
        g2.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        FontMetrics fm = g2.getFontMetrics();
        int textWidth = fm.stringWidth(message);
        g2.drawString(message, centerX - textWidth / 2, centerY + spinnerSize / 2 + 10);
        
        g2.dispose();
    }
}
