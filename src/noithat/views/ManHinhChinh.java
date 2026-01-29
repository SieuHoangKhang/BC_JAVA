package noithat.views;

import noithat.utils.*;
import noithat.database.DatabaseHelper;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.sql.*;

public class ManHinhChinh extends JFrame {
    private RippleButton btnLogout;
    private JLabel[] statValueLabels = new JLabel[4];
    
    public ManHinhChinh() {
        initComponents();
        AnimationHelper.fadeIn(getContentPane(), 300);
    }
    
    private void initComponents() {
        SessionManager session = SessionManager.getInstance();
        
        setTitle("Hệ Thống Quản Lý Cửa Hàng Nội Thất");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 800);
        setLocationRelativeTo(null);
        setResizable(true);
        
        // Main panel with gradient background
        JPanel mainPanel = new GradientPanel(
            new Color(240, 242, 245),
            new Color(255, 255, 255)
        );
        mainPanel.setLayout(new BorderLayout());
        
        // ===== HEADER WITH GLASSMORPHISM =====
        JPanel headerWrapper = new JPanel(new BorderLayout());
        headerWrapper.setOpaque(false);
        headerWrapper.setBorder(new EmptyBorder(15, 20, 15, 20));
        
        GlassmorphismPanel headerPanel = new GlassmorphismPanel(15, 0.85f);
        headerPanel.setLayout(new BorderLayout(20, 0));
        headerPanel.setBorder(new EmptyBorder(20, 30, 20, 30));
        
        // Welcome text
        JPanel welcomePanel = new JPanel();
        welcomePanel.setOpaque(false);
        welcomePanel.setLayout(new BoxLayout(welcomePanel, BoxLayout.Y_AXIS));
        
        JLabel lblWelcome = new JLabel("Chào mừng, " + session.getFullName());
        lblWelcome.setFont(FontHelper.HEADING_LARGE);
        lblWelcome.setForeground(ColorTheme.TEXT_PRIMARY);
        
        JLabel lblRole = new JLabel("Vai trò: " + session.getRole());
        lblRole.setFont(FontHelper.BODY_MEDIUM);
        lblRole.setForeground(ColorTheme.TEXT_SECONDARY);
        
        welcomePanel.add(lblWelcome);
        welcomePanel.add(Box.createVerticalStrut(5));
        welcomePanel.add(lblRole);
        
        // Logout button
        btnLogout = new RippleButton("Đăng Xuất", ColorTheme.DANGER);
        btnLogout.setPreferredSize(new Dimension(140, 45));
        btnLogout.setFont(FontHelper.BUTTON);
        btnLogout.addActionListener(e -> logout());
        
        headerPanel.add(welcomePanel, BorderLayout.WEST);
        headerPanel.add(btnLogout, BorderLayout.EAST);
        headerWrapper.add(headerPanel);
        
        // ===== CONTENT AREA =====
        JPanel contentPanel = new JPanel();
        contentPanel.setOpaque(false);
        contentPanel.setLayout(new BorderLayout(20, 20));
        contentPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        
        // Statistics Panel with Glassmorphism Cards
        JPanel statsPanel = new JPanel();
        statsPanel.setOpaque(false);
        statsPanel.setLayout(new GridLayout(1, 4, 20, 0));
        statsPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 160));
        
        // Load real statistics
        String productCount = getProductCount();
        String customerCount = getCustomerCount();
        String orderCount = getOrderCount();
        String revenue = getTotalRevenue();
        
        statsPanel.add(createStatsCard("SẢN PHẨM", productCount, "Tổng số sản phẩm", 
            ColorTheme.ACCENT_BLUE, "📦"));
        statsPanel.add(createStatsCard("KHÁCH HÀNG", customerCount, "Khách hàng đang hoạt động", 
            ColorTheme.ACCENT_GREEN, "👥"));
        statsPanel.add(createStatsCard("ĐƠN HÀNG", orderCount, "Tổng đơn hàng", 
            ColorTheme.ACCENT_PURPLE, "📋"));
        statsPanel.add(createStatsCard("DOANH THU", revenue, "Doanh thu hoàn thành", 
            ColorTheme.ACCENT_ORANGE, "💰"));
        
        // Menu Grid with enhanced buttons
        JPanel menuPanel = new JPanel();
        menuPanel.setOpaque(false);
        menuPanel.setLayout(new GridLayout(2, 4, 15, 15));
        menuPanel.setBorder(new EmptyBorder(20, 0, 0, 0));
        
        menuPanel.add(createMenuButton("Quản Lý Sản Phẩm", ColorTheme.ACCENT_BLUE, 
            e -> new FormQuanLySanPham().setVisible(true)));
        menuPanel.add(createMenuButton("Quản Lý Đơn Hàng", ColorTheme.ACCENT_PURPLE,
            e -> new FormQuanLyDonHang().setVisible(true)));
        menuPanel.add(createMenuButton("Quản Lý Khách Hàng", ColorTheme.ACCENT_GREEN,
            e -> new FormQuanLyKhachHang().setVisible(true)));
        menuPanel.add(createMenuButton("Quản Lý Kho", ColorTheme.ACCENT_ORANGE,
            e -> new FormQuanLyKho().setVisible(true)));
        menuPanel.add(createMenuButton("Quản Lý Danh Mục", ColorTheme.INFO,
            e -> new FormQuanLyDanhMuc().setVisible(true)));
        menuPanel.add(createMenuButton("Quản Lý Nhà Cung Cấp", ColorTheme.SECONDARY,
            e -> new FormQuanLyNhaCungCap().setVisible(true)));
        menuPanel.add(createMenuButton("Báo Cáo & Thống Kê", ColorTheme.PRIMARY,
            e -> new FormBaoCao().setVisible(true)));
        
        // Assemble content
        JPanel centerWrapper = new JPanel();
        centerWrapper.setOpaque(false);
        centerWrapper.setLayout(new BoxLayout(centerWrapper, BoxLayout.Y_AXIS));
        centerWrapper.add(statsPanel);
        centerWrapper.add(Box.createVerticalStrut(20));
        centerWrapper.add(menuPanel);
        
        contentPanel.add(centerWrapper, BorderLayout.NORTH);
        
        // Assemble main panel
        mainPanel.add(headerWrapper, BorderLayout.NORTH);
        mainPanel.add(contentPanel, BorderLayout.CENTER);
        
        add(mainPanel);
    }
    
    private JPanel createStatsCard(String title, String value, String description, 
                                   Color accentColor, String icon) {
        GlassmorphismPanel card = new GlassmorphismPanel(15, 0.9f);
        card.setLayout(new BorderLayout(10, 10));
        card.setBorder(new EmptyBorder(20, 20, 20, 20));
        
        // Add subtle shadow
        card.setBorder(BorderFactory.createCompoundBorder(
            new EnhancedShadowBorder(EnhancedShadowBorder.ShadowLevel.MEDIUM, 15),
            new EmptyBorder(20, 20, 20, 20)
        ));
        
        // Icon and title
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        topPanel.setOpaque(false);
        
        JLabel lblIcon = new JLabel(icon);
        lblIcon.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 24));
        
        JLabel lblTitle = new JLabel(title);
        lblTitle.setFont(FontHelper.BODY_SMALL);
        lblTitle.setForeground(ColorTheme.TEXT_SECONDARY);
        
        topPanel.add(lblIcon);
        topPanel.add(lblTitle);
        
        // Value
        JLabel lblValue = new JLabel(value);
        lblValue.setFont(FontHelper.HEADING_LARGE);
        lblValue.setForeground(accentColor);
        lblValue.setHorizontalAlignment(SwingConstants.CENTER);
        
        // Store reference for potential animation
        statValueLabels[getComponentCount() % 4] = lblValue;
        
        // Description
        JLabel lblDesc = new JLabel(description);
        lblDesc.setFont(FontHelper.BODY_TINY);
        lblDesc.setForeground(ColorTheme.TEXT_SECONDARY);
        lblDesc.setHorizontalAlignment(SwingConstants.CENTER);
        
        // Colored bar at bottom
        JPanel colorBar = new JPanel();
        colorBar.setBackground(accentColor);
        colorBar.setPreferredSize(new Dimension(0, 4));
        
        // Assemble
        JPanel centerPanel = new JPanel();
        centerPanel.setOpaque(false);
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.add(Box.createVerticalStrut(10));
        centerPanel.add(lblValue);
        centerPanel.add(Box.createVerticalStrut(5));
        centerPanel.add(lblDesc);
        
        card.add(topPanel, BorderLayout.NORTH);
        card.add(centerPanel, BorderLayout.CENTER);
        card.add(colorBar, BorderLayout.SOUTH);
        
        return card;
    }
    
    private RippleButton createMenuButton(String text, Color color, 
                                         java.awt.event.ActionListener action) {
        RippleButton btn = new RippleButton(text, color);
        btn.setFont(FontHelper.HEADING_SMALL);
        btn.setPreferredSize(new Dimension(200, 90));
        btn.setCornerRadius(12);
        btn.addActionListener(action);
        return btn;
    }
    
    private void logout() {
        int confirm = JOptionPane.showConfirmDialog(this, 
            "Bạn có chắc chắn muốn đăng xuất?", 
            "Xác nhận", 
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE);
        
        if (confirm == JOptionPane.YES_OPTION) {
            SessionManager session = SessionManager.getInstance();
            ActivityLogger.logLogout(session.getUserId(), session.getUsername());
            session.logout();
            
            ToastNotification.show(this, "Đăng xuất thành công!", ToastNotification.SUCCESS);
            
            FormDangNhap loginForm = new FormDangNhap();
            loginForm.setVisible(true);
            this.dispose();
        }
    }
    
    // Statistics methods
    private String getProductCount() {
        try (Connection conn = DatabaseHelper.getDBConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT COUNT(*) AS count FROM Products")) {
            if (rs.next()) {
                return String.valueOf(rs.getInt("count"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "0";
    }
    
    private String getCustomerCount() {
        try (Connection conn = DatabaseHelper.getDBConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT COUNT(*) AS count FROM Customers")) {
            if (rs.next()) {
                return String.valueOf(rs.getInt("count"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "0";
    }
    
    private String getOrderCount() {
        try (Connection conn = DatabaseHelper.getDBConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT COUNT(*) AS count FROM Orders")) {
            if (rs.next()) {
                return String.valueOf(rs.getInt("count"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "0";
    }
    
    private String getTotalRevenue() {
        try (Connection conn = DatabaseHelper.getDBConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT ISNULL(SUM(TotalAmount), 0) AS total FROM Orders WHERE Status = 'Completed'")) {
            if (rs.next()) {
                return CurrencyHelper.formatCurrency(rs.getDouble("total"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "0 đ";
    }
}
