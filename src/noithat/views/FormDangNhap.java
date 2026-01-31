package noithat.views;

import noithat.database.DatabaseHelper;
import noithat.utils.*;
import noithat.utils.ElegantTheme;
import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class FormDangNhap extends JFrame {
    private JTextField txtUsername;
    private JPasswordField txtPassword;
    private ToolbarButton btnLogin;
    private JButton btnExit;
    private JLabel lblStatus;
    
    public FormDangNhap() {
        initComponents();
    }
    
    private void initComponents() {
        setTitle("He Thong Quan Ly Cua Hang Noi That");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 550);
        setLocationRelativeTo(null);
        setResizable(false);
        
        // MAIN LAYOUT - Split screen design
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(ElegantTheme.CLASSIC_WHITE);
        
        // LEFT SIDE - Branding
        JPanel leftPanel = createBrandingPanel();
        
        // RIGHT SIDE - Login form
        JPanel rightPanel = createLoginPanel();
        
        mainPanel.add(leftPanel, BorderLayout.CENTER);
        mainPanel.add(rightPanel, BorderLayout.EAST);
        
        add(mainPanel);
    }
    
    private JPanel createBrandingPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(ElegantTheme.PRIMARY);
        
        // Gradient overlay for sophistication
        JPanel gradientPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                
                Color color1 = ElegantTheme.PRIMARY;
                Color color2 = ElegantTheme.PRIMARY_HOVER;
                
                GradientPaint gradient = new GradientPaint(0, 0, color1, getWidth(), getHeight(), color2);
                g2.setPaint(gradient);
                g2.fillRect(0, 0, getWidth(), getHeight());
                g2.dispose();
            }
        };
        gradientPanel.setLayout(new BoxLayout(gradientPanel, BoxLayout.Y_AXIS));
        gradientPanel.setBorder(new EmptyBorder(60, 50, 60, 50));
        
        // Title
        JLabel lblTitle = new JLabel("NỘI THẤT");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 42));
        lblTitle.setForeground(ElegantTheme.CLASSIC_WHITE);
        lblTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        // Subtitle
        JLabel lblSubtitle = new JLabel("Cửa Hàng Nội Thất Cao Cấp");
        lblSubtitle.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        lblSubtitle.setForeground(new Color(255, 255, 255, 200));
        lblSubtitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        // Decorative line
        JSeparator separator = new JSeparator();
        separator.setMaximumSize(new Dimension(100, 2));
        separator.setForeground(ElegantTheme.CLASSIC_WHITE);
        separator.setBackground(ElegantTheme.CLASSIC_WHITE);
        separator.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        // Tagline
        JLabel lblTagline = new JLabel("Hệ thống quản lý chuyên nghiệp");
        lblTagline.setFont(new Font("Segoe UI", Font.ITALIC, 14));
        lblTagline.setForeground(new Color(255, 255, 255, 180));
        lblTagline.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        gradientPanel.add(Box.createVerticalGlue());
        gradientPanel.add(lblTitle);
        gradientPanel.add(Box.createVerticalStrut(12));
        gradientPanel.add(separator);
        gradientPanel.add(Box.createVerticalStrut(12));
        gradientPanel.add(lblSubtitle);
        gradientPanel.add(Box.createVerticalStrut(8));
        gradientPanel.add(lblTagline);
        gradientPanel.add(Box.createVerticalGlue());
        
        panel.add(gradientPanel, BorderLayout.CENTER);
        
        return panel;
    }
    
    private JPanel createLoginPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(ElegantTheme.SURFACE);
        panel.setBorder(new EmptyBorder(60, 50, 60, 50));
        panel.setPreferredSize(new Dimension(400, 550));
        
        // Welcome text
        JLabel lblWelcome = new JLabel("Đăng Nhập");
        lblWelcome.setFont(new Font("Segoe UI", Font.BOLD, 28));
        lblWelcome.setForeground(ElegantTheme.TEXT_PRIMARY);
        lblWelcome.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JLabel lblWelcomeSub = new JLabel("Vui lòng nhập thông tin đăng nhập");
        lblWelcomeSub.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblWelcomeSub.setForeground(ElegantTheme.TEXT_SECONDARY);
        lblWelcomeSub.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        // Username field
        JLabel lblUsername = new JLabel("Tên đăng nhập");
        lblUsername.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lblUsername.setForeground(ElegantTheme.TEXT_PRIMARY);
        lblUsername.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        txtUsername = new JTextField();
        txtUsername.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtUsername.setPreferredSize(new Dimension(320, 42));
        txtUsername.setMaximumSize(new Dimension(320, 42));
        txtUsername.setBorder(new CompoundBorder(
            new LineBorder(ElegantTheme.NEUTRAL_200, 1, true),
            new EmptyBorder(10, 14, 10, 14)
        ));
        txtUsername.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        // Password field
        JLabel lblPassword = new JLabel("Mật khẩu");
        lblPassword.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lblPassword.setForeground(ElegantTheme.TEXT_PRIMARY);
        lblPassword.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        txtPassword = new JPasswordField();
        txtPassword.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtPassword.setPreferredSize(new Dimension(320, 42));
        txtPassword.setMaximumSize(new Dimension(320, 42));
        txtPassword.setBorder(new CompoundBorder(
            new LineBorder(ElegantTheme.NEUTRAL_200, 1, true),
            new EmptyBorder(10, 14, 10, 14)
        ));
        txtPassword.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        // Status label
        lblStatus = new JLabel(" ");
        lblStatus.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblStatus.setForeground(ElegantTheme.DANGER);
        lblStatus.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        // Login button
        btnLogin = new ToolbarButton("Đăng Nhập", ElegantTheme.PRIMARY);
        btnLogin.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnLogin.setPreferredSize(new Dimension(320, 44));
        btnLogin.setMaximumSize(new Dimension(320, 44));
        btnLogin.setAlignmentX(Component.LEFT_ALIGNMENT);
        btnLogin.addActionListener(e -> login());
        
        // Exit link
        btnExit = new JButton("Thoát ứng dụng");
        btnExit.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        btnExit.setForeground(ElegantTheme.TEXT_TERTIARY);
        btnExit.setBorderPainted(false);
        btnExit.setContentAreaFilled(false);
        btnExit.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnExit.setAlignmentX(Component.LEFT_ALIGNMENT);
        btnExit.addActionListener(e -> System.exit(0));
        
        // Assembly
        panel.add(lblWelcome);
        panel.add(Box.createVerticalStrut(8));
        panel.add(lblWelcomeSub);
        panel.add(Box.createVerticalStrut(35));
        
        panel.add(lblUsername);
        panel.add(Box.createVerticalStrut(8));
        panel.add(txtUsername);
        panel.add(Box.createVerticalStrut(20));
        
        panel.add(lblPassword);
        panel.add(Box.createVerticalStrut(8));
        panel.add(txtPassword);
        panel.add(Box.createVerticalStrut(12));
        
        panel.add(lblStatus);
        panel.add(Box.createVerticalStrut(24));
        
        panel.add(btnLogin);
        panel.add(Box.createVerticalStrut(16));
        panel.add(btnExit);
        
        panel.add(Box.createVerticalGlue());
        
        // Enter key listeners
        txtUsername.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) login();
            }
        });
        
        txtPassword.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) login();
            }
        });
        
        return panel;
    }
    
    private void login() {
        String username = txtUsername.getText().trim();
        String password = new String(txtPassword.getPassword());
        
        if (username.isEmpty() || password.isEmpty()) {
            lblStatus.setText("Lỗi: Vui lòng nhập đầy đủ thông tin!");
            return;
        }
        
        lblStatus.setText("Đang đăng nhập...");
        lblStatus.setForeground(ElegantTheme.INFO);
        
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
                
                lblStatus.setText("Đăng nhập thành công!");
                lblStatus.setForeground(ElegantTheme.SUCCESS);
                
                // Delay to show success message
                Timer timer = new Timer(500, e -> {
                    ManHinhChinh mainWindow = new ManHinhChinh();
                    mainWindow.setVisible(true);
                    this.dispose();
                });
                timer.setRepeats(false);
                timer.start();
                
            } else {
                lblStatus.setText("Sai tên đăng nhập hoặc mật khẩu!");
                lblStatus.setForeground(ElegantTheme.DANGER);
                txtPassword.setText("");
                txtPassword.requestFocus();
            }
        } catch (SQLException e) {
            lblStatus.setText("Lỗi kết nối database!");
            lblStatus.setForeground(ElegantTheme.DANGER);
            e.printStackTrace();
        }
    }
}
