package noithat.database;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

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
    
    /**
     * Tạo kết nối database mới
     * LƯU Ý: Người gọi phải đóng connection sau khi sử dụng!
     */
    public static Connection getDBConnection() throws SQLException {
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            Connection conn = DriverManager.getConnection(connectionUrl);
            return conn;
        } catch (ClassNotFoundException e) {
            System.err.println("✗ Lỗi: Không tìm thấy JDBC Driver!");
            throw new SQLException("JDBC Driver not found", e);
        }
    }
    
    /**
     * Thực thi câu lệnh UPDATE/INSERT/DELETE với PreparedStatement
     * Tự động đóng connection và statement
     */
    public static boolean executeUpdate(String query, Object... params) {
        try (Connection conn = getDBConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            for (int i = 0; i < params.length; i++) {
                pstmt.setObject(i + 1, params[i]);
            }
            
            pstmt.executeUpdate();
            return true;
            
        } catch (SQLException e) {
            System.err.println("✗ Lỗi executeUpdate: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Thực thi câu lệnh UPDATE/INSERT/DELETE và trả về số dòng bị ảnh hưởng
     */
    public static int executeUpdateWithCount(String query, Object... params) throws SQLException {
        try (Connection conn = getDBConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            for (int i = 0; i < params.length; i++) {
                pstmt.setObject(i + 1, params[i]);
            }
            
            return pstmt.executeUpdate();
            
        } catch (SQLException e) {
            System.err.println("✗ Lỗi executeUpdateWithCount: " + e.getMessage());
            throw e;
        }
    }
    
    /**
     * Thực thi câu lệnh INSERT và trả về ID được tạo
     */
    public static int executeInsertAndGetId(String query, Object... params) throws SQLException {
        try (Connection conn = getDBConnection();
             PreparedStatement pstmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            
            for (int i = 0; i < params.length; i++) {
                pstmt.setObject(i + 1, params[i]);
            }
            
            pstmt.executeUpdate();
            
            ResultSet rs = pstmt.getGeneratedKeys();
            if (rs.next()) {
                return rs.getInt(1);
            }
            throw new SQLException("Failed to get generated ID");
            
        } catch (SQLException e) {
            System.err.println("✗ Lỗi executeInsertAndGetId: " + e.getMessage());
            throw e;
        }
    }
    
    /**
     * Interface callback để xử lý ResultSet
     */
    @FunctionalInterface
    public interface ResultSetHandler<T> {
        T handle(ResultSet rs) throws SQLException;
    }
    
    /**
     * Thực thi câu lệnh SELECT và xử lý kết quả qua callback
     * Tự động đóng tất cả resources
     */
    public static <T> T executeQuery(String query, ResultSetHandler<T> handler, Object... params) throws SQLException {
        try (Connection conn = getDBConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            for (int i = 0; i < params.length; i++) {
                pstmt.setObject(i + 1, params[i]);
            }
            
            try (ResultSet rs = pstmt.executeQuery()) {
                return handler.handle(rs);
            }
            
        } catch (SQLException e) {
            System.err.println("✗ Lỗi executeQuery: " + e.getMessage());
            throw e;
        }
    }
    
    /**
     * Thực thi câu lệnh SELECT và trả về List kết quả
     */
    public static <T> List<T> executeQueryList(String query, ResultSetHandler<T> rowMapper, Object... params) throws SQLException {
        return executeQuery(query, rs -> {
            List<T> results = new ArrayList<>();
            while (rs.next()) {
                results.add(rowMapper.handle(rs));
            }
            return results;
        }, params);
    }
    
    /**
     * Thực thi câu lệnh SELECT và trả về đối tượng đầu tiên (hoặc null)
     */
    public static <T> T executeQuerySingle(String query, ResultSetHandler<T> rowMapper, Object... params) throws SQLException {
        return executeQuery(query, rs -> {
            if (rs.next()) {
                return rowMapper.handle(rs);
            }
            return null;
        }, params);
    }
    
    /**
     * Kiểm tra kết nối database
     */
    public static boolean testConnection() {
        try (Connection conn = getDBConnection()) {
            return conn != null && !conn.isClosed();
        } catch (SQLException e) {
            System.err.println("✗ Test connection failed: " + e.getMessage());
            return false;
        }
    }
}
