package com.demo.order.dto;

public class OrderDTO {

    private Long orderId;
    private String customerName;
    private String status;
    private int quantity;
    private ProductDTO product;   // enriched from Product Service




    public OrderDTO() {}

    public OrderDTO(Long orderId, String customerName, String status, int quantity) {
        this.orderId = orderId;
        this.customerName = customerName;
        this.status = status;
        this.quantity = quantity;
    }

    public Long getOrderId() { return orderId; }
    public void setOrderId(Long orderId) { this.orderId = orderId; }

    public String getCustomerName() { return customerName; }
    public void setCustomerName(String customerName) { this.customerName = customerName; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }

    public ProductDTO getProduct() { return product; }
    public void setProduct(ProductDTO product) { this.product = product; }
}
