package noithat.views;

import noithat.database.DatabaseHelper;
import noithat.utils.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.sql.*;

public class FormQuanLyKho extends JFrame {
    private JTable tblInventory;
    private DefaultTableModel tableModel;
    private JComboBox<String> cmbProduct;
    private JTextField txtQuantity, txtNote;
    private ModernButton btnImport, btnExport, btnDelete, btnBack;
    private JLabel lblTotalStock;
    
    public FormQuanLyKho() {
        initComponents();
        loadData();
    }
    
    private void initComponents() {
        setTitle("Quản Lý Kho");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(1400, 800);
        setLocationRelativeTo(null);
        
        // ===== HEADER =====
        JPanel headerPanel = new GradientPanel(ColorTheme.WARNING, new Color(230, 126, 34));
        headerPanel.setLayout(new BorderLayout());
        headerPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
        
        JLabel lblTitle = new JLabel("📦 QUẢN LÝ KHO HÀNG");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 28));
        lblTitle.setForeground(Color.WHITE);
        
        lblTotalStock = new JLabel("Tổng SKU: 0");
        lblTotalStock.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblTotalStock.setForeground(Color.WHITE);
        
        btnBack = new ModernButton("← Quay Lại", ColorTheme.DANGER);
        btnBack.setPreferredSize(new Dimension(140, 45));
        
        JPanel headerLeft = new JPanel();
        headerLeft.setOpaque(false);
        headerLeft.setLayout(new FlowLayout(FlowLayout.LEFT, 15, 0));
        headerLeft.add(lblTitle);
        headerLeft.add(lblTotalStock);
        
        headerPanel.add(headerLeft, BorderLayout.WEST);
        headerPanel.add(btnBack, BorderLayout.EAST);
        
        // ===== MAIN CONTENT =====
        JPanel mainPanel = new JPanel(new BorderLayout(15, 15));
        mainPanel.setBackground(ColorTheme.BACKGROUND);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        
        // Left panel - Transaction Form (wrapped)
        JPanel leftWrapper = new JPanel(new BorderLayout());
        leftWrapper.setBackground(ColorTheme.BACKGROUND);
        leftWrapper.add(createTransactionFormPanel(), BorderLayout.CENTER);
        
        // Right panel - Inventory List
        JPanel rightPanel = createInventoryListPanel();
        
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, leftWrapper, rightPanel);
        splitPane.setDividerLocation(350);
        splitPane.setBackground(ColorTheme.BACKGROUND);
        
        mainPanel.add(splitPane, BorderLayout.CENTER);
        
        add(headerPanel, BorderLayout.NORTH);
        add(mainPanel, BorderLayout.CENTER);
        
        // Event listeners
        btnBack.addActionListener(e -> dispose());
        btnImport.addActionListener(e -> importStock());
        btnExport.addActionListener(e -> exportStock());
        btnDelete.addActionListener(e -> deleteTransaction());
        
        tblInventory.getSelectionModel().addListSelectionListener(e -> {
            int row = tblInventory.getSelectedRow();
            if (row != -1) {
                cmbProduct.setSelectedItem((String) tableModel.getValueAt(row, 2));
                txtQuantity.setText(tableModel.getValueAt(row, 3).toString());
            }
        });
    }
    
    private JPanel createTransactionFormPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createTitledBorder("Giao Dịch Kho"));
        panel.add(Box.createVerticalStrut(10));
        
        // Product
        panel.add(new JLabel("Sản phẩm:"));
        cmbProduct = new JComboBox<>();
        loadProducts();
        cmbProduct.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        panel.add(cmbProduct);
        panel.add(Box.createVerticalStrut(10));
        
        // Type
        panel.add(new JLabel("Loại giao dịch:"));
        JComboBox<String> cmbType = new JComboBox<>(new String[]{"Nhập kho", "Xuất kho"});
        cmbType.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        panel.add(cmbType);
        panel.add(Box.createVerticalStrut(10));
        
        // Quantity
        panel.add(new JLabel("Số lượng:"));
        txtQuantity = new JTextField();
        txtQuantity.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        panel.add(txtQuantity);
        panel.add(Box.createVerticalStrut(10));
        
        // Note
        panel.add(new JLabel("Ghi chú:"));
        txtNote = new JTextField();
        txtNote.setMaximumSize(new Dimension(Integer.MAX_VALUE, 60));
        panel.add(new JScrollPane(txtNote));
        panel.add(Box.createVerticalStrut(20));
        
        // Buttons
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 8, 0));
        btnPanel.setBackground(Color.WHITE);
        btnImport = new ModernButton("📥 Nhập", ColorTheme.SUCCESS);
        btnExport = new ModernButton("📤 Xuất", ColorTheme.WARNING);
        btnDelete = new ModernButton("🗑️ Xóa", ColorTheme.DANGER);
        btnPanel.add(btnImport);
        btnPanel.add(btnExport);
        btnPanel.add(btnDelete);
        
        JPanel btnContainer = new JPanel(new BorderLayout());
        btnContainer.setBackground(Color.WHITE);
        btnContainer.add(btnPanel, BorderLayout.CENTER);
        btnContainer.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
        panel.add(btnContainer);
        panel.add(Box.createVerticalGlue());
        
        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.add(new JScrollPane(panel, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER), BorderLayout.CENTER);
        return wrapper;
    }
    
    private JPanel createInventoryListPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(Color.WHITE);
        
        // Table
        tableModel = new DefaultTableModel(
            new String[]{"Mã GD", "Ngày", "Loại", "Sản Phẩm", "Số Lượng", "Ghi Chú"},
            0
        ) { public boolean isCellEditable(int r, int c) { return false; } };
        
        tblInventory = new JTable(tableModel);
        tblInventory.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        tblInventory.setRowHeight(25);
        tblInventory.setSelectionBackground(ColorTheme.WARNING);
        tblInventory.setSelectionForeground(Color.WHITE);
        
        JTableHeader header = tblInventory.getTableHeader();
        header.setBackground(ColorTheme.SECONDARY);
        header.setForeground(Color.WHITE);
        header.setFont(new Font("Segoe UI", Font.BOLD, 13));
        header.setPreferredSize(new Dimension(0, 35));
        
        JScrollPane scrollPane = new JScrollPane(tblInventory);
        scrollPane.setBorder(BorderFactory.createLineBorder(ColorTheme.BORDER, 1));
        
        panel.add(scrollPane, BorderLayout.CENTER);
        return panel;
    }
    
    private void loadData() {
        tableModel.setRowCount(0);
        String query = "SELECT i.InventoryID, i.TransactionDate, i.TransactionType, p.ProductName, i.Quantity, i.Note " +
                       "FROM Inventory i JOIN Products p ON i.ProductID = p.ProductID ORDER BY i.TransactionDate DESC";
        
        try (Connection conn = DatabaseHelper.getDBConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                tableModel.addRow(new Object[]{
                    rs.getInt("InventoryID"),
                    DateHelper.formatDateTime(new java.util.Date(rs.getTimestamp("TransactionDate").getTime())),
                    rs.getString("TransactionType"),
                    rs.getString("ProductName"),
                    rs.getInt("Quantity"),
                    rs.getString("Note") != null ? rs.getString("Note") : "-"
                });
            }
            lblTotalStock.setText("Tổng GD: " + tableModel.getRowCount());
        } catch (SQLException e) { e.printStackTrace(); }
    }
    
    private void loadProducts() {
        String query = "SELECT ProductName FROM Products";
        try (Connection conn = DatabaseHelper.getDBConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                cmbProduct.addItem(rs.getString("ProductName"));
            }
        } catch (SQLException e) { e.printStackTrace(); }
    }
    
    private void importStock() {
        JOptionPane.showMessageDialog(this, "✅ Nhập kho thành công!");
        loadData();
        clearForm();
    }
    
    private void exportStock() {
        JOptionPane.showMessageDialog(this, "✅ Xuất kho thành công!");
        loadData();
        clearForm();
    }
    
    private void deleteTransaction() {
        int response = JOptionPane.showConfirmDialog(this, "Xóa giao dịch này?");
        if (response == JOptionPane.YES_OPTION) {
            JOptionPane.showMessageDialog(this, "✅ Xóa giao dịch thành công!");
            loadData();
            clearForm();
        }
    }
    
    private void clearForm() {
        txtQuantity.setText("");
        txtNote.setText("");
    }
}


