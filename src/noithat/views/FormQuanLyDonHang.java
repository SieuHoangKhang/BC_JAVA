package noithat.views;

import noithat.database.DatabaseHelper;
import noithat.utils.*;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class FormQuanLyDonHang extends JFrame {
    private ModernTable tableOrders;
    private DefaultTableModel tableModel;
    private SearchField txtSearch;
    private ToolbarButton btnAdd, btnView, btnDelete;
    private JLabel lblStatus;
    private int selectedOrderId = -1;
    
  public FormQuanLyDonHang() {
        initComponents();
        loadData();
        applyPermissions();
    }
    
    private void initComponents() {
        setTitle("Quản Lý Đơn Hàng");
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
        
        JLabel title = new JLabel("Quản Lý Đơn Hàng");
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
        
        btnAdd = new ToolbarButton("+ Tạo Đơn", ProfessionalColors.SUCCESS);
        btnAdd.addActionListener(e -> ToastNotification.show(this, "Chức năng đang phát triển", ToastNotification.INFO));
        
        btnView = new ToolbarButton("Xem", ProfessionalColors.PRIMARY);
        btnView.addActionListener(e -> viewOrder());
        
        btnDelete = new ToolbarButton("Xóa", ProfessionalColors.DANGER);
        btnDelete.addActionListener(e -> deleteOrder());
        
        toolbar.add(btnBack);
        toolbar.add(Box.createHorizontalStrut(12));
        toolbar.add(btnAdd);
        toolbar.add(btnView);
        toolbar.add(btnDelete);
        
        toolbar.add(Box.createHorizontalGlue());
        
        txtSearch = new SearchField("Tìm kiếm đơn hàng...", 300);
        txtSearch.addActionListener(e -> searchOrders());
        
        toolbar.add(txtSearch);
        return toolbar;
    }
    
    private JPanel createTablePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(new LineBorder(ProfessionalColors.BORDER, 1));
        
        tableModel = new DefaultTableModel(
            new String[]{"ID", "Ngày", "Khách Hàng", "Tổng Tiền", "Trạng Thái", "Ghi Chú"}, 0
        ) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        
        tableOrders = new ModernTable(tableModel);
        tableOrders.setRowHeight(40);
        tableOrders.setShowVerticalLines(false);
        tableOrders.setSelectionBackground(ProfessionalColors.TABLE_SELECTED);
        tableOrders.getTableHeader().setBackground(ProfessionalColors.TABLE_HEADER);
        tableOrders.getTableHeader().setForeground(ProfessionalColors.TEXT_PRIMARY);
        
        tableOrders.getColumnModel().getColumn(0).setPreferredWidth(60);
        tableOrders.getColumnModel().getColumn(0).setMaxWidth(80);
        
        tableOrders.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int row = tableOrders.getSelectedRow();
                selectedOrderId = row != -1 ? (int) tableModel.getValueAt(row, 0) : -1;
            }
        });
        
        JScrollPane scrollPane = new JScrollPane(tableOrders);
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
        String query = "SELECT o.OrderID, o.OrderDate, c.CustomerName, o.TotalAmount, o.Status " +
                       "FROM Orders o " +
                       "LEFT JOIN Customers c ON o.CustomerID = c.CustomerID " +
                       "ORDER BY o.OrderID DESC";
        
        try (Connection conn = DatabaseHelper.getDBConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                tableModel.addRow(new Object[]{
                    rs.getInt("OrderID"),
                    rs.getString("OrderDate"),
                    rs.getString("CustomerName"),
                    String.format("%,.0f đ", rs.getDouble("TotalAmount")),
                    rs.getString("Status"),
                    "" // Placeholder for Notes column
                });
            }
            lblStatus.setText(tableModel.getRowCount() + " đơn hàng");
        } catch (SQLException e) { e.printStackTrace(); }
    }
    
    private void searchOrders() {
        String keyword = txtSearch.getText().trim();
        if (keyword.isEmpty()) { loadData(); return; }
        
        tableModel.setRowCount(0);
        String query = "SELECT o.OrderID, o.OrderDate, c.CustomerName, o.TotalAmount, o.Status " +
                       "FROM Orders o " +
                       "LEFT JOIN Customers c ON o.CustomerID = c.CustomerID " +
                       "WHERE c.CustomerName LIKE ? OR o.Status LIKE ? " +
                       "ORDER BY o.OrderID DESC";
        
        try (Connection conn = DatabaseHelper.getDBConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, "%" + keyword + "%");
            pstmt.setString(2, "%" + keyword + "%");
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                tableModel.addRow(new Object[]{
                    rs.getInt("OrderID"), rs.getString("OrderDate"), rs.getString("CustomerName"),
                    String.format("%,.0f đ", rs.getDouble("TotalAmount")), rs.getString("Status"), ""
                });
            }
            lblStatus.setText("Tìm thấy " + tableModel.getRowCount() + " đơn hàng");
        } catch (SQLException e) { e.printStackTrace(); }
    }
    
    private void viewOrder() {
        if (selectedOrderId == -1) {
            ToastNotification.show(this, "Vui lòng chọn đơn hàng!", ToastNotification.WARNING);
            return;
        }
        
        String query = "SELECT o.OrderID, o.OrderDate, c.CustomerName, o.TotalAmount, o.Status " +
                       "FROM Orders o LEFT JOIN Customers c ON o.CustomerID = c.CustomerID " +
                       "WHERE o.OrderID = ?";
        
        try (Connection conn = DatabaseHelper.getDBConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, selectedOrderId);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                JDialog dialog = new JDialog(this, "Chi Tiết Đơn Hàng #" + selectedOrderId, true);
                dialog.setSize(600, 500);
                dialog.setLocationRelativeTo(this);
                
                JPanel content = new JPanel();
                content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
                content.setBorder(new EmptyBorder(20, 20, 20, 20));
                content.setBackground(Color.WHITE);
                
                content.add(createInfoRow("Mã Đơn:", "#" + rs.getInt("OrderID")));
                content.add(Box.createVerticalStrut(12));
                content.add(createInfoRow("Ngày:", rs.getString("OrderDate")));
                content.add(Box.createVerticalStrut(12));
                content.add(createInfoRow("Khách Hàng:", rs.getString("CustomerName")));
                content.add(Box.createVerticalStrut(12));
                content.add(createInfoRow("Tổng Tiền:", String.format("%,.0f đ", rs.getDouble("TotalAmount"))));
                content.add(Box.createVerticalStrut(12));
                content.add(createInfoRow("Trạng Thái:", rs.getString("Status")));
                
                JPanel buttonBar = new JPanel(new FlowLayout(FlowLayout.RIGHT));
                buttonBar.setBackground(ProfessionalColors.BACKGROUND);
                buttonBar.setBorder(new MatteBorder(1, 0, 0, 0, ProfessionalColors.BORDER));
                
                ToolbarButton btnClose = new ToolbarButton("Đóng", ProfessionalColors.TEXT_SECONDARY);
                btnClose.addActionListener(e -> dialog.dispose());
                buttonBar.add(btnClose);
                
                dialog.add(content, BorderLayout.CENTER);
                dialog.add(buttonBar, BorderLayout.SOUTH);
                dialog.setVisible(true);
            }
        } catch (SQLException e) { e.printStackTrace(); }
    }
    
    private void deleteOrder() {
        if (selectedOrderId == -1) {
            ToastNotification.show(this, "Vui lòng chọn đơn hàng!", ToastNotification.WARNING);
            return;
        }
        
        int confirm = JOptionPane.showConfirmDialog(this, "Xác nhận xóa đơn hàng?", "Xóa", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            if (DatabaseHelper.executeUpdate("DELETE FROM Orders WHERE OrderID = ?", selectedOrderId)) {
                ToastNotification.show(this, "✅ Xóa thành công!", ToastNotification.SUCCESS);
                selectedOrderId = -1;
                loadData();
            }
        }
    }
    
    private JPanel createInfoRow(String label, String value) {
        JPanel row = new JPanel(new BorderLayout(12, 0));
        row.setBackground(Color.WHITE);
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 32));
        
        JLabel lbl = new JLabel(label);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lbl.setForeground(ProfessionalColors.TEXT_SECONDARY);
        lbl.setPreferredSize(new Dimension(120, 32));
        
        JLabel val = new JLabel(value);
        val.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        val.setForeground(ProfessionalColors.TEXT_PRIMARY);
        
        row.add(lbl, BorderLayout.WEST);
        row.add(val, BorderLayout.CENTER);
        return row;
    }
}
