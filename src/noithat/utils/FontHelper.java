package noithat.utils;

import java.awt.*;
import java.io.IOException;
import java.io.InputStream;

/**
 * Font management system with premium typography
 */
public class FontHelper {
    // Heading fonts
    public static final Font HEADING_EXTRA_LARGE = new Font("Segoe UI", Font.BOLD, 32);
    public static final Font HEADING_LARGE = new Font("Segoe UI", Font.BOLD, 28);
    public static final Font HEADING_MEDIUM = new Font("Segoe UI", Font.BOLD, 24);
    public static final Font HEADING_SMALL = new Font("Segoe UI", Font.BOLD, 20);
    
    // Body fonts
    public static final Font BODY_LARGE = new Font("Segoe UI", Font.PLAIN, 16);
    public static final Font BODY_MEDIUM = new Font("Segoe UI", Font.PLAIN, 14);
    public static final Font BODY_SMALL = new Font("Segoe UI", Font.PLAIN, 13);
    public static final Font BODY_TINY = new Font("Segoe UI", Font.PLAIN, 11);
    
    // Special fonts
    public static final Font BUTTON = new Font("Segoe UI", Font.BOLD, 13);
    public static final Font LABEL = new Font("Segoe UI", Font.PLAIN, 12);
    public static final Font CAPTION = new Font("Segoe UI", Font.PLAIN, 11);
    public static final Font MONO = new Font("Consolas", Font.PLAIN, 12);
    
    /**
     * Load custom font from resources (if available)
     */
    public static Font loadFont(String fontPath, float size) {
        try {
            InputStream is = FontHelper.class.getResourceAsStream(fontPath);
            if (is != null) {
                Font font = Font.createFont(Font.TRUETYPE_FONT, is);
                return font.deriveFont(size);
            }
        } catch (FontFormatException | IOException e) {
            System.err.println("Failed to load font: " + fontPath);
            e.printStackTrace();
        }
        // Fallback to default
        return new Font("Segoe UI", Font.PLAIN, (int) size);
    }
    
    /**
     * Create derived font with different size
     */
    public static Font withSize(Font font, float size) {
        return font.deriveFont(size);
    }
    
    /**
     * Create derived font with different style
     */
    public static Font withStyle(Font font, int style) {
        return font.deriveFont(style);
    }
    
    /**
     * Create bold version of font
     */
    public static Font bold(Font font) {
        return font.deriveFont(Font.BOLD);
    }
    
    /**
     * Create italic version of font
     */
    public static Font italic(Font font) {
        return font.deriveFont(Font.ITALIC);
    }
}
