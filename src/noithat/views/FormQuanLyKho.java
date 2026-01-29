package noithat.views;

import noithat.database.DatabaseHelper;
import noithat.utils.*;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class FormQuanLyKho extends JFrame {
    private ModernTable tableInventory;
    private DefaultTableModel tableModel;
    private SearchField txtSearch;
    private ToolbarButton btnImport, btnExport, btnDelete;
    private JLabel lblStatus;
    
    public FormQuanLyKho() {
        initComponents();
        loadData();
        applyPermissions();
    }
    
    private void initComponents() {
        setTitle("Quản Lý Kho");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(1300, 750);
        setMinimumSize(new Dimension(1100, 650));
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
        
        JLabel title = new JLabel("Quản Lý Kho Hàng");
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
        
        btnImport = new ToolbarButton("Nhập Kho", ProfessionalColors.SUCCESS);
        btnImport.addActionListener(e -> showImportDialog());
        
        btnExport = new ToolbarButton("Xuất Kho", ProfessionalColors.WARNING);
        btnExport.addActionListener(e -> showExportDialog());
        
        btnDelete = new ToolbarButton("Xóa", ProfessionalColors.DANGER);
        btnDelete.addActionListener(e -> deleteTransaction());
        
        toolbar.add(btnBack);
        toolbar.add(Box.createHorizontalStrut(12));
        toolbar.add(btnImport);
        toolbar.add(btnExport);
        toolbar.add(btnDelete);
        
        toolbar.add(Box.createHorizontalGlue());
        
        txtSearch = new SearchField("Tìm kiếm giao dịch...", 300);
        txtSearch.addActionListener(e -> searchInventory());
        
        toolbar.add(txtSearch);
        return toolbar;
    }
    
    private JPanel createTablePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(new LineBorder(ProfessionalColors.BORDER, 1));
        
        tableModel = new DefaultTableModel(
            new String[]{"ID", "Ngày", "Loại", "Sản Phẩm", "Số Lượng", "Ghi Chú"}, 0
        ) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        
        tableInventory = new ModernTable(tableModel);
        tableInventory.setRowHeight(40);
        tableInventory.setShowVerticalLines(false);
        tableInventory.setSelectionBackground(ProfessionalColors.TABLE_SELECTED);
        tableInventory.getTableHeader().setBackground(ProfessionalColors.TABLE_HEADER);
        tableInventory.getTableHeader().setForeground(ProfessionalColors.TEXT_PRIMARY);
        
        tableInventory.getColumnModel().getColumn(0).setPreferredWidth(60);
        tableInventory.getColumnModel().getColumn(0).setMaxWidth(80);
        
        JScrollPane scrollPane = new JScrollPane(tableInventory);
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
        String query = "SELECT InventoryID, TransactionDate, TransactionType, ProductName, Quantity, Note FROM Inventory i " +
                       "JOIN Products p ON i.ProductID = p.ProductID ORDER BY InventoryID DESC";
        
        try (Connection conn = DatabaseHelper.getDBConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                tableModel.addRow(new Object[]{
                    rs.getInt("InventoryID"),
                    rs.getString("TransactionDate"),
                    rs.getString("TransactionType"),
                    rs.getString("ProductName"),
                    rs.getInt("Quantity"),
                    rs.getString("Note")
                });
            }
            lblStatus.setText(tableModel.getRowCount() + " giao dịch");
        } catch (SQLException e) { e.printStackTrace(); }
    }
    
    private void searchInventory() {
        String keyword = txtSearch.getText().trim();
        if (keyword.isEmpty()) { loadData(); return; }
        
        tableModel.setRowCount(0);
        String query = "SELECT InventoryID, TransactionDate, TransactionType, ProductName, Quantity, Note FROM Inventory i " +
                       "JOIN Products p ON i.ProductID = p.ProductID WHERE ProductName LIKE ? ORDER BY InventoryID DESC";
        
        try (Connection conn = DatabaseHelper.getDBConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, "%" + keyword + "%");
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                tableModel.addRow(new Object[]{
                    rs.getInt("InventoryID"), rs.getString("TransactionDate"), rs.getString("TransactionType"),
                    rs.getString("ProductName"), rs.getInt("Quantity"), rs.getString("Note")
                });
            }
            lblStatus.setText("Tìm thấy " + tableModel.getRowCount() + " giao dịch");
        } catch (SQLException e) { e.printStackTrace(); }
    }
    
    private void showImportDialog() {
        JDialog dialog = new JDialog(this, "Nhập Kho", true);
        dialog.setSize(500, 300);
        dialog.setLocationRelativeTo(this);
        
        JComboBox<String> cmbProduct = new JComboBox<>();
        JTextField txtQuantity = new JTextField();
        JTextArea txtNotes = new JTextArea(3, 20);
        txtNotes.setLineWrap(true);
        
        java.util.Map<String, Integer> productMap = new java.util.HashMap<>();
        try (Connection conn = DatabaseHelper.getDBConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT ProductID, ProductName FROM Products")) {
            while (rs.next()) {
                String name = rs.getString("ProductName");
                productMap.put(name, rs.getInt("ProductID"));
                cmbProduct.addItem(name);
            }
        } catch (SQLException e) { e.printStackTrace(); }
        
        JPanel form = new JPanel();
        form.setLayout(new BoxLayout(form, BoxLayout.Y_AXIS));
        form.setBorder(new EmptyBorder(16, 16, 16, 16));
        form.setBackground(Color.WHITE);
        
        form.add(createFormRow("Sản Phẩm:", cmbProduct));
        form.add(Box.createVerticalStrut(12));
        form.add(createFormRow("Số Lượng:", txtQuantity));
        form.add(Box.createVerticalStrut(12));
        form.add(createFormRow("Ghi Chú:", new JScrollPane(txtNotes)));
        
        JPanel buttonBar = createDialogButtons(dialog, () -> {
            try {
                int quantity = Integer.parseInt(txtQuantity.getText().trim());
                if (quantity <= 0) {
                    ToastNotification.show(dialog, "Số lượng phải > 0!", ToastNotification.WARNING);
                    return;
                }
                
                Integer productId = productMap.get(cmbProduct.getSelectedItem());
                Connection conn = DatabaseHelper.getDBConnection();
                conn.setAutoCommit(false);
                try {
                    PreparedStatement pstmt = conn.prepareStatement(
                        "INSERT INTO Inventory (ProductID, TransactionType, Quantity, TransactionDate, Notes) VALUES (?, 'IN', ?, GETDATE(), ?)");
                    pstmt.setInt(1, productId);
                    pstmt.setInt(2, quantity);
                    pstmt.setString(3, txtNotes.getText().trim());
                    pstmt.executeUpdate();
                    
                    PreparedStatement pstmt2 = conn.prepareStatement("UPDATE Products SET Stock = Stock + ? WHERE ProductID = ?");
                    pstmt2.setInt(1, quantity);
                    pstmt2.setInt(2, productId);
                    pstmt2.executeUpdate();
                    
                    conn.commit();
                    ToastNotification.show(this, "✅ Nhập kho thành công!", ToastNotification.SUCCESS);
                    dialog.dispose();
                    loadData();
                } catch (SQLException ex) {
                    conn.rollback();
                    throw ex;
                } finally {
                    conn.setAutoCommit(true);
                    conn.close();
                }
            } catch (Exception ex) {
                ToastNotification.show(dialog, "Lỗi: " + ex.getMessage(), ToastNotification.ERROR);
            }
        });
        
        dialog.add(form, BorderLayout.CENTER);
        dialog.add(buttonBar, BorderLayout.SOUTH);
        dialog.setVisible(true);
    }
    
    private void showExportDialog() {
        JDialog dialog = new JDialog(this, "Xuất Kho", true);
        dialog.setSize(500, 300);
        dialog.setLocationRelativeTo(this);
        
        JComboBox<String> cmbProduct = new JComboBox<>();
        JTextField txtQuantity = new JTextField();
        JTextArea txtNotes = new JTextArea(3, 20);
        txtNotes.setLineWrap(true);
        
        java.util.Map<String, Integer> productMap = new java.util.HashMap<>();
        java.util.Map<String, Integer> stockMap = new java.util.HashMap<>();
        try (Connection conn = DatabaseHelper.getDBConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT ProductID, ProductName, Stock FROM Products")) {
            while (rs.next()) {
                String name = rs.getString("ProductName");
                int stock = rs.getInt("Stock");
                String displayName = name + " (Tồn: " + stock + ")";
                productMap.put(displayName, rs.getInt("ProductID"));
                stockMap.put(displayName, stock);
                cmbProduct.addItem(displayName);
            }
        } catch (SQLException e) { e.printStackTrace(); }
        
        JPanel form = new JPanel();
        form.setLayout(new BoxLayout(form, BoxLayout.Y_AXIS));
        form.setBorder(new EmptyBorder(16, 16, 16, 16));
        form.setBackground(Color.WHITE);
        
        form.add(createFormRow("Sản Phẩm:", cmbProduct));
        form.add(Box.createVerticalStrut(12));
        form.add(createFormRow("Số Lượng:", txtQuantity));
        form.add(Box.createVerticalStrut(12));
        form.add(createFormRow("Ghi Chú:", new JScrollPane(txtNotes)));
        
        JPanel buttonBar = createDialogButtons(dialog, () -> {
            try {
                int quantity = Integer.parseInt(txtQuantity.getText().trim());
                String selected = (String) cmbProduct.getSelectedItem();
                
                if (quantity <= 0) {
                    ToastNotification.show(dialog, "Số lượng phải > 0!", ToastNotification.WARNING);
                    return;
                }
                if (stockMap.get(selected) < quantity) {
                    ToastNotification.show(dialog, "Vượt quá tồn kho!", ToastNotification.WARNING);
                    return;
                }
                
                Integer productId = productMap.get(selected);
                Connection conn = DatabaseHelper.getDBConnection();
                conn.setAutoCommit(false);
                try {
                    PreparedStatement pstmt = conn.prepareStatement(
                        "INSERT INTO Inventory (ProductID, TransactionType, Quantity, TransactionDate, Notes) VALUES (?, 'OUT', ?, GETDATE(), ?)");
                    pstmt.setInt(1, productId);
                    pstmt.setInt(2, quantity);
                    pstmt.setString(3, txtNotes.getText().trim());
                    pstmt.executeUpdate();
                    
                    PreparedStatement pstmt2 = conn.prepareStatement("UPDATE Products SET Stock = Stock - ? WHERE ProductID = ?");
                    pstmt2.setInt(1, quantity);
                    pstmt2.setInt(2, productId);
                    pstmt2.executeUpdate();
                    
                    conn.commit();
                    ToastNotification.show(this, "✅ Xuất kho thành công!", ToastNotification.SUCCESS);
                    dialog.dispose();
                    loadData();
                } catch (SQLException ex) {
                    conn.rollback();
                    throw ex;
                } finally {
                    conn.setAutoCommit(true);
                    conn.close();
                }
            } catch (Exception ex) {
                ToastNotification.show(dialog, "Lỗi: " + ex.getMessage(), ToastNotification.ERROR);
            }
        });
        
        dialog.add(form, BorderLayout.CENTER);
        dialog.add(buttonBar, BorderLayout.SOUTH);
        dialog.setVisible(true);
    }
    
    private void deleteTransaction() {
        int row = tableInventory.getSelectedRow();
        if (row == -1) {
            ToastNotification.show(this, "Vui lòng chọn giao dịch!", ToastNotification.WARNING);
            return;
        }
        
        int confirm = JOptionPane.showConfirmDialog(this, "Xóa giao dịch sẽ hoàn tác tồn kho. Xác nhận?",
            "Xóa", JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                int inventoryId = (int) tableModel.getValueAt(row, 0);
                Connection conn = DatabaseHelper.getDBConnection();
                PreparedStatement pstmt = conn.prepareStatement(
                    "SELECT ProductID, TransactionType, Quantity FROM Inventory WHERE InventoryID = ?");
                pstmt.setInt(1, inventoryId);
                ResultSet rs = pstmt.executeQuery();
                
                if (rs.next()) {
                    int productId = rs.getInt("ProductID");
                    String type = rs.getString("TransactionType");
                    int qty = rs.getInt("Quantity");
                    
                    conn.setAutoCommit(false);
                    try {
                        PreparedStatement del = conn.prepareStatement("DELETE FROM Inventory WHERE InventoryID = ?");
                        del.setInt(1, inventoryId);
                        del.executeUpdate();
                        
                        String updateQuery = type.equals("IN") ? 
                            "UPDATE Products SET Stock = Stock - ? WHERE ProductID = ?" :
                            "UPDATE Products SET Stock = Stock + ? WHERE ProductID = ?";
                        PreparedStatement upd = conn.prepareStatement(updateQuery);
                        upd.setInt(1, qty);
                        upd.setInt(2, productId);
                        upd.executeUpdate();
                        
                        conn.commit();
                        ToastNotification.show(this, "✅ Xóa thành công!", ToastNotification.SUCCESS);
                        loadData();
                    } catch (SQLException ex) {
                        conn.rollback();
                        throw ex;
                    } finally {
                        conn.setAutoCommit(true);
                        conn.close();
                    }
                }
            } catch (Exception e) {
                ToastNotification.show(this, "Lỗi: " + e.getMessage(), ToastNotification.ERROR);
            }
        }
    }
    
    private JPanel createFormRow(String label, JComponent field) {
        JPanel row = new JPanel(new BorderLayout(12, 0));
        row.setBackground(Color.WHITE);
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, field instanceof JScrollPane ? 80 : 32));
        
        JLabel lbl = new JLabel(label);
        lbl.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lbl.setPreferredSize(new Dimension(100, 32));
        
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

    private void applyPermissions() {
        String role = SessionManager.getInstance().getRole();

        if (!PermissionService.canView(role, "INVENTORY")) {
            JOptionPane.showMessageDialog(this,
                "BAN KHONG CO QUYEN TRUY CAP CHUC NANG NAY!",
                "Tu choi truy cap",
                JOptionPane.ERROR_MESSAGE);
            dispose();
            return;
        }

        if (!PermissionService.canCreate(role, "INVENTORY")) {
            btnImport.setEnabled(false);
            btnImport.setToolTipText("Ban khong co quyen nhap kho");
            btnExport.setEnabled(false);
            btnExport.setToolTipText("Ban khong co quyen xuat kho");
        }

        if (!PermissionService.canDelete(role, "INVENTORY")) {
            btnDelete.setEnabled(false);
            btnDelete.setToolTipText("Ban khong co quyen xoa giao dich");
        }
    }
}
