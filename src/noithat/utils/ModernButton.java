package noithat.utils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.LinearGradientPaint;
import java.awt.MultipleGradientPaint;
import java.awt.geom.Point2D;

/**
 * Premium ModernButton with gradient support, smooth animations, and professional styling
 * Using ElegantTheme for sophisticated furniture store design
 */
public class ModernButton extends JButton {
    
    // ============================================
    // COLORS - Using ElegantTheme
    // ============================================
    private Color normalColor;
    private Color gradientEndColor;
    private Color hoverColor;
    private Color pressedColor;
    private Color disabledColor;
    @SuppressWarnings("unused")
    private Color rippleColor;
    
    // ============================================
    // STATE
    // ============================================
    private boolean isHovered = false;
    private boolean isPressed = false;
    private boolean useGradient = true;
    private boolean useRipple = true;
    private int cornerRadius = 12;
    
    // ============================================
    // RIPPLE EFFECT
    // ============================================
    private float rippleProgress = 0f;
    private Point rippleCenter = null;
    private Timer rippleTimer;
    
    // ============================================
    // SHADOW
    // ============================================
    private int shadowOffset = 4;
    @SuppressWarnings("unused")
    private int shadowBlur = 12;
    
    // ============================================
    // CONSTRUCTORS
    // ============================================
    
    public ModernButton(String text) {
        this(text, ElegantTheme.PRIMARY);
    }
    
    public ModernButton(String text, Color normalColor) {
        super(text);
        this.normalColor = normalColor;
        this.gradientEndColor = ElegantTheme.darken(normalColor, 0.15f);
        this.hoverColor = ElegantTheme.lighten(normalColor, 0.08f);
        this.pressedColor = ElegantTheme.darken(normalColor, 0.2f);
        this.disabledColor = ElegantTheme.NEUTRAL_300;
        this.rippleColor = new Color(255, 255, 255, 100);
        initButton();
    }
    
    public ModernButton(String text, Color normalColor, Color textColor) {
        this(text, normalColor);
        setForeground(textColor);
    }
    
    // ============================================
    // PROPERTY SETTERS
    // ============================================
    
    public void setUseGradient(boolean useGradient) {
        this.useGradient = useGradient;
        repaint();
    }
    
    public void setCornerRadius(int radius) {
        this.cornerRadius = radius;
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
    
    public void setRippleEffect(boolean enabled) {
        this.useRipple = enabled;
    }
    
    public void setButtonColors(Color normal, Color hover, Color pressed) {
        this.normalColor = normal;
        this.gradientEndColor = ModernTheme.darken(normal, 0.15f);
        this.hoverColor = hover;
        this.pressedColor = pressed;
        repaint();
    }
    
    // ============================================
    // INITIALIZATION
    // ============================================
    
    private void initButton() {
        setFont(FontHelper.button());
        setForeground(Color.WHITE);
        setBackground(normalColor);
        setOpaque(false);
        setBorderPainted(false);
        setFocusPainted(false);
        setCursor(new Cursor(Cursor.HAND_CURSOR));
        setPreferredSize(new Dimension(140, 44));
        setContentAreaFilled(false);
        setDoubleBuffered(true);
        
        // Initialize ripple timer
        rippleTimer = new Timer(16, e -> {
            rippleProgress += 0.15f;
            if (rippleProgress >= 1f) {
                rippleProgress = 0f;
                rippleCenter = null;
                rippleTimer.stop();
            }
            repaint();
        });
        
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                if (isEnabled()) {
                    isHovered = true;
                    repaint();
                }
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                isHovered = false;
                isPressed = false;
                repaint();
            }
            
            @Override
            public void mousePressed(MouseEvent e) {
                if (isEnabled()) {
                    isPressed = true;
                    rippleCenter = e.getPoint();
                    rippleProgress = 0f;
                    if (useRipple) {
                        rippleTimer.start();
                    }
                    repaint();
                }
            }
            
            @Override
            public void mouseReleased(MouseEvent e) {
                isPressed = false;
                repaint();
            }
        });
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
        
        if (!isEnabled()) {
            paintDisabled(g2d, width, height);
            g2d.dispose();
            return;
        }
        
        // Determine colors based on state
        Color startColor = normalColor;
        Color endColor = gradientEndColor;
        
        if (isPressed) {
            startColor = pressedColor;
            endColor = ElegantTheme.darken(pressedColor, 0.1f);
        } else if (isHovered) {
            startColor = hoverColor;
            endColor = ElegantTheme.darken(hoverColor, 0.1f);
        }
        
        // Draw shadow
        paintShadow(g2d, width, height, startColor);
        
        // Draw background with gradient
        if (useGradient) {
            LinearGradientPaint gradient = new LinearGradientPaint(
                new Point2D.Double(0, 0),
                new Point2D.Double(width, height),
                new float[]{0f, 1f},
                new Color[]{startColor, endColor},
                MultipleGradientPaint.CycleMethod.NO_CYCLE
            );
            g2d.setPaint(gradient);
        } else {
            g2d.setColor(startColor);
        }
        
        // Draw rounded rectangle
        g2d.fillRoundRect(0, 0, width - 4, height - 4, cornerRadius, cornerRadius);
        
        // Draw ripple effect
        if (useRipple && rippleCenter != null && rippleProgress > 0) {
            paintRipple(g2d, width, height);
        }
        
        // Draw hover highlight
        if (isHovered && !isPressed) {
            g2d.setColor(ElegantTheme.withAlpha(Color.WHITE, 30));
            g2d.fillRoundRect(0, 0, width - 4, height - 4, cornerRadius, cornerRadius);
        }
        
        // Draw subtle border on hover
        if (isHovered) {
            g2d.setColor(ElegantTheme.withAlpha(Color.WHITE, 50));
            g2d.setStroke(new BasicStroke(1.5f));
            g2d.drawRoundRect(1, 1, width - 6, height - 8, cornerRadius - 1, cornerRadius - 1);
        }
        
        // Draw text
        g2d.setColor(getForeground());
        g2d.setFont(getFont());
        
        FontMetrics fm = g2d.getFontMetrics();
        String text = getText();
        int textWidth = fm.stringWidth(text);
        int textHeight = fm.getHeight();
        
        int x = (width - 4 - textWidth) / 2;
        int y = (height - 4 - textHeight) / 2 + fm.getAscent();
        
        g2d.drawString(text, x, y);
        
        g2d.dispose();
    }
    
    private void paintShadow(Graphics2D g2d, int width, int height, Color color) {
        if (isPressed) return;
        
        // Create shadow
        g2d.setColor(ElegantTheme.withAlpha(color, 40));
        g2d.fillRoundRect(2, shadowOffset + 2, width - 4 - 2, height - 4 - shadowOffset - 2, cornerRadius, cornerRadius);
        
        // Lighter shadow layer
        g2d.setColor(ElegantTheme.withAlpha(color, 20));
        g2d.fillRoundRect(1, shadowOffset + 1, width - 4 - 1, height - 4 - shadowOffset - 1, cornerRadius, cornerRadius);
    }
    
    private void paintRipple(Graphics2D g2d, int width, int height) {
        int maxRadius = (int) Math.sqrt(width * width + height * height);
        int radius = (int) (maxRadius * rippleProgress);
        int alpha = (int) (80 * (1 - rippleProgress));
        
        g2d.setColor(new Color(255, 255, 255, alpha));
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f - rippleProgress));
        
        g2d.fillOval(
            rippleCenter.x - radius,
            rippleCenter.y - radius,
            radius * 2,
            radius * 2
        );
        
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
    }
    
    private void paintDisabled(Graphics2D g2d, int width, int height) {
        g2d.setColor(ElegantTheme.withAlpha(disabledColor, 100));
        g2d.fillRoundRect(0, 0, width - 4, height - 4, cornerRadius, cornerRadius);
        
        g2d.setColor(ElegantTheme.withAlpha(disabledColor, 50));
        g2d.setStroke(new BasicStroke(1));
        g2d.drawRoundRect(1, 1, width - 6, height - 8, cornerRadius - 1, cornerRadius - 1);
        
        g2d.setColor(ElegantTheme.withAlpha(getForeground(), 50));
        g2d.setFont(getFont());
        
        FontMetrics fm = g2d.getFontMetrics();
        String text = getText();
        int textWidth = fm.stringWidth(text);
        int textHeight = fm.getHeight();
        
        int x = (width - 4 - textWidth) / 2;
        int y = (height - 4 - textHeight) / 2 + fm.getAscent();
        
        g2d.drawString(text, x, y);
    }
    
    @Override
    public void paintBorder(Graphics g) {
        // No border - we draw our own
    }
    
    // ============================================
    // PREFERRED SIZE
    // ============================================
    
    @Override
    public Dimension getPreferredSize() {
        FontMetrics fm = getFontMetrics(getFont());
        int textWidth = fm.stringWidth(getText()) + 32;
        int textHeight = fm.getHeight() + 16;
        return new Dimension(Math.max(textWidth, 100), Math.max(textHeight, 40));
    }
}
