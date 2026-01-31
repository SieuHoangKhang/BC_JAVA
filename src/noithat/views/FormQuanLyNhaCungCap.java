package noithat.views;

import noithat.database.DatabaseHelper;
import noithat.utils.*;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class FormQuanLyNhaCungCap extends JFrame {
    private ModernTable tableSuppliers;
    private DefaultTableModel tableModel;
    private SearchField txtSearch;
    private ToolbarButton btnAdd, btnEdit, btnDelete, btnReload;
    private JLabel lblStatus;
    private int selectedSupplierId = -1;
    
    public FormQuanLyNhaCungCap() {
        initComponents();
        loadData();
        applyPermissions();
    }
    
    private void initComponents() {
        setTitle("Quản Lý Nhà Cung Cấp");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(1200, 700);
        setMinimumSize(new Dimension(1000, 600));
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
        
        JLabel title = new JLabel("Quản Lý Nhà Cung Cấp");
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
        
        ToolbarButton btnBack = new ToolbarButton("Quay Lai", ProfessionalColors.DANGER);
        btnBack.setPreferredSize(new Dimension(120, 36));
        btnBack.addActionListener(e -> dispose());
        
        btnAdd = new ToolbarButton("+ Thêm", ProfessionalColors.SUCCESS);
        btnAdd.addActionListener(e -> showAddDialog());
        
        btnEdit = new ToolbarButton("Sửa", ProfessionalColors.PRIMARY);
        btnEdit.addActionListener(e -> showEditDialog());
        
        btnDelete = new ToolbarButton("Xóa", ProfessionalColors.DANGER);
        btnDelete.addActionListener(e -> deleteSupplier());
        
        btnReload = new ToolbarButton("Làm Mới", ProfessionalColors.INFO);
        btnReload.setPreferredSize(new Dimension(120, 36));
        btnReload.addActionListener(e -> reloadData());
        
        toolbar.add(btnBack);
        toolbar.add(Box.createHorizontalStrut(12));
        toolbar.add(btnAdd);
        toolbar.add(btnEdit);
        toolbar.add(btnDelete);
        toolbar.add(Box.createHorizontalStrut(8));
        toolbar.add(btnReload);
        
        toolbar.add(Box.createHorizontalGlue());
        
        txtSearch = new SearchField("Tìm kiếm nhà cung cấp...", 300);
        txtSearch.addActionListener(e -> searchSuppliers());
        
        toolbar.add(txtSearch);
        return toolbar;
    }
    
    private JPanel createTablePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(new LineBorder(ProfessionalColors.BORDER, 1));
        
        tableModel = new DefaultTableModel(
            new String[]{"ID", "Tên NCC", "Người Liên Hệ", "Điện Thoại", "Email"}, 0
        ) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        
        tableSuppliers = new ModernTable(tableModel);
        tableSuppliers.setRowHeight(40);
        tableSuppliers.setShowVerticalLines(false);
        tableSuppliers.setSelectionBackground(ProfessionalColors.TABLE_SELECTED);
        tableSuppliers.getTableHeader().setBackground(ProfessionalColors.TABLE_HEADER);
        tableSuppliers.getTableHeader().setForeground(ProfessionalColors.TEXT_PRIMARY);
        
        tableSuppliers.getColumnModel().getColumn(0).setPreferredWidth(60);
        tableSuppliers.getColumnModel().getColumn(0).setMaxWidth(80);
        
        tableSuppliers.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int row = tableSuppliers.getSelectedRow();
                selectedSupplierId = row != -1 ? (int) tableModel.getValueAt(row, 0) : -1;
            }
        });
        
        JScrollPane scrollPane = new JScrollPane(tableSuppliers);
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
        String query = "SELECT SupplierID, SupplierName, ContactPerson, Phone, Email FROM Suppliers ORDER BY SupplierID DESC";
        
        try (Connection conn = DatabaseHelper.getDBConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                tableModel.addRow(new Object[]{
                    rs.getInt("SupplierID"),
                    rs.getString("SupplierName"),
                    rs.getString("ContactPerson"),
                    rs.getString("Phone"),
                    rs.getString("Email")
                });
            }
            lblStatus.setText(tableModel.getRowCount() + " nhà cung cấp");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    private void reloadData() {
        txtSearch.setText("");
        loadData();
        selectedSupplierId = -1;
        tableSuppliers.clearSelection();
        ToastNotification.show(this, "Đã làm mới dữ liệu!", ToastNotification.SUCCESS);
    }
    
    private void searchSuppliers() {
        String keyword = txtSearch.getText().trim();
        if (keyword.isEmpty()) { loadData(); return; }
        
        tableModel.setRowCount(0);
        String query = "SELECT SupplierID, SupplierName, ContactPerson, Phone, Email FROM Suppliers WHERE SupplierName LIKE ?";
        
        try (Connection conn = DatabaseHelper.getDBConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, "%" + keyword + "%");
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                tableModel.addRow(new Object[]{
                    rs.getInt("SupplierID"), rs.getString("SupplierName"),
                    rs.getString("ContactPerson"), rs.getString("Phone"), rs.getString("Email")
                });
            }
            lblStatus.setText("Tìm thấy " + tableModel.getRowCount() + " nhà cung cấp");
        } catch (SQLException e) { e.printStackTrace(); }
    }
    
    private void showAddDialog() {
        JDialog dialog = new JDialog(this, "Thêm Nhà Cung Cấp", true);
        dialog.setSize(500, 350);
        dialog.setLocationRelativeTo(this);
        
        JTextField txtName = new JTextField();
        JTextField txtContact = new JTextField();
        JTextField txtPhone = new JTextField();
        JTextField txtEmail = new JTextField();
        
        JPanel form = new JPanel();
        form.setLayout(new BoxLayout(form, BoxLayout.Y_AXIS));
        form.setBorder(new EmptyBorder(16, 16, 16, 16));
        form.setBackground(Color.WHITE);
        
        form.add(createFormRow("Tên NCC:", txtName));
        form.add(Box.createVerticalStrut(12));
        form.add(createFormRow("Người Liên Hệ:", txtContact));
        form.add(Box.createVerticalStrut(12));
        form.add(createFormRow("Điện Thoại:", txtPhone));
        form.add(Box.createVerticalStrut(12));
        form.add(createFormRow("Email:", txtEmail));
        
        JPanel buttonBar = createDialogButtons(dialog, () -> {
            if (txtName.getText().trim().isEmpty() || txtContact.getText().trim().isEmpty()) {
                ToastNotification.show(dialog, "Vui lòng nhập đủ thông tin!", ToastNotification.WARNING);
                return;
            }
            
            String query = "INSERT INTO Suppliers (SupplierName, ContactPerson, Phone, Email) VALUES (?, ?, ?, ?)";
            if (DatabaseHelper.executeUpdate(query, txtName.getText().trim(), txtContact.getText().trim(),
                txtPhone.getText().trim(), txtEmail.getText().trim())) {
                ToastNotification.show(this, "✅ Thêm thành công!", ToastNotification.SUCCESS);
                dialog.dispose();
                loadData();
            }
        });
        
        dialog.add(form, BorderLayout.CENTER);
        dialog.add(buttonBar, BorderLayout.SOUTH);
        dialog.setVisible(true);
    }
    
    private void showEditDialog() {
        if (selectedSupplierId == -1) {
            ToastNotification.show(this, "Vui lòng chọn nhà cung cấp!", ToastNotification.WARNING);
            return;
        }
        
        String query = "SELECT SupplierName, ContactPerson, Phone, Email FROM Suppliers WHERE SupplierID = ?";
        try (Connection conn = DatabaseHelper.getDBConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, selectedSupplierId);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                JDialog dialog = new JDialog(this, "Sửa Nhà Cung Cấp", true);
                dialog.setSize(500, 350);
                dialog.setLocationRelativeTo(this);
                
                JTextField txtName = new JTextField(rs.getString("SupplierName"));
                JTextField txtContact = new JTextField(rs.getString("ContactPerson"));
                JTextField txtPhone = new JTextField(rs.getString("Phone"));
                JTextField txtEmail = new JTextField(rs.getString("Email"));
                
                JPanel form = new JPanel();
                form.setLayout(new BoxLayout(form, BoxLayout.Y_AXIS));
                form.setBorder(new EmptyBorder(16, 16, 16, 16));
                form.setBackground(Color.WHITE);
                
                form.add(createFormRow("Tên NCC:", txtName));
                form.add(Box.createVerticalStrut(12));
                form.add(createFormRow("Người Liên Hệ:", txtContact));
                form.add(Box.createVerticalStrut(12));
                form.add(createFormRow("Điện Thoại:", txtPhone));
                form.add(Box.createVerticalStrut(12));
                form.add(createFormRow("Email:", txtEmail));
                
                JPanel buttonBar = createDialogButtons(dialog, () -> {
                    String updateQuery = "UPDATE Suppliers SET SupplierName = ?, ContactPerson = ?, Phone = ?, Email = ? WHERE SupplierID = ?";
                    if (DatabaseHelper.executeUpdate(updateQuery, txtName.getText().trim(), txtContact.getText().trim(),
                        txtPhone.getText().trim(), txtEmail.getText().trim(), selectedSupplierId)) {
                        ToastNotification.show(this, "✅ Cập nhật thành công!", ToastNotification.SUCCESS);
                        dialog.dispose();
                        loadData();
                    }
                });
                
                dialog.add(form, BorderLayout.CENTER);
                dialog.add(buttonBar, BorderLayout.SOUTH);
                dialog.setVisible(true);
            }
        } catch (SQLException e) { e.printStackTrace(); }
    }
    
    private void deleteSupplier() {
        if (selectedSupplierId == -1) {
            ToastNotification.show(this, "Vui lòng chọn nhà cung cấp!", ToastNotification.WARNING);
            return;
        }
        
        int confirm = JOptionPane.showConfirmDialog(this, "Xác nhận xóa?", "Xóa NCC", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            if (DatabaseHelper.executeUpdate("DELETE FROM Suppliers WHERE SupplierID = ?", selectedSupplierId)) {
                ToastNotification.show(this, "✅ Xóa thành công!", ToastNotification.SUCCESS);
                selectedSupplierId = -1;
                loadData();
            }
        }
    }
    
    private JPanel createFormRow(String label, JComponent field) {
        JPanel row = new JPanel(new BorderLayout(12, 0));
        row.setBackground(Color.WHITE);
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 32));
        
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

    private void applyPermissions() {
        String role = SessionManager.getInstance().getRole();

        if (!PermissionService.canView(role, "SUPPLIER")) {
            JOptionPane.showMessageDialog(this,
                "BAN KHONG CO QUYEN TRUY CAP CHUC NANG NAY!",
                "Tu choi truy cap",
                JOptionPane.ERROR_MESSAGE);
            dispose();
            return;
        }

        if (!PermissionService.canCreate(role, "SUPPLIER")) {
            btnAdd.setEnabled(false);
            btnAdd.setToolTipText("Ban khong co quyen them NCC");
        }

        if (!PermissionService.canUpdate(role, "SUPPLIER")) {
            btnEdit.setEnabled(false);
            btnEdit.setToolTipText("Ban khong co quyen sua NCC");
        }

        if (!PermissionService.canDelete(role, "SUPPLIER")) {
            btnDelete.setEnabled(false);
            btnDelete.setToolTipText("Ban khong co quyen xoa NCC");
        }
    }
}
