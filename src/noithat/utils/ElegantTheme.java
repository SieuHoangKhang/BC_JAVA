package noithat.utils;

import java.awt.Color;
import java.awt.LinearGradientPaint;
import java.awt.MultipleGradientPaint;

/**
 * Elegant Color Theme - Professional Furniture Store Design
 * Sophisticated color palette inspired by luxury interior design
 */
public class ElegantTheme {
    
    // ============================================
    // PRIMARY COLORS - Warm Elegant Tones
    // ============================================
    public static final Color PRIMARY = new Color(139, 90, 43);      // Warm Brown (Wood tone)
    public static final Color PRIMARY_HOVER = new Color(101, 65, 31);  // Darker Brown
    public static final Color PRIMARY_LIGHT = new Color(180, 140, 100); // Light Brown
    public static final Color PRIMARY_SOFT = new Color(245, 240, 230); // Cream
    
    // ============================================
    // ACCENT COLORS - Sophisticated
    // ============================================
    public static final Color ACCENT_GOLD = new Color(212, 175, 55);   // Gold
    public static final Color ACCENT_COPPER = new Color(184, 115, 51); // Copper
    public static final Color ACCENT_CREAM = new Color(255, 253, 245); // Cream
    public static final Color ACCENT_TAUPE = new Color(147, 130, 110); // Taupe
    public static final Color ACCENT_OLIVE = new Color(107, 142, 35);  // Olive Green
    public static final Color ACCENT_TERRACOTTA = new Color(226, 114, 91); // Terracotta
    
    // ============================================
    // CLASSIC COLORS
    // ============================================
    public static final Color CLASSIC_BLACK = new Color(45, 45, 45);
    public static final Color CLASSIC_WHITE = new Color(255, 255, 255);
    public static final Color CLASSIC_CREAM = new Color(255, 250, 240);
    public static final Color CLASSIC_GRAY = new Color(128, 128, 128);
    
    // ============================================
    // SEMANTIC COLORS
    // ============================================
    public static final Color SUCCESS = new Color(76, 175, 80);
    public static final Color SUCCESS_LIGHT = new Color(232, 245, 233);
    public static final Color SUCCESS_DARK = new Color(56, 142, 60);
    
    public static final Color WARNING = new Color(255, 193, 7);
    public static final Color WARNING_LIGHT = new Color(255, 249, 196);
    public static final Color WARNING_DARK = new Color(255, 152, 0);
    
    public static final Color DANGER = new Color(244, 67, 54);
    public static final Color DANGER_LIGHT = new Color(255, 235, 238);
    public static final Color DANGER_DARK = new Color(211, 47, 47);
    
    public static final Color INFO = new Color(33, 150, 243);
    public static final Color INFO_LIGHT = new Color(227, 242, 253);
    
    // ============================================
    // NEUTRAL COLORS - Warm Grays
    // ============================================
    public static final Color NEUTRAL_50 = new Color(250, 250, 248);   // Warm White
    public static final Color NEUTRAL_100 = new Color(245, 243, 239); // Off White
    public static final Color NEUTRAL_200 = new Color(230, 225, 218); // Light Gray
    public static final Color NEUTRAL_300 = new Color(199, 190, 180); // Medium Gray
    public static final Color NEUTRAL_400 = new Color(148, 140, 132); // Gray
    public static final Color NEUTRAL_500 = new Color(121, 112, 104); // Dark Gray
    public static final Color NEUTRAL_600 = new Color(97, 90, 82);    // Darker Gray
    public static final Color NEUTRAL_700 = new Color(73, 68, 60);    // Very Dark Gray
    public static final Color NEUTRAL_800 = new Color(49, 46, 42);    // Almost Black
    public static final Color NEUTRAL_900 = new Color(30, 28, 25);    // Black
    
    // ============================================
    // BACKGROUND COLORS
    // ============================================
    public static final Color BACKGROUND = NEUTRAL_100;
    public static final Color BACKGROUND_ALT = NEUTRAL_50;
    public static final Color SURFACE = CLASSIC_WHITE;
    public static final Color SURFACE_HOVER = NEUTRAL_100;
    public static final Color SURFACE_BORDER = NEUTRAL_200;
    
    // ============================================
    // TEXT COLORS
    // ============================================
    public static final Color TEXT_PRIMARY = NEUTRAL_900;
    public static final Color TEXT_SECONDARY = NEUTRAL_600;
    public static final Color TEXT_TERTIARY = NEUTRAL_400;
    public static final Color TEXT_DISABLED = NEUTRAL_300;
    public static final Color TEXT_INVERSE = CLASSIC_WHITE;
    
    // ============================================
    // GRADIENTS
    // ============================================
    public static LinearGradientPaint GRADIENT_PRIMARY = new LinearGradientPaint(
        0f, 0f, 1f, 0f,
        new float[]{0f, 1f},
        new Color[]{PRIMARY, ACCENT_GOLD},
        MultipleGradientPaint.CycleMethod.NO_CYCLE
    );
    
    public static LinearGradientPaint GRADIENT_WARM = new LinearGradientPaint(
        0f, 0f, 0f, 1f,
        new float[]{0f, 1f},
        new Color[]{new Color(255, 250, 240), new Color(250, 248, 245)},
        MultipleGradientPaint.CycleMethod.NO_CYCLE
    );
    
    public static LinearGradientPaint GRADIENT_HERO = new LinearGradientPaint(
        0f, 0f, 1f, 1f,
        new float[]{0f, 1f},
        new Color[]{PRIMARY, new Color(101, 65, 31)},
        MultipleGradientPaint.CycleMethod.NO_CYCLE
    );
    
    // ============================================
    // SHADOW COLORS
    // ============================================
    public static final Color SHADOW_SMALL = new Color(0, 0, 0, 15);
    public static final Color SHADOW_MEDIUM = new Color(0, 0, 0, 25);
    public static final Color SHADOW_LARGE = new Color(0, 0, 0, 35);
    public static final Color SHADOW_GLOW = new Color(139, 90, 43, 50);
    
    // ============================================
    // STATUS COLORS
    // ============================================
    public static final Color STATUS_ACTIVE = SUCCESS;
    public static final Color STATUS_ACTIVE_BG = SUCCESS_LIGHT;
    public static final Color STATUS_PENDING = WARNING;
    public static final Color STATUS_PENDING_BG = WARNING_LIGHT;
    public static final Color STATUS_INACTIVE = NEUTRAL_400;
    public static final Color STATUS_INACTIVE_BG = NEUTRAL_100;
    public static final Color STATUS_ERROR = DANGER;
    public static final Color STATUS_ERROR_BG = DANGER_LIGHT;
    public static final Color STATUS_COMPLETED = SUCCESS;
    public static final Color STATUS_COMPLETED_BG = SUCCESS_LIGHT;
    public static final Color STATUS_PROCESSING = new Color(33, 150, 243);
    public static final Color STATUS_PROCESSING_BG = INFO_LIGHT;
    
    // ============================================
    // TABLE COLORS
    // ============================================
    public static final Color TABLE_HEADER = NEUTRAL_100;
    public static final Color TABLE_HEADER_TEXT = NEUTRAL_700;
    public static final Color TABLE_ROW_EVEN = CLASSIC_WHITE;
    public static final Color TABLE_ROW_ODD = NEUTRAL_50;
    public static final Color TABLE_ROW_HOVER = NEUTRAL_100;
    public static final Color TABLE_SELECTED = PRIMARY_SOFT;
    public static final Color TABLE_BORDER = NEUTRAL_200;
    
    // ============================================
    // BUTTON COLORS
    // ============================================
    public static final Color BUTTON_PRIMARY = PRIMARY;
    public static final Color BUTTON_PRIMARY_HOVER = PRIMARY_HOVER;
    public static final Color BUTTON_SECONDARY = NEUTRAL_200;
    public static final Color BUTTON_SECONDARY_HOVER = NEUTRAL_300;
    public static final Color BUTTON_SUCCESS = SUCCESS;
    public static final Color BUTTON_DANGER = DANGER;
    
    // ============================================
    // INPUT COLORS
    // ============================================
    public static final Color INPUT_BACKGROUND = CLASSIC_WHITE;
    public static final Color INPUT_BORDER = NEUTRAL_300;
    public static final Color INPUT_BORDER_HOVER = NEUTRAL_400;
    public static final Color INPUT_BORDER_FOCUSED = PRIMARY;
    public static final Color INPUT_PLACEHOLDER = NEUTRAL_400;
    
    // ============================================
    // UTILITY METHODS
    // ============================================
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
    
    public static Color getStatusColor(String status) {
        if (status == null) return NEUTRAL_400;
        switch (status.toLowerCase()) {
            case "completed":
            case "done":
                return SUCCESS;
            case "pending":
            case "waiting":
                return WARNING;
            case "cancelled":
            case "deleted":
                return DANGER;
            case "processing":
                return INFO;
            case "active":
                return SUCCESS;
            case "inactive":
                return NEUTRAL_400;
            default:
                return NEUTRAL_500;
        }
    }
    
    public static Color getStatusBackground(String status) {
        if (status == null) return NEUTRAL_100;
        switch (status.toLowerCase()) {
            case "completed":
            case "done":
                return SUCCESS_LIGHT;
            case "pending":
            case "waiting":
                return WARNING_LIGHT;
            case "cancelled":
            case "deleted":
                return DANGER_LIGHT;
            case "processing":
                return INFO_LIGHT;
            case "active":
                return SUCCESS_LIGHT;
            case "inactive":
                return NEUTRAL_100;
            default:
                return NEUTRAL_100;
        }
    }
    
    public static String getStatusText(String status) {
        if (status == null) return "Không xác định";
        switch (status.toLowerCase()) {
            case "completed":
                return "Hoàn thành";
            case "pending":
                return "Chờ xử lý";
            case "processing":
                return "Đang xử lý";
            case "cancelled":
                return "Đã hủy";
            case "active":
                return "Hoạt động";
            case "inactive":
                return "Ngừng hoạt động";
            default:
                return status;
        }
    }
}
