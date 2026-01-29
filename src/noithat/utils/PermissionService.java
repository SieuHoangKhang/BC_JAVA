package noithat.utils;

import noithat.database.DatabaseHelper;
import java.sql.*;

public class PermissionService {
    public static boolean hasPermission(int userId, String module, String action) {
        String query = "SELECT COUNT(*) as cnt FROM RolePermissions rp " +
                       "JOIN Users u ON rp.Role = " +
                       "(SELECT RoleName FROM Roles WHERE RoleID = u.RoleID) " +
                       "WHERE u.UserID = ? AND rp.Module = ? AND rp.Action = ? AND rp.Allowed = 1";
        
        try (Connection conn = DatabaseHelper.getDBConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, userId);
            pstmt.setString(2, module);
            pstmt.setString(3, action);
            
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("cnt") > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    public static int getRoleId(int userId) {
        String query = "SELECT RoleID FROM Users WHERE UserID = ?";
        try (Connection conn = DatabaseHelper.getDBConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, userId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("RoleID");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }
    
    public static String getRoleName(int roleId) {
        String query = "SELECT RoleName FROM Roles WHERE RoleID = ?";
        try (Connection conn = DatabaseHelper.getDBConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, roleId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getString("RoleName");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "Unknown";
    }
}

