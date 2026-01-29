package noithat.views;

import noithat.database.DatabaseHelper;
import noithat.utils.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class FormBaoCao extends JFrame {
    private JTable tableReport;
    private DefaultTableModel tableModel;
    private ModernButton btnRefresh, btnExport, btnBack;
    private JComboBox<String> cmbReportType;
    private JLabel lblStatus;
    
    public FormBaoCao() {
        initComponents();
        loadData();
    }
    
    private void initComponents() {
        setTitle("Báo Cáo & Thống Kê");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(1400, 750);
        setLocationRelativeTo(null);
        setResizable(true);
        
        // Header Panel
        JPanel headerPanel = new GradientPanel(ColorTheme.SUCCESS, new Color(46, 204, 113));
        headerPanel.setLayout(new BorderLayout());
        headerPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
        
        JLabel lblTitle = new JLabel("📊 BÁO CÁO & THỐNG KÊ");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 28));
        lblTitle.setForeground(Color.WHITE);
        
        lblStatus = new JLabel("Tổng: 0 bản ghi");
        lblStatus.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblStatus.setForeground(Color.WHITE);
        
        btnBack = new ModernButton("← Quay Lại", ColorTheme.DANGER);
        btnBack.setPreferredSize(new Dimension(140, 45));
        btnBack.addActionListener(e -> this.dispose());
        
        JPanel headerLeft = new JPanel();
        headerLeft.setOpaque(false);
        headerLeft.setLayout(new FlowLayout(FlowLayout.LEFT, 15, 0));
        headerLeft.add(lblTitle);
        headerLeft.add(lblStatus);
        
        headerPanel.add(headerLeft, BorderLayout.WEST);
        headerPanel.add(btnBack, BorderLayout.EAST);
        
        // Control Panel
        JPanel controlPanel = new JPanel();
        controlPanel.setBackground(ColorTheme.SURFACE);
        controlPanel.setBorder(BorderFactory.createEmptyBorder(0, 20, 10, 20));
        controlPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 15, 15));
        
        JLabel lblReportType = new JLabel("🔍 Loại Báo Cáo:");
        lblReportType.setFont(new Font("Segoe UI", Font.BOLD, 12));
        cmbReportType = new JComboBox<>(new String[]{
            "Doanh Thu Bán Hàng",
            "Sản Phẩm Bán Chạy",
            "Khách Hàng Thường Xuyên",
            "Tồn Kho Sản Phẩm"
        });
        cmbReportType.setPreferredSize(new Dimension(220, 40));
        cmbReportType.addActionListener(e -> loadData());
        
        btnRefresh = new ModernButton("🔄 Làm mới", ColorTheme.SUCCESS);
        btnRefresh.setPreferredSize(new Dimension(120, 40));
        btnRefresh.addActionListener(e -> loadData());
        
        btnExport = new ModernButton("📊 Xuất Excel", ColorTheme.INFO);
        btnExport.setPreferredSize(new Dimension(130, 40));
        btnExport.addActionListener(e -> ToastNotification.show(this, "Chức năng xuất Excel đang phát triển!", ToastNotification.INFO));
        
        controlPanel.add(lblReportType);
        controlPanel.add(cmbReportType);
        controlPanel.add(btnRefresh);
        controlPanel.add(btnExport);
        
        // Table Panel
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBackground(ColorTheme.BACKGROUND);
        tablePanel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));
        
        tableModel = new DefaultTableModel(
            new String[]{"STT", "Dữ Liệu", "Giá Trị"},
            0
        ) { public boolean isCellEditable(int r, int c) { return false; } };
        
        tableReport = new JTable(tableModel);
        tableReport.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        tableReport.setRowHeight(28);
        tableReport.setSelectionBackground(ColorTheme.SUCCESS);
        tableReport.setSelectionForeground(Color.WHITE);
        tableReport.setGridColor(ColorTheme.BORDER);
        
        javax.swing.table.JTableHeader header = tableReport.getTableHeader();
        header.setBackground(ColorTheme.SECONDARY);
        header.setForeground(Color.WHITE);
        header.setFont(new Font("Segoe UI", Font.BOLD, 13));
        header.setPreferredSize(new Dimension(0, 35));
        
        JScrollPane scrollPane = new JScrollPane(tableReport);
        scrollPane.setBorder(BorderFactory.createLineBorder(ColorTheme.BORDER, 1));
        tablePanel.add(scrollPane, BorderLayout.CENTER);
        
        // Main layout
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(ColorTheme.BACKGROUND);
        mainPanel.add(controlPanel, BorderLayout.NORTH);
        mainPanel.add(tablePanel, BorderLayout.CENTER);
        
        add(headerPanel, BorderLayout.NORTH);
        add(mainPanel, BorderLayout.CENTER);
    }
    
    private void loadData() {
        tableModel.setRowCount(0);
        String reportType = (String) cmbReportType.getSelectedItem();
        
        try {
            if (reportType.equals("Doanh Thu Bán Hàng")) {
                loadRevenueReport();
            } else if (reportType.equals("Sản Phẩm Bán Chạy")) {
                loadTopProductsReport();
            } else if (reportType.equals("Khách Hàng Thường Xuyên")) {
                loadTopCustomersReport();
            } else if (reportType.equals("Tồn Kho Sản Phẩm")) {
                loadInventoryReport();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            ToastNotification.show(this, "Lỗi tải báo cáo: " + e.getMessage(), ToastNotification.ERROR);
        }
    }
    
    private void loadRevenueReport() throws SQLException {
        String sql = "SELECT TOP 10 o.OrderDate, o.TotalAmount, c.CustomerName FROM Orders o " +
                     "JOIN Customers c ON o.CustomerID = c.CustomerID " +
                     "ORDER BY o.OrderDate DESC";
        
        try (Connection conn = DatabaseHelper.getDBConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            int count = 1;
            while (rs.next()) {
                tableModel.addRow(new Object[]{
                    count++,
                    rs.getDate("OrderDate"),
                    CurrencyHelper.formatCurrency(rs.getDouble("TotalAmount"))
                });
            }
            lblStatus.setText("Tổng: " + (count - 1) + " đơn hàng");
        }
    }
    
    private void loadTopProductsReport() throws SQLException {
        String sql = "SELECT TOP 10 p.ProductName, SUM(od.Quantity) AS TotalQty, SUM(od.Quantity * od.Price) AS Revenue " +
                     "FROM OrderDetails od JOIN Products p ON od.ProductID = p.ProductID " +
                     "GROUP BY p.ProductName ORDER BY TotalQty DESC";
        
        try (Connection conn = DatabaseHelper.getDBConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            int count = 1;
            while (rs.next()) {
                tableModel.addRow(new Object[]{
                    count++,
                    rs.getString("ProductName") + " (" + rs.getInt("TotalQty") + " cái)",
                    CurrencyHelper.formatCurrency(rs.getDouble("Revenue"))
                });
            }
            lblStatus.setText("Tổng: " + (count - 1) + " sản phẩm");
        }
    }
    
    private void loadTopCustomersReport() throws SQLException {
        String sql = "SELECT TOP 10 c.CustomerName, COUNT(o.OrderID) AS OrderCount, SUM(o.TotalAmount) AS TotalSpent " +
                     "FROM Customers c LEFT JOIN Orders o ON c.CustomerID = o.CustomerID " +
                     "GROUP BY c.CustomerID, c.CustomerName ORDER BY TotalSpent DESC";
        
        try (Connection conn = DatabaseHelper.getDBConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            int count = 1;
            while (rs.next()) {
                tableModel.addRow(new Object[]{
                    count++,
                    rs.getString("CustomerName") + " (" + rs.getInt("OrderCount") + " đơn)",
                    CurrencyHelper.formatCurrency(rs.getDouble("TotalSpent"))
                });
            }
            lblStatus.setText("Tổng: " + (count - 1) + " khách hàng");
        }
    }
    
    private void loadInventoryReport() throws SQLException {
        String sql = "SELECT TOP 15 p.ProductName, i.Quantity, p.Price FROM Inventory i " +
                     "JOIN Products p ON i.ProductID = p.ProductID " +
                     "ORDER BY i.Quantity DESC";
        
        try (Connection conn = DatabaseHelper.getDBConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            int count = 1;
            while (rs.next()) {
                int qty = rs.getInt("Quantity");
                String status = qty > 10 ? "Đủ" : qty > 0 ? "Sắp hết" : "Hết hàng";
                tableModel.addRow(new Object[]{
                    count++,
                    rs.getString("ProductName") + " (" + status + ")",
                    qty + " cái"
                });
            }
            lblStatus.setText("Tổng: " + (count - 1) + " sản phẩm");
        }
    }
}

