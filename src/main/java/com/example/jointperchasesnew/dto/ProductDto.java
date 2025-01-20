package com.example.jointperchasesnew.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

public class ProductDto {
    private String name;
    private Double price;
    private String description;

    @NotBlank(message = "Product name can't be blank")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Positive (message = "Price must be bigger than 0")
    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    @NotBlank(message = "Description can't be blank")
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
