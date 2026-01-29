package noithat.views;

import noithat.database.DatabaseHelper;
import noithat.utils.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.*;

public class FormQuanLyDanhMuc extends JFrame {
    private JTextField txtCategoryName, txtDescription;
    private JTable tableCategories;
    private DefaultTableModel tableModel;
    private ModernButton btnAdd, btnEdit, btnDelete, btnBack;
    private JLabel lblStatus;
    
    public FormQuanLyDanhMuc() {
        initComponents();
        loadData();
    }
    
    private void initComponents() {
        setTitle("Quản Lý Danh Mục");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(1200, 700);
        setLocationRelativeTo(null);
        setResizable(true);
        
        // Header Panel
        JPanel headerPanel = new GradientPanel(ColorTheme.INFO, new Color(52, 73, 94));
        headerPanel.setLayout(new BorderLayout());
        headerPanel.setPreferredSize(new Dimension(1200, 45));
        
        JLabel lblTitle = new JLabel("Quản Lý Danh Mục Sản Phẩm");
        lblTitle.setFont(new Font("Arial", Font.BOLD, 20));
        lblTitle.setForeground(Color.WHITE);
        lblTitle.setBorder(BorderFactory.createEmptyBorder(5, 20, 5, 5));
        
        lblStatus = new JLabel("0 danh mục");
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
        splitPane.setDividerLocation(300);
        splitPane.setResizeWeight(0.2);
        
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
        JLabel formTitle = new JLabel("Thông Tin Danh Mục");
        formTitle.setFont(new Font("Arial", Font.BOLD, 14));
        formTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(formTitle);
        panel.add(Box.createVerticalStrut(10));
        
        // Category Name
        panel.add(createFieldPanel("Tên Danh Mục:", txtCategoryName = new JTextField(20)));
        
        // Description
        panel.add(createFieldPanel("Mô Tả:", txtDescription = new JTextField(20)));
        
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
            new String[]{"Mã DM", "Tên Danh Mục", "Mô Tả"},
            0
        ) { public boolean isCellEditable(int r, int c) { return false; } };
        
        tableCategories = new JTable(tableModel);
        tableCategories.setRowHeight(25);
        tableCategories.setFont(new Font("Arial", Font.PLAIN, 12));
        tableCategories.getTableHeader().setBackground(ColorTheme.INFO);
        tableCategories.getTableHeader().setForeground(Color.WHITE);
        tableCategories.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));
        
        tableCategories.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = tableCategories.getSelectedRow();
                if (row >= 0) {
                    txtCategoryName.setText(tableModel.getValueAt(row, 1).toString());
                    txtDescription.setText(tableModel.getValueAt(row, 2).toString());
                }
            }
        });
        
        JScrollPane scrollPane = new JScrollPane(tableCategories);
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
        txtCategoryName.setText("");
        txtDescription.setText("");
    }
    
    private void loadData() {
        tableModel.setRowCount(0);
        String sql = "SELECT CategoryID, CategoryName, Description FROM Categories";
        
        try (Connection conn = DatabaseHelper.getDBConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            int count = 0;
            while (rs.next()) {
                tableModel.addRow(new Object[]{
                    rs.getInt("CategoryID"),
                    rs.getString("CategoryName"),
                    rs.getString("Description")
                });
                count++;
            }
            lblStatus.setText(count + " danh mục");
        } catch (SQLException e) { 
            e.printStackTrace();
        }
    }
    
    private void handleAction(String action) {
        int selectedRow = tableCategories.getSelectedRow();
        
        if (action.equals("CREATE")) {
            clearForm();
        } else if (action.equals("UPDATE")) {
            if (selectedRow < 0) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn danh mục!");
                return;
            }
            String name = JOptionPane.showInputDialog(this, "Tên danh mục:", txtCategoryName.getText());
            if (name != null && !name.trim().isEmpty()) {
                String desc = JOptionPane.showInputDialog(this, "Mô tả:", txtDescription.getText());
                if (desc != null) {
                    int categoryId = (int) tableModel.getValueAt(selectedRow, 0);
                    String sql = "UPDATE Categories SET CategoryName=?, Description=? WHERE CategoryID=?";
                    try (Connection conn = DatabaseHelper.getDBConnection();
                         PreparedStatement pstmt = conn.prepareStatement(sql)) {
                        pstmt.setString(1, name);
                        pstmt.setString(2, desc);
                        pstmt.setInt(3, categoryId);
                        pstmt.executeUpdate();
                        loadData();
                        clearForm();
                        JOptionPane.showMessageDialog(this, "Cập nhật thành công!");
                    } catch (SQLException ex) { ex.printStackTrace(); }
                }
            }
        } else if (action.equals("DELETE")) {
            if (selectedRow < 0) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn danh mục!");
                return;
            }
            int result = JOptionPane.showConfirmDialog(this, "Xóa danh mục này?");
            if (result == JOptionPane.YES_OPTION) {
                int categoryId = (int) tableModel.getValueAt(selectedRow, 0);
                String sql = "DELETE FROM Categories WHERE CategoryID=?";
                try (Connection conn = DatabaseHelper.getDBConnection();
                     PreparedStatement pstmt = conn.prepareStatement(sql)) {
                    pstmt.setInt(1, categoryId);
                    pstmt.executeUpdate();
                    loadData();
                    clearForm();
                    JOptionPane.showMessageDialog(this, "Xóa thành công!");
                } catch (SQLException ex) { ex.printStackTrace(); }
            }
        }
    }
}

