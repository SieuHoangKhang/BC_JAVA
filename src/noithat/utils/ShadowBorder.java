package noithat.utils;

import javax.swing.border.AbstractBorder;
import java.awt.*;

public class ShadowBorder extends AbstractBorder {
    private int shadowSize;
    
    public ShadowBorder(int shadowSize) {
        this.shadowSize = shadowSize;
    }
    
    @Override
    public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        g2d.setColor(new Color(0, 0, 0, 40));
        for (int i = 1; i <= shadowSize; i++) {
            g2d.drawRect(x + i, y + i, width - i - 1, height - i - 1);
        }
    }
    
    @Override
    public Insets getBorderInsets(Component c) {
        return new Insets(shadowSize, shadowSize, shadowSize, shadowSize);
    }
}
