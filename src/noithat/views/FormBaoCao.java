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
        setSize(1400, 800);
        setLocationRelativeTo(null);
        setResizable(true);
        
        // Header Panel
        JPanel headerPanel = new GradientPanel(ColorTheme.SUCCESS, new Color(46, 204, 113));
        headerPanel.setLayout(new BorderLayout());
        headerPanel.setPreferredSize(new Dimension(1400, 45));
        
        JLabel lblTitle = new JLabel("Báo Cáo & Thống Kê");
        lblTitle.setFont(new Font("Arial", Font.BOLD, 20));
        lblTitle.setForeground(Color.WHITE);
        lblTitle.setBorder(BorderFactory.createEmptyBorder(5, 20, 5, 5));
        
        lblStatus = new JLabel("0 bản ghi");
        lblStatus.setFont(new Font("Arial", Font.PLAIN, 12));
        lblStatus.setForeground(Color.WHITE);
        lblStatus.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 20));
        
        btnBack = new ModernButton("Quay Lại");
        btnBack.setPreferredSize(new Dimension(100, 35));
        btnBack.addActionListener(e -> this.dispose());
        
        headerPanel.add(lblTitle, BorderLayout.WEST);
        headerPanel.add(btnBack, BorderLayout.EAST);
        headerPanel.add(lblStatus, BorderLayout.CENTER);
        
        // Control Panel
        JPanel controlPanel = new JPanel();
        controlPanel.setBackground(Color.WHITE);
        controlPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        controlPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 5));
        
        JLabel lblReportType = new JLabel("Loại Báo Cáo:");
        lblReportType.setFont(new Font("Arial", Font.PLAIN, 12));
        cmbReportType = new JComboBox<>(new String[]{
            "Doanh Thu Bán Hàng",
            "Sản Phẩm Bán Chạy",
            "Khách Hàng Thường Xuyên",
            "Tồn Kho Sản Phẩm"
        });
        cmbReportType.setPreferredSize(new Dimension(200, 30));
        cmbReportType.addActionListener(e -> loadData());
        
        btnRefresh = new ModernButton("🔄 Tải Lại");
        btnRefresh.addActionListener(e -> loadData());
        
        btnExport = new ModernButton("📊 Xuất Excel");
        btnExport.addActionListener(e -> JOptionPane.showMessageDialog(this, "Chức năng xuất Excel đang phát triển!"));
        
        controlPanel.add(lblReportType);
        controlPanel.add(cmbReportType);
        controlPanel.add(btnRefresh);
        controlPanel.add(btnExport);
        
        // Table Panel
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBackground(Color.WHITE);
        
        tableModel = new DefaultTableModel(
            new String[]{"STT", "Dữ Liệu", "Giá Trị"},
            0
        ) { public boolean isCellEditable(int r, int c) { return false; } };
        
        tableReport = new JTable(tableModel);
        tableReport.setRowHeight(25);
        tableReport.setFont(new Font("Arial", Font.PLAIN, 12));
        tableReport.getTableHeader().setBackground(ColorTheme.SUCCESS);
        tableReport.getTableHeader().setForeground(Color.WHITE);
        tableReport.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));
        
        JScrollPane scrollPane = new JScrollPane(tableReport);
        tablePanel.add(scrollPane, BorderLayout.CENTER);
        
        // Main layout
        JPanel mainPanel = new JPanel(new BorderLayout());
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
            JOptionPane.showMessageDialog(this, "Lỗi tải báo cáo!");
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
            lblStatus.setText(count - 1 + " đơn hàng");
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
            lblStatus.setText(count - 1 + " sản phẩm");
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
            lblStatus.setText(count - 1 + " khách hàng");
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
            lblStatus.setText(count - 1 + " sản phẩm");
        }
    }
}

