package noithat.utils;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Modern toolbar button with flat design - no ugly boxes
 */
public class ToolbarButton extends JButton {
    private Color baseColor;
    private Color hoverColor;
    private boolean isHovered = false;
    
    public ToolbarButton(String text, Color color) {
        super(text);
        this.baseColor = color != null ? color : ProfessionalColors.PRIMARY;
        this.hoverColor = ProfessionalColors.darken(baseColor, 0.1f);
        
        setupButton();
        setupListeners();
    }
    
    private void setupButton() {
        setFont(new Font("Segoe UI", Font.PLAIN, 13));
        setForeground(Color.WHITE);
        
        // CRITICAL: Complete custom painting
        setFocusPainted(false);
        setBorderPainted(false);
        setContentAreaFilled(false);
        setOpaque(false);
        
        setBorder(new EmptyBorder(8, 16, 8, 16));
        setCursor(new Cursor(Cursor.HAND_CURSOR));
    }
    
    private void setupListeners() {
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
        });
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        
        // Draw rounded background
        Color bgColor = isHovered ? hoverColor : baseColor;
        if (getModel().isPressed()) {
            bgColor = ProfessionalColors.darken(baseColor, 0.2f);
        }
        
        g2.setColor(bgColor);
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), 6, 6);
        
        // Draw text manually (NO super.paintComponent!)
        g2.setColor(Color.WHITE);
        g2.setFont(getFont());
        
        FontMetrics fm = g2.getFontMetrics();
        String text = getText();
        int textWidth = fm.stringWidth(text);
        int textHeight = fm.getHeight();
        
        int x = (getWidth() - textWidth) / 2;
        int y = (getHeight() - textHeight) / 2 + fm.getAscent();
        
        g2.drawString(text, x, y);
        
        g2.dispose();
    }
    
    @Override
    protected void paintBorder(Graphics g) {
        // No border painting
    }
}
