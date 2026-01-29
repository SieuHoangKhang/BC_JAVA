package noithat.utils;

public class Validator {
    public static boolean isValidEmail(String email) {
        if (email == null || email.isEmpty()) return false;
        return email.matches("^[A-Za-z0-9+_.-]+@(.+)$");
    }
    
    public static boolean isValidPhone(String phone) {
        if (phone == null || phone.isEmpty()) return false;
        return phone.matches("^[0-9+\\-\\s()]*$") && phone.length() >= 8;
    }
    
    public static boolean isValidUsername(String username) {
        if (username == null || username.isEmpty()) return false;
        return username.length() >= 3 && username.matches("^[a-zA-Z0-9_]*$");
    }
    
    public static boolean isValidPassword(String password) {
        return password != null && password.length() >= 3;
    }
    
    public static boolean isEmpty(String value) {
        return value == null || value.trim().isEmpty();
    }
    
    public static boolean isNumeric(String value) {
        if (value == null || value.isEmpty()) return false;
        try {
            Double.parseDouble(value);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
