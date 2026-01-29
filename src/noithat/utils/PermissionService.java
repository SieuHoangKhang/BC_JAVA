package noithat.utils;

import noithat.database.DatabaseHelper;
import java.sql.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Service for checking user permissions based on role
 */
public class PermissionService {
    
    // Simple cache to avoid repeated DB queries
    private static Map<String, Boolean> permissionCache = new HashMap<>();
    
    /**
     * Check if a role has permission for a specific module and action
     * @param role User role (Admin, Manager, Staff)
     * @param module Module name (PRODUCT, ORDER, CUSTOMER, etc.)
     * @param action Action type (VIEW, CREATE, UPDATE, DELETE)
     * @return true if permitted
     */
    public static boolean hasPermission(String role, String module, String action) {
        if (role == null || module == null || action == null) {
            return false;
        }
        
        // Check cache first
        String cacheKey = role + ":" + module + ":" + action;
        if (permissionCache.containsKey(cacheKey)) {
            return permissionCache.get(cacheKey);
        }
        
        String query = "SELECT Allowed FROM RolePermissions " +
                       "WHERE Role = ? AND Module = ? AND Action = ?";
        
        try (Connection conn = DatabaseHelper.getDBConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, role);
            pstmt.setString(2, module);
            pstmt.setString(3, action);
            
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                boolean allowed = rs.getBoolean("Allowed");
                permissionCache.put(cacheKey, allowed);
                return allowed;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        // Default deny
        permissionCache.put(cacheKey, false);
        return false;
    }
    
    // Convenience methods for common permission checks
    public static boolean canView(String role, String module) {
        return hasPermission(role, module, "VIEW");
    }
    
    public static boolean canCreate(String role, String module) {
        return hasPermission(role, module, "CREATE");
    }
    
    public static boolean canUpdate(String role, String module) {
        return hasPermission(role, module, "UPDATE");
    }
    
    public static boolean canDelete(String role, String module) {
        return hasPermission(role, module, "DELETE");
    }
    
    public static boolean canExport(String role, String module) {
        return hasPermission(role, module, "EXPORT");
    }
    
    /**
     * Clear permission cache (useful when roles/permissions change)
     */
    public static void clearCache() {
        permissionCache.clear();
    }
}

