package noithat.database;

import java.sql.*;

public class DatabaseHelper {
    private static final String SERVER = "localhost";
    private static final String PORT = "1433";
    private static final String DATABASE = "DB_QuanLyNoiThat";
    private static final String USERNAME = "sa";
    private static final String PASSWORD = "123";
    
    private static final String connectionUrl = 
        "jdbc:sqlserver://" + SERVER + ":" + PORT + ";" +
        "databaseName=" + DATABASE + ";" +
        "user=" + USERNAME + ";" +
        "password=" + PASSWORD + ";" +
        "encrypt=false;" +
        "trustServerCertificate=true;";
    
    public static Connection getDBConnection() {
        Connection connection = null;
        
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            System.out.println("✓ Tải driver JDBC thành công!");
            
            connection = DriverManager.getConnection(connectionUrl);
            System.out.println("✓ Kết nối database thành công!");
            
            return connection;
            
        } catch (ClassNotFoundException e) {
            System.err.println("✗ Lỗi: Không tìm thấy JDBC Driver!");
            System.err.println("  Hãy thêm file mssql-jdbc-xxx.jar vào thư mục lib");
            e.printStackTrace();
        } catch (SQLException e) {
            System.err.println("✗ Lỗi kết nối database!");
            System.err.println("  Kiểm tra lại: Server, Port, Database, Username, Password");
            e.printStackTrace();
        }
        
        return null;
    }
    
    public static boolean executeUpdate(String query, Object... params) {
        try (Connection conn = getDBConnection()) {
            if (conn == null) return false;
            
            PreparedStatement pstmt = conn.prepareStatement(query);
            for (int i = 0; i < params.length; i++) {
                pstmt.setObject(i + 1, params[i]);
            }
            
            pstmt.executeUpdate();
            pstmt.close();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public static ResultSet executeQuery(String query, Object... params) {
        try {
            Connection conn = getDBConnection();
            if (conn == null) return null;
            
            PreparedStatement pstmt = conn.prepareStatement(query);
            for (int i = 0; i < params.length; i++) {
                pstmt.setObject(i + 1, params[i]);
            }
            
            return pstmt.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
}
