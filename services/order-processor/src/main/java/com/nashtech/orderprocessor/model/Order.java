package com.nashtech.orderprocessor.model;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public class Order {

    private Long id;

    @NotBlank(message = "customerName is required")
    private String customerName;

    @NotBlank(message = "product is required")
    private String product;

    @Min(value = 1, message = "quantity must be at least 1")
    private int quantity;

    public Order() {
    }

    public Order(Long id, String customerName, String product, int quantity) {
        this.id = id;
        this.customerName = customerName;
        this.product = product;
        this.quantity = quantity;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}