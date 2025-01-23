package com.example.jointperchasesnew.dto;

import jakarta.validation.constraints.NotBlank;

public class OrderCancellationDto {
    private String username;
    private String productName;

    @NotBlank(message = "Username can't be blank")
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @NotBlank(message = "Product name can't be blank")
    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }
}
