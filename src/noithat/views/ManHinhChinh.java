package noithat.views;

import noithat.utils.*;
import javax.swing.*;
import java.awt.*;

public class ManHinhChinh extends JFrame {
    private JLabel lblWelcome;
    private JLabel lblRole;
    private ModernButton btnQuanLySanPham;
    private ModernButton btnQuanLyDonHang;
    private ModernButton btnQuanLyKhachHang;
    private ModernButton btnQuanLyKho;
    private ModernButton btnQuanLyDanhMuc;
    private ModernButton btnQuanLyNhaCungCap;
    private ModernButton btnBaoCao;
    private ModernButton btnLogout;
    
    public ManHinhChinh() {
        initComponents();
    }
    
    private void initComponents() {
        SessionManager session = SessionManager.getInstance();
        
        setTitle("Hệ Thống Quản Lý Của Hàng Nội Thất");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1100, 700);
        setLocationRelativeTo(null);
        setResizable(true);
        
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(ColorTheme.SECONDARY);
        headerPanel.setLayout(new BorderLayout());
        headerPanel.setBorder(BorderFactory.createEmptyBorder(15, 30, 15, 30));
        
        JLabel lblWelcomeText = new JLabel("Chào mừng, " + session.getFullName() + " (" + session.getRole() + ")");
        lblWelcomeText.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblWelcomeText.setForeground(ColorTheme.TEXT_LIGHT);
        
        btnLogout = new ModernButton("Đăng Xuất", ColorTheme.DANGER);
        btnLogout.setPreferredSize(new Dimension(120, 40));
        btnLogout.setFont(new Font("Segoe UI", Font.BOLD, 11));
        
        headerPanel.add(lblWelcomeText, BorderLayout.WEST);
        headerPanel.add(btnLogout, BorderLayout.EAST);
        
        JPanel contentPanel = new JPanel();
        contentPanel.setBackground(ColorTheme.BACKGROUND);
        contentPanel.setLayout(new BorderLayout(15, 15));
        contentPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        
        JPanel statsPanel = new JPanel();
        statsPanel.setBackground(ColorTheme.BACKGROUND);
        statsPanel.setLayout(new GridLayout(2, 2, 20, 20));
        statsPanel.setPreferredSize(new Dimension(0, 250));
        
        statsPanel.add(createCard("SẢN PHẨM", ColorTheme.ACCENT_BLUE, "0", "Tổng số sản phẩm"));
        statsPanel.add(createCard("KHÁCH HÀNG", ColorTheme.ACCENT_GREEN, "0", "Tổng số khách hàng"));
        statsPanel.add(createCard("ĐƠN HÀNG", ColorTheme.ACCENT_PURPLE, "0", "Tổng số đơn hàng"));
        statsPanel.add(createCard("DOANH THU", ColorTheme.ACCENT_ORANGE, "0 đ", "Tổng doanh thu"));
        
        JPanel menuPanel = new JPanel();
        menuPanel.setBackground(ColorTheme.BACKGROUND);
        menuPanel.setLayout(new GridLayout(2, 4, 15, 15));
        menuPanel.setBorder(BorderFactory.createEmptyBorder(15, 0, 0, 0));
        
        btnQuanLySanPham = createMenuButton("📦 Sản Phẩm", ColorTheme.ACCENT_BLUE);
        btnQuanLyDonHang = createMenuButton("📋 Đơn Hàng", ColorTheme.ACCENT_PURPLE);
        btnQuanLyKhachHang = createMenuButton("👥 Khách Hàng", ColorTheme.ACCENT_GREEN);
        btnQuanLyKho = createMenuButton("📊 Kho", ColorTheme.ACCENT_ORANGE);
        btnQuanLyDanhMuc = createMenuButton("🏷️ Danh Mục", ColorTheme.INFO);
        btnQuanLyNhaCungCap = createMenuButton("🏢 Nhà Cung Cấp", ColorTheme.SECONDARY_LIGHT);
        btnBaoCao = createMenuButton("📈 Báo Cáo", ColorTheme.ACCENT_BLUE);
        
        menuPanel.add(btnQuanLySanPham);
        menuPanel.add(btnQuanLyDonHang);
        menuPanel.add(btnQuanLyKhachHang);
        menuPanel.add(btnQuanLyKho);
        menuPanel.add(btnQuanLyDanhMuc);
        menuPanel.add(btnQuanLyNhaCungCap);
        menuPanel.add(btnBaoCao);
        
        contentPanel.add(statsPanel, BorderLayout.NORTH);
        contentPanel.add(menuPanel, BorderLayout.CENTER);
        
        JPanel mainPanel = new JPanel();
        mainPanel.setBackground(ColorTheme.BACKGROUND);
        mainPanel.setLayout(new BorderLayout());
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(contentPanel, BorderLayout.CENTER);
        
        add(mainPanel);
        
        btnLogout.addActionListener(e -> logout());
        btnQuanLySanPham.addActionListener(e -> new FormQuanLySanPham().setVisible(true));
        btnQuanLyDonHang.addActionListener(e -> new FormQuanLyDonHang().setVisible(true));
        btnQuanLyKhachHang.addActionListener(e -> new FormQuanLyKhachHang().setVisible(true));
        btnQuanLyKho.addActionListener(e -> new FormQuanLyKho().setVisible(true));
        btnQuanLyDanhMuc.addActionListener(e -> new FormQuanLyDanhMuc().setVisible(true));
        btnQuanLyNhaCungCap.addActionListener(e -> new FormQuanLyNhaCungCap().setVisible(true));
        btnBaoCao.addActionListener(e -> new FormBaoCao().setVisible(true));
    }
    
    private ModernButton createMenuButton(String text, Color color) {
        ModernButton btn = new ModernButton(text, color);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btn.setPreferredSize(new Dimension(150, 80));
        return btn;
    }
    
    private JPanel createCard(String title, Color borderColor, String value, String description) {
        JPanel card = new JPanel();
        card.setBackground(ColorTheme.SURFACE);
        card.setLayout(new BorderLayout());
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 4, 0, 0, borderColor),
            BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));
        
        JLabel lblTitle = new JLabel(title);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblTitle.setForeground(borderColor);
        
        JLabel lblValue = new JLabel(value);
        lblValue.setFont(new Font("Segoe UI", Font.BOLD, 36));
        lblValue.setForeground(borderColor);
        lblValue.setHorizontalAlignment(SwingConstants.CENTER);
        
        JLabel lblDesc = new JLabel(description);
        lblDesc.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        lblDesc.setForeground(ColorTheme.TEXT_SECONDARY);
        lblDesc.setHorizontalAlignment(SwingConstants.CENTER);
        
        JPanel centerPanel = new JPanel();
        centerPanel.setBackground(ColorTheme.SURFACE);
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.add(Box.createVerticalStrut(10));
        centerPanel.add(lblValue);
        centerPanel.add(Box.createVerticalStrut(10));
        centerPanel.add(lblDesc);
        centerPanel.add(Box.createVerticalStrut(10));
        
        card.add(lblTitle, BorderLayout.NORTH);
        card.add(centerPanel, BorderLayout.CENTER);
        
        return card;
    }
    
    private void logout() {
        int confirm = JOptionPane.showConfirmDialog(this, 
            "Bạn có chắc chắn muốn đăng xuất?", 
            "Xác nhận", 
            JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            SessionManager session = SessionManager.getInstance();
            ActivityLogger.logLogout(session.getUserId(), session.getUsername());
            session.logout();
            
            FormDangNhap loginForm = new FormDangNhap();
            loginForm.setVisible(true);
            this.dispose();
        }
    }
}

