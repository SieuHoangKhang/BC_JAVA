package noithat.views;

import noithat.database.DatabaseHelper;
import noithat.utils.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class FormDangNhap extends JFrame {
    private JTextField txtUsername;
    private JPasswordField txtPassword;
    private ModernButton btnLogin;
    private ModernButton btnExit;
    private JLabel lblStatus;
    
    public FormDangNhap() {
        initComponents();
    }
    
    private void initComponents() {
        setTitle("Hệ Thống Quản Lý Của Hàng Nội Thất - Đăng Nhập");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 500);
        setLocationRelativeTo(null);
        setResizable(false);
        
        GradientPanel gradientPanel = new GradientPanel(ColorTheme.PRIMARY, ColorTheme.PRIMARY_LIGHT);
        gradientPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        
        JPanel loginPanel = new JPanel();
        loginPanel.setBackground(ColorTheme.SURFACE);
        loginPanel.setLayout(new GridBagLayout());
        loginPanel.setBorder(BorderFactory.createEmptyBorder(50, 50, 50, 50));
        loginPanel.setPreferredSize(new Dimension(450, 420));
        
        GridBagConstraints gbc2 = new GridBagConstraints();
        gbc2.insets = new Insets(0, 0, 20, 0);
        gbc2.gridwidth = 2;
        
        JLabel lblIcon = new JLabel("🏠");
        lblIcon.setFont(new Font("Arial", Font.PLAIN, 60));
        gbc2.gridx = 0;
        gbc2.gridy = 0;
        loginPanel.add(lblIcon, gbc2);
        
        JLabel lblTitle = new JLabel("ĐĂNG NHẬP");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblTitle.setForeground(ColorTheme.SECONDARY);
        gbc2.gridy = 1;
        gbc2.insets = new Insets(0, 0, 30, 0);
        loginPanel.add(lblTitle, gbc2);
        
        JLabel lblUsername = new JLabel("Tên đăng nhập:");
        lblUsername.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblUsername.setForeground(ColorTheme.TEXT_PRIMARY);
        gbc2.gridx = 0;
        gbc2.gridy = 2;
        gbc2.gridwidth = 2;
        gbc2.anchor = GridBagConstraints.WEST;
        gbc2.insets = new Insets(0, 0, 8, 0);
        loginPanel.add(lblUsername, gbc2);
        
        txtUsername = createStyledTextField();
        gbc2.gridx = 0;
        gbc2.gridy = 3;
        gbc2.fill = GridBagConstraints.HORIZONTAL;
        gbc2.insets = new Insets(0, 0, 15, 0);
        loginPanel.add(txtUsername, gbc2);
        
        JLabel lblPassword = new JLabel("Mật khẩu:");
        lblPassword.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblPassword.setForeground(ColorTheme.TEXT_PRIMARY);
        gbc2.gridx = 0;
        gbc2.gridy = 4;
        gbc2.fill = GridBagConstraints.NONE;
        gbc2.anchor = GridBagConstraints.WEST;
        gbc2.insets = new Insets(0, 0, 8, 0);
        loginPanel.add(lblPassword, gbc2);
        
        txtPassword = new JPasswordField(25);
        txtPassword.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        txtPassword.setPreferredSize(new Dimension(300, 40));
        txtPassword.setBorder(new RoundedBorder(8, ColorTheme.BORDER));
        gbc2.gridx = 0;
        gbc2.gridy = 5;
        gbc2.fill = GridBagConstraints.HORIZONTAL;
        gbc2.insets = new Insets(0, 0, 25, 0);
        loginPanel.add(txtPassword, gbc2);
        
        lblStatus = new JLabel("");
        lblStatus.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        lblStatus.setForeground(ColorTheme.DANGER);
        gbc2.gridx = 0;
        gbc2.gridy = 6;
        gbc2.insets = new Insets(0, 0, 20, 0);
        loginPanel.add(lblStatus, gbc2);
        
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(ColorTheme.SURFACE);
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 0));
        
        btnLogin = new ModernButton("Đăng Nhập", ColorTheme.SUCCESS);
        btnLogin.setPreferredSize(new Dimension(150, 45));
        btnLogin.setFont(new Font("Segoe UI", Font.BOLD, 13));
        
        btnExit = new ModernButton("Thoát", ColorTheme.DANGER);
        btnExit.setPreferredSize(new Dimension(150, 45));
        btnExit.setFont(new Font("Segoe UI", Font.BOLD, 13));
        
        buttonPanel.add(btnLogin);
        buttonPanel.add(btnExit);
        
        gbc2.gridx = 0;
        gbc2.gridy = 7;
        gbc2.gridwidth = 2;
        gbc2.fill = GridBagConstraints.HORIZONTAL;
        gbc2.insets = new Insets(10, 0, 0, 0);
        loginPanel.add(buttonPanel, gbc2);
        
        gbc.gridx = 0;
        gbc.gridy = 0;
        gradientPanel.add(loginPanel, gbc);
        
        add(gradientPanel);
        
        btnLogin.addActionListener(e -> login());
        btnExit.addActionListener(e -> System.exit(0));
        
        txtUsername.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) login();
            }
        });
        
        txtPassword.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) login();
            }
        });
    }
    
    private JTextField createStyledTextField() {
        JTextField tf = new JTextField(25);
        tf.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        tf.setPreferredSize(new Dimension(300, 40));
        tf.setBorder(new RoundedBorder(8, ColorTheme.BORDER));
        return tf;
    }
    
    private void login() {
        String username = txtUsername.getText().trim();
        String password = new String(txtPassword.getPassword());
        
        if (username.isEmpty() || password.isEmpty()) {
            lblStatus.setText("Vui lòng nhập đầy đủ tên đăng nhập và mật khẩu!");
            return;
        }
        
        String query = "SELECT * FROM Users WHERE Username = ? AND Password = ?";
        try (Connection conn = DatabaseHelper.getDBConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, username);
            pstmt.setString(2, password);
            
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                int userId = rs.getInt("UserID");
                String fullName = rs.getString("FullName");
                String role = rs.getString("Role");
                
                SessionManager.getInstance().setUser(userId, username, fullName, role);
                ActivityLogger.logLogin(userId, username);
                
                ManHinhChinh mainWindow = new ManHinhChinh();
                mainWindow.setVisible(true);
                this.dispose();
            } else {
                lblStatus.setText("Tên đăng nhập hoặc mật khẩu không chính xác!");
                txtPassword.setText("");
                txtPassword.requestFocus();
            }
        } catch (SQLException e) {
            lblStatus.setText("Lỗi kết nối database!");
            e.printStackTrace();
        }
    }
}

