package noithat.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateHelper {
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
    private static final SimpleDateFormat dateTimeFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
    
    public static String formatDate(Date date) {
        if (date == null) return "";
        return dateFormat.format(date);
    }
    
    public static String formatDateTime(Date date) {
        if (date == null) return "";
        return dateTimeFormat.format(date);
    }
    
    public static Date parseDate(String dateStr) {
        try {
            if (dateStr == null || dateStr.isEmpty()) return null;
            return dateFormat.parse(dateStr);
        } catch (Exception e) {
            return null;
        }
    }
    
    public static Date getCurrentDate() {
        return new Date();
    }
}
