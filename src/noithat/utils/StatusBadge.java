package noithat.utils;

import javax.swing.*;
import java.awt.*;

/**
 * Status Badge Component - Modern Status Indicator
 * Displays status with consistent styling and animations
 */
public class StatusBadge extends JPanel {
    
    // ============================================
    // CONFIGURATION
    // ============================================
    private String text;
    private StatusType statusType;
    private BadgeStyle style = BadgeStyle.FILLED;
    private int cornerRadius = 20;
    private int horizontalPadding = 12;
    private int verticalPadding = 6;
    
    // ============================================
    // STATUS TYPES
    // ============================================
    public enum StatusType {
        SUCCESS(ElegantTheme.SUCCESS, ElegantTheme.SUCCESS_LIGHT, "Hoàn thành"),
        WARNING(ElegantTheme.WARNING, ElegantTheme.WARNING_LIGHT, "Chờ xử lý"),
        DANGER(ElegantTheme.DANGER, ElegantTheme.DANGER_LIGHT, "Lỗi"),
        INFO(ElegantTheme.INFO, ElegantTheme.INFO_LIGHT, "Thông tin"),
        NEUTRAL(ElegantTheme.NEUTRAL_600, ElegantTheme.NEUTRAL_100, "Không xác định"),
        ACTIVE(ElegantTheme.SUCCESS, ElegantTheme.SUCCESS_LIGHT, "Hoạt động"),
        INACTIVE(ElegantTheme.NEUTRAL_400, ElegantTheme.NEUTRAL_100, "Ngừng hoạt động"),
        PENDING(ElegantTheme.WARNING, ElegantTheme.WARNING_LIGHT, "Đang chờ"),
        PROCESSING(ElegantTheme.INFO, ElegantTheme.INFO_LIGHT, "Đang xử lý"),
        COMPLETED(ElegantTheme.SUCCESS, ElegantTheme.SUCCESS_LIGHT, "Đã hoàn thành"),
        CANCELLED(ElegantTheme.DANGER, ElegantTheme.DANGER_LIGHT, "Đã hủy");
        
        public final Color color;
        public final Color backgroundColor;
        public final String defaultText;
        
        StatusType(Color color, Color backgroundColor, String defaultText) {
            this.color = color;
            this.backgroundColor = backgroundColor;
            this.defaultText = defaultText;
        }
        
        public static StatusType fromString(String status) {
            if (status == null) return NEUTRAL;
            switch (status.toLowerCase()) {
                case "success":
                case "completed":
                case "active":
                case "done":
                    return SUCCESS;
                case "warning":
                case "pending":
                case "waiting":
                    return WARNING;
                case "danger":
                case "error":
                case "cancelled":
                case "inactive":
                case "deleted":
                    return DANGER;
                case "info":
                case "processing":
                    return INFO;
                default:
                    return NEUTRAL;
            }
        }
    }
    
    // ============================================
    // BADGE STYLES
    // ============================================
    public enum BadgeStyle {
        FILLED,      // Filled background with white text
        OUTLINED,    // Outlined with colored text
        SOFT         // Soft background with colored text
    }
    
    // ============================================
    // CONSTRUCTORS
    // ============================================
    
    public StatusBadge(String text, StatusType statusType) {
        this.text = text;
        this.statusType = statusType;
        initBadge();
    }
    
    public StatusBadge(String text, StatusType statusType, BadgeStyle style) {
        this.text = text;
        this.statusType = statusType;
        this.style = style;
        initBadge();
    }
    
    public StatusBadge(StatusType statusType) {
        this(statusType.defaultText, statusType);
    }
    
    // ============================================
    // PROPERTY SETTERS
    // ============================================
    
    public void setText(String text) {
        this.text = text;
        revalidate();
        repaint();
    }
    
    public String getText() {
        return text;
    }
    
    public void setStatusType(StatusType statusType) {
        this.statusType = statusType;
        repaint();
    }
    
    public StatusType getStatusType() {
        return statusType;
    }
    
    public void setStyle(BadgeStyle style) {
        this.style = style;
        repaint();
    }
    
    public BadgeStyle getStyle() {
        return style;
    }
    
    public void setCornerRadius(int radius) {
        this.cornerRadius = radius;
        repaint();
    }
    
    public void setPadding(int horizontal, int vertical) {
        this.horizontalPadding = horizontal;
        this.verticalPadding = vertical;
        revalidate();
        repaint();
    }
    
    // ============================================
    // INITIALIZATION
    // ============================================
    
    private void initBadge() {
        setOpaque(false);
        setForeground(statusType.color);
        setFont(FontHelper.captionMedium());
        setCursor(Cursor.getDefaultCursor());
    }
    
    // ============================================
    // PAINTING
    // ============================================
    
    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        
        int width = getWidth();
        int height = getHeight();
        
        switch (style) {
            case FILLED:
                paintFilled(g2d, width, height);
                break;
            case OUTLINED:
                paintOutlined(g2d, width, height);
                break;
            case SOFT:
                paintSoft(g2d, width, height);
                break;
        }
        
        // Draw text
        g2d.setColor(getTextColor());
        g2d.setFont(getFont());
        
        FontMetrics fm = g2d.getFontMetrics();
        int textWidth = fm.stringWidth(text);
        int textHeight = fm.getHeight();
        
        int x = (width - textWidth) / 2;
        int y = (height - textHeight) / 2 + fm.getAscent();
        
        g2d.drawString(text, x, y);
        
        g2d.dispose();
    }
    
    private void paintFilled(Graphics2D g2d, int width, int height) {
        g2d.setColor(statusType.color);
        g2d.fillRoundRect(0, 0, width, height, cornerRadius, cornerRadius);
    }
    
    private void paintOutlined(Graphics2D g2d, int width, int height) {
        g2d.setColor(statusType.color);
        g2d.setStroke(new BasicStroke(1.5f));
        g2d.drawRoundRect(1, 1, width - 2, height - 2, cornerRadius, cornerRadius);
    }
    
    private void paintSoft(Graphics2D g2d, int width, int height) {
        // Soft background with low opacity
        Color bgColor = ElegantTheme.withAlpha(statusType.backgroundColor, 150);
        g2d.setColor(bgColor);
        g2d.fillRoundRect(0, 0, width, height, cornerRadius, cornerRadius);
        
        // Subtle border
        g2d.setColor(ElegantTheme.withAlpha(statusType.color, 80));
        g2d.setStroke(new BasicStroke(1f));
        g2d.drawRoundRect(0, 0, width - 1, height - 1, cornerRadius, cornerRadius);
    }
    
    private Color getTextColor() {
        switch (style) {
            case FILLED:
                return Color.WHITE;
            case OUTLINED:
            case SOFT:
            default:
                return statusType.color;
        }
    }
    
    // ============================================
    // PREFERRED SIZE
    // ============================================
    
    @Override
    public Dimension getPreferredSize() {
        FontMetrics fm = getFontMetrics(getFont());
        int textWidth = fm.stringWidth(text != null ? text : "");
        int textHeight = fm.getHeight();
        
        return new Dimension(
            textWidth + horizontalPadding * 2,
            textHeight + verticalPadding * 2
        );
    }
    
    @Override
    public Dimension getMinimumSize() {
        return getPreferredSize();
    }
}
