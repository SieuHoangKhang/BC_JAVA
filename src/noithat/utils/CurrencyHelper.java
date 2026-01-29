package noithat.utils;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

public class CurrencyHelper {
    private static final DecimalFormatSymbols symbols = new DecimalFormatSymbols();
    private static final DecimalFormat currencyFormat;
    
    static {
        symbols.setGroupingSeparator('.');
        symbols.setDecimalSeparator(',');
        currencyFormat = new DecimalFormat("#,##0.00", symbols);
    }
    
    public static String formatCurrency(double amount) {
        return currencyFormat.format(amount) + " đ";
    }
    
    public static String formatPrice(double amount) {
        return currencyFormat.format(amount);
    }
    
    public static double parsePrice(String priceStr) {
        try {
            if (priceStr == null || priceStr.isEmpty()) return 0;
            String cleaned = priceStr.replace(".", "").replace(",", ".").replace(" đ", "");
            return Double.parseDouble(cleaned);
        } catch (Exception e) {
            return 0;
        }
    }
}

