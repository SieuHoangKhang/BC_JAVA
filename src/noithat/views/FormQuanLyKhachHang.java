package noithat.views;

import noithat.database.DatabaseHelper;
import noithat.utils.*;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class FormQuanLyKhachHang extends JFrame {
    private ModernTable tableCustomers;
    private DefaultTableModel tableModel;
    private SearchField txtSearch;
    private ToolbarButton btnAdd, btnEdit, btnDelete;
    private JLabel lblStatus;
    private int selectedCustomerId = -1;
    
    public FormQuanLyKhachHang() {
        initComponents();
        loadData();
        applyPermissions();
    }
    
    private void initComponents() {
        setTitle("Quản Lý Khách Hàng");
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
        
        JLabel title = new JLabel("Quản Lý Khách Hàng");
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
        btnDelete.addActionListener(e -> deleteCustomer());
        
        toolbar.add(btnBack);
        toolbar.add(Box.createHorizontalStrut(12));
        toolbar.add(btnAdd);
        toolbar.add(btnEdit);
        toolbar.add(btnDelete);
        
        toolbar.add(Box.createHorizontalGlue());
        
        txtSearch = new SearchField("Tìm kiếm khách hàng...", 300);
        txtSearch.addActionListener(e -> searchCustomers());
        
        toolbar.add(txtSearch);
        return toolbar;
    }
    
    private JPanel createTablePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(new LineBorder(ProfessionalColors.BORDER, 1));
        
        tableModel = new DefaultTableModel(
            new String[]{"ID", "Tên KH", "Điện Thoại", "Email", "Địa Chỉ"}, 0
        ) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        
        tableCustomers = new ModernTable(tableModel);
        tableCustomers.setRowHeight(40);
        tableCustomers.setShowVerticalLines(false);
        tableCustomers.setSelectionBackground(ProfessionalColors.TABLE_SELECTED);
        tableCustomers.getTableHeader().setBackground(ProfessionalColors.TABLE_HEADER);
        tableCustomers.getTableHeader().setForeground(ProfessionalColors.TEXT_PRIMARY);
        
        tableCustomers.getColumnModel().getColumn(0).setPreferredWidth(60);
        tableCustomers.getColumnModel().getColumn(0).setMaxWidth(80);
        
        tableCustomers.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int row = tableCustomers.getSelectedRow();
                selectedCustomerId = row != -1 ? (int) tableModel.getValueAt(row, 0) : -1;
            }
        });
        
        JScrollPane scrollPane = new JScrollPane(tableCustomers);
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
        String query = "SELECT CustomerID, CustomerName, Phone, Email, Address FROM Customers ORDER BY CustomerID DESC";
        
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
            lblStatus.setText(tableModel.getRowCount() + " khách hàng");
        } catch (SQLException e) { e.printStackTrace(); }
    }
    
    private void searchCustomers() {
        String keyword = txtSearch.getText().trim();
        if (keyword.isEmpty()) { loadData(); return; }
        
        tableModel.setRowCount(0);
        String query = "SELECT CustomerID, CustomerName, Phone, Email, Address FROM Customers WHERE CustomerName LIKE ? OR Phone LIKE ?";
        
        try (Connection conn = DatabaseHelper.getDBConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, "%" + keyword + "%");
            pstmt.setString(2, "%" + keyword + "%");
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                tableModel.addRow(new Object[]{
                    rs.getInt("CustomerID"), rs.getString("CustomerName"),
                    rs.getString("Phone"), rs.getString("Email"), rs.getString("Address")
                });
            }
            lblStatus.setText("Tìm thấy " + tableModel.getRowCount() + " khách hàng");
        } catch (SQLException e) { e.printStackTrace(); }
    }
    
    private void showAddDialog() {
        JDialog dialog = new JDialog(this, "Thêm Khách Hàng", true);
        dialog.setSize(500, 350);
        dialog.setLocationRelativeTo(this);
        
        JTextField txtName = new JTextField();
        JTextField txtPhone = new JTextField();
        JTextField txtEmail = new JTextField();
        JTextArea txtAddress = new JTextArea(3, 20);
        txtAddress.setLineWrap(true);
        
        JPanel form = new JPanel();
        form.setLayout(new BoxLayout(form, BoxLayout.Y_AXIS));
        form.setBorder(new EmptyBorder(16, 16, 16, 16));
        form.setBackground(Color.WHITE);
        
        form.add(createFormRow("Tên KH:", txtName));
        form.add(Box.createVerticalStrut(12));
        form.add(createFormRow("Điện Thoại:", txtPhone));
        form.add(Box.createVerticalStrut(12));
        form.add(createFormRow("Email:", txtEmail));
        form.add(Box.createVerticalStrut(12));
        form.add(createFormRow("Địa Chỉ:", new JScrollPane(txtAddress)));
        
        JPanel buttonBar = createDialogButtons(dialog, () -> {
            if (txtName.getText().trim().isEmpty() || txtPhone.getText().trim().isEmpty()) {
                ToastNotification.show(dialog, "Vui lòng nhập tên và SĐT!", ToastNotification.WARNING);
                return;
            }
            
            String query = "INSERT INTO Customers (CustomerName, Phone, Email, Address) VALUES (?, ?, ?, ?)";
            if (DatabaseHelper.executeUpdate(query, txtName.getText().trim(), txtPhone.getText().trim(),
                txtEmail.getText().trim(), txtAddress.getText().trim())) {
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
        if (selectedCustomerId == -1) {
            ToastNotification.show(this, "Vui lòng chọn khách hàng!", ToastNotification.WARNING);
            return;
        }
        
        String query = "SELECT CustomerName, Phone, Email, Address FROM Customers WHERE CustomerID = ?";
        try (Connection conn = DatabaseHelper.getDBConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, selectedCustomerId);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                JDialog dialog = new JDialog(this, "Sửa Khách Hàng", true);
                dialog.setSize(500, 350);
                dialog.setLocationRelativeTo(this);
                
                JTextField txtName = new JTextField(rs.getString("CustomerName"));
                JTextField txtPhone = new JTextField(rs.getString("Phone"));
                JTextField txtEmail = new JTextField(rs.getString("Email"));
                JTextArea txtAddress = new JTextArea(rs.getString("Address"), 3, 20);
                txtAddress.setLineWrap(true);
                
                JPanel form = new JPanel();
                form.setLayout(new BoxLayout(form, BoxLayout.Y_AXIS));
                form.setBorder(new EmptyBorder(16, 16, 16, 16));
                form.setBackground(Color.WHITE);
                
                form.add(createFormRow("Tên KH:", txtName));
                form.add(Box.createVerticalStrut(12));
                form.add(createFormRow("Điện Thoại:", txtPhone));
                form.add(Box.createVerticalStrut(12));
                form.add(createFormRow("Email:", txtEmail));
                form.add(Box.createVerticalStrut(12));
                form.add(createFormRow("Địa Chỉ:", new JScrollPane(txtAddress)));
                
                JPanel buttonBar = createDialogButtons(dialog, () -> {
                    String updateQuery = "UPDATE Customers SET CustomerName = ?, Phone = ?, Email = ?, Address = ? WHERE CustomerID = ?";
                    if (DatabaseHelper.executeUpdate(updateQuery, txtName.getText().trim(), txtPhone.getText().trim(),
                        txtEmail.getText().trim(), txtAddress.getText().trim(), selectedCustomerId)) {
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
    
    private void deleteCustomer() {
        if (selectedCustomerId == -1) {
            ToastNotification.show(this, "Vui lòng chọn khách hàng!", ToastNotification.WARNING);
            return;
        }
        
        int confirm = JOptionPane.showConfirmDialog(this, "Xác nhận xóa?", "Xóa KH", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            if (DatabaseHelper.executeUpdate("DELETE FROM Customers WHERE CustomerID = ?", selectedCustomerId)) {
                ToastNotification.show(this, "✅ Xóa thành công!", ToastNotification.SUCCESS);
                selectedCustomerId = -1;
                loadData();
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
}
