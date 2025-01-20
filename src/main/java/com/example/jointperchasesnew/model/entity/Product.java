package com.example.jointperchasesnew.model.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "products")
public class Product extends BaseEntity {
    private String name;
    private Double price;
    private String description;
    private Set<GroupPurchase> purchases = new HashSet<>();

    @NotNull(message = "Product name cannot be null")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Positive (message = "The price should be positive")
    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    @NotNull(message = "Description cannot be null")
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @OneToMany(cascade = {CascadeType.ALL}, mappedBy = "product")
    @NotNull(message = "Group purchases cannot be null")
    public Set<GroupPurchase> getPurchases() {
        return purchases;
    }

    public void setPurchases(Set<GroupPurchase> purchases) {
        this.purchases = purchases;
    }
}
