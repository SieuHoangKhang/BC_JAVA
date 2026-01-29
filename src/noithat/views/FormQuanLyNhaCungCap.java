package noithat.views;

import noithat.database.DatabaseHelper;
import noithat.utils.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.*;

public class FormQuanLyNhaCungCap extends JFrame {
    private JTextField txtSupplierName, txtContactPerson, txtPhone, txtEmail, txtAddress;
    private JTable tableSuppliers;
    private DefaultTableModel tableModel;
    private ModernButton btnAdd, btnEdit, btnDelete, btnBack;
    private JLabel lblStatus;
    
    public FormQuanLyNhaCungCap() {
        initComponents();
        loadData();
    }
    
    private void initComponents() {
        setTitle("Quản Lý Nhà Cung Cấp");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(1400, 800);
        setLocationRelativeTo(null);
        setResizable(true);
        
        // Header Panel
        JPanel headerPanel = new GradientPanel(ColorTheme.PRIMARY, new Color(52, 152, 219));
        headerPanel.setLayout(new BorderLayout());
        headerPanel.setPreferredSize(new Dimension(1400, 45));
        
        JLabel lblTitle = new JLabel("Quản Lý Nhà Cung Cấp");
        lblTitle.setFont(new Font("Arial", Font.BOLD, 20));
        lblTitle.setForeground(Color.WHITE);
        lblTitle.setBorder(BorderFactory.createEmptyBorder(5, 20, 5, 5));
        
        lblStatus = new JLabel("0 nhà cung cấp");
        lblStatus.setFont(new Font("Arial", Font.PLAIN, 12));
        lblStatus.setForeground(Color.WHITE);
        lblStatus.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 20));
        
        btnBack = new ModernButton("Quay Lại");
        btnBack.setPreferredSize(new Dimension(100, 35));
        btnBack.addActionListener(e -> this.dispose());
        
        headerPanel.add(lblTitle, BorderLayout.WEST);
        headerPanel.add(btnBack, BorderLayout.EAST);
        headerPanel.add(lblStatus, BorderLayout.CENTER);
        
        // Main Content Panel with 2-Column Layout
        JPanel mainPanel = new JPanel(new BorderLayout());
        
        // Left Panel: Form
        JPanel leftPanel = createFormPanel();
        
        // Right Panel: Table
        JPanel rightPanel = createTablePanel();
        
        // Split Pane
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, leftPanel, rightPanel);
        splitPane.setDividerLocation(350);
        splitPane.setResizeWeight(0.25);
        
        mainPanel.add(splitPane, BorderLayout.CENTER);
        
        // Add components
        add(headerPanel, BorderLayout.NORTH);
        add(mainPanel, BorderLayout.CENTER);
    }
    
    private JPanel createFormPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Title
        JLabel formTitle = new JLabel("Thông Tin Nhà Cung Cấp");
        formTitle.setFont(new Font("Arial", Font.BOLD, 14));
        formTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(formTitle);
        panel.add(Box.createVerticalStrut(10));
        
        // Supplier Name
        panel.add(createFieldPanel("Tên Nhà Cung Cấp:", txtSupplierName = new JTextField(20)));
        
        // Contact Person
        panel.add(createFieldPanel("Người Liên Hệ:", txtContactPerson = new JTextField(20)));
        
        // Phone
        panel.add(createFieldPanel("Điện Thoại:", txtPhone = new JTextField(20)));
        
        // Email
        panel.add(createFieldPanel("Email:", txtEmail = new JTextField(20)));
        
        // Address
        panel.add(createFieldPanel("Địa Chỉ:", txtAddress = new JTextField(20)));
        
        panel.add(Box.createVerticalStrut(20));
        
        // Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        buttonPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        buttonPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        buttonPanel.setBackground(Color.WHITE);
        
        btnAdd = new ModernButton("➕ Thêm");
        btnEdit = new ModernButton("✏️ Sửa");
        btnDelete = new ModernButton("🗑️ Xóa");
        
        btnAdd.addActionListener(e -> handleAction("CREATE"));
        btnEdit.addActionListener(e -> handleAction("UPDATE"));
        btnDelete.addActionListener(e -> handleAction("DELETE"));
        
        buttonPanel.add(btnAdd);
        buttonPanel.add(btnEdit);
        buttonPanel.add(btnDelete);
        
        panel.add(buttonPanel);
        panel.add(Box.createVerticalGlue());
        
        JScrollPane scrollPane = new JScrollPane(panel, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        
        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.add(scrollPane, BorderLayout.CENTER);
        return wrapper;
    }
    
    private JPanel createTablePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        
        tableModel = new DefaultTableModel(
            new String[]{"Mã NCC", "Tên NCC", "Người Liên Hệ", "Điện Thoại", "Email"},
            0
        ) { public boolean isCellEditable(int r, int c) { return false; } };
        
        tableSuppliers = new JTable(tableModel);
        tableSuppliers.setRowHeight(25);
        tableSuppliers.setFont(new Font("Arial", Font.PLAIN, 12));
        tableSuppliers.getTableHeader().setBackground(ColorTheme.PRIMARY);
        tableSuppliers.getTableHeader().setForeground(Color.WHITE);
        tableSuppliers.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));
        
        tableSuppliers.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = tableSuppliers.getSelectedRow();
                if (row >= 0) {
                    txtSupplierName.setText(tableModel.getValueAt(row, 1).toString());
                    txtContactPerson.setText(tableModel.getValueAt(row, 2).toString());
                    txtPhone.setText(tableModel.getValueAt(row, 3).toString());
                    txtEmail.setText(tableModel.getValueAt(row, 4).toString());
                }
            }
        });
        
        JScrollPane scrollPane = new JScrollPane(tableSuppliers);
        panel.add(scrollPane, BorderLayout.CENTER);
        
        return panel;
    }
    
    private JPanel createFieldPanel(String label, JTextField field) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
        panel.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.setBackground(Color.WHITE);
        
        JLabel lbl = new JLabel(label);
        lbl.setFont(new Font("Arial", Font.PLAIN, 12));
        lbl.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        field.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        field.setFont(new Font("Arial", Font.PLAIN, 12));
        
        panel.add(lbl);
        panel.add(Box.createVerticalStrut(5));
        panel.add(field);
        panel.add(Box.createVerticalStrut(5));
        
        return panel;
    }
    
    private void clearForm() {
        txtSupplierName.setText("");
        txtContactPerson.setText("");
        txtPhone.setText("");
        txtEmail.setText("");
        txtAddress.setText("");
    }
    
    private void loadData() {
        tableModel.setRowCount(0);
        String sql = "SELECT SupplierID, SupplierName, ContactPerson, Phone, Email FROM Suppliers";
        
        try (Connection conn = DatabaseHelper.getDBConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            int count = 0;
            while (rs.next()) {
                tableModel.addRow(new Object[]{
                    rs.getInt("SupplierID"),
                    rs.getString("SupplierName"),
                    rs.getString("ContactPerson"),
                    rs.getString("Phone"),
                    rs.getString("Email")
                });
                count++;
            }
            lblStatus.setText(count + " nhà cung cấp");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    private void handleAction(String action) {
        int selectedRow = tableSuppliers.getSelectedRow();
        
        if (action.equals("CREATE")) {
            clearForm();
        } else if (action.equals("UPDATE")) {
            if (selectedRow < 0) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn nhà cung cấp!");
                return;
            }
            String name = JOptionPane.showInputDialog(this, "Tên nhà cung cấp:", txtSupplierName.getText());
            if (name != null && !name.trim().isEmpty()) {
                String contact = JOptionPane.showInputDialog(this, "Người liên hệ:", txtContactPerson.getText());
                if (contact != null) {
                    String phone = JOptionPane.showInputDialog(this, "Điện thoại:", txtPhone.getText());
                    if (phone != null) {
                        String email = JOptionPane.showInputDialog(this, "Email:", txtEmail.getText());
                        if (email != null) {
                            String address = JOptionPane.showInputDialog(this, "Địa chỉ:", txtAddress.getText());
                            if (address != null) {
                                int supplierId = (int) tableModel.getValueAt(selectedRow, 0);
                                String sql = "UPDATE Suppliers SET SupplierName=?, ContactPerson=?, Phone=?, Email=? WHERE SupplierID=?";
                                try (Connection conn = DatabaseHelper.getDBConnection();
                                     PreparedStatement pstmt = conn.prepareStatement(sql)) {
                                    pstmt.setString(1, name);
                                    pstmt.setString(2, contact);
                                    pstmt.setString(3, phone);
                                    pstmt.setString(4, email);
                                    pstmt.setInt(5, supplierId);
                                    pstmt.executeUpdate();
                                    loadData();
                                    clearForm();
                                    JOptionPane.showMessageDialog(this, "Cập nhật thành công!");
                                } catch (SQLException ex) { ex.printStackTrace(); }
                            }
                        }
                    }
                }
            }
        } else if (action.equals("DELETE")) {
            if (selectedRow < 0) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn nhà cung cấp!");
                return;
            }
            int result = JOptionPane.showConfirmDialog(this, "Xóa nhà cung cấp này?");
            if (result == JOptionPane.YES_OPTION) {
                int supplierId = (int) tableModel.getValueAt(selectedRow, 0);
                String sql = "DELETE FROM Suppliers WHERE SupplierID=?";
                try (Connection conn = DatabaseHelper.getDBConnection();
                     PreparedStatement pstmt = conn.prepareStatement(sql)) {
                    pstmt.setInt(1, supplierId);
                    pstmt.executeUpdate();
                    loadData();
                    clearForm();
                    JOptionPane.showMessageDialog(this, "Xóa thành công!");
                } catch (SQLException ex) { ex.printStackTrace(); }
            }
        }
    }
}

