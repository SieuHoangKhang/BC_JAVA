package noithat.utils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.RoundRectangle2D;

/**
 * Enhanced ModernButton with gradient support and smooth animations
 */
public class ModernButton extends JButton {
    private Color normalColor;
    private Color gradientEndColor;
    private boolean isHovered = false;
    private boolean isPressed = false;
    private boolean useGradient = true;
    private int cornerRadius = 10;
    
    public ModernButton(String text) {
        this(text, ColorTheme.PRIMARY);
    }
    
    public ModernButton(String text, Color normalColor) {
        super(text);
        this.normalColor = normalColor;
        this.gradientEndColor = ColorTheme.darken(normalColor, 0.15f);
        initButton();
    }
    
    public void setUseGradient(boolean useGradient) {
        this.useGradient = useGradient;
        repaint();
    }
    
    public void setCornerRadius(int radius) {
        this.cornerRadius = radius;
        repaint();
    }
    
    private void initButton() {
        setFont(FontHelper.BUTTON);
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
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        
        int width = getWidth();
        int height = getHeight();
        
        // Determine colors based on state
        Color startColor = normalColor;
        Color endColor = gradientEndColor;
        
        if (isPressed) {
            startColor = ColorTheme.darken(normalColor, 0.2f);
            endColor = ColorTheme.darken(gradientEndColor, 0.2f);
        } else if (isHovered) {
            startColor = ColorTheme.brighten(normalColor, 0.1f);
            endColor = ColorTheme.brighten(gradientEndColor, 0.1f);
        }
        
        // Draw shadow
        if (!isPressed) {
            g2d.setColor(ColorTheme.SHADOW_LIGHT);
            g2d.fillRoundRect(2, 4, width - 4, height - 4, cornerRadius, cornerRadius);
        }
        
        // Draw background with gradient
        if (useGradient) {
            GradientPaint gradient = new GradientPaint(
                0, 0, startColor,
                width, height, endColor
            );
            g2d.setPaint(gradient);
        } else {
            g2d.setColor(startColor);
        }
        
        g2d.fillRoundRect(0, 0, width - 4, height - 6, cornerRadius, cornerRadius);
        
        // Hover overlay
        if (isHovered && !isPressed) {
            g2d.setColor(ColorTheme.HOVER_OVERLAY);
            g2d.fillRoundRect(0, 0, width - 4, height - 6, cornerRadius, cornerRadius);
        }
        
        // Optional subtle border
        if (isHovered) {
            g2d.setColor(ColorTheme.withAlpha(Color.WHITE, 60));
            g2d.setStroke(new BasicStroke(1.5f));
            g2d.drawRoundRect(1, 1, width - 6, height - 8, cornerRadius, cornerRadius);
        }
        
        // Draw text manually (NO super.paintComponent!)
        g2d.setColor(Color.WHITE);
        g2d.setFont(getFont());
        
        FontMetrics fm = g2d.getFontMetrics();
        String text = getText();
        int textWidth = fm.stringWidth(text);
        int textHeight = fm.getHeight();
        
        int x = (width - textWidth) / 2;
        int y = (height - textHeight) / 2 + fm.getAscent() - 3; // Adjust for shadow offset
        
        g2d.drawString(text, x, y);
        
        g2d.dispose();
    }
    
    @Override
    public void paintBorder(Graphics g) {
        // No border
    }
}
