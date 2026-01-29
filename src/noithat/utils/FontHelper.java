package noithat.utils;

import java.awt.Font;
import java.util.HashMap;
import java.util.Map;

/**
 * Professional Typography System
 * Modern fonts for enterprise applications
 */
public class FontHelper {
    
    // ============================================
    // FONT NAMES
    // ============================================
    public static final String FONT_SANS_SERIF = "Segoe UI";
    public static final String FONT_MONOSPACE = "Consolas";
    
    // ============================================
    // FONT SIZES
    // ============================================
    public static final int SIZE_DISPLAY = 48;  // Hero titles
    public static final int SIZE_H1 = 36;       // Page titles
    public static final int SIZE_H2 = 28;       // Section headers
    public static final int SIZE_H3 = 24;       // Card titles
    public static final int SIZE_H4 = 20;       // Sub-headers
    public static final int SIZE_BODY_LARGE = 18;  // Large body text
    public static final int SIZE_BODY = 14;     // Default body text
    public static final int SIZE_BODY_SMALL = 13;  // Secondary body
    public static final int SIZE_CAPTION = 11; // Labels, captions
    public static final int SIZE_TINY = 10;     // Tiny text
    
    // ============================================
    // FONT WEIGHTS
    // ============================================
    public static final int WEIGHT_BOLD = Font.BOLD;
    public static final int WEIGHT_REGULAR = Font.PLAIN;
    public static final int WEIGHT_MEDIUM = Font.BOLD; // Use bold for medium
    
    // ============================================
    // STYLED FONTS CACHE
    // ============================================
    private static final Map<String, Font> fontCache = new HashMap<>();
    
    // ============================================
    // TITLE FONTS
    // ============================================
    public static Font display() {
        return getFont(FONT_SANS_SERIF, SIZE_DISPLAY, WEIGHT_BOLD);
    }
    
    public static Font displayLight() {
        return getFont(FONT_SANS_SERIF, SIZE_DISPLAY, WEIGHT_REGULAR);
    }
    
    public static Font h1() {
        return getFont(FONT_SANS_SERIF, SIZE_H1, WEIGHT_BOLD);
    }
    
    public static Font h1Light() {
        return getFont(FONT_SANS_SERIF, SIZE_H1, WEIGHT_REGULAR);
    }
    
    public static Font h2() {
        return getFont(FONT_SANS_SERIF, SIZE_H2, WEIGHT_BOLD);
    }
    
    public static Font h2Light() {
        return getFont(FONT_SANS_SERIF, SIZE_H2, WEIGHT_REGULAR);
    }
    
    public static Font h3() {
        return getFont(FONT_SANS_SERIF, SIZE_H3, WEIGHT_BOLD);
    }
    
    public static Font h3Light() {
        return getFont(FONT_SANS_SERIF, SIZE_H3, WEIGHT_REGULAR);
    }
    
    public static Font h4() {
        return getFont(FONT_SANS_SERIF, SIZE_H4, WEIGHT_BOLD);
    }
    
    public static Font h4Light() {
        return getFont(FONT_SANS_SERIF, SIZE_H4, WEIGHT_REGULAR);
    }
    
    // ============================================
    // BODY FONTS
    // ============================================
    public static Font bodyLarge() {
        return getFont(FONT_SANS_SERIF, SIZE_BODY_LARGE, WEIGHT_REGULAR);
    }
    
    public static Font body() {
        return getFont(FONT_SANS_SERIF, SIZE_BODY, WEIGHT_REGULAR);
    }
    
    public static Font bodyMedium() {
        return getFont(FONT_SANS_SERIF, SIZE_BODY, WEIGHT_MEDIUM);
    }
    
    public static Font bodyBold() {
        return getFont(FONT_SANS_SERIF, SIZE_BODY, WEIGHT_BOLD);
    }
    
    public static Font bodySmall() {
        return getFont(FONT_SANS_SERIF, SIZE_BODY_SMALL, WEIGHT_REGULAR);
    }
    
    public static Font bodySmallMedium() {
        return getFont(FONT_SANS_SERIF, SIZE_BODY_SMALL, WEIGHT_MEDIUM);
    }
    
    public static Font bodySmallBold() {
        return getFont(FONT_SANS_SERIF, SIZE_BODY_SMALL, WEIGHT_BOLD);
    }
    
    // ============================================
    // LABEL & CAPTION FONTS
    // ============================================
    public static Font caption() {
        return getFont(FONT_SANS_SERIF, SIZE_CAPTION, WEIGHT_REGULAR);
    }
    
    public static Font captionMedium() {
        return getFont(FONT_SANS_SERIF, SIZE_CAPTION, WEIGHT_MEDIUM);
    }
    
    public static Font captionBold() {
        return getFont(FONT_SANS_SERIF, SIZE_CAPTION, WEIGHT_BOLD);
    }
    
    public static Font tiny() {
        return getFont(FONT_SANS_SERIF, SIZE_TINY, WEIGHT_REGULAR);
    }
    
    // ============================================
    // SPECIALTY FONTS
    // ============================================
    public static Font mono() {
        return getFont(FONT_MONOSPACE, SIZE_BODY, WEIGHT_REGULAR);
    }
    
    public static Font monoSmall() {
        return getFont(FONT_MONOSPACE, SIZE_BODY_SMALL, WEIGHT_REGULAR);
    }
    
    // ============================================
    // BUTTON FONTS
    // ============================================
    public static Font button() {
        return getFont(FONT_SANS_SERIF, SIZE_BODY, WEIGHT_MEDIUM);
    }
    
    public static Font buttonSmall() {
        return getFont(FONT_SANS_SERIF, SIZE_BODY_SMALL, WEIGHT_MEDIUM);
    }
    
    // ============================================
    // TABLE FONTS
    // ============================================
    public static Font tableHeader() {
        return getFont(FONT_SANS_SERIF, SIZE_BODY_SMALL, WEIGHT_BOLD);
    }
    
    public static Font tableBody() {
        return getFont(FONT_SANS_SERIF, SIZE_BODY, WEIGHT_REGULAR);
    }
    
    // ============================================
    // FORM FONTS
    // ============================================
    public static Font label() {
        return getFont(FONT_SANS_SERIF, SIZE_BODY_SMALL, WEIGHT_MEDIUM);
    }
    
    public static Font input() {
        return getFont(FONT_SANS_SERIF, SIZE_BODY, WEIGHT_REGULAR);
    }
    
    public static Font placeholder() {
        return getFont(FONT_SANS_SERIF, SIZE_BODY, WEIGHT_REGULAR);
    }
    
    // ============================================
    // HELPER METHOD
    // ============================================
    private static Font getFont(String family, int size, int style) {
        String key = family + "_" + size + "_" + style;
        if (!fontCache.containsKey(key)) {
            Font font = new Font(family, style, size);
            fontCache.put(key, font);
            return font;
        }
        return fontCache.get(key);
    }
    
    // ============================================
    // UTILITY METHODS
    // ============================================
    
    /**
     * Scale font size by percentage
     */
    public static Font scale(Font font, float scaleFactor) {
        int newSize = Math.max(1, Math.round(font.getSize() * scaleFactor));
        return font.deriveFont(font.getStyle(), newSize);
    }
    
    /**
     * Get font with different size
     */
    public static Font withSize(Font font, int newSize) {
        return font.deriveFont(font.getStyle(), newSize);
    }
    
    /**
     * Get font with different style
     */
    public static Font withStyle(Font font, int newStyle) {
        return font.deriveFont(newStyle, font.getSize());
    }
    
    /**
     * Get font with different weight
     */
    public static Font withWeight(Font font, int weight) {
        return font.deriveFont(weight, font.getSize());
    }
    
    /**
     * Calculate text width for a font
     */
    public static int getTextWidth(String text, Font font) {
        if (text == null || text.isEmpty()) return 0;
        return (int) font.getStringBounds(text, 
            new java.awt.font.FontRenderContext(null, true, true))
            .getWidth();
    }
    
    /**
     * Get recommended component height based on context
     */
    public static int getComponentHeight(Context context) {
        switch (context) {
            case BUTTON:
                return 40;
            case BUTTON_SMALL:
                return 32;
            case INPUT:
                return 44;
            case INPUT_SMALL:
                return 36;
            case TABLE_ROW:
                return 48;
            case TABLE_ROW_COMPACT:
                return 36;
            case CARD:
                return 56;
            case TOOLBAR:
                return 48;
            case NAVIGATION:
                return 44;
            case HEADER:
                return 64;
            case MODAL:
                return 56;
            default:
                return 40;
        }
    }
    
    public enum Context {
        BUTTON,
        BUTTON_SMALL,
        INPUT,
        INPUT_SMALL,
        TABLE_ROW,
        TABLE_ROW_COMPACT,
        CARD,
        TOOLBAR,
        NAVIGATION,
        HEADER,
        MODAL
    }
}
