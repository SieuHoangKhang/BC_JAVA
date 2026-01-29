package noithat.utils;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;

/**
 * Premium Glassmorphism Card - Modern UI Component
 * Inspired by iOS, macOS, and modern web design
 */
public class GlassmorphismCard extends JPanel {
    
    // ============================================
    // COLORS & STYLES
    // ============================================
    private Color backgroundColor;
    private Color borderColor;
    private Color shadowColor;
    private int cornerRadius = 16;
    private float backgroundOpacity = 0.95f;
    private float borderOpacity = 0.3f;
    private int shadowOffset = 8;
    private int shadowBlur = 24;
    private boolean showTopHighlight = true;
    
    // ============================================
    // CONSTRUCTORS
    // ============================================
    
    public GlassmorphismCard() {
        this(ModernTheme.SURFACE);
    }
    
    public GlassmorphismCard(Color backgroundColor) {
        this.backgroundColor = backgroundColor;
        this.borderColor = ModernTheme.withAlpha(Color.WHITE, 100);
        this.shadowColor = ModernTheme.withAlpha(ModernTheme.NEUTRAL_900, 50);
        initCard();
    }
    
    public GlassmorphismCard(LayoutManager layout) {
        this(layout, ModernTheme.SURFACE);
    }
    
    public GlassmorphismCard(LayoutManager layout, Color backgroundColor) {
        super(layout);
        this.backgroundColor = backgroundColor;
        this.borderColor = ModernTheme.withAlpha(Color.WHITE, 100);
        this.shadowColor = ModernTheme.withAlpha(ModernTheme.NEUTRAL_900, 50);
        initCard();
    }
    
    // ============================================
    // PROPERTY SETTERS
    // ============================================
    
    public void setCornerRadius(int radius) {
        this.cornerRadius = radius;
        repaint();
    }
    
    public void setBackgroundOpacity(float opacity) {
        this.backgroundOpacity = Math.max(0f, Math.min(1f, opacity));
        repaint();
    }
    
    public void setBorderOpacity(float opacity) {
        this.borderOpacity = Math.max(0f, Math.min(1f, opacity));
        repaint();
    }
    
    public void setShadowOffset(int offset) {
        this.shadowOffset = offset;
        repaint();
    }
    
    public void setShadowBlur(int blur) {
        this.shadowBlur = blur;
        repaint();
    }
    
    public void setShowTopHighlight(boolean show) {
        this.showTopHighlight = show;
        repaint();
    }
    
    public void setCardColors(Color background, Color border, Color shadow) {
        this.backgroundColor = background;
        this.borderColor = border;
        this.shadowColor = shadow;
        repaint();
    }
    
    // ============================================
    // INITIALIZATION
    // ============================================
    
    private void initCard() {
        setOpaque(false);
        setBackground(backgroundColor);
        setDoubleBuffered(true);
    }
    
    // ============================================
    // PAINTING
    // ============================================
    
    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
        
        int width = getWidth();
        int height = getHeight();
        
        // Draw shadow
        paintShadow(g2d, width, height);
        
        // Draw background with rounded corners
        RoundRectangle2D.Float backgroundShape = new RoundRectangle2D.Float(
            0, 0, width - 1, height - 1, cornerRadius, cornerRadius
        );
        
        // Create gradient background
        paintBackground(g2d, width, height, backgroundShape);
        
        // Draw border
        paintBorder(g2d, width, height);
        
        // Draw top highlight
        if (showTopHighlight) {
            paintTopHighlight(g2d, width, height);
        }
        
        g2d.dispose();
    }
    
    private void paintShadow(Graphics2D g2d, int width, int height) {
        // Multi-layer shadow for depth
        for (int i = 0; i < 3; i++) {
            float alpha = (shadowBlur > 0) ? 0.08f : 0.05f;
            int offset = shadowOffset + (i * 2);
            
            g2d.setColor(ModernTheme.withAlpha(shadowColor, (int)(alpha * 255)));
            
            Graphics2D shadowG2d = (Graphics2D) g2d.create();
            shadowG2d.translate(offset, offset);
            shadowG2d.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, cornerRadius, cornerRadius);
            shadowG2d.dispose();
        }
    }
    
    private void paintBackground(Graphics2D g2d, int width, int height, RoundRectangle2D.Float shape) {
        // Create subtle gradient
        Color topColor = ModernTheme.withAlpha(backgroundColor, (int)(backgroundOpacity * 255));
        Color bottomColor = ModernTheme.withAlpha(
            ModernTheme.lighten(backgroundColor, 0.02f), 
            (int)(backgroundOpacity * 255)
        );
        
        GradientPaint gradient = new GradientPaint(
            0, 0, topColor,
            0, height, bottomColor
        );
        
        g2d.setPaint(gradient);
        g2d.fill(shape);
    }
    
    private void paintBorder(Graphics2D g2d, int width, int height) {
        g2d.setColor(ModernTheme.withAlpha(borderColor, (int)(borderOpacity * 255)));
        g2d.setStroke(new BasicStroke(1f));
        g2d.drawRoundRect(0, 0, width - 1, height - 1, cornerRadius, cornerRadius);
    }
    
    private void paintTopHighlight(Graphics2D g2d, int width, int height) {
        // Subtle highlight at the top
        GradientPaint highlight = new GradientPaint(
            0, 0,
            ModernTheme.withAlpha(Color.WHITE, 60),
            0, cornerRadius,
            ModernTheme.withAlpha(Color.WHITE, 0)
        );
        
        g2d.setPaint(highlight);
        g2d.setClip(new RoundRectangle2D.Float(
            0, 0, width, cornerRadius, cornerRadius, cornerRadius
        ));
        g2d.fillRect(0, 0, width, cornerRadius);
        g2d.setClip(null);
    }
    
    // ============================================
    // PREFERRED SIZE
    // ============================================
    
    @Override
    public Dimension getPreferredSize() {
        return new Dimension(super.getPreferredSize().width + 20, super.getPreferredSize().height + 20);
    }
}
