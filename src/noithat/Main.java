package noithat;

import noithat.views.FormDangNhap;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class Main {
    public static void main(String[] args) {
        // ===== CRITICAL: DISABLE ALL CHECKBOX/MNEMONIC INDICATORS =====
        try {
            // Disable mnemonics completely
            UIManager.put("Button.showMnemonics", Boolean.FALSE);
            UIManager.put("Button.defaultButtonFollowsFocus", Boolean.FALSE);
            
            // Remove all focus/selection indicators
            UIManager.put("Button.focus", new Color(0, 0, 0, 0));
            UIManager.put("Button.select", new Color(0, 0, 0, 0));
            
            // Create empty 1x1 transparent icon to replace checkbox
            BufferedImage emptyImage = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
            Icon emptyIcon = new ImageIcon(emptyImage);
            
            UIManager.put("CheckBox.icon", emptyIcon);
            UIManager.put("RadioButton.icon", emptyIcon);
            UIManager.put("Button.icon", emptyIcon);
            
            // Remove borders and margins  
            UIManager.put("Button.border", BorderFactory.createEmptyBorder());
            UIManager.put("Button.margin", new Insets(0, 0, 0, 0));
            
            // Set Look and Feel after UIManager settings
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                FormDangNhap form = new FormDangNhap();
                form.setVisible(true);
            }
        });
    }
}
