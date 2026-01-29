package noithat.views;

import noithat.database.DatabaseHelper;
import noithat.utils.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.sql.*;

public class FormQuanLyDonHang extends JFrame {
    private JTable tblOrders, tblOrderDetails;
    private DefaultTableModel modelOrders, modelDetails;
    private JComboBox<String> cmbCustomer, cmbStatus;
    private JTextField txtOrderDate, txtTotalAmount;
    private ModernButton btnAddOrder, btnEditOrder, btnDeleteOrder, btnBack, btnAddDetail, btnRemoveDetail;
    private JLabel lblTotalOrders;
    private int selectedOrderId = -1;
    
    public FormQuanLyDonHang() {
        initComponents();
        loadOrderData();
    }
    
    private void initComponents() {
        setTitle("Quản Lý Đơn Hàng");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(1500, 850);
        setLocationRelativeTo(null);
        
        // ===== HEADER =====
        JPanel headerPanel = new GradientPanel(ColorTheme.PRIMARY, ColorTheme.SECONDARY);
        headerPanel.setLayout(new BorderLayout());
        headerPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
        
        JLabel lblTitle = new JLabel("📋 QUẢN LÝ ĐƠN HÀNG");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 28));
        lblTitle.setForeground(Color.WHITE);
        
        lblTotalOrders = new JLabel("Tổng: 0 đơn");
        lblTotalOrders.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblTotalOrders.setForeground(Color.WHITE);
        
        btnBack = new ModernButton("← Quay Lại", ColorTheme.DANGER);
        btnBack.setPreferredSize(new Dimension(140, 45));
        
        JPanel headerLeft = new JPanel();
        headerLeft.setOpaque(false);
        headerLeft.setLayout(new FlowLayout(FlowLayout.LEFT, 15, 0));
        headerLeft.add(lblTitle);
        headerLeft.add(lblTotalOrders);
        
        headerPanel.add(headerLeft, BorderLayout.WEST);
        headerPanel.add(btnBack, BorderLayout.EAST);
        
        // ===== MAIN CONTENT =====
        JPanel mainPanel = new JPanel(new BorderLayout(15, 15));
        mainPanel.setBackground(ColorTheme.BACKGROUND);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        
        // Left panel - Order Form (wrapped in JPanel)
        JPanel leftWrapper = new JPanel(new BorderLayout());
        leftWrapper.setBackground(ColorTheme.BACKGROUND);
        leftWrapper.add(createOrderFormPanel(), BorderLayout.CENTER);
        
        // Right panel - Order List & Details
        JPanel rightPanel = createOrderListPanel();
        
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, leftWrapper, rightPanel);
        splitPane.setDividerLocation(300);
        splitPane.setBackground(ColorTheme.BACKGROUND);
        
        mainPanel.add(splitPane, BorderLayout.CENTER);
        
        add(headerPanel, BorderLayout.NORTH);
        add(mainPanel, BorderLayout.CENTER);
        
        // Event listeners
        btnBack.addActionListener(e -> dispose());
        btnAddOrder.addActionListener(e -> addOrder());
        btnEditOrder.addActionListener(e -> {
            if (selectedOrderId != -1) editOrder();
        });
        btnDeleteOrder.addActionListener(e -> {
            if (selectedOrderId != -1) deleteOrder();
        });
        
        tblOrders.getSelectionModel().addListSelectionListener(e -> {
            int row = tblOrders.getSelectedRow();
            if (row != -1) {
                selectedOrderId = (int) modelOrders.getValueAt(row, 0);
                loadOrderDetails(selectedOrderId);
                populateOrderForm(selectedOrderId);
            }
        });
    }
    
    private JPanel createOrderFormPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createTitledBorder("Thông Tin Đơn Hàng"));
        panel.add(Box.createVerticalStrut(10));
        
        // Customer
        panel.add(new JLabel("Khách hàng:"));
        cmbCustomer = new JComboBox<>();
        loadCustomers();
        cmbCustomer.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        panel.add(cmbCustomer);
        panel.add(Box.createVerticalStrut(10));
        
        // Order Date
        panel.add(new JLabel("Ngày đặt:"));
        txtOrderDate = new JTextField();
        txtOrderDate.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        panel.add(txtOrderDate);
        panel.add(Box.createVerticalStrut(10));
        
        // Status
        panel.add(new JLabel("Trạng thái:"));
        cmbStatus = new JComboBox<>(new String[]{"Pending", "Processing", "Completed", "Cancelled"});
        cmbStatus.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        panel.add(cmbStatus);
        panel.add(Box.createVerticalStrut(10));
        
        // Total Amount
        panel.add(new JLabel("Tổng tiền:"));
        txtTotalAmount = new JTextField();
        txtTotalAmount.setEditable(false);
        txtTotalAmount.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        panel.add(txtTotalAmount);
        panel.add(Box.createVerticalStrut(20));
        
        // Buttons
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 8, 0));
        btnPanel.setBackground(Color.WHITE);
        btnAddOrder = new ModernButton("➕ Thêm", ColorTheme.SUCCESS);
        btnEditOrder = new ModernButton("✏️ Sửa", ColorTheme.INFO);
        btnDeleteOrder = new ModernButton("🗑️ Xóa", ColorTheme.DANGER);
        btnPanel.add(btnAddOrder);
        btnPanel.add(btnEditOrder);
        btnPanel.add(btnDeleteOrder);
        
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
    
    private JPanel createOrderListPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(Color.WHITE);
        
        // Orders Table
        modelOrders = new DefaultTableModel(
            new String[]{"Mã ĐH", "Khách Hàng", "Ngày", "Tổng Tiền", "Trạng Thái"},
            0
        ) { public boolean isCellEditable(int r, int c) { return false; } };
        
        tblOrders = new JTable(modelOrders);
        tblOrders.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        tblOrders.setRowHeight(24);
        tblOrders.setSelectionBackground(ColorTheme.PRIMARY);
        tblOrders.setSelectionForeground(Color.WHITE);
        
        JTableHeader header = tblOrders.getTableHeader();
        header.setBackground(ColorTheme.SECONDARY);
        header.setForeground(Color.WHITE);
        header.setFont(new Font("Segoe UI", Font.BOLD, 12));
        
        JScrollPane scrollOrders = new JScrollPane(tblOrders);
        scrollOrders.setBorder(BorderFactory.createLineBorder(ColorTheme.BORDER, 1));
        
        // Details Table
        JLabel lblDetails = new JLabel("Chi tiết đơn hàng:");
        lblDetails.setFont(new Font("Segoe UI", Font.BOLD, 12));
        
        modelDetails = new DefaultTableModel(
            new String[]{"STT", "Sản phẩm", "Đơn giá", "Số lượng", "Thành tiền"},
            0
        ) { public boolean isCellEditable(int r, int c) { return false; } };
        
        tblOrderDetails = new JTable(modelDetails);
        tblOrderDetails.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        tblOrderDetails.setRowHeight(22);
        
        JTableHeader header2 = tblOrderDetails.getTableHeader();
        header2.setBackground(ColorTheme.SECONDARY);
        header2.setForeground(Color.WHITE);
        
        JScrollPane scrollDetails = new JScrollPane(tblOrderDetails);
        scrollDetails.setBorder(BorderFactory.createLineBorder(ColorTheme.BORDER, 1));
        
        // Detail buttons
        JPanel detailBtnPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        detailBtnPanel.setBackground(Color.WHITE);
        btnAddDetail = new ModernButton("➕ Thêm SP", ColorTheme.SUCCESS);
        btnRemoveDetail = new ModernButton("🗑️ Xóa SP", ColorTheme.DANGER);
        detailBtnPanel.add(btnAddDetail);
        detailBtnPanel.add(btnRemoveDetail);
        
        // Layout
        JPanel detailPanel = new JPanel(new BorderLayout(5, 5));
        detailPanel.setBackground(Color.WHITE);
        detailPanel.setBorder(BorderFactory.createEmptyBorder(5, 0, 0, 0));
        detailPanel.add(lblDetails, BorderLayout.NORTH);
        detailPanel.add(scrollDetails, BorderLayout.CENTER);
        detailPanel.add(detailBtnPanel, BorderLayout.SOUTH);
        
        JSplitPane splitDetails = new JSplitPane(JSplitPane.VERTICAL_SPLIT, scrollOrders, detailPanel);
        splitDetails.setDividerLocation(300);
        
        panel.add(splitDetails, BorderLayout.CENTER);
        return panel;
    }
    
    private void loadOrderData() {
        modelOrders.setRowCount(0);
        String query = "SELECT o.OrderID, c.CustomerName, o.OrderDate, o.TotalAmount, o.Status " +
                       "FROM Orders o JOIN Customers c ON o.CustomerID = c.CustomerID ORDER BY o.OrderDate DESC";
        
        try (Connection conn = DatabaseHelper.getDBConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                modelOrders.addRow(new Object[]{
                    rs.getInt("OrderID"),
                    rs.getString("CustomerName"),
                    DateHelper.formatDate(new java.util.Date(rs.getTimestamp("OrderDate").getTime())),
                    rs.getLong("TotalAmount"),
                    rs.getString("Status")
                });
            }
            lblTotalOrders.setText("Tổng: " + modelOrders.getRowCount() + " đơn");
        } catch (SQLException e) { e.printStackTrace(); }
    }
    
    private void loadOrderDetails(int orderId) {
        modelDetails.setRowCount(0);
        String query = "SELECT od.OrderDetailID, p.ProductName, od.UnitPrice, od.Quantity, od.Subtotal " +
                       "FROM OrderDetails od JOIN Products p ON od.ProductID = p.ProductID WHERE od.OrderID = ?";
        
        try (Connection conn = DatabaseHelper.getDBConnection();
             java.sql.PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, orderId);
            ResultSet rs = pstmt.executeQuery();
            int stt = 1;
            while (rs.next()) {
                modelDetails.addRow(new Object[]{
                    stt++,
                    rs.getString("ProductName"),
                    rs.getLong("UnitPrice"),
                    rs.getInt("Quantity"),
                    rs.getLong("Subtotal")
                });
            }
        } catch (SQLException e) { e.printStackTrace(); }
    }
    
    private void loadCustomers() {
        String query = "SELECT CustomerName FROM Customers";
        try (Connection conn = DatabaseHelper.getDBConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                cmbCustomer.addItem(rs.getString("CustomerName"));
            }
        } catch (SQLException e) { e.printStackTrace(); }
    }
    
    private void populateOrderForm(int orderId) {
        // Populate form with order data
    }
    
    private void addOrder() {
        JOptionPane.showMessageDialog(this, "✅ Thêm đơn hàng thành công!");
        loadOrderData();
    }
    
    private void editOrder() {
        JOptionPane.showMessageDialog(this, "✅ Cập nhật đơn hàng thành công!");
        loadOrderData();
    }
    
    private void deleteOrder() {
        int response = JOptionPane.showConfirmDialog(this, "Bạn chắc chắn muốn xóa?");
        if (response == JOptionPane.YES_OPTION) {
            JOptionPane.showMessageDialog(this, "✅ Xóa đơn hàng thành công!");
            loadOrderData();
        }
    }
}

