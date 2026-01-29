package noithat.utils;

import javax.swing.border.AbstractBorder;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;

/**
 * Enhanced shadow border with multi-layer shadows for depth
 */
public class EnhancedShadowBorder extends AbstractBorder {
    private final int shadowSize;
    private final int cornerRadius;
    private final ShadowLevel level;
    
    public enum ShadowLevel {
        SMALL(2, 10),    // Buttons, inputs
        MEDIUM(4, 15),   // Cards
        LARGE(8, 20),    // Dialogs
        EXTRA_LARGE(12, 25); // Tooltips
        
        final int offset;
        final int blur;
        
        ShadowLevel(int offset, int blur) {
            this.offset = offset;
            this.blur = blur;
        }
    }
    
    public EnhancedShadowBorder(ShadowLevel level, int cornerRadius) {
        this.level = level;
        this.shadowSize = level.blur;
        this.cornerRadius = cornerRadius;
    }
    
    @Override
    public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        // Draw multiple shadow layers for realistic depth
        int layers = 3;
        for (int i = layers; i > 0; i--) {
            float alpha = (float) i / (layers + 1);
            int offset = level.offset * i / layers;
            int blur = shadowSize * i / layers;
            
            g2.setColor(new Color(0, 0, 0, (int)(alpha * 40)));
            
            // Shadow shape
            RoundRectangle2D shadow = new RoundRectangle2D.Float(
                x + blur/2,
                y + offset + blur/2,
                width - blur,
                height - blur,
                cornerRadius,
                cornerRadius
            );
            
            g2.fill(shadow);
        }
        
        g2.dispose();
    }
    
    @Override
    public Insets getBorderInsets(Component c) {
        return new Insets(shadowSize, shadowSize, shadowSize + level.offset, shadowSize);
    }
    
    @Override
    public Insets getBorderInsets(Component c, Insets insets) {
        insets.left = shadowSize;
        insets.top = shadowSize;
        insets.right = shadowSize;
        insets.bottom = shadowSize + level.offset;
        return insets;
    }
}
