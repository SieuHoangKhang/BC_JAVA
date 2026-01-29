package noithat.utils;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;

public class ToastNotification extends JWindow {
    public static final int INFO = 1;
    public static final int SUCCESS = 2;
    public static final int WARNING = 3;
    public static final int ERROR = 4;
    
    private final int duration;
    
    public ToastNotification(Window owner, String message, int type, int duration) {
        super(owner);
        this.duration = duration;
        setLayout(new BorderLayout());
        
        Color bgColor;
        Color fgColor = Color.WHITE;
        String iconSymbol;
        
        switch (type) {
            case SUCCESS:
                bgColor = new Color(46, 204, 113); // Green
                iconSymbol = "✅";
                break;
            case WARNING:
                bgColor = new Color(243, 156, 18); // Orange
                iconSymbol = "⚠️";
                break;
            case ERROR:
                bgColor = new Color(231, 76, 60);  // Red
                iconSymbol = "❌";
                break;
            case INFO:
            default:
                bgColor = new Color(52, 152, 219); // Blue
                iconSymbol = "ℹ️";
                break;
        }
        
        JPanel panel = new JPanel(new BorderLayout(15, 0));
        panel.setBackground(bgColor);
        panel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        
        JLabel iconLabel = new JLabel(iconSymbol);
        iconLabel.setForeground(fgColor);
        iconLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 20));
        
        JLabel textLabel = new JLabel(message);
        textLabel.setForeground(fgColor);
        textLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        
        panel.add(iconLabel, BorderLayout.WEST);
        panel.add(textLabel, BorderLayout.CENTER);
        
        add(panel);
        pack();
        
        // Shape the window
        setShape(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), 20, 20));
        
        // Timer to close
        Timer timer = new Timer(duration, e -> {
            setVisible(false);
            dispose();
        });
        timer.setRepeats(false);
        timer.start();
    }
    
    public static void show(Component owner, String message, int type) {
        Window window = SwingUtilities.getWindowAncestor(owner);
        ToastNotification toast = new ToastNotification(window, message, type, 3000);
        
        // Position toast
        if (window != null) {
            int x = window.getX() + (window.getWidth() - toast.getWidth()) / 2;
            int y = window.getY() + window.getHeight() - toast.getHeight() - 50;
            toast.setLocation(x, y);
        } else {
            Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
            int x = (screenSize.width - toast.getWidth()) / 2;
            int y = screenSize.height - toast.getHeight() - 100;
            toast.setLocation(x, y);
        }
        
        toast.setVisible(true);
    }
    
    // Fallback/Legacy methods if needed, though mostly replaced now
    public static void show(String message) {
        show(null, message, INFO);
    }
}
