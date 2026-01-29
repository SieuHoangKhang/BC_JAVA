package noithat.utils;

import java.awt.Color;

/**
 * Professional color palette for enterprise-grade UI
 * Based on modern design systems (Tailwind, Material)
 */
public class ProfessionalColors {
    
    // Primary Actions
    public static final Color PRIMARY = new Color(37, 99, 235);      // Blue 600
    public static final Color PRIMARY_HOVER = new Color(29, 78, 216); // Blue 700
    public static final Color PRIMARY_LIGHT = new Color(96, 165, 250); // Blue 400
    
    // Status Colors
    public static final Color SUCCESS = new Color(22, 163, 74);      // Green 600
    public static final Color SUCCESS_HOVER = new Color(21, 128, 61); // Green 700
    public static final Color SUCCESS_LIGHT = new Color(134, 239, 172); // Green 300
    
    public static final Color WARNING = new Color(234, 88, 12);      // Orange 600
    public static final Color WARNING_HOVER = new Color(194, 65, 12); // Orange 700
    public static final Color WARNING_LIGHT = new Color(253, 186, 116); // Orange 300
    
    public static final Color DANGER = new Color(220, 38, 38);       // Red 600
    public static final Color DANGER_HOVER = new Color(185, 28, 28);  // Red 700
    public static final Color DANGER_LIGHT = new Color(252, 165, 165); // Red 300
    
    public static final Color INFO = new Color(14, 165, 233);        // Cyan 600
    public static final Color INFO_HOVER = new Color(8, 145, 178);    // Cyan 700
    
    // Neutral Palette
    public static final Color BACKGROUND = new Color(248, 250, 252); // Slate 50
    public static final Color SURFACE = new Color(255, 255, 255);    // White
    public static final Color SURFACE_HOVER = new Color(248, 250, 252); // Slate 50
    
    public static final Color BORDER = new Color(226, 232, 240);     // Slate 200
    public static final Color BORDER_LIGHT = new Color(241, 245, 249); // Slate 100
    public static final Color BORDER_DARK = new Color(203, 213, 225); // Slate 300
    
    // Text Colors
    public static final Color TEXT_PRIMARY = new Color(30, 41, 59);   // Slate 800
    public static final Color TEXT_SECONDARY = new Color(100, 116, 139); // Slate 500
    public static final Color TEXT_TERTIARY = new Color(148, 163, 184); // Slate 400
    public static final Color TEXT_DISABLED = new Color(203, 213, 225); // Slate 300
    
    // Table Colors
    public static final Color TABLE_HEADER = new Color(241, 245, 249); // Slate 100
    public static final Color TABLE_ROW_EVEN = new Color(255, 255, 255); // White
    public static final Color TABLE_ROW_ODD = new Color(248, 250, 252); // Slate 50
    public static final Color TABLE_SELECTED = new Color(219, 234, 254); // Blue 100
    public static final Color TABLE_HOVER = new Color(241, 245, 249); // Slate 100
    
    // Status Badge Colors
    public static final Color STATUS_ACTIVE_BG = new Color(220, 252, 231);  // Green 100
    public static final Color STATUS_ACTIVE_TEXT = new Color(22, 101, 52);   // Green 800
    
    public static final Color STATUS_PENDING_BG = new Color(254, 243, 199); // Yellow 100
    public static final Color STATUS_PENDING_TEXT = new Color(133, 77, 14);  // Yellow 800
    
    public static final Color STATUS_INACTIVE_BG = new Color(254, 226, 226); // Red 100
    public static final Color STATUS_INACTIVE_TEXT = new Color(153, 27, 27);  // Red 800
    
    // Shadow Colors
    public static final Color SHADOW_LIGHT = new Color(0, 0, 0, 10);  // 4% opacity
    public static final Color SHADOW_MEDIUM = new Color(0, 0, 0, 25); // 10% opacity
    public static final Color SHADOW_HEAVY = new Color(0, 0, 0, 40);  // 16% opacity
    
    // Helper methods
    public static Color withAlpha(Color color, int alpha) {
        return new Color(color.getRed(), color.getGreen(), color.getBlue(), alpha);
    }
    
    public static Color lighten(Color color, float factor) {
        int r = Math.min(255, (int)(color.getRed() + (255 - color.getRed()) * factor));
        int g = Math.min(255, (int)(color.getGreen() + (255 - color.getGreen()) * factor));
        int b = Math.min(255, (int)(color.getBlue() + (255 - color.getBlue()) * factor));
        return new Color(r, g, b);
    }
    
    public static Color darken(Color color, float factor) {
        int r = Math.max(0, (int)(color.getRed() * (1 - factor)));
        int g = Math.max(0, (int)(color.getGreen() * (1 - factor)));
        int b = Math.max(0, (int)(color.getBlue() * (1 - factor)));
        return new Color(r, g, b);
    }
}
