package noithat.models;

public class Order {
    private int orderId;
    private int customerId;
    private String customerName;
    private java.util.Date orderDate;
    private java.util.Date deliveryDate;
    private double totalAmount;
    private String status;
    private String notes;
    private int createdBy;
    
    public Order() {
    }
    
    public Order(int orderId, int customerId, String customerName, java.util.Date orderDate,
                 java.util.Date deliveryDate, double totalAmount, String status, String notes, int createdBy) {
        this.orderId = orderId;
        this.customerId = customerId;
        this.customerName = customerName;
        this.orderDate = orderDate;
        this.deliveryDate = deliveryDate;
        this.totalAmount = totalAmount;
        this.status = status;
        this.notes = notes;
        this.createdBy = createdBy;
    }
    
    public int getOrderId() { return orderId; }
    public void setOrderId(int orderId) { this.orderId = orderId; }
    
    public int getCustomerId() { return customerId; }
    public void setCustomerId(int customerId) { this.customerId = customerId; }
    
    public String getCustomerName() { return customerName; }
    public void setCustomerName(String customerName) { this.customerName = customerName; }
    
    public java.util.Date getOrderDate() { return orderDate; }
    public void setOrderDate(java.util.Date orderDate) { this.orderDate = orderDate; }
    
    public java.util.Date getDeliveryDate() { return deliveryDate; }
    public void setDeliveryDate(java.util.Date deliveryDate) { this.deliveryDate = deliveryDate; }
    
    public double getTotalAmount() { return totalAmount; }
    public void setTotalAmount(double totalAmount) { this.totalAmount = totalAmount; }
    
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    
    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
    
    public int getCreatedBy() { return createdBy; }
    public void setCreatedBy(int createdBy) { this.createdBy = createdBy; }
}
