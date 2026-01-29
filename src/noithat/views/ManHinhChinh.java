package noithat.views;

import noithat.utils.*;
import noithat.database.DatabaseHelper;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

/**
 * Main Dashboard - Professional Furniture Store Management
 * Fixed: Menu items now properly open their respective forms
 */
public class ManHinhChinh extends JFrame {
    
    private JPanel sidebarPanel;
    private JPanel mainContentPanel;
    private SessionManager session;
    private JPanel currentMenuItem;
    private Color menuHoverColor = new Color(245, 240, 230);
    
    public ManHinhChinh() {
        session = SessionManager.getInstance();
        initComponents();
        AnimationHelper.fadeIn(getContentPane(), 300);
    }
    
    private void initComponents() {
        setTitle("Hệ Thống Quản Lý Cửa Hàng Nội Thất");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1400, 850);
        setLocationRelativeTo(null);
        setResizable(true);
        
        // Main container
        JPanel mainContainer = new JPanel(new BorderLayout(0, 0));
        mainContainer.setBackground(ElegantTheme.NEUTRAL_100);
        
        // Create sidebar and main content
        createSidebar();
        createMainContent();
        
        mainContainer.add(sidebarPanel, BorderLayout.WEST);
        mainContainer.add(mainContentPanel, BorderLayout.CENTER);
        
        add(mainContainer);
    }
    
    private void createSidebar() {
        sidebarPanel = new JPanel();
        sidebarPanel.setLayout(new BoxLayout(sidebarPanel, BoxLayout.Y_AXIS));
        sidebarPanel.setPreferredSize(new Dimension(270, getHeight()));
        sidebarPanel.setBackground(ElegantTheme.SURFACE);
        sidebarPanel.setBorder(new EmptyBorder(25, 20, 25, 20));
        
        // Logo area
        JPanel logoPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        logoPanel.setOpaque(false);
        logoPanel.setMaximumSize(new Dimension(250, 70));
        
        JLabel lblLogo = new JLabel("NỘI THẤT");
        lblLogo.setFont(FontHelper.h3());
        lblLogo.setForeground(ElegantTheme.PRIMARY);
        logoPanel.add(lblLogo);
        
        JLabel lblSubtitle = new JLabel("Nội Thất Cao Cấp");
        lblSubtitle.setFont(FontHelper.bodySmall());
        lblSubtitle.setForeground(ElegantTheme.TEXT_SECONDARY);
        logoPanel.add(lblSubtitle);
        
        sidebarPanel.add(logoPanel);
        sidebarPanel.add(Box.createVerticalStrut(25));
        
        // Menu section label
        JLabel lblMenu = new JLabel("QUẢN LÝ");
        lblMenu.setFont(FontHelper.caption());
        lblMenu.setForeground(ElegantTheme.TEXT_TERTIARY);
        lblMenu.setAlignmentX(Component.LEFT_ALIGNMENT);
        sidebarPanel.add(lblMenu);
        sidebarPanel.add(Box.createVerticalStrut(12));
        
        // Menu items with proper interaction - CLICK TO OPEN FORMS
        addMenuItem("📦", "Sản Phẩm", () -> openForm(new FormQuanLySanPham()));
        addMenuItem("🛒", "Đơn Hàng", () -> openForm(new FormQuanLyDonHang()));
        addMenuItem("👥", "Khách Hàng", () -> openForm(new FormQuanLyKhachHang()));
        addMenuItem("🏭", "Kho Hàng", () -> openForm(new FormQuanLyKho()));
        addMenuItem("📁", "Danh Mục", () -> openForm(new FormQuanLyDanhMuc()));
        addMenuItem("🚚", "Nhà Cung Cấp", () -> openForm(new FormQuanLyNhaCungCap()));
        addMenuItem("📊", "Báo Cáo", () -> openForm(new FormBaoCao()));
        
        sidebarPanel.add(Box.createVerticalGlue());
        
        // Separator
        JSeparator separator = new JSeparator();
        separator.setForeground(ElegantTheme.NEUTRAL_200);
        separator.setMaximumSize(new Dimension(230, 1));
        sidebarPanel.add(separator);
        sidebarPanel.add(Box.createVerticalStrut(18));
        
        // User info section
        JPanel userPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 0));
        userPanel.setOpaque(false);
        userPanel.setMaximumSize(new Dimension(250, 60));
        
        // Avatar
        JLabel lblAvatar = new JLabel(session.getFullName().substring(0, 1).toUpperCase());
        lblAvatar.setFont(FontHelper.h4());
        lblAvatar.setForeground(ElegantTheme.CLASSIC_WHITE);
        lblAvatar.setHorizontalAlignment(SwingConstants.CENTER);
        lblAvatar.setPreferredSize(new Dimension(46, 46));
        lblAvatar.setBackground(ElegantTheme.PRIMARY);
        lblAvatar.setOpaque(true);
        lblAvatar.setBorder(BorderFactory.createLineBorder(ElegantTheme.CLASSIC_WHITE, 3));
        
        JPanel userInfo = new JPanel();
        userInfo.setLayout(new BoxLayout(userInfo, BoxLayout.Y_AXIS));
        userInfo.setOpaque(false);
        
        JLabel lblName = new JLabel(session.getFullName());
        lblName.setFont(FontHelper.bodyMedium());
        lblName.setForeground(ElegantTheme.TEXT_PRIMARY);
        lblName.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JLabel lblRole = new JLabel("Quản Lý");
        lblRole.setFont(FontHelper.tiny());
        lblRole.setForeground(ElegantTheme.TEXT_SECONDARY);
        lblRole.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        userInfo.add(lblName);
        userInfo.add(lblRole);
        
        userPanel.add(lblAvatar);
        userPanel.add(userInfo);
        
        sidebarPanel.add(userPanel);
        sidebarPanel.add(Box.createVerticalStrut(15));
        
        // Logout button
        ModernButton btnLogout = new ModernButton("Đăng Xuất", ElegantTheme.DANGER);
        btnLogout.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnLogout.setMaximumSize(new Dimension(230, 44));
        btnLogout.addActionListener(e -> logout());
        sidebarPanel.add(btnLogout);
        sidebarPanel.add(Box.createVerticalStrut(10));
    }
    
    private void addMenuItem(String icon, String text, Runnable action) {
        JPanel menuItem = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 10));
        menuItem.setOpaque(false);
        menuItem.setMaximumSize(new Dimension(250, 52));
        menuItem.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Icon
        JLabel lblIcon = new JLabel(icon);
        lblIcon.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 18));
        lblIcon.setPreferredSize(new Dimension(28, 28));
        
        // Text
        JLabel lblText = new JLabel(text);
        lblText.setFont(FontHelper.bodyMedium());
        lblText.setForeground(ElegantTheme.TEXT_PRIMARY);
        
        menuItem.add(lblIcon);
        menuItem.add(lblText);
        
        menuItem.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                if (menuItem != currentMenuItem) {
                    menuItem.setBackground(menuHoverColor);
                }
                menuItem.repaint();
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                if (menuItem != currentMenuItem) {
                    menuItem.setBackground(new Color(0, 0, 0, 0));
                }
                menuItem.repaint();
            }
            
            @Override
            public void mouseClicked(MouseEvent e) {
                setActiveMenu(menuItem);
                action.run();
            }
        });
        
        sidebarPanel.add(menuItem);
    }
    
    private void setActiveMenu(JPanel menuItem) {
        // Reset previous active menu
        if (currentMenuItem != null) {
            currentMenuItem.setBackground(new Color(0, 0, 0, 0));
            for (Component c : currentMenuItem.getComponents()) {
                if (c instanceof JLabel) {
                    ((JLabel) c).setForeground(ElegantTheme.TEXT_PRIMARY);
                }
            }
        }
        
        // Set new active menu
        currentMenuItem = menuItem;
        currentMenuItem.setBackground(ElegantTheme.withAlpha(ElegantTheme.PRIMARY, 15));
        
        // Highlight text
        for (Component c : currentMenuItem.getComponents()) {
            if (c instanceof JLabel) {
                ((JLabel) c).setForeground(ElegantTheme.PRIMARY);
            }
        }
        
        sidebarPanel.repaint();
    }
    
    private void openForm(JFrame form) {
        form.setVisible(true);
        form.setLocationRelativeTo(this);
    }
    
    private void createMainContent() {
        mainContentPanel = new JPanel(new BorderLayout(0, 0));
        mainContentPanel.setBackground(ElegantTheme.NEUTRAL_100);
        
        // Header
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(ElegantTheme.SURFACE);
        headerPanel.setBorder(new EmptyBorder(22, 30, 22, 30));
        
        JLabel lblTitle = new JLabel("TRANG CHỦ");
        lblTitle.setFont(FontHelper.h2());
        lblTitle.setForeground(ElegantTheme.TEXT_PRIMARY);
        
        // Date with Vietnamese format
        java.util.Calendar cal = java.util.Calendar.getInstance();
        String[] days = {"Chủ Nhật", "Thứ Hai", "Thứ Ba", "Thứ Tư", "Thứ Năm", "Thứ Sáu", "Thứ Bảy"};
        int dayOfWeek = cal.get(java.util.Calendar.DAY_OF_WEEK) - 1;
        String dateStr = days[dayOfWeek] + ", " + new java.text.SimpleDateFormat("dd/MM/yyyy").format(cal.getTime());
        
        JLabel lblDate = new JLabel(dateStr);
        lblDate.setFont(FontHelper.bodyMedium());
        lblDate.setForeground(ElegantTheme.TEXT_SECONDARY);
        
        headerPanel.add(lblTitle, BorderLayout.WEST);
        headerPanel.add(lblDate, BorderLayout.EAST);
        
        mainContentPanel.add(headerPanel, BorderLayout.NORTH);
        
        // Dashboard Content
        mainContentPanel.add(createDashboardPanel(), BorderLayout.CENTER);
    }
    
    private JPanel createDashboardPanel() {
        JPanel container = new JPanel(new BorderLayout(0, 0));
        container.setBackground(ElegantTheme.NEUTRAL_100);
        container.setBorder(new EmptyBorder(20, 25, 25, 25));
        
        // Top: Statistics Cards
        JPanel statsRow = new JPanel(new GridLayout(1, 4, 20, 0));
        statsRow.setOpaque(false);
        
        String productCount = getProductCount();
        String customerCount = getCustomerCount();
        String orderCount = getOrderCount();
        String revenue = getTotalRevenue();
        
        statsRow.add(createStatCard("SẢN PHẨM", productCount, "Tổng số sản phẩm", 
            ElegantTheme.PRIMARY));
        statsRow.add(createStatCard("KHÁCH HÀNG", customerCount, "Khách hàng", 
            ElegantTheme.SUCCESS));
        statsRow.add(createStatCard("ĐƠN HÀNG", orderCount, "Đơn hàng", 
            ElegantTheme.INFO));
        statsRow.add(createStatCard("DOANH THU", revenue, "Tổng doanh thu", 
            ElegantTheme.ACCENT_GOLD));
        
        container.add(statsRow, BorderLayout.NORTH);
        container.add(Box.createVerticalStrut(20), BorderLayout.CENTER);
        
        // Bottom: Content area
        JPanel bottomPanel = new JPanel(new GridLayout(1, 2, 20, 0));
        bottomPanel.setOpaque(false);
        
        // Left: Revenue Chart
        JPanel chartPanel = new JPanel(new BorderLayout());
        chartPanel.setBackground(ElegantTheme.SURFACE);
        chartPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        
        JLabel lblChartTitle = new JLabel("📈 DOANH THU THEO THÁNG");
        lblChartTitle.setFont(FontHelper.h4());
        lblChartTitle.setForeground(ElegantTheme.TEXT_PRIMARY);
        chartPanel.add(lblChartTitle, BorderLayout.NORTH);
        
        // Chart area with simple bars
        JPanel chartArea = new JPanel();
        chartArea.setLayout(new BoxLayout(chartArea, BoxLayout.Y_AXIS));
        chartArea.setOpaque(false);
        chartArea.setBorder(new EmptyBorder(20, 10, 10, 10));
        
        String[] months = {"T1", "T2", "T3", "T4", "T5", "T6"};
        int[] revenues = {120, 150, 180, 140, 200, 220};
        int maxRevenue = 250;
        
        JPanel barsPanel = new JPanel(new GridLayout(1, 6, 8, 0));
        barsPanel.setOpaque(false);
        
        for (int i = 0; i < 6; i++) {
            JPanel barContainer = new JPanel(new BorderLayout(0, 5));
            barContainer.setOpaque(false);
            
            int height = (revenues[i] * 150) / maxRevenue;
            JPanel bar = new JPanel();
            bar.setPreferredSize(new Dimension(40, height));
            bar.setBackground(ElegantTheme.PRIMARY);
            bar.setMaximumSize(new Dimension(50, 150));
            
            JLabel lblValue = new JLabel(String.valueOf(revenues[i]));
            lblValue.setFont(FontHelper.bodySmall());
            lblValue.setForeground(ElegantTheme.TEXT_SECONDARY);
            lblValue.setHorizontalAlignment(SwingConstants.CENTER);
            
            JLabel lblMonth = new JLabel(months[i]);
            lblMonth.setFont(FontHelper.tiny());
            lblMonth.setForeground(ElegantTheme.TEXT_TERTIARY);
            lblMonth.setHorizontalAlignment(SwingConstants.CENTER);
            
            barContainer.add(bar, BorderLayout.CENTER);
            barContainer.add(lblValue, BorderLayout.NORTH);
            barContainer.add(lblMonth, BorderLayout.SOUTH);
            
            barsPanel.add(barContainer);
        }
        
        chartArea.add(barsPanel);
        chartPanel.add(chartArea, BorderLayout.CENTER);
        
        // Right: Recent Orders
        JPanel ordersPanel = new JPanel(new BorderLayout());
        ordersPanel.setBackground(ElegantTheme.SURFACE);
        ordersPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        
        JLabel lblOrdersTitle = new JLabel("🛒 ĐƠN HÀNG MỚI NHẤT");
        lblOrdersTitle.setFont(FontHelper.h4());
        lblOrdersTitle.setForeground(ElegantTheme.TEXT_PRIMARY);
        ordersPanel.add(lblOrdersTitle, BorderLayout.NORTH);
        
        // Table
        String[] columnNames = {"Mã", "Khách hàng", "Ngày", "Tổng tiền", "Trạng thái"};
        Object[][] data = getRecentOrders();
        
        JTable table = new JTable(data, columnNames);
        table.setFont(FontHelper.tableBody());
        table.setRowHeight(36);
        table.setSelectionBackground(ElegantTheme.withAlpha(ElegantTheme.PRIMARY, 15));
        table.setSelectionForeground(ElegantTheme.TEXT_PRIMARY);
        table.setGridColor(ElegantTheme.NEUTRAL_200);
        table.getTableHeader().setFont(FontHelper.tableHeader());
        table.getTableHeader().setBackground(ElegantTheme.NEUTRAL_100);
        table.getTableHeader().setForeground(ElegantTheme.TEXT_SECONDARY);
        table.setShowGrid(true);
        
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setBackground(ElegantTheme.SURFACE);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(15, 0, 0, 0));
        
        ordersPanel.add(scrollPane, BorderLayout.CENTER);
        
        bottomPanel.add(chartPanel);
        bottomPanel.add(ordersPanel);
        
        container.add(bottomPanel, BorderLayout.SOUTH);
        
        return container;
    }
    
    private Object[][] getRecentOrders() {
        return new Object[][] {
            {"DH001", "Nguyễn Văn A", "28/01/2026", "15,500,000 VNĐ", "Hoàn thành"},
            {"DH002", "Trần Thị B", "28/01/2026", "8,200,000 VNĐ", "Đang xử lý"},
            {"DH003", "Lê Văn C", "27/01/2026", "22,000,000 VNĐ", "Hoàn thành"},
            {"DH004", "Phạm Thị D", "27/01/2026", "5,750,000 VNĐ", "Đang giao"},
            {"DH005", "Hoàng Văn E", "26/01/2026", "12,300,000 VNĐ", "Hoàn thành"},
        };
    }
    
    private JPanel createStatCard(String title, String value, String subtitle, Color accentColor) {
        JPanel card = new JPanel(new BorderLayout(12, 0));
        card.setBackground(ElegantTheme.SURFACE);
        card.setBorder(new EmptyBorder(20, 20, 20, 20));
        
        // Left colored bar
        JPanel colorBar = new JPanel();
        colorBar.setBackground(accentColor);
        colorBar.setPreferredSize(new Dimension(6, 55));
        
        // Content
        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setOpaque(false);
        
        JLabel lblTitle = new JLabel(title);
        lblTitle.setFont(FontHelper.caption());
        lblTitle.setForeground(ElegantTheme.TEXT_SECONDARY);
        lblTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JLabel lblValue = new JLabel(value);
        lblValue.setFont(FontHelper.h1());
        lblValue.setForeground(accentColor);
        lblValue.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JLabel lblSubtitle = new JLabel(subtitle);
        lblSubtitle.setFont(FontHelper.tiny());
        lblSubtitle.setForeground(ElegantTheme.TEXT_TERTIARY);
        lblSubtitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        content.add(lblTitle);
        content.add(Box.createVerticalStrut(5));
        content.add(lblValue);
        content.add(Box.createVerticalStrut(5));
        content.add(lblSubtitle);
        
        card.add(colorBar, BorderLayout.WEST);
        card.add(content, BorderLayout.CENTER);
        
        return card;
    }
    
    private String getProductCount() {
        try {
            return String.valueOf(DatabaseHelper.executeQuery(
                "SELECT COUNT(*) FROM Products",
                (DatabaseHelper.ResultSetHandler<Integer>) rs -> {
                    if (rs.next()) return rs.getInt(1);
                    return 0;
                }
            ));
        } catch (SQLException e) { e.printStackTrace(); }
        return "0";
    }
    
    private String getCustomerCount() {
        try {
            return String.valueOf(DatabaseHelper.executeQuery(
                "SELECT COUNT(*) FROM Customers",
                (DatabaseHelper.ResultSetHandler<Integer>) rs -> {
                    if (rs.next()) return rs.getInt(1);
                    return 0;
                }
            ));
        } catch (SQLException e) { e.printStackTrace(); }
        return "0";
    }
    
    private String getOrderCount() {
        try {
            return String.valueOf(DatabaseHelper.executeQuery(
                "SELECT COUNT(*) FROM Orders",
                (DatabaseHelper.ResultSetHandler<Integer>) rs -> {
                    if (rs.next()) return rs.getInt(1);
                    return 0;
                }
            ));
        } catch (SQLException e) { e.printStackTrace(); }
        return "0";
    }
    
    private String getTotalRevenue() {
        try {
            Double total = DatabaseHelper.executeQuery(
                "SELECT ISNULL(SUM(TotalAmount), 0) FROM Orders",
                (DatabaseHelper.ResultSetHandler<Double>) rs -> {
                    if (rs.next()) return rs.getDouble(1);
                    return 0.0;
                }
            );
            return String.format("%,.0f VNĐ", total);
        } catch (SQLException e) { e.printStackTrace(); }
        return "0 VNĐ";
    }
    
    private void logout() {
        int confirm = JOptionPane.showConfirmDialog(this, 
            "Bạn có muốn đăng xuất?", "Xác Nhận", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            ActivityLogger.logLogout(session.getUserId(), session.getUsername());
            session.logout();
            ToastNotification.show(this, "Đăng xuất thành công!", ToastNotification.SUCCESS);
            this.dispose();
            new FormDangNhap().setVisible(true);
        }
    }
}
