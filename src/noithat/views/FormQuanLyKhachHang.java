package noithat.views;

import noithat.database.DatabaseHelper;
import noithat.utils.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.sql.*;

public class FormQuanLyKhachHang extends JFrame {
    private JTable tableCustomers;
    private DefaultTableModel tableModel;
    private JTextField txtSearch;
    private ModernButton btnSearch, btnRefresh, btnBack;
    private JLabel lblTotalCount;
    
    public FormQuanLyKhachHang() {
        initComponents();
        loadData();
    }
    
    private void initComponents() {
        setTitle("Quản Lý Khách Hàng");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(1300, 700);
        setLocationRelativeTo(null);
        setResizable(true);
        
        // ===== HEADER =====
        JPanel headerPanel = new GradientPanel(ColorTheme.SUCCESS, new Color(46, 204, 113));
        headerPanel.setLayout(new BorderLayout());
        headerPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
        
        JLabel lblTitle = new JLabel("👥 QUẢN LÝ KHÁCH HÀNG");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 28));
        lblTitle.setForeground(Color.WHITE);
        
        lblTotalCount = new JLabel("Tổng: 0 khách hàng");
        lblTotalCount.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblTotalCount.setForeground(Color.WHITE);
        
        btnBack = new ModernButton("← Quay Lại", ColorTheme.DANGER);
        btnBack.setPreferredSize(new Dimension(140, 45));
        
        JPanel headerLeft = new JPanel();
        headerLeft.setOpaque(false);
        headerLeft.setLayout(new FlowLayout(FlowLayout.LEFT, 15, 0));
        headerLeft.add(lblTitle);
        headerLeft.add(lblTotalCount);
        
        headerPanel.add(headerLeft, BorderLayout.WEST);
        headerPanel.add(btnBack, BorderLayout.EAST);
        
        // ===== SEARCH =====
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
        
        // ===== TABLE =====
        JPanel tablePanel = new JPanel();
        tablePanel.setBackground(ColorTheme.BACKGROUND);
        tablePanel.setLayout(new BorderLayout());
        tablePanel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));
        
        tableModel = new DefaultTableModel(
            new String[]{"ID", "Tên Khách Hàng", "Điện Thoại", "Email", "Địa Chỉ"},
            0
        ) { 
            public boolean isCellEditable(int r, int c) { return false; } 
        };
        
        tableCustomers = new JTable(tableModel);
        tableCustomers.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        tableCustomers.setRowHeight(28);
        tableCustomers.setSelectionBackground(ColorTheme.SUCCESS);
        tableCustomers.setSelectionForeground(Color.WHITE);
        tableCustomers.setGridColor(ColorTheme.BORDER);
        
        JTableHeader header = tableCustomers.getTableHeader();
        header.setBackground(ColorTheme.SECONDARY);
        header.setForeground(Color.WHITE);
        header.setFont(new Font("Segoe UI", Font.BOLD, 13));
        header.setPreferredSize(new Dimension(0, 35));
        
        JScrollPane scrollPane = new JScrollPane(tableCustomers);
        scrollPane.setBorder(BorderFactory.createLineBorder(ColorTheme.BORDER, 1));
        tablePanel.add(scrollPane, BorderLayout.CENTER);
        
        // ===== ACTIONS =====
        JPanel actionPanel = new JPanel();
        actionPanel.setBackground(ColorTheme.SURFACE);
        actionPanel.setLayout(new FlowLayout(FlowLayout.RIGHT, 10, 15));
        actionPanel.setBorder(BorderFactory.createEmptyBorder(0, 20, 10, 20));
        
        ModernButton btnAdd = new ModernButton("➕ Thêm", ColorTheme.SUCCESS);
        btnAdd.setPreferredSize(new Dimension(110, 40));
        
        ModernButton btnEdit = new ModernButton("✏️ Sửa", ColorTheme.INFO);
        btnEdit.setPreferredSize(new Dimension(100, 40));
        
        ModernButton btnDelete = new ModernButton("🗑️ Xóa", ColorTheme.DANGER);
        btnDelete.setPreferredSize(new Dimension(100, 40));
        
        actionPanel.add(btnAdd);
        actionPanel.add(btnEdit);
        actionPanel.add(btnDelete);
        
        // ===== MAIN =====
        JPanel mainPanel = new JPanel();
        mainPanel.setBackground(ColorTheme.BACKGROUND);
        mainPanel.setLayout(new BorderLayout());
        mainPanel.add(searchPanel, BorderLayout.NORTH);
        mainPanel.add(tablePanel, BorderLayout.CENTER);
        mainPanel.add(actionPanel, BorderLayout.SOUTH);
        
        add(headerPanel, BorderLayout.NORTH);
        add(mainPanel, BorderLayout.CENTER);
        
        // ===== EVENTS =====
        btnBack.addActionListener(e -> dispose());
        btnSearch.addActionListener(e -> searchCustomers());
        btnRefresh.addActionListener(e -> loadData());
        btnAdd.addActionListener(e -> handleAction("CREATE"));
        btnEdit.addActionListener(e -> handleAction("UPDATE"));
        btnDelete.addActionListener(e -> handleAction("DELETE"));
    }
    
    private void loadData() {
        tableModel.setRowCount(0);
        String query = "SELECT CustomerID, CustomerName, Phone, Email, Address FROM Customers";
        
        try (Connection conn = DatabaseHelper.getDBConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                tableModel.addRow(new Object[]{
                    rs.getInt("CustomerID"),
                    rs.getString("CustomerName"),
                    rs.getString("Phone"),
                    rs.getString("Email"),
                    rs.getString("Address")
                });
            }
            lblTotalCount.setText("Tổng: " + tableModel.getRowCount() + " khách hàng");
        } catch (SQLException e) { e.printStackTrace(); }
    }
    
    private void searchCustomers() {
        String keyword = txtSearch.getText().trim();
        tableModel.setRowCount(0);
        
        String query = "SELECT CustomerID, CustomerName, Phone, Email, Address FROM Customers " +
                       "WHERE CustomerName LIKE ? OR Phone LIKE ?";
        
        try (Connection conn = DatabaseHelper.getDBConnection();
             java.sql.PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, "%" + keyword + "%");
            pstmt.setString(2, "%" + keyword + "%");
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                tableModel.addRow(new Object[]{
                    rs.getInt("CustomerID"),
                    rs.getString("CustomerName"),
                    rs.getString("Phone"),
                    rs.getString("Email"),
                    rs.getString("Address")
                });
            }
            lblTotalCount.setText("Tìm thấy: " + tableModel.getRowCount() + " khách hàng");
        } catch (SQLException e) { e.printStackTrace(); }
    }
    
    private void handleAction(String action) {
        // Action is handled
    }
}

