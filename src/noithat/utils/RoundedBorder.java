package noithat.utils;

import javax.swing.border.AbstractBorder;
import java.awt.*;

public class RoundedBorder extends AbstractBorder {
    private int radius;
    private Color color;
    private int thickness;
    
    public RoundedBorder(int radius) {
        this(radius, new Color(189, 195, 199), 1);
    }
    
    public RoundedBorder(int radius, Color color) {
        this(radius, color, 1);
    }
    
    public RoundedBorder(int radius, Color color, int thickness) {
        this.radius = radius;
        this.color = color;
        this.thickness = thickness;
    }
    
    @Override
    public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setColor(color);
        g2d.setStroke(new BasicStroke(thickness));
        g2d.drawRoundRect(x, y, width - 1, height - 1, radius, radius);
    }
    
    @Override
    public Insets getBorderInsets(Component c) {
        return new Insets(thickness + 2, thickness + 5, thickness + 2, thickness + 5);
    }
}
