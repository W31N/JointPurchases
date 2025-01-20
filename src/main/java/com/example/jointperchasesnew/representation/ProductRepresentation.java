package com.example.jointperchasesnew.representation;

import org.springframework.hateoas.RepresentationModel;

public class ProductRepresentation extends RepresentationModel<ProductRepresentation> {
    private String name;
    private Double price;
    private String description;

    public ProductRepresentation() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
