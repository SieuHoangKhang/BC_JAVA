package noithat.utils;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;

/**
 * Stats Card Component - Modern Statistics Display
 * Beautiful card for showing dashboard statistics
 */
public class StatsCard extends JPanel {
    
    // ============================================
    // COLORS & STYLES
    // ============================================
    private Color accentColor;
    private Color accentBackgroundColor;
    private String title;
    private String value;
    private String iconText;
    private int cornerRadius = 16;
    private int iconSize = 48;
    private boolean showTrend = false;
    private String trendText = "";
    private boolean trendPositive = true;
    
    // ============================================
    // CONSTRUCTORS
    // ============================================
    
    public StatsCard(String title, String value, Color accentColor) {
        this.title = title;
        this.value = value;
        this.accentColor = accentColor;
        this.accentBackgroundColor = ModernTheme.withAlpha(accentColor, 100);
        initCard();
    }
    
    public StatsCard(String title, String value, Color accentColor, String iconText) {
        this(title, value, accentColor);
        this.iconText = iconText;
    }
    
    // ============================================
    // PROPERTY SETTERS
    // ============================================
    
    public void setTitle(String title) {
        this.title = title;
        repaint();
    }
    
    public String getTitle() {
        return title;
    }
    
    public void setValue(String value) {
        this.value = value;
        repaint();
    }
    
    public String getValue() {
        return value;
    }
    
    public void setAccentColor(Color color) {
        this.accentColor = color;
        this.accentBackgroundColor = ModernTheme.withAlpha(color, 100);
        repaint();
    }
    
    public Color getAccentColor() {
        return accentColor;
    }
    
    public void setIconText(String iconText) {
        this.iconText = iconText;
        repaint();
    }
    
    public String getIconText() {
        return iconText;
    }
    
    public void setShowTrend(boolean show) {
        this.showTrend = show;
        repaint();
    }
    
    public void setTrend(String trend, boolean positive) {
        this.trendText = trend;
        this.trendPositive = positive;
        this.showTrend = true;
        repaint();
    }
    
    public void setCornerRadius(int radius) {
        this.cornerRadius = radius;
        repaint();
    }
    
    // ============================================
    // INITIALIZATION
    // ============================================
    
    private void initCard() {
        setOpaque(false);
        setFont(FontHelper.body());
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
        
        int width = getWidth();
        int height = getHeight();
        
        // Draw shadow
        paintShadow(g2d, width, height);
        
        // Draw background
        paintBackground(g2d, width, height);
        
        // Draw icon
        if (iconText != null && !iconText.isEmpty()) {
            paintIcon(g2d, width, height);
        }
        
        // Draw content
        paintContent(g2d, width, height);
        
        // Draw accent bar
        paintAccentBar(g2d, width, height);
        
        g2d.dispose();
    }
    
    private void paintShadow(Graphics2D g2d, int width, int height) {
        // Main shadow
        g2d.setColor(ModernTheme.withAlpha(accentColor, 15));
        g2d.fillRoundRect(2, 4, width - 2, height - 4, cornerRadius, cornerRadius);
        
        // Subtle shadow
        g2d.setColor(ModernTheme.withAlpha(accentColor, 5));
        g2d.fillRoundRect(1, 2, width - 1, height - 2, cornerRadius, cornerRadius);
    }
    
    private void paintBackground(Graphics2D g2d, int width, int height) {
        // Gradient background
        Color topColor = ModernTheme.SURFACE;
        Color bottomColor = ModernTheme.lighten(ModernTheme.SURFACE, 0.02f);
        
        GradientPaint gradient = new GradientPaint(
            0, 0, topColor,
            0, height, bottomColor
        );
        
        g2d.setPaint(gradient);
        g2d.fillRoundRect(0, 0, width, height, cornerRadius, cornerRadius);
    }
    
    private void paintIcon(Graphics2D g2d, int width, int height) {
        // Icon background
        int iconX = 20;
        int iconY = (height - iconSize) / 2;
        
        RoundRectangle2D.Float iconBg = new RoundRectangle2D.Float(
            iconX, iconY, iconSize, iconSize, 12, 12
        );
        
        g2d.setColor(accentBackgroundColor);
        g2d.fill(iconBg);
        
        // Icon
        g2d.setColor(accentColor);
        g2d.setFont(new Font("Segoe UI Symbol", Font.PLAIN, 24));
        FontMetrics fm = g2d.getFontMetrics();
        int textWidth = fm.stringWidth(iconText);
        int textHeight = fm.getHeight();
        
        int textX = iconX + (iconSize - textWidth) / 2;
        int textY = iconY + (iconSize - textHeight) / 2 + fm.getAscent() - 2;
        
        g2d.drawString(iconText, textX, textY);
    }
    
    private void paintContent(Graphics2D g2d, int width, int height) {
        int contentX = iconText != null && !iconText.isEmpty() ? 85 : 20;
        
        // Title
        g2d.setColor(ModernTheme.TEXT_SECONDARY);
        g2d.setFont(FontHelper.caption());
        g2d.drawString(title, contentX, 30);
        
        // Value
        g2d.setColor(ModernTheme.TEXT_PRIMARY);
        g2d.setFont(FontHelper.h3());
        g2d.drawString(value, contentX, 60);
        
        // Trend
        if (showTrend && !trendText.isEmpty()) {
            paintTrend(g2d, width, contentX);
        }
    }
    
    private void paintTrend(Graphics2D g2d, int width, int contentX) {
        Color trendColor = trendPositive ? ModernTheme.SUCCESS : ModernTheme.DANGER;
        String trendSymbol = trendPositive ? "↑" : "↓";
        
        g2d.setFont(FontHelper.captionMedium());
        
        String fullTrend = trendSymbol + " " + trendText;
        int trendWidth = g2d.getFontMetrics().stringWidth(fullTrend);
        
        int trendX = contentX;
        int trendY = 82;
        
        // Trend background
        g2d.setColor(ModernTheme.withAlpha(trendColor, 80));
        g2d.fillRoundRect(trendX, trendY - 12, trendWidth + 4, 18, 6, 6);
        
        // Trend text
        g2d.setColor(trendColor);
        g2d.drawString(fullTrend, trendX + 2, trendY);
    }
    
    private void paintAccentBar(Graphics2D g2d, int width, int height) {
        // Left accent bar
        g2d.setColor(accentColor);
        g2d.fillRoundRect(0, 0, 4, height, 0, 0);
    }
    
    // ============================================
    // PREFERRED SIZE
    // ============================================
    
    @Override
    public Dimension getPreferredSize() {
        return new Dimension(220, 110);
    }
}
