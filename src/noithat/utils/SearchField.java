package noithat.utils;

import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import java.awt.*;

/**
 * Clean modern search field with placeholder
 */
public class SearchField extends JTextField {
    private String placeholder;
    
    public SearchField(String placeholder) {
        this(placeholder, 250);
    }
    
    public SearchField(String placeholder, int width) {
        this.placeholder = placeholder;
        setPreferredSize(new Dimension(width, 32));
        setFont(new Font("Segoe UI", Font.PLAIN, 13));
        setBorder(new CompoundBorder(
            new RoundedBorder(6, ProfessionalColors.BORDER),
            new EmptyBorder(4, 10, 4, 10)
        ));
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        if (getText().isEmpty() && !isFocusOwner()) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(ProfessionalColors.TEXT_TERTIARY);
            g2.setFont(getFont());
            
            FontMetrics fm = g2.getFontMetrics();
            int x = getInsets().left;
            int y = (getHeight() + fm.getAscent() - fm.getDescent()) / 2;
            g2.drawString(placeholder, x, y);
            g2.dispose();
        }
    }
}
