package noithat.utils;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ActivityLogger {
    private static final String LOG_DIR = "logs";
    private static final String LOG_FILE = "activity.log";
    
    static {
        new File(LOG_DIR).mkdirs();
    }
    
    public static void log(int userId, String username, String action, String details) {
        String timestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        String logMessage = String.format("[%s] User ID: %d, Username: %s, Action: %s, Details: %s",
                                         timestamp, userId, username, action, details);
        
        try (FileWriter fw = new FileWriter(LOG_DIR + File.separator + LOG_FILE, true);
             BufferedWriter bw = new BufferedWriter(fw)) {
            bw.write(logMessage);
            bw.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public static void logLogin(int userId, String username) {
        log(userId, username, "LOGIN", "Đăng nhập thành công");
    }
    
    public static void logLogout(int userId, String username) {
        log(userId, username, "LOGOUT", "Đăng xuất");
    }
    
    public static void logCreate(int userId, String username, String module, String details) {
        log(userId, username, "CREATE", "Module: " + module + ", " + details);
    }
    
    public static void logUpdate(int userId, String username, String module, String details) {
        log(userId, username, "UPDATE", "Module: " + module + ", " + details);
    }
    
    public static void logDelete(int userId, String username, String module, String details) {
        log(userId, username, "DELETE", "Module: " + module + ", " + details);
    }
    
    public static void logExport(int userId, String username, String module) {
        log(userId, username, "EXPORT", "Module: " + module);
    }
}

