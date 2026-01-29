package noithat.models;

public class Product {
    private int productId;
    private String productName;
    private String description;
    private String unit;
    private double price;
    private int categoryId;
    private String categoryName;
    private int supplierId;
    private String supplierName;
    private boolean active;
    
    public Product() {
    }
    
    public Product(int productId, String productName, String description, String unit,
                   double price, int categoryId, int supplierId, boolean active) {
        this.productId = productId;
        this.productName = productName;
        this.description = description;
        this.unit = unit;
        this.price = price;
        this.categoryId = categoryId;
        this.supplierId = supplierId;
        this.active = active;
    }
    
    public int getProductId() { return productId; }
    public void setProductId(int productId) { this.productId = productId; }
    
    public String getProductName() { return productName; }
    public void setProductName(String productName) { this.productName = productName; }
    
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    
    public String getUnit() { return unit; }
    public void setUnit(String unit) { this.unit = unit; }
    
    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }
    
    public int getCategoryId() { return categoryId; }
    public void setCategoryId(int categoryId) { this.categoryId = categoryId; }
    
    public String getCategoryName() { return categoryName; }
    public void setCategoryName(String categoryName) { this.categoryName = categoryName; }
    
    public int getSupplierId() { return supplierId; }
    public void setSupplierId(int supplierId) { this.supplierId = supplierId; }
    
    public String getSupplierName() { return supplierName; }
    public void setSupplierName(String supplierName) { this.supplierName = supplierName; }
    
    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }
    
    @Override
    public String toString() {
        return productName;
    }
}
