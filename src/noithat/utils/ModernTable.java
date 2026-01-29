package noithat.utils;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Modern table with enhanced styling and hover effects
 */
public class ModernTable extends JTable {
    private int hoveredRow = -1;
    
    public ModernTable(TableModel model) {
        super(model);
        setupModernStyle();
    }
    
    private void setupModernStyle() {
        // Basic settings
        setRowHeight(32);
        setShowGrid(true);
        setGridColor(ColorTheme.BORDER);
        setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        setFont(new Font("Segoe UI", Font.PLAIN, 13));
        
        // Selection colors
        setSelectionBackground(ColorTheme.withAlpha(ColorTheme.PRIMARY, 40));
        setSelectionForeground(ColorTheme.TEXT_PRIMARY);
        
        // Header styling
        JTableHeader header = getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 13));
        header.setBackground(ColorTheme.SECONDARY);
        header.setForeground(Color.WHITE);
        header.setPreferredSize(new Dimension(header.getPreferredSize().width, 40));
        header.setReorderingAllowed(false);
        
        // Custom header renderer
        header.setDefaultRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {
                JLabel label = new JLabel(value.toString());
                label.setFont(new Font("Segoe UI", Font.BOLD, 13));
                label.setForeground(Color.WHITE);
                label.setBackground(ColorTheme.SECONDARY);
                label.setOpaque(true);
                label.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
                label.setHorizontalAlignment(SwingConstants.LEFT);
                return label;
            }
        });
        
        // Custom cell renderer with zebra striping and hover
        setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                
                if (isSelected) {
                    c.setBackground(ColorTheme.withAlpha(ColorTheme.PRIMARY, 60));
                    c.setForeground(ColorTheme.TEXT_PRIMARY);
                } else if (row == hoveredRow) {
                    c.setBackground(ColorTheme.withAlpha(ColorTheme.PRIMARY, 20));
                    c.setForeground(ColorTheme.TEXT_PRIMARY);
                } else {
                    // Zebra striping
                    if (row % 2 == 0) {
                        c.setBackground(Color.WHITE);
                    } else {
                        c.setBackground(new Color(248, 249, 250));
                    }
                    c.setForeground(ColorTheme.TEXT_PRIMARY);
                }
                
                ((JLabel) c).setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
                return c;
            }
        });
        
        // Hover effect
        addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                int row = rowAtPoint(e.getPoint());
                if (row != hoveredRow) {
                    hoveredRow = row;
                    repaint();
                }
            }
        });
        
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseExited(MouseEvent e) {
                hoveredRow = -1;
                repaint();
            }
        });
    }
    
    /**
     * Prevent cell editing by default
     */
    @Override
    public boolean isCellEditable(int row, int column) {
        return false;
    }
}
