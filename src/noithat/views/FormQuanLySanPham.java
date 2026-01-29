package noithat.views;

import noithat.database.DatabaseHelper;
import noithat.utils.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.sql.*;

public class FormQuanLySanPham extends JFrame {
    private JTable tblProducts;
    private DefaultTableModel tableModel;
    private JTextField txtSearch;
    private ModernButton btnSearch, btnRefresh, btnBack;
    private JLabel lblTotalCount;
    private int selectedProductId = -1;
    
    public FormQuanLySanPham() {
        initComponents();
        loadData();
    }
    
    private void initComponents() {
        setTitle("Quản Lý Sản Phẩm");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(1400, 750);
        setLocationRelativeTo(null);
        setResizable(true);
        
        // ===== HEADER PANEL =====
        JPanel headerPanel = new GradientPanel(ColorTheme.PRIMARY, ColorTheme.SECONDARY);
        headerPanel.setLayout(new BorderLayout());
        headerPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
        
        JLabel lblTitle = new JLabel("📦 QUẢN LÝ SẢN PHẨM");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 28));
        lblTitle.setForeground(Color.WHITE);
        
        btnBack = new ModernButton("← Quay Lại", ColorTheme.DANGER);
        btnBack.setPreferredSize(new Dimension(140, 45));
        btnBack.setFont(new Font("Segoe UI", Font.BOLD, 12));
        
        lblTotalCount = new JLabel("Tổng: 0 sản phẩm");
        lblTotalCount.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblTotalCount.setForeground(Color.WHITE);
        
        JPanel headerLeft = new JPanel();
        headerLeft.setOpaque(false);
        headerLeft.setLayout(new FlowLayout(FlowLayout.LEFT, 15, 0));
        headerLeft.add(lblTitle);
        headerLeft.add(lblTotalCount);
        
        headerPanel.add(headerLeft, BorderLayout.WEST);
        headerPanel.add(btnBack, BorderLayout.EAST);
        
        // ===== SEARCH PANEL =====
        JPanel searchPanel = new JPanel();
        searchPanel.setBackground(ColorTheme.SURFACE);
        searchPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 15, 15));
        searchPanel.setBorder(BorderFactory.createEmptyBorder(0, 20, 10, 20));
        
        JLabel lblSearch = new JLabel("🔍 Tìm kiếm:");
        lblSearch.setFont(new Font("Segoe UI", Font.BOLD, 12));
        
        txtSearch = new JTextField(35);
        txtSearch.setPreferredSize(new Dimension(350, 40));
        txtSearch.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        txtSearch.setBorder(new RoundedBorder(8, ColorTheme.BORDER));
        
        btnSearch = new ModernButton("🔎 Tìm", ColorTheme.INFO);
        btnSearch.setPreferredSize(new Dimension(100, 40));
        
        btnRefresh = new ModernButton("🔄 Làm mới", ColorTheme.SUCCESS);
        btnRefresh.setPreferredSize(new Dimension(120, 40));
        
        searchPanel.add(lblSearch);
        searchPanel.add(txtSearch);
        searchPanel.add(btnSearch);
        searchPanel.add(btnRefresh);
        
        // ===== TABLE PANEL =====
        JPanel tablePanel = new JPanel();
        tablePanel.setBackground(ColorTheme.BACKGROUND);
        tablePanel.setLayout(new BorderLayout(0, 10));
        tablePanel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));
        
        tableModel = new DefaultTableModel(
            new String[]{"ID", "Tên Sản Phẩm", "Danh Mục", "Giá", "Tồn Kho", "Nhà Cung Cấp"},
            0
        ) {
            @Override
            public boolean isCellEditable(int r, int c) { return false; }
        };
        
        tblProducts = new JTable(tableModel);
        tblProducts.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        tblProducts.setRowHeight(28);
        tblProducts.setSelectionBackground(ColorTheme.PRIMARY);
        tblProducts.setSelectionForeground(Color.WHITE);
        tblProducts.setGridColor(ColorTheme.BORDER);
        tblProducts.setShowGrid(true);
        
        JTableHeader header = tblProducts.getTableHeader();
        header.setBackground(ColorTheme.SECONDARY);
        header.setForeground(Color.WHITE);
        header.setFont(new Font("Segoe UI", Font.BOLD, 13));
        header.setPreferredSize(new Dimension(0, 35));
        
        JScrollPane scrollPane = new JScrollPane(tblProducts);
        scrollPane.setBorder(BorderFactory.createLineBorder(ColorTheme.BORDER, 1));
        scrollPane.getViewport().setBackground(Color.WHITE);
        
        tablePanel.add(scrollPane, BorderLayout.CENTER);
        
        // ===== ACTION PANEL =====
        JPanel actionPanel = new JPanel();
        actionPanel.setBackground(ColorTheme.SURFACE);
        actionPanel.setLayout(new FlowLayout(FlowLayout.RIGHT, 10, 15));
        actionPanel.setBorder(BorderFactory.createEmptyBorder(0, 20, 10, 20));
        
        ModernButton btnAdd = new ModernButton("➕ Thêm Mới", ColorTheme.SUCCESS);
        btnAdd.setPreferredSize(new Dimension(130, 40));
        
        ModernButton btnEdit = new ModernButton("✏️ Chỉnh Sửa", ColorTheme.INFO);
        btnEdit.setPreferredSize(new Dimension(130, 40));
        
        ModernButton btnDelete = new ModernButton("🗑️ Xóa", ColorTheme.DANGER);
        btnDelete.setPreferredSize(new Dimension(100, 40));
        
        actionPanel.add(btnAdd);
        actionPanel.add(btnEdit);
        actionPanel.add(btnDelete);
        
        // ===== MAIN LAYOUT =====
        JPanel mainPanel = new JPanel();
        mainPanel.setBackground(ColorTheme.BACKGROUND);
        mainPanel.setLayout(new BorderLayout());
        mainPanel.add(searchPanel, BorderLayout.NORTH);
        mainPanel.add(tablePanel, BorderLayout.CENTER);
        mainPanel.add(actionPanel, BorderLayout.SOUTH);
        
        add(headerPanel, BorderLayout.NORTH);
        add(mainPanel, BorderLayout.CENTER);
        
        // ===== EVENT LISTENERS =====
        btnBack.addActionListener(e -> dispose());
        btnSearch.addActionListener(e -> searchProducts());
        btnRefresh.addActionListener(e -> loadData());
        btnAdd.addActionListener(e -> JOptionPane.showMessageDialog(this, "Mở form thêm sản phẩm"));
        btnEdit.addActionListener(e -> JOptionPane.showMessageDialog(this, "Mở form chỉnh sửa sản phẩm"));
        btnDelete.addActionListener(e -> JOptionPane.showMessageDialog(this, "Xóa sản phẩm?"));
        
        tblProducts.getSelectionModel().addListSelectionListener(e -> {
            int row = tblProducts.getSelectedRow();
            if (row != -1) {
                selectedProductId = (int) tableModel.getValueAt(row, 0);
            }
        });
    }
    
    private void loadData() {
        tableModel.setRowCount(0);
        String query = "SELECT p.ProductID, p.ProductName, c.CategoryName, p.Price, p.Stock, s.SupplierName " +
                       "FROM Products p " +
                       "JOIN Categories c ON p.CategoryID = c.CategoryID " +
                       "JOIN Suppliers s ON p.SupplierID = s.SupplierID";
        
        try (Connection conn = DatabaseHelper.getDBConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                tableModel.addRow(new Object[]{
                    rs.getInt("ProductID"),
                    rs.getString("ProductName"),
                    rs.getString("CategoryName"),
                    rs.getLong("Price"),
                    rs.getInt("Stock"),
                    rs.getString("SupplierName")
                });
            }
            lblTotalCount.setText("Tổng: " + tableModel.getRowCount() + " sản phẩm");
        } catch (SQLException e) { e.printStackTrace(); }
    }
    
    private void searchProducts() {
        String keyword = txtSearch.getText().trim();
        if (keyword.isEmpty()) {
            loadData();
            return;
        }
        
        tableModel.setRowCount(0);
        String query = "SELECT p.ProductID, p.ProductName, c.CategoryName, p.Price, p.Stock, s.SupplierName " +
                       "FROM Products p " +
                       "JOIN Categories c ON p.CategoryID = c.CategoryID " +
                       "JOIN Suppliers s ON p.SupplierID = s.SupplierID " +
                       "WHERE p.ProductName LIKE ?";
        
        try (Connection conn = DatabaseHelper.getDBConnection();
             java.sql.PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, "%" + keyword + "%");
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                tableModel.addRow(new Object[]{
                    rs.getInt("ProductID"),
                    rs.getString("ProductName"),
                    rs.getString("CategoryName"),
                    rs.getLong("Price"),
                    rs.getInt("Stock"),
                    rs.getString("SupplierName")
                });
            }
            lblTotalCount.setText("Tìm thấy: " + tableModel.getRowCount() + " sản phẩm");
        } catch (SQLException e) { e.printStackTrace(); }
    }
}


