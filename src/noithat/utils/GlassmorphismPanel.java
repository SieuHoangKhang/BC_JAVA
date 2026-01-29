package noithat.utils;

import javax.swing.*;
import java.awt.*;

/**
 * Glassmorphism panel with frosted glass effect
 */
public class GlassmorphismPanel extends JPanel {
    private final int cornerRadius;
    private final float transparency;
    
    public GlassmorphismPanel(int cornerRadius) {
        this(cornerRadius, 0.7f);
    }
    
    public GlassmorphismPanel(int cornerRadius, float transparency) {
        this.cornerRadius = cornerRadius;
        this.transparency = transparency;
        setOpaque(false);
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        int width = getWidth();
        int height = getHeight();
        
        // Background with transparency
        g2.setColor(ColorTheme.withAlpha(Color.WHITE, (int)(transparency * 255)));
        g2.fillRoundRect(0, 0, width, height, cornerRadius, cornerRadius);
        
        // Subtle white border
        g2.setColor(ColorTheme.GLASSMORPHISM_BORDER);
        g2.setStroke(new BasicStroke(1.5f));
        g2.drawRoundRect(1, 1, width - 2, height - 2, cornerRadius, cornerRadius);
        
        // Optional: Gradient overlay for depth
        GradientPaint overlay = new GradientPaint(
            0, 0, ColorTheme.withAlpha(Color.WHITE, 40),
            0, height, ColorTheme.withAlpha(Color.WHITE, 0)
        );
        g2.setPaint(overlay);
        g2.fillRoundRect(0, 0, width, height / 2, cornerRadius, cornerRadius);
        
        g2.dispose();
        super.paintComponent(g);
    }
}
