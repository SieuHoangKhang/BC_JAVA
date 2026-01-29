package noithat.views;

import noithat.database.DatabaseHelper;
import noithat.utils.*;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;
import java.util.*;

public class FormQuanLyDonHang extends JFrame {
    private ModernTable tableOrders;
    private DefaultTableModel tableModel;
    private SearchField txtSearch;
    private ToolbarButton btnAdd, btnView, btnEdit, btnDelete;
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
        btnAdd.addActionListener(e -> showCreateOrderDialog());
        
        btnEdit = new ToolbarButton("Sửa", ProfessionalColors.PRIMARY);
        btnEdit.addActionListener(e -> showEditOrderDialog());
        
        btnView = new ToolbarButton("Xem", new Color(52, 152, 219));
        btnView.addActionListener(e -> viewOrder());
        
        btnDelete = new ToolbarButton("Xóa", ProfessionalColors.DANGER);
        btnDelete.addActionListener(e -> deleteOrder());
        
        toolbar.add(btnBack);
        toolbar.add(Box.createHorizontalStrut(12));
        toolbar.add(btnAdd);
        toolbar.add(btnEdit);
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
            new String[]{"ID", "Ngày", "Khách Hàng", "Tổng Tiền", "Trạng Thái", "Người Tạo"}, 0
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
        String query = "SELECT o.OrderID, o.OrderDate, c.CustomerName, o.TotalAmount, o.Status, u.FullName AS CreatedBy " +
                       "FROM Orders o " +
                       "LEFT JOIN Customers c ON o.CustomerID = c.CustomerID " +
                       "LEFT JOIN Users u ON o.UserID = u.UserID " +
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
                    rs.getString("CreatedBy")
                });
            }
            lblStatus.setText(tableModel.getRowCount() + " đơn hàng");
        } catch (SQLException e) { e.printStackTrace(); }
    }
    
    private void searchOrders() {
        String keyword = txtSearch.getText().trim();
        if (keyword.isEmpty()) { loadData(); return; }
        
        tableModel.setRowCount(0);
        String query = "SELECT o.OrderID, o.OrderDate, c.CustomerName, o.TotalAmount, o.Status, u.FullName AS CreatedBy " +
                       "FROM Orders o " +
                       "LEFT JOIN Customers c ON o.CustomerID = c.CustomerID " +
                       "LEFT JOIN Users u ON o.UserID = u.UserID " +
                       "WHERE c.CustomerName LIKE ? OR o.Status LIKE ? OR o.OrderID LIKE ? " +
                       "ORDER BY o.OrderID DESC";
        
        try (Connection conn = DatabaseHelper.getDBConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, "%" + keyword + "%");
            pstmt.setString(2, "%" + keyword + "%");
            pstmt.setString(3, "%" + keyword + "%");
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                tableModel.addRow(new Object[]{
                    rs.getInt("OrderID"), rs.getString("OrderDate"), rs.getString("CustomerName"),
                    String.format("%,.0f đ", rs.getDouble("TotalAmount")), rs.getString("Status"), rs.getString("CreatedBy")
                });
            }
            lblStatus.setText("Tìm thấy " + tableModel.getRowCount() + " đơn hàng");
        } catch (SQLException e) { e.printStackTrace(); }
    }
    
    private void showCreateOrderDialog() {
        JDialog dialog = new JDialog(this, "Tạo Đơn Hàng Mới", true);
        dialog.setSize(700, 550);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new BorderLayout());
        
        // Form panel
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        formPanel.setBackground(Color.WHITE);
        
        // Customer selection
        JComboBox<String> cmbCustomer = new JComboBox<>();
        Map<String, Integer> customerMap = new HashMap<>();
        try (Connection conn = DatabaseHelper.getDBConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT CustomerID, CustomerName FROM Customers")) {
            while (rs.next()) {
                String name = rs.getString("CustomerName");
                customerMap.put(name, rs.getInt("CustomerID"));
                cmbCustomer.addItem(name);
            }
        } catch (SQLException e) { e.printStackTrace(); }
        
        // Payment method
        JComboBox<String> cmbPayment = new JComboBox<>(new String[]{"Tiền mặt", "Chuyển khoản", "Thẻ tín dụng", "Trả góp"});
        
        // Status
        JComboBox<String> cmbStatus = new JComboBox<>(new String[]{"Pending", "Processing", "Completed", "Cancelled"});
        
        // Products for order details
        JPanel productsPanel = new JPanel(new BorderLayout());
        productsPanel.setBorder(BorderFactory.createTitledBorder("Sản Phẩm"));
        productsPanel.setBackground(Color.WHITE);
        
        DefaultTableModel productModel = new DefaultTableModel(new String[]{"Sản Phẩm", "Giá", "Số Lượng", "Thành Tiền"}, 0);
        JTable productTable = new JTable(productModel);
        productTable.setRowHeight(35);
        JScrollPane productScroll = new JScrollPane(productTable);
        productsPanel.add(productScroll, BorderLayout.CENTER);
        
        // Add product controls
        JPanel addProductPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        addProductPanel.setBackground(Color.WHITE);
        
        JComboBox<String> cmbProduct = new JComboBox<>();
        Map<String, Integer> productMap = new HashMap<>();
        Map<String, Double> productPriceMap = new HashMap<>();
        Map<String, Integer> productStockMap = new HashMap<>();
        
        try (Connection conn = DatabaseHelper.getDBConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT ProductID, ProductName, Price, Stock FROM Products WHERE Stock > 0")) {
            while (rs.next()) {
                String name = rs.getString("ProductName") + " (Stock: " + rs.getInt("Stock") + ")";
                productMap.put(name, rs.getInt("ProductID"));
                productPriceMap.put(name, rs.getDouble("Price"));
                productStockMap.put(name, rs.getInt("Stock"));
                cmbProduct.addItem(name);
            }
        } catch (SQLException e) { e.printStackTrace(); }
        
        JSpinner spnQuantity = new JSpinner(new SpinnerNumberModel(1, 1, 100, 1));
        spnQuantity.setPreferredSize(new Dimension(60, 30));
        
        JButton btnAddProduct = new JButton("+ Thêm");
        btnAddProduct.addActionListener(e -> {
            String selected = (String) cmbProduct.getSelectedItem();
            if (selected != null && productMap.containsKey(selected)) {
                int qty = (Integer) spnQuantity.getValue();
                int maxStock = productStockMap.get(selected);
                if (qty > maxStock) {
                    ToastNotification.show(dialog, "Số lượng vượt quá tồn kho!", ToastNotification.WARNING);
                    return;
                }
                double price = productPriceMap.get(selected);
                double total = price * qty;
                productModel.addRow(new Object[]{selected, String.format("%,.0f đ", price), qty, String.format("%,.0f đ", total)});
                updateOrderTotal(productModel);
            }
        });
        
        JButton btnRemoveProduct = new JButton("Xóa");
        btnRemoveProduct.addActionListener(e -> {
            int row = productTable.getSelectedRow();
            if (row >= 0) {
                productModel.removeRow(row);
                updateOrderTotal(productModel);
            }
        });
        
        addProductPanel.add(new JLabel("Sản phẩm:"));
        addProductPanel.add(cmbProduct);
        addProductPanel.add(new JLabel("SL:"));
        addProductPanel.add(spnQuantity);
        addProductPanel.add(btnAddProduct);
        addProductPanel.add(btnRemoveProduct);
        
        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setBackground(Color.WHITE);
        
        JLabel lblTotal = new JLabel("Tổng tiền: 0 đ");
        lblTotal.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblTotal.setForeground(ProfessionalColors.DANGER);
        
        JTextArea txtNote = new JTextArea(2, 20);
        txtNote.setLineWrap(true);
        
        bottomPanel.add(lblTotal, BorderLayout.NORTH);
        bottomPanel.add(new JLabel("Ghi chú:"), BorderLayout.WEST);
        bottomPanel.add(new JScrollPane(txtNote), BorderLayout.CENTER);
        
        productsPanel.add(addProductPanel, BorderLayout.NORTH);
        productsPanel.add(bottomPanel, BorderLayout.SOUTH);
        
        // Add form rows
        formPanel.add(createFormRow("Khách hàng:", cmbCustomer));
        formPanel.add(Box.createVerticalStrut(12));
        formPanel.add(createFormRow("Thanh toán:", cmbPayment));
        formPanel.add(Box.createVerticalStrut(12));
        formPanel.add(createFormRow("Trạng thái:", cmbStatus));
        formPanel.add(Box.createVerticalStrut(12));
        formPanel.add(productsPanel);
        
        // Buttons
        JPanel buttonBar = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        buttonBar.setBackground(ProfessionalColors.BACKGROUND);
        buttonBar.setBorder(new MatteBorder(1, 0, 0, 0, ProfessionalColors.BORDER));
        
        ToolbarButton btnCancel = new ToolbarButton("Hủy", ProfessionalColors.TEXT_SECONDARY);
        btnCancel.addActionListener(e -> dialog.dispose());
        
        ToolbarButton btnSave = new ToolbarButton("Tạo Đơn", ProfessionalColors.SUCCESS);
        btnSave.addActionListener(e -> {
            try {
                String customerName = (String) cmbCustomer.getSelectedItem();
                if (customerName == null || customerMap.isEmpty()) {
                    ToastNotification.show(dialog, "Vui lòng chọn khách hàng!", ToastNotification.WARNING);
                    return;
                }
                
                if (productModel.getRowCount() == 0) {
                    ToastNotification.show(dialog, "Vui lòng thêm sản phẩm!", ToastNotification.WARNING);
                    return;
                }
                
                int customerId = customerMap.get(customerName);
                int userId = SessionManager.getInstance().getUserId();
                String paymentMethod = (String) cmbPayment.getSelectedItem();
                String status = (String) cmbStatus.getSelectedItem();
                String note = txtNote.getText().trim();
                
                Connection conn = DatabaseHelper.getDBConnection();
                conn.setAutoCommit(false);
                
                try {
                    // Create order
                    PreparedStatement pstmt = conn.prepareStatement(
                        "INSERT INTO Orders (CustomerID, UserID, PaymentMethod, Status, Note) VALUES (?, ?, ?, ?, ?)",
                        Statement.RETURN_GENERATED_KEYS
                    );
                    pstmt.setInt(1, customerId);
                    pstmt.setInt(2, userId);
                    pstmt.setString(3, paymentMethod);
                    pstmt.setString(4, status);
                    pstmt.setString(5, note);
                    pstmt.executeUpdate();
                    
                    ResultSet rs = pstmt.getGeneratedKeys();
                    int orderId = 0;
                    if (rs.next()) {
                        orderId = rs.getInt(1);
                    }
                    pstmt.close();
                    
                    // Add order details and update stock
                    double totalAmount = 0;
                    for (int i = 0; i < productModel.getRowCount(); i++) {
                        String productName = (String) productModel.getValueAt(i, 0);
                        int quantity = (Integer) productModel.getValueAt(i, 2);
                        String priceStr = ((String) productModel.getValueAt(i, 1)).replace(".", "").replace(" đ", "");
                        double price = Double.parseDouble(priceStr);
                        int productId = productMap.get(productName);
                        double subtotal = price * quantity;
                        totalAmount += subtotal;
                        
                        PreparedStatement pstmt2 = conn.prepareStatement(
                            "INSERT INTO OrderDetails (OrderID, ProductID, Quantity, UnitPrice) VALUES (?, ?, ?, ?)"
                        );
                        pstmt2.setInt(1, orderId);
                        pstmt2.setInt(2, productId);
                        pstmt2.setInt(3, quantity);
                        pstmt2.setDouble(4, price);
                        pstmt2.executeUpdate();
                        pstmt2.close();
                        
                        // Update stock
                        PreparedStatement pstmt3 = conn.prepareStatement(
                            "UPDATE Products SET Stock = Stock - ? WHERE ProductID = ?"
                        );
                        pstmt3.setInt(1, quantity);
                        pstmt3.setInt(2, productId);
                        pstmt3.executeUpdate();
                        pstmt3.close();
                    }
                    
                    // Update total amount
                    PreparedStatement pstmt4 = conn.prepareStatement(
                        "UPDATE Orders SET TotalAmount = ? WHERE OrderID = ?"
                    );
                    pstmt4.setDouble(1, totalAmount);
                    pstmt4.setInt(2, orderId);
                    pstmt4.executeUpdate();
                    pstmt4.close();
                    
                    conn.commit();
                    conn.setAutoCommit(true);
                    conn.close();
                    
                    ToastNotification.show(this, "✅ Tạo đơn hàng thành công!", ToastNotification.SUCCESS);
                    dialog.dispose();
                    loadData();
                    
                } catch (SQLException ex) {
                    conn.rollback();
                    throw ex;
                }
                
            } catch (Exception ex) {
                ToastNotification.show(dialog, "Lỗi: " + ex.getMessage(), ToastNotification.ERROR);
                ex.printStackTrace();
            }
        });
        
        buttonBar.add(btnCancel);
        buttonBar.add(btnSave);
        
        dialog.add(formPanel, BorderLayout.CENTER);
        dialog.add(buttonBar, BorderLayout.SOUTH);
        dialog.setVisible(true);
    }
    
    private void showEditOrderDialog() {
        if (selectedOrderId == -1) {
            ToastNotification.show(this, "Vui lòng chọn đơn hàng!", ToastNotification.WARNING);
            return;
        }
        
        String query = "SELECT o.*, c.CustomerName FROM Orders o LEFT JOIN Customers c ON o.CustomerID = c.CustomerID WHERE o.OrderID = ?";
        try (Connection conn = DatabaseHelper.getDBConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, selectedOrderId);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                JDialog dialog = new JDialog(this, "Sửa Đơn Hàng #" + selectedOrderId, true);
                dialog.setSize(600, 400);
                dialog.setLocationRelativeTo(this);
                dialog.setLayout(new BorderLayout());
                
                JPanel form = new JPanel();
                form.setLayout(new BoxLayout(form, BoxLayout.Y_AXIS));
                form.setBorder(new EmptyBorder(20, 20, 20, 20));
                form.setBackground(Color.WHITE);
                
                // Status combo
                JComboBox<String> cmbStatus = new JComboBox<>(new String[]{"Pending", "Processing", "Completed", "Cancelled"});
                cmbStatus.setSelectedItem(rs.getString("Status"));
                
                // Payment combo
                JComboBox<String> cmbPayment = new JComboBox<>(new String[]{"Tiền mặt", "Chuyển khoản", "Thẻ tín dụng", "Trả góp"});
                cmbPayment.setSelectedItem(rs.getString("PaymentMethod"));
                
                // Note
                JTextArea txtNote = new JTextArea(rs.getString("Note"), 3, 20);
                txtNote.setLineWrap(true);
                
                form.add(createFormRow("Trạng thái:", cmbStatus));
                form.add(Box.createVerticalStrut(12));
                form.add(createFormRow("Thanh toán:", cmbPayment));
                form.add(Box.createVerticalStrut(12));
                form.add(createFormRow("Ghi chú:", new JScrollPane(txtNote)));
                
                JPanel buttonBar = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
                buttonBar.setBackground(ProfessionalColors.BACKGROUND);
                buttonBar.setBorder(new MatteBorder(1, 0, 0, 0, ProfessionalColors.BORDER));
                
                ToolbarButton btnCancel = new ToolbarButton("Hủy", ProfessionalColors.TEXT_SECONDARY);
                btnCancel.addActionListener(e -> dialog.dispose());
                
                ToolbarButton btnSave = new ToolbarButton("Lưu", ProfessionalColors.SUCCESS);
                btnSave.addActionListener(e -> {
                    String updateQuery = "UPDATE Orders SET Status = ?, PaymentMethod = ?, Note = ? WHERE OrderID = ?";
                    if (DatabaseHelper.executeUpdate(updateQuery, cmbStatus.getSelectedItem(), 
                        cmbPayment.getSelectedItem(), txtNote.getText().trim(), selectedOrderId)) {
                        ToastNotification.show(this, "✅ Cập nhật thành công!", ToastNotification.SUCCESS);
                        dialog.dispose();
                        loadData();
                    }
                });
                
                buttonBar.add(btnCancel);
                buttonBar.add(btnSave);
                
                dialog.add(form, BorderLayout.CENTER);
                dialog.add(buttonBar, BorderLayout.SOUTH);
                dialog.setVisible(true);
            }
        } catch (SQLException e) { e.printStackTrace(); }
    }
    
    @SuppressWarnings("unused")
    private void updateOrderTotal(DefaultTableModel productModel) {
        double total = 0;
        for (int i = 0; i < productModel.getRowCount(); i++) {
            String priceStr = ((String) productModel.getValueAt(i, 3)).replace(".", "").replace(" đ", "");
            total += Double.parseDouble(priceStr);
        }
        // This would need to update a label - simplified for now
    }
    
    private void viewOrder() {
        if (selectedOrderId == -1) {
            ToastNotification.show(this, "Vui lòng chọn đơn hàng!", ToastNotification.WARNING);
            return;
        }
        
        String query = "SELECT o.*, c.CustomerName, u.FullName AS CreatedBy " +
                       "FROM Orders o " +
                       "LEFT JOIN Customers c ON o.CustomerID = c.CustomerID " +
                       "LEFT JOIN Users u ON o.UserID = u.UserID " +
                       "WHERE o.OrderID = ?";
        
        try (Connection conn = DatabaseHelper.getDBConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, selectedOrderId);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                JDialog dialog = new JDialog(this, "Chi Tiết Đơn Hàng #" + selectedOrderId, true);
                dialog.setSize(700, 550);
                dialog.setLocationRelativeTo(this);
                dialog.setLayout(new BorderLayout());
                
                JPanel content = new JPanel();
                content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
                content.setBorder(new EmptyBorder(20, 20, 20, 20));
                content.setBackground(Color.WHITE);
                
                content.add(createInfoRow("Mã Đơn:", "#" + rs.getInt("OrderID")));
                content.add(Box.createVerticalStrut(10));
                content.add(createInfoRow("Ngày tạo:", rs.getString("OrderDate")));
                content.add(Box.createVerticalStrut(10));
                content.add(createInfoRow("Khách Hàng:", rs.getString("CustomerName")));
                content.add(Box.createVerticalStrut(10));
                content.add(createInfoRow("Tổng Tiền:", String.format("%,.0f đ", rs.getDouble("TotalAmount"))));
                content.add(Box.createVerticalStrut(10));
                content.add(createInfoRow("Trạng Thái:", rs.getString("Status")));
                content.add(Box.createVerticalStrut(10));
                content.add(createInfoRow("Thanh toán:", rs.getString("PaymentMethod")));
                content.add(Box.createVerticalStrut(10));
                content.add(createInfoRow("Người tạo:", rs.getString("CreatedBy")));
                content.add(Box.createVerticalStrut(10));
                
                String note = rs.getString("Note");
                if (note != null && !note.isEmpty()) {
                    content.add(createInfoRow("Ghi chú:", note));
                }
                
                // Load order details
                JPanel detailsPanel = new JPanel(new BorderLayout());
                detailsPanel.setBorder(BorderFactory.createTitledBorder("Chi Tiết Đơn Hàng"));
                detailsPanel.setBackground(Color.WHITE);
                
                DefaultTableModel detailModel = new DefaultTableModel(new String[]{"Sản Phẩm", "Đơn Giá", "Số Lượng", "Thành Tiền"}, 0);
                JTable detailTable = new JTable(detailModel);
                detailTable.setRowHeight(30);
                
                try (PreparedStatement pstmt2 = conn.prepareStatement(
                        "SELECT od.ProductID, p.ProductName, od.UnitPrice, od.Quantity, od.Subtotal " +
                        "FROM OrderDetails od JOIN Products p ON od.ProductID = p.ProductID " +
                        "WHERE od.OrderID = ?")) {
                    pstmt2.setInt(1, selectedOrderId);
                    ResultSet rs2 = pstmt2.executeQuery();
                    while (rs2.next()) {
                        detailModel.addRow(new Object[]{
                            rs2.getString("ProductName"),
                            String.format("%,.0f đ", rs2.getDouble("UnitPrice")),
                            rs2.getInt("Quantity"),
                            String.format("%,.0f đ", rs2.getDouble("Subtotal"))
                        });
                    }
                }
                
                detailsPanel.add(new JScrollPane(detailTable), BorderLayout.CENTER);
                content.add(Box.createVerticalStrut(10));
                content.add(detailsPanel);
                
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
        
        int confirm = JOptionPane.showConfirmDialog(this, "Xác nhận xóa đơn hàng? Các chi tiết đơn hàng cũng sẽ bị xóa.", 
            "Xóa", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                Connection conn = DatabaseHelper.getDBConnection();
                conn.setAutoCommit(false);
                
                try {
                    // Restore stock
                    PreparedStatement pstmt = conn.prepareStatement(
                        "SELECT ProductID, Quantity FROM OrderDetails WHERE OrderID = ?"
                    );
                    pstmt.setInt(1, selectedOrderId);
                    ResultSet rs = pstmt.executeQuery();
                    while (rs.next()) {
                        int productId = rs.getInt("ProductID");
                        int quantity = rs.getInt("Quantity");
                        
                        PreparedStatement pstmt2 = conn.prepareStatement(
                            "UPDATE Products SET Stock = Stock + ? WHERE ProductID = ?"
                        );
                        pstmt2.setInt(1, quantity);
                        pstmt2.setInt(2, productId);
                        pstmt2.executeUpdate();
                        pstmt2.close();
                    }
                    pstmt.close();
                    
                    // Delete order (OrderDetails will be deleted by CASCADE)
                    PreparedStatement pstmt3 = conn.prepareStatement("DELETE FROM Orders WHERE OrderID = ?");
                    pstmt3.setInt(1, selectedOrderId);
                    pstmt3.executeUpdate();
                    pstmt3.close();
                    
                    conn.commit();
                    conn.setAutoCommit(true);
                    conn.close();
                    
                    ToastNotification.show(this, "✅ Xóa thành công!", ToastNotification.SUCCESS);
                    selectedOrderId = -1;
                    loadData();
                } catch (SQLException ex) {
                    conn.rollback();
                    throw ex;
                }
            } catch (Exception e) {
                ToastNotification.show(this, "Lỗi: " + e.getMessage(), ToastNotification.ERROR);
            }
        }
    }
    
    private JPanel createFormRow(String label, JComponent field) {
        JPanel row = new JPanel(new BorderLayout(12, 0));
        row.setBackground(Color.WHITE);
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, field instanceof JScrollPane ? 80 : 35));
        
        JLabel lbl = new JLabel(label);
        lbl.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lbl.setPreferredSize(new Dimension(120, 32));
        
        field.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        row.add(lbl, BorderLayout.WEST);
        row.add(field, BorderLayout.CENTER);
        return row;
    }
    
    private JPanel createInfoRow(String label, String value) {
        JPanel row = new JPanel(new BorderLayout(12, 0));
        row.setBackground(Color.WHITE);
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 32));
        
        JLabel lbl = new JLabel(label);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lbl.setForeground(ProfessionalColors.TEXT_SECONDARY);
        lbl.setPreferredSize(new Dimension(120, 32));
        
        JLabel val = new JLabel(value != null ? value : "");
        val.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        val.setForeground(ProfessionalColors.TEXT_PRIMARY);
        
        row.add(lbl, BorderLayout.WEST);
        row.add(val, BorderLayout.CENTER);
        return row;
    }
    
    private void applyPermissions() {
        String role = SessionManager.getInstance().getRole();
        
        if (!PermissionService.canView(role, "ORDER")) {
            JOptionPane.showMessageDialog(this,
                "BAN KHONG CO QUYEN TRUY CAP CHUC NANG NAY!",
                "Tu choi truy cap",
                JOptionPane.ERROR_MESSAGE);
            dispose();
            return;
        }
        
        if (!PermissionService.canCreate(role, "ORDER")) {
            btnAdd.setEnabled(false);
            btnAdd.setToolTipText("Ban khong co quyen tao don hang");
        }
        
        if (!PermissionService.canUpdate(role, "ORDER")) {
            btnEdit.setEnabled(false);
            btnEdit.setToolTipText("Ban khong co quyen sua don hang");
        }
        
        if (!PermissionService.canDelete(role, "ORDER")) {
            btnDelete.setEnabled(false);
            btnDelete.setToolTipText("Ban khong co quyen xoa don hang");
        }
    }
}
