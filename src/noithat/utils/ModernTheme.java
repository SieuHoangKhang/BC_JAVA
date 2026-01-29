package noithat.utils;

import java.awt.Color;
import java.awt.LinearGradientPaint;
import java.awt.MultipleGradientPaint;

/**
 * Professional Color Theme - Modern & Beautiful Design System
 * Inspired by top enterprise applications (Notion, Figma, Linear style)
 */
public class ModernTheme {
    
    // ============================================
    // PRIMARY BRAND COLORS - Modern Blue
    // ============================================
    public static final Color PRIMARY = new Color(59, 130, 246);      // Blue 500
    public static final Color PRIMARY_HOVER = new Color(37, 99, 235);   // Blue 600
    public static final Color PRIMARY_DARK = new Color(29, 78, 216);   // Blue 700
    public static final Color PRIMARY_LIGHT = new Color(96, 165, 250); // Blue 400
    public static final Color PRIMARY_SUPER_LIGHT = new Color(219, 234, 254); // Blue 100
    
    // ============================================
    // ACCENT COLORS - Vibrant & Modern
    // ============================================
    public static final Color ACCENT_PURPLE = new Color(139, 92, 246);  // Violet 500
    public static final Color ACCENT_PINK = new Color(236, 72, 153);   // Pink 500
    public static final Color ACCENT_ORANGE = new Color(249, 115, 22); // Orange 500
    public static final Color ACCENT_GREEN = new Color(34, 197, 94);   // Green 500
    public static final Color ACCENT_RED = new Color(239, 68, 68);    // Red 500
    public static final Color ACCENT_TEAL = new Color(20, 184, 166);  // Teal 500
    public static final Color ACCENT_BLUE = new Color(52, 152, 219);  // Sky Blue
    
    // ============================================
    // SEMANTIC COLORS
    // ============================================
    public static final Color SUCCESS = new Color(34, 197, 94);
    public static final Color SUCCESS_LIGHT = new Color(220, 252, 231);
    public static final Color SUCCESS_DARK = new Color(21, 128, 61);
    
    public static final Color WARNING = new Color(245, 158, 11);
    public static final Color WARNING_LIGHT = new Color(254, 243, 199);
    public static final Color WARNING_DARK = new Color(180, 83, 9);
    
    public static final Color DANGER = new Color(239, 68, 68);
    public static final Color DANGER_LIGHT = new Color(254, 228, 228);
    public static final Color DANGER_DARK = new Color(185, 28, 28);
    
    public static final Color INFO = new Color(59, 130, 246);
    public static final Color INFO_LIGHT = new Color(219, 234, 254);
    
    // ============================================
    // NEUTRAL COLORS - Slate Scale
    // ============================================
    public static final Color NEUTRAL_50 = new Color(248, 250, 252);   // Slate 50
    public static final Color NEUTRAL_100 = new Color(241, 245, 249); // Slate 100
    public static final Color NEUTRAL_200 = new Color(226, 232, 240); // Slate 200
    public static final Color NEUTRAL_300 = new Color(203, 213, 225); // Slate 300
    public static final Color NEUTRAL_400 = new Color(148, 163, 184); // Slate 400
    public static final Color NEUTRAL_500 = new Color(100, 116, 139); // Slate 500
    public static final Color NEUTRAL_600 = new Color(71, 85, 105);   // Slate 600
    public static final Color NEUTRAL_700 = new Color(51, 65, 85);    // Slate 700
    public static final Color NEUTRAL_800 = new Color(30, 41, 59);    // Slate 800
    public static final Color NEUTRAL_900 = new Color(15, 23, 42);    // Slate 900
    
    // ============================================
    // BACKGROUND COLORS
    // ============================================
    public static final Color BACKGROUND = NEUTRAL_50;
    public static final Color BACKGROUND_ALT = NEUTRAL_100;
    public static final Color SURFACE = Color.WHITE;
    public static final Color SURFACE_HOVER = NEUTRAL_100;
    public static final Color SURFACE_BORDER = NEUTRAL_200;
    
    // ============================================
    // TEXT COLORS
    // ============================================
    public static final Color TEXT_PRIMARY = NEUTRAL_900;
    public static final Color TEXT_SECONDARY = NEUTRAL_600;
    public static final Color TEXT_TERTIARY = NEUTRAL_400;
    public static final Color TEXT_DISABLED = NEUTRAL_300;
    public static final Color TEXT_INVERSE = Color.WHITE;
    
    // ============================================
    // GRADIENTS (using simple GradientPaint for compatibility)
    // ============================================
    public static LinearGradientPaint GRADIENT_PRIMARY = new LinearGradientPaint(
        0f, 0f, 1f, 0f,
        new float[]{0f, 1f},
        new Color[]{PRIMARY, new Color(99, 102, 241)},
        MultipleGradientPaint.CycleMethod.NO_CYCLE
    );
    
    public static LinearGradientPaint GRADIENT_SURFACE = new LinearGradientPaint(
        0f, 0f, 0f, 1f,
        new float[]{0f, 1f},
        new Color[]{new Color(255, 255, 255), new Color(248, 250, 252)},
        MultipleGradientPaint.CycleMethod.NO_CYCLE
    );
    
    public static LinearGradientPaint GRADIENT_HERO = new LinearGradientPaint(
        0f, 0f, 1f, 1f,
        new float[]{0f, 1f},
        new Color[]{PRIMARY, new Color(139, 92, 246)},
        MultipleGradientPaint.CycleMethod.NO_CYCLE
    );
    
    // ============================================
    // SHADOW COLORS
    // ============================================
    public static final Color SHADOW_SMALL = new Color(0, 0, 0, 20);
    public static final Color SHADOW_MEDIUM = new Color(0, 0, 0, 40);
    public static final Color SHADOW_LARGE = new Color(0, 0, 0, 80);
    public static final Color SHADOW_GLOW = new Color(59, 130, 246, 100);
    
    // ============================================
    // STATUS COLORS FOR BADGES
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
    public static final Color STATUS_PROCESSING = new Color(59, 130, 246);
    public static final Color STATUS_PROCESSING_BG = INFO_LIGHT;
    
    // ============================================
    // TABLE COLORS
    // ============================================
    public static final Color TABLE_HEADER = NEUTRAL_100;
    public static final Color TABLE_HEADER_TEXT = NEUTRAL_700;
    public static final Color TABLE_ROW_EVEN = Color.WHITE;
    public static final Color TABLE_ROW_ODD = NEUTRAL_50;
    public static final Color TABLE_ROW_HOVER = NEUTRAL_100;
    public static final Color TABLE_SELECTED = PRIMARY_SUPER_LIGHT;
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
    public static final Color INPUT_BACKGROUND = Color.WHITE;
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
