package noithat.views;

import noithat.database.DatabaseHelper;
import noithat.utils.*;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class FormQuanLySanPham extends JFrame {
    private ModernTable tblProducts;
    private DefaultTableModel tableModel;
    private SearchField txtSearch;
    private ToolbarButton btnAdd, btnEdit, btnDelete;
    private JLabel lblStatus;
    private int selectedProductId = -1;
    
    public FormQuanLySanPham() {
        initComponents();
        loadData();
        applyPermissions(); // Apply role-based permissions
    }
    
    private void initComponents() {
        setTitle("Quản Lý Sản Phẩm");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(1400, 800);
        setMinimumSize(new Dimension(1200, 700));
        setLocationRelativeTo(null);
        setBackground(ProfessionalColors.BACKGROUND);
        
        JPanel header = createHeader();
        JPanel toolbar = createToolbar();
        JPanel tablePanel = createTablePanel();
        JPanel statusBar = createStatusBar();
        
        setLayout(new BorderLayout());
        add(header, BorderLayout.NORTH);
        add(toolbar, BorderLayout.BEFORE_FIRST_LINE);
        
        JPanel contentWrapper = new JPanel(new BorderLayout());
        contentWrapper.setBackground(ProfessionalColors.BACKGROUND);
        contentWrapper.setBorder(new EmptyBorder(0, 16, 16, 16));
        contentWrapper.add(tablePanel, BorderLayout.CENTER);
        add(contentWrapper, BorderLayout.CENTER);
        add(statusBar, BorderLayout.SOUTH);
    }
    
    private JPanel createHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(Color.WHITE);
        header.setBorder(new CompoundBorder(
            new MatteBorder(0, 0, 1, 0, ProfessionalColors.BORDER),
            new EmptyBorder(12, 20, 12, 20)
        ));
        
        JLabel title = new JLabel("Quản Lý Sản Phẩm");
        title.setFont(new Font("Segoe UI", Font.BOLD, 24));
        title.setForeground(ProfessionalColors.TEXT_PRIMARY);
        
        ToolbarButton btnClose = new ToolbarButton("X", ProfessionalColors.DANGER);
        btnClose.setPreferredSize(new Dimension(40, 36));
        btnClose.addActionListener(e -> dispose());
        
        header.add(title, BorderLayout.WEST);
        header.add(btnClose, BorderLayout.EAST);
        return header;
    }
    
    private JPanel createToolbar() {
        JPanel toolbar = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 8));
        toolbar.setBackground(Color.WHITE);
        toolbar.setBorder(new CompoundBorder(
            new MatteBorder(0, 0, 1, 0, ProfessionalColors.BORDER_LIGHT),
            new EmptyBorder(4, 16, 4, 16)
        ));
        
        ToolbarButton btnBack = new ToolbarButton("← Quay Lại", ProfessionalColors.DANGER);
        btnBack.setPreferredSize(new Dimension(120, 36));
        btnBack.addActionListener(e -> dispose());
        
        btnAdd = new ToolbarButton("+ Thêm", ProfessionalColors.SUCCESS);
        btnAdd.addActionListener(e -> showAddDialog());
        
        btnEdit = new ToolbarButton("Sửa", ProfessionalColors.PRIMARY);
        btnEdit.addActionListener(e -> showEditDialog());
        
        btnDelete = new ToolbarButton("Xóa", ProfessionalColors.DANGER);
        btnDelete.addActionListener(e -> deleteProduct());
        
        toolbar.add(btnBack);
        toolbar.add(Box.createHorizontalStrut(12));
        toolbar.add(btnAdd);
        toolbar.add(btnEdit);
        toolbar.add(btnDelete);
        
        toolbar.add(Box.createHorizontalGlue());
        
        txtSearch = new SearchField("Tìm kiếm sản phẩm...", 300);
        txtSearch.addActionListener(e -> searchProducts());
        
        toolbar.add(txtSearch);
        return toolbar;
    }
    
    private JPanel createTablePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(new LineBorder(ProfessionalColors.BORDER, 1));
        
        tableModel = new DefaultTableModel(
            new String[]{"ID", "Tên Sản Phẩm", "Danh Mục", "Giá", "Tồn Kho", "NCC"}, 0
        ) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        
        tblProducts = new ModernTable(tableModel);
        tblProducts.setRowHeight(40);
        tblProducts.setShowVerticalLines(false);
        tblProducts.setSelectionBackground(ProfessionalColors.TABLE_SELECTED);
        tblProducts.getTableHeader().setBackground(ProfessionalColors.TABLE_HEADER);
        tblProducts.getTableHeader().setForeground(ProfessionalColors.TEXT_PRIMARY);
        
        tblProducts.getColumnModel().getColumn(0).setPreferredWidth(60);
        tblProducts.getColumnModel().getColumn(0).setMaxWidth(80);
        
        tblProducts.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int row = tblProducts.getSelectedRow();
                selectedProductId = row != -1 ? (int) tableModel.getValueAt(row, 0) : -1;
            }
        });
        
        JScrollPane scrollPane = new JScrollPane(tblProducts);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        panel.add(scrollPane, BorderLayout.CENTER);
        return panel;
    }
    
    private JPanel createStatusBar() {
        JPanel statusBar = new JPanel(new BorderLayout());
        statusBar.setBackground(Color.WHITE);
        statusBar.setBorder(new CompoundBorder(
            new MatteBorder(1, 0, 0, 0, ProfessionalColors.BORDER),
            new EmptyBorder(6, 20, 6, 20)
        ));
        
        lblStatus = new JLabel("Sẵn sàng");
        lblStatus.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblStatus.setForeground(ProfessionalColors.TEXT_SECONDARY);
        
        statusBar.add(lblStatus, BorderLayout.WEST);
        return statusBar;
    }
    
    private void loadData() {
        tableModel.setRowCount(0);
        String query = "SELECT p.ProductID, p.ProductName, c.CategoryName, p.Price, p.Stock, s.SupplierName " +
                       "FROM Products p " +
                       "LEFT JOIN Categories c ON p.CategoryID = c.CategoryID " +
                       "LEFT JOIN Suppliers s ON p.SupplierID = s.SupplierID " +
                       "ORDER BY p.ProductID DESC";
        
        try (Connection conn = DatabaseHelper.getDBConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                tableModel.addRow(new Object[]{
                    rs.getInt("ProductID"),
                    rs.getString("ProductName"),
                    rs.getString("CategoryName"),
                    String.format("%,.0f đ", rs.getDouble("Price")),
                    rs.getInt("Stock"),
                    rs.getString("SupplierName")
                });
            }
            lblStatus.setText(tableModel.getRowCount() + " sản phẩm");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    private void searchProducts() {
        String keyword = txtSearch.getText().trim();
        if (keyword.isEmpty()) { loadData(); return; }
        
        tableModel.setRowCount(0);
        String query = "SELECT p.ProductID, p.ProductName, c.CategoryName, p.Price, p.Stock, s.SupplierName " +
                       "FROM Products p " +
                       "LEFT JOIN Categories c ON p.CategoryID = c.CategoryID " +
                       "LEFT JOIN Suppliers s ON p.SupplierID = s.SupplierID " +
                       "WHERE p.ProductName LIKE ?";
        
        try (Connection conn = DatabaseHelper.getDBConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, "%" + keyword + "%");
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                tableModel.addRow(new Object[]{
                    rs.getInt("ProductID"), rs.getString("ProductName"), rs.getString("CategoryName"),
                    String.format("%,.0f đ", rs.getDouble("Price")), rs.getInt("Stock"), rs.getString("SupplierName")
                });
            }
            lblStatus.setText("Tìm thấy " + tableModel.getRowCount() + " sản phẩm");
        } catch (SQLException e) { e.printStackTrace(); }
    }
    
    private void showAddDialog() {
        JDialog dialog = new JDialog(this, "Thêm Sản Phẩm", true);
        dialog.setSize(550, 450);
        dialog.setLocationRelativeTo(this);
        
        JTextField txtName = new JTextField();
        JComboBox<String> cmbCategory = new JComboBox<>();
        JComboBox<String> cmbSupplier = new JComboBox<>();
        JTextField txtPrice = new JTextField();
        JTextField txtStock = new JTextField();
        JTextArea txtDesc = new JTextArea(3, 20);
        txtDesc.setLineWrap(true);
        
        java.util.Map<String, Integer> categoryMap = new java.util.HashMap<>();
        java.util.Map<String, Integer> supplierMap = new java.util.HashMap<>();
        
        try (Connection conn = DatabaseHelper.getDBConnection()) {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT CategoryID, CategoryName FROM Categories");
            while (rs.next()) {
                String name = rs.getString("CategoryName");
                categoryMap.put(name, rs.getInt("CategoryID"));
                cmbCategory.addItem(name);
            }
            
            rs = stmt.executeQuery("SELECT SupplierID, SupplierName FROM Suppliers");
            while (rs.next()) {
                String name = rs.getString("SupplierName");
                supplierMap.put(name, rs.getInt("SupplierID"));
                cmbSupplier.addItem(name);
            }
        } catch (SQLException e) { e.printStackTrace(); }
        
        JPanel form = new JPanel();
        form.setLayout(new BoxLayout(form, BoxLayout.Y_AXIS));
        form.setBorder(new EmptyBorder(16, 16, 16, 16));
        form.setBackground(Color.WHITE);
        
        form.add(createFormRow("Tên Sản Phẩm:", txtName));
        form.add(Box.createVerticalStrut(12));
        form.add(createFormRow("Danh Mục:", cmbCategory));
        form.add(Box.createVerticalStrut(12));
        form.add(createFormRow("Nhà Cung Cấp:", cmbSupplier));
        form.add(Box.createVerticalStrut(12));
        form.add(createFormRow("Giá:", txtPrice));
        form.add(Box.createVerticalStrut(12));
        form.add(createFormRow("Tồn Kho:", txtStock));
        form.add(Box.createVerticalStrut(12));
        form.add(createFormRow("Mô Tả:", new JScrollPane(txtDesc)));
        
        JPanel buttonBar = createDialogButtons(dialog, () -> {
            try {
                String name = txtName.getText().trim();
                double price = Double.parseDouble(txtPrice.getText().trim());
                int stock = Integer.parseInt(txtStock.getText().trim());
                
                if (name.isEmpty() || price <= 0 || stock < 0) {
                    ToastNotification.show(dialog, "Dữ liệu không hợp lệ!", ToastNotification.WARNING);
                    return;
                }
                
                Integer categoryId = categoryMap.get(cmbCategory.getSelectedItem());
                Integer supplierId = supplierMap.get(cmbSupplier.getSelectedItem());
                
                String query = "INSERT INTO Products (ProductName, CategoryID, SupplierID, Price, Stock, Description) VALUES (?, ?, ?, ?, ?, ?)";
                if (DatabaseHelper.executeUpdate(query, name, categoryId, supplierId, price, stock, txtDesc.getText().trim())) {
                    ToastNotification.show(this, "✅ Thêm thành công!", ToastNotification.SUCCESS);
                    dialog.dispose();
                    loadData();
                }
            } catch (Exception ex) {
                ToastNotification.show(dialog, "Lỗi: " + ex.getMessage(), ToastNotification.ERROR);
            }
        });
        
        dialog.add(form, BorderLayout.CENTER);
        dialog.add(buttonBar, BorderLayout.SOUTH);
        dialog.setVisible(true);
    }
    
    private void showEditDialog() {
        if (selectedProductId == -1) {
            ToastNotification.show(this, "Vui lòng chọn sản phẩm!", ToastNotification.WARNING);
            return;
        }
        
        String query = "SELECT ProductName, CategoryID, SupplierID, Price, Stock, Description FROM Products WHERE ProductID = ?";
        try (Connection conn = DatabaseHelper.getDBConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, selectedProductId);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                JDialog dialog = new JDialog(this, "Sửa Sản Phẩm", true);
                dialog.setSize(550, 450);
                dialog.setLocationRelativeTo(this);
                
                JTextField txtName = new JTextField(rs.getString("ProductName"));
                JComboBox<String> cmbCategory = new JComboBox<>();
                JComboBox<String> cmbSupplier = new JComboBox<>();
                JTextField txtPrice = new JTextField(String.valueOf(rs.getDouble("Price")));
                JTextField txtStock = new JTextField(String.valueOf(rs.getInt("Stock")));
                JTextArea txtDesc = new JTextArea(rs.getString("Description"), 3, 20);
                txtDesc.setLineWrap(true);
                
                int currentCatId = rs.getInt("CategoryID");
                int currentSuppId = rs.getInt("SupplierID");
                
                java.util.Map<String, Integer> categoryMap = new java.util.HashMap<>();
                java.util.Map<String, Integer> supplierMap = new java.util.HashMap<>();
                
                Statement stmt = conn.createStatement();
                ResultSet rsC = stmt.executeQuery("SELECT CategoryID, CategoryName FROM Categories");
                while (rsC.next()) {
                    String name = rsC.getString("CategoryName");
                    int id = rsC.getInt("CategoryID");
                    categoryMap.put(name, id);
                    cmbCategory.addItem(name);
                    if (id == currentCatId) cmbCategory.setSelectedItem(name);
                }
                
                ResultSet rsS = stmt.executeQuery("SELECT SupplierID, SupplierName FROM Suppliers");
                while (rsS.next()) {
                    String name = rsS.getString("SupplierName");
                    int id = rsS.getInt("SupplierID");
                    supplierMap.put(name, id);
                    cmbSupplier.addItem(name);
                    if (id == currentSuppId) cmbSupplier.setSelectedItem(name);
                }
                
                JPanel form = new JPanel();
                form.setLayout(new BoxLayout(form, BoxLayout.Y_AXIS));
                form.setBorder(new EmptyBorder(16, 16, 16, 16));
                form.setBackground(Color.WHITE);
                
                form.add(createFormRow("Tên Sản Phẩm:", txtName));
                form.add(Box.createVerticalStrut(12));
                form.add(createFormRow("Danh Mục:", cmbCategory));
                form.add(Box.createVerticalStrut(12));
                form.add(createFormRow("Nhà Cung Cấp:", cmbSupplier));
                form.add(Box.createVerticalStrut(12));
                form.add(createFormRow("Giá:", txtPrice));
                form.add(Box.createVerticalStrut(12));
                form.add(createFormRow("Tồn Kho:", txtStock));
                form.add(Box.createVerticalStrut(12));
                form.add(createFormRow("Mô Tả:", new JScrollPane(txtDesc)));
                
                JPanel buttonBar = createDialogButtons(dialog, () -> {
                    try {
                        String name = txtName.getText().trim();
                        double price = Double.parseDouble(txtPrice.getText().trim());
                        int stock = Integer.parseInt(txtStock.getText().trim());
                        
                        Integer categoryId = categoryMap.get(cmbCategory.getSelectedItem());
                        Integer supplierId = supplierMap.get(cmbSupplier.getSelectedItem());
                        
                        String updateQuery = "UPDATE Products SET ProductName = ?, CategoryID = ?, SupplierID = ?, Price = ?, Stock = ?, Description = ? WHERE ProductID = ?";
                        if (DatabaseHelper.executeUpdate(updateQuery, name, categoryId, supplierId, price, stock, txtDesc.getText().trim(), selectedProductId)) {
                            ToastNotification.show(this, "Cập nhật thành công!", ToastNotification.SUCCESS);
                            dialog.dispose();
                            loadData();
                        }
                    } catch (Exception ex) {
                        ToastNotification.show(dialog, "Lỗi: " + ex.getMessage(), ToastNotification.ERROR);
                    }
                });
                
                dialog.add(form, BorderLayout.CENTER);
                dialog.add(buttonBar, BorderLayout.SOUTH);
                dialog.setVisible(true);
            }
        } catch (SQLException e) { e.printStackTrace(); }
    }
    
    private void deleteProduct() {
        if (selectedProductId == -1) {
            ToastNotification.show(this, "Vui lòng chọn sản phẩm!", ToastNotification.WARNING);
            return;
        }
        
        int confirm = JOptionPane.showConfirmDialog(this, "Xác nhận xóa sản phẩm?", "Xóa", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            if (DatabaseHelper.executeUpdate("DELETE FROM Products WHERE ProductID = ?", selectedProductId)) {
                ToastNotification.show(this, "Xóa thành công!", ToastNotification.SUCCESS);
                selectedProductId = -1;
                loadData();
            }
        }
    }
    
    /**
     * Apply role-based permissions to form
     */
    private void applyPermissions() {
        String role = SessionManager.getInstance().getRole();
        
        // Check VIEW permission
        if (!PermissionService.canView(role, "PRODUCT")) {
            javax.swing.JOptionPane.showMessageDialog(this,
                "BẠN KHÔNG CÓ QUYỀN TRUY CẪP CHỨC NĂNG NÀY!",
                "Từ chối truy cập",
                javax.swing.JOptionPane.ERROR_MESSAGE);
            dispose();
            return;
        }
        
        // Disable buttons based on permissions
        if (!PermissionService.canCreate(role, "PRODUCT")) {
            btnAdd.setEnabled(false);
            btnAdd.setToolTipText("Bạn không có quyền thêm sản phẩm");
        }
        
        if (!PermissionService.canUpdate(role, "PRODUCT")) {
            btnEdit.setEnabled(false);
            btnEdit.setToolTipText("Bạn không có quyền sửa sản phẩm");
        }
        
        if (!PermissionService.canDelete(role, "PRODUCT")) {
            btnDelete.setEnabled(false);
            btnDelete.setToolTipText("Bạn không có quyền xóa sản phẩm");
        }
    }
    
    private JPanel createFormRow(String label, JComponent field) {
        JPanel row = new JPanel(new BorderLayout(12, 0));
        row.setBackground(Color.WHITE);
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, field instanceof JScrollPane ? 80 : 32));
        
        JLabel lbl = new JLabel(label);
        lbl.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lbl.setPreferredSize(new Dimension(120, 32));
        
        field.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        row.add(lbl, BorderLayout.WEST);
        row.add(field, BorderLayout.CENTER);
        return row;
    }
    
    private JPanel createDialogButtons(JDialog dialog, Runnable onSave) {
        JPanel buttonBar = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 12));
        buttonBar.setBackground(ProfessionalColors.BACKGROUND);
        buttonBar.setBorder(new MatteBorder(1, 0, 0, 0, ProfessionalColors.BORDER));
        
        ToolbarButton btnCancel = new ToolbarButton("Hủy", ProfessionalColors.TEXT_SECONDARY);
        btnCancel.addActionListener(e -> dialog.dispose());
        
        ToolbarButton btnSave = new ToolbarButton("Lưu", ProfessionalColors.SUCCESS);
        btnSave.addActionListener(e -> onSave.run());
        
        buttonBar.add(btnCancel);
        buttonBar.add(btnSave);
        return buttonBar;
    }
}
