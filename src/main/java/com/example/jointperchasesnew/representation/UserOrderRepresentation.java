package com.example.jointperchasesnew.representation;

import org.springframework.hateoas.RepresentationModel;

public class UserOrderRepresentation extends RepresentationModel<UserOrderRepresentation> {
    private String username;
    private String productName;
    private Integer quantity;
    private Double totalPrice;

    public UserOrderRepresentation() {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(Double totalPrice) {
        this.totalPrice = totalPrice;
    }
}
