package noithat.utils;

public class SessionManager {
    private static SessionManager instance;
    private int userId;
    private String username;
    private String fullName;
    private String role;
    
    private SessionManager() {
    }
    
    public static SessionManager getInstance() {
        if (instance == null) {
            instance = new SessionManager();
        }
        return instance;
    }
    
    public void setUser(int userId, String username, String fullName, String role) {
        this.userId = userId;
        this.username = username;
        this.fullName = fullName;
        this.role = role;
    }
    
    public int getUserId() { return userId; }
    public String getUsername() { return username; }
    public String getFullName() { return fullName; }
    public String getRole() { return role; }
    
    public void logout() {
        userId = 0;
        username = null;
        fullName = null;
        role = null;
    }
    
    public boolean isLoggedIn() {
        return userId > 0;
    }
}

