package noithat.models;

public class Inventory {
    private int inventoryId;
    private int productId;
    private String productName;
    private int quantity;
    private java.util.Date lastUpdated;
    
    public Inventory() {
    }
    
    public Inventory(int inventoryId, int productId, String productName, int quantity, java.util.Date lastUpdated) {
        this.inventoryId = inventoryId;
        this.productId = productId;
        this.productName = productName;
        this.quantity = quantity;
        this.lastUpdated = lastUpdated;
    }
    
    public int getInventoryId() { return inventoryId; }
    public void setInventoryId(int inventoryId) { this.inventoryId = inventoryId; }
    
    public int getProductId() { return productId; }
    public void setProductId(int productId) { this.productId = productId; }
    
    public String getProductName() { return productName; }
    public void setProductName(String productName) { this.productName = productName; }
    
    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }
    
    public java.util.Date getLastUpdated() { return lastUpdated; }
    public void setLastUpdated(java.util.Date lastUpdated) { this.lastUpdated = lastUpdated; }
}
