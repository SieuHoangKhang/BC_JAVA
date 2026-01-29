package noithat.views;

import noithat.database.DatabaseHelper;
import noithat.utils.*;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class FormQuanLyDanhMuc extends JFrame {
    private ModernTable tableCategories;
    private DefaultTableModel tableModel;
    private SearchField txtSearch;
    private ToolbarButton btnAdd, btnEdit, btnDelete;
    private JLabel lblStatus;
    private int selectedCategoryId = -1;
    
    public FormQuanLyDanhMuc() {
        initComponents();
        loadData();
        applyPermissions();
    }
    
    private void initComponents() {
        setTitle("Quản Lý Danh Mục");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(1000, 700);
        setMinimumSize(new Dimension(900, 600));
        setLocationRelativeTo(null);
        setBackground(ProfessionalColors.BACKGROUND);
        
        // HEADER
        JPanel header = createHeader();
        
        // TOOLBAR
        JPanel toolbar = createToolbar();
        
        // TABLE
        JPanel tablePanel = createTablePanel();
        
        // STATUS BAR
        JPanel statusBar = createStatusBar();
        
        // LAYOUT
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
        
        JLabel title = new JLabel("Quản Lý Danh Mục");
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
        btnDelete.addActionListener(e -> deleteCategory());
        
        toolbar.add(btnBack);
        toolbar.add(Box.createHorizontalStrut(12));
        toolbar.add(btnAdd);
        toolbar.add(btnEdit);
        toolbar.add(btnDelete);
        
        toolbar.add(Box.createHorizontalGlue());
        
        txtSearch = new SearchField("Tìm kiếm danh mục...", 300);
        txtSearch.addActionListener(e -> searchCategories());
        
        toolbar.add(txtSearch);
        
        return toolbar;
    }
    
    private JPanel createTablePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(new LineBorder(ProfessionalColors.BORDER, 1));
        
        tableModel = new DefaultTableModel(
            new String[]{"ID", "Tên Danh Mục", "Mô Tả"}, 0
        ) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        
        tableCategories = new ModernTable(tableModel);
        tableCategories.setRowHeight(40);
        tableCategories.setShowVerticalLines(false);
        tableCategories.setSelectionBackground(ProfessionalColors.TABLE_SELECTED);
        tableCategories.getTableHeader().setBackground(ProfessionalColors.TABLE_HEADER);
        tableCategories.getTableHeader().setForeground(ProfessionalColors.TEXT_PRIMARY);
        tableCategories.getTableHeader().setBorder(new MatteBorder(0, 0, 1, 0, ProfessionalColors.BORDER));
        
        tableCategories.getColumnModel().getColumn(0).setPreferredWidth(60);
        tableCategories.getColumnModel().getColumn(0).setMaxWidth(80);
        tableCategories.getColumnModel().getColumn(1).setPreferredWidth(200);
        tableCategories.getColumnModel().getColumn(2).setPreferredWidth(400);
        
        tableCategories.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int row = tableCategories.getSelectedRow();
                selectedCategoryId = row != -1 ? (int) tableModel.getValueAt(row, 0) : -1;
            }
        });
        
        JScrollPane scrollPane = new JScrollPane(tableCategories);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getViewport().setBackground(Color.WHITE);
        
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
        String query = "SELECT CategoryID, CategoryName, Description FROM Categories ORDER BY CategoryID DESC";
        
        try (Connection conn = DatabaseHelper.getDBConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                tableModel.addRow(new Object[]{
                    rs.getInt("CategoryID"),
                    rs.getString("CategoryName"),
                    rs.getString("Description")
                });
            }
            lblStatus.setText(tableModel.getRowCount() + " danh mục");
        } catch (SQLException e) {
            e.printStackTrace();
            ToastNotification.show(this, "Lỗi tải dữ liệu!", ToastNotification.ERROR);
        }
    }
    
    private void searchCategories() {
        String keyword = txtSearch.getText().trim();
        if (keyword.isEmpty()) {
            loadData();
            return;
        }
        
        tableModel.setRowCount(0);
        String query = "SELECT CategoryID, CategoryName, Description FROM Categories WHERE CategoryName LIKE ?";
        
        try (Connection conn = DatabaseHelper.getDBConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, "%" + keyword + "%");
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                tableModel.addRow(new Object[]{
                    rs.getInt("CategoryID"),
                    rs.getString("CategoryName"),
                    rs.getString("Description")
                });
            }
            lblStatus.setText("Tìm thấy " + tableModel.getRowCount() + " danh mục");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    private void showAddDialog() {
        JDialog dialog = createStandardDialog("Thêm Danh Mục Mới");
        
        JTextField txtName = new JTextField();
        JTextArea txtDesc = new JTextArea(3, 20);
        txtDesc.setLineWrap(true);
        txtDesc.setWrapStyleWord(true);
        
        JPanel form = new JPanel();
        form.setLayout(new BoxLayout(form, BoxLayout.Y_AXIS));
        form.setBorder(new EmptyBorder(16, 16, 16, 16));
        form.setBackground(Color.WHITE);
        
        form.add(createFormRow("Tên Danh Mục:", txtName));
        form.add(Box.createVerticalStrut(12));
        form.add(createFormRow("Mô Tả:", new JScrollPane(txtDesc)));
        
        JPanel buttonBar = createDialogButtons(dialog,
            () -> {
                String name = txtName.getText().trim();
                if (name.isEmpty()) {
                    ToastNotification.show(dialog, "Vui lòng nhập tên!", ToastNotification.WARNING);
                    return;
                }
                
                String query = "INSERT INTO Categories (CategoryName, Description) VALUES (?, ?)";
                if (DatabaseHelper.executeUpdate(query, name, txtDesc.getText().trim())) {
                    ToastNotification.show(this, "✅ Thêm thành công!", ToastNotification.SUCCESS);
                    dialog.dispose();
                    loadData();
                } else {
                    ToastNotification.show(dialog, "Thêm thất bại!", ToastNotification.ERROR);
                }
            });
        
        dialog.add(form, BorderLayout.CENTER);
        dialog.add(buttonBar, BorderLayout.SOUTH);
        dialog.setVisible(true);
    }
    
    private void showEditDialog() {
        if (selectedCategoryId == -1) {
            ToastNotification.show(this, "Vui lòng chọn danh mục!", ToastNotification.WARNING);
            return;
        }
        
        String query = "SELECT CategoryName, Description FROM Categories WHERE CategoryID = ?";
        try (Connection conn = DatabaseHelper.getDBConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, selectedCategoryId);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                JDialog dialog = createStandardDialog("Sửa Danh Mục");
                
                JTextField txtName = new JTextField(rs.getString("CategoryName"));
                JTextArea txtDesc = new JTextArea(rs.getString("Description"), 3, 20);
                txtDesc.setLineWrap(true);
                txtDesc.setWrapStyleWord(true);
                
                JPanel form = new JPanel();
                form.setLayout(new BoxLayout(form, BoxLayout.Y_AXIS));
                form.setBorder(new EmptyBorder(16, 16, 16, 16));
                form.setBackground(Color.WHITE);
                
                form.add(createFormRow("Tên Danh Mục:", txtName));
                form.add(Box.createVerticalStrut(12));
                form.add(createFormRow("Mô Tả:", new JScrollPane(txtDesc)));
                
                JPanel buttonBar = createDialogButtons(dialog,
                    () -> {
                        String name = txtName.getText().trim();
                        if (name.isEmpty()) {
                            ToastNotification.show(dialog, "Vui lòng nhập tên!", ToastNotification.WARNING);
                            return;
                        }
                        
                        String updateQuery = "UPDATE Categories SET CategoryName = ?, Description = ? WHERE CategoryID = ?";
                        if (DatabaseHelper.executeUpdate(updateQuery, name, txtDesc.getText().trim(), selectedCategoryId)) {
                            ToastNotification.show(this, "✅ Cập nhật thành công!", ToastNotification.SUCCESS);
                            dialog.dispose();
                            loadData();
                        } else {
                            ToastNotification.show(dialog, "Cập nhật thất bại!", ToastNotification.ERROR);
                        }
                    });
                
                dialog.add(form, BorderLayout.CENTER);
                dialog.add(buttonBar, BorderLayout.SOUTH);
                dialog.setVisible(true);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    private void deleteCategory() {
        if (selectedCategoryId == -1) {
            ToastNotification.show(this, "Vui lòng chọn danh mục!", ToastNotification.WARNING);
            return;
        }
        
        int confirm = JOptionPane.showConfirmDialog(this, "Xác nhận xóa danh mục này?",
            "Xóa Danh Mục", JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            String query = "DELETE FROM Categories WHERE CategoryID = ?";
            if (DatabaseHelper.executeUpdate(query, selectedCategoryId)) {
                ToastNotification.show(this, "✅ Xóa thành công!", ToastNotification.SUCCESS);
                selectedCategoryId = -1;
                loadData();
            } else {
                ToastNotification.show(this, "Xóa thất bại!", ToastNotification.ERROR);
            }
        }
    }
    
    private JDialog createStandardDialog(String title) {
        JDialog dialog = new JDialog(this, title, true);
        dialog.setSize(500, 300);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new BorderLayout());
        return dialog;
    }
    
    private JPanel createFormRow(String label, JComponent field) {
        JPanel row = new JPanel(new BorderLayout(12, 0));
        row.setBackground(Color.WHITE);
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 32));
        
        JLabel lbl = new JLabel(label);
        lbl.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lbl.setForeground(ProfessionalColors.TEXT_PRIMARY);
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
