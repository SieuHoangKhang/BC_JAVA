package noithat.utils;

import javax.swing.*;
import java.awt.*;

public class ToastNotification extends JFrame {
    public ToastNotification(String message, int duration) {
        setUndecorated(true);
        setOpacity(0.85f);
        
        JLabel label = new JLabel(message);
        label.setFont(new Font("Arial", Font.PLAIN, 14));
        label.setForeground(Color.WHITE);
        label.setHorizontalAlignment(SwingConstants.CENTER);
        
        JPanel panel = new JPanel();
        panel.setBackground(new Color(50, 50, 50));
        panel.add(label);
        
        add(panel);
        setSize(300, 60);
        
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setLocation((int)(screenSize.getWidth() - 300) / 2,
                    (int)(screenSize.getHeight() - 100));
        
        setVisible(true);
        
        Timer timer = new Timer(duration, e -> dispose());
        timer.setRepeats(false);
        timer.start();
    }
    
    public static void show(String message) {
        new ToastNotification(message, 3000);
    }
    
    public static void show(String message, int duration) {
        new ToastNotification(message, duration);
    }
}

