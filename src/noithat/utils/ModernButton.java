package noithat.utils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.RoundRectangle2D;

public class ModernButton extends JButton {
    private Color hoverColor;
    private Color pressedColor;
    private Color normalColor;
    private boolean isHovered = false;
    private boolean isPressed = false;
    
    public ModernButton(String text) {
        super(text);
        this.normalColor = new Color(100, 150, 200);
        this.hoverColor = new Color(80, 130, 180);
        this.pressedColor = new Color(60, 110, 160);
        
        initButton();
    }
    
    public ModernButton(String text, Color normalColor) {
        super(text);
        this.normalColor = normalColor;
        this.hoverColor = new Color(
            Math.min(normalColor.getRed() + 25, 255),
            Math.min(normalColor.getGreen() + 25, 255),
            Math.min(normalColor.getBlue() + 25, 255)
        );
        this.pressedColor = new Color(
            Math.max(normalColor.getRed() - 30, 0),
            Math.max(normalColor.getGreen() - 30, 0),
            Math.max(normalColor.getBlue() - 30, 0)
        );
        
        initButton();
    }
    
    private void initButton() {
        setFont(new Font("Segoe UI", Font.BOLD, 12));
        setForeground(Color.WHITE);
        setBackground(normalColor);
        setOpaque(false);
        setBorderPainted(false);
        setFocusPainted(false);
        setCursor(new Cursor(Cursor.HAND_CURSOR));
        setPreferredSize(new Dimension(120, 40));
        setContentAreaFilled(false);
        
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                isHovered = true;
                repaint();
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                isHovered = false;
                isPressed = false;
                repaint();
            }
            
            @Override
            public void mousePressed(MouseEvent e) {
                isPressed = true;
                repaint();
            }
            
            @Override
            public void mouseReleased(MouseEvent e) {
                isPressed = false;
                repaint();
            }
        });
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        
        int width = getWidth();
        int height = getHeight();
        int arc = 15;
        
        Color bgColor = normalColor;
        if (isPressed) {
            bgColor = pressedColor;
        } else if (isHovered) {
            bgColor = hoverColor;
        }
        
        g2d.setColor(bgColor);
        g2d.fillRoundRect(0, 0, width, height, arc, arc);
        
        if (isHovered && !isPressed) {
            g2d.setColor(new Color(255, 255, 255, 50));
            g2d.setStroke(new BasicStroke(2));
            g2d.drawRoundRect(1, 1, width - 2, height - 2, arc, arc);
        }
        
        super.paintComponent(g);
    }
    
    @Override
    public void paintBorder(Graphics g) {
    }
}
