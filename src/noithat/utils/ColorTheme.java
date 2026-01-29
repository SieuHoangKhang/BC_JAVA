package noithat.utils;

import java.awt.*;

/**
 * Enhanced ColorTheme with premium gradients and glassmorphism support
 */
public class ColorTheme {
    // ===== NEUTRALS =====
    public static final Color BACKGROUND = new Color(248, 249, 250);
    public static final Color SURFACE = new Color(255, 255, 255);
    public static final Color TEXT_PRIMARY = new Color(44, 62, 80);
    public static final Color TEXT_SECONDARY = new Color(127, 140, 141);
    public static final Color TEXT_LIGHT = new Color(255, 255, 255);
    public static final Color BORDER = new Color(224, 224, 224);
    
    // ===== SOLID COLORS (for compatibility) =====
    public static final Color PRIMARY = new Color(102, 126, 234);
    public static final Color PRIMARY_LIGHT = new Color(174, 186, 239); // Added PRIMARY_LIGHT
    public static final Color SECONDARY = new Color(52, 73, 94);
    public static final Color SECONDARY_LIGHT = new Color(149, 165, 166);
    public static final Color SUCCESS = new Color(46, 204, 113);
    public static final Color DANGER = new Color(231, 76, 60);
    public static final Color WARNING = new Color(241, 196, 15);
    public static final Color INFO = new Color(52, 152, 219);
    
    // ===== ACCENT COLORS (Vibrant) =====
    public static final Color ACCENT_BLUE = new Color(52, 152, 219);
    public static final Color ACCENT_GREEN = new Color(46, 204, 113);
    public static final Color ACCENT_PURPLE = new Color(155, 89, 182);
    public static final Color ACCENT_ORANGE = new Color(230, 126, 34);
    public static final Color ACCENT_PINK = new Color(245, 87, 108);
    public static final Color ACCENT_TEAL = new Color(26, 188, 156);
    
    // ===== GRADIENT START/END COLORS =====
    // Primary gradient (Purple)
    public static final Color PRIMARY_GRADIENT_START = new Color(102, 126, 234);
    public static final Color PRIMARY_GRADIENT_END = new Color(118, 75, 162);
    
    // Success gradient (Teal)
    public static final Color SUCCESS_GRADIENT_START = new Color(17, 153, 142);
    public static final Color SUCCESS_GRADIENT_END = new Color(56, 239, 125);
    
    // Warning gradient (Pink)
    public static final Color WARNING_GRADIENT_START = new Color(240, 147, 251);
    public static final Color WARNING_GRADIENT_END = new Color(245, 87, 108);
    
    // Danger gradient (Coral)
    public static final Color DANGER_GRADIENT_START = new Color(250, 112, 154);
    public static final Color DANGER_GRADIENT_END = new Color(254, 225, 64);
    
    // Info gradient (Blue)
    public static final Color INFO_GRADIENT_START = new Color(48, 207, 208);
    public static final Color INFO_GRADIENT_END = new Color(51, 8, 103);
    
    // Secondary gradient (Dark blue)
    public static final Color SECONDARY_GRADIENT_START = new Color(52, 73, 94);
    public static final Color SECONDARY_GRADIENT_END = new Color(44, 62, 80);
    
    // ===== GLASSMORPHISM =====
    public static final Color GLASSMORPHISM_BG = new Color(255, 255, 255, 180);
    public static final Color GLASSMORPHISM_BORDER = new Color(255, 255, 255, 100);
    public static final Color GLASSMORPHISM_SHADOW = new Color(0, 0, 0, 30);
    
    // ===== OVERLAY & STATES =====
    public static final Color OVERLAY_DARK = new Color(0, 0, 0, 150);
    public static final Color OVERLAY_LIGHT = new Color(255, 255, 255, 200);
    public static final Color HOVER_OVERLAY = new Color(255, 255, 255, 25);
    public static final Color ACTIVE_OVERLAY = new Color(0, 0, 0, 15);
    public static final Color DISABLED_OVERLAY = new Color(200, 200, 200, 100);
    
    // ===== SHADOWS =====
    public static final Color SHADOW_LIGHT = new Color(0, 0, 0, 15);
    public static final Color SHADOW_MEDIUM = new Color(0, 0, 0, 25);
    public static final Color SHADOW_DARK = new Color(0, 0, 0, 40);
    
    /**
     * Create a linear gradient paint
     */
    public static GradientPaint createGradientPaint(int width, int height, Color start, Color end) {
        return new GradientPaint(0, 0, start, width, height, end);
    }
    
    /**
     * Create vertical gradient
     */
    public static GradientPaint createVerticalGradient(int height, Color start, Color end) {
        return new GradientPaint(0, 0, start, 0, height, end);
    }
    
    /**
     * Create horizontal gradient
     */
    public static GradientPaint createHorizontalGradient(int width, Color start, Color end) {
        return new GradientPaint(0, 0, start, width, 0, end);
    }
    
    /**
     * Brighten a color by percentage
     */
    public static Color brighten(Color color, float percentage) {
        int r = Math.min(255, (int)(color.getRed() * (1 + percentage)));
        int g = Math.min(255, (int)(color.getGreen() * (1 + percentage)));
        int b = Math.min(255, (int)(color.getBlue() * (1 + percentage)));
        return new Color(r, g, b, color.getAlpha());
    }
    
    /**
     * Darken a color by percentage
     */
    public static Color darken(Color color, float percentage) {
        int r = (int)(color.getRed() * (1 - percentage));
        int g = (int)(color.getGreen() * (1 - percentage));
        int b = (int)(color.getBlue() * (1 - percentage));
        return new Color(r, g, b, color.getAlpha());
    }
    
    /**
     * Set alpha transparency
     */
    public static Color withAlpha(Color color, int alpha) {
        return new Color(color.getRed(), color.getGreen(), color.getBlue(), alpha);
    }
    
    /**
     * Lighten a color by percentage (alternative to brighten)
     */
    public static Color lighten(Color color, float factor) {
        int r = Math.min(255, (int)(color.getRed() + (255 - color.getRed()) * factor));
        int g = Math.min(255, (int)(color.getGreen() + (255 - color.getGreen()) * factor));
        int b = Math.min(255, (int)(color.getBlue() + (255 - color.getBlue()) * factor));
        return new Color(r, g, b, color.getAlpha());
    }
}
