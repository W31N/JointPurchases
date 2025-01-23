package com.example.jointperchasesnew.model.entity;

import com.example.jointperchasesnew.model.enums.PurchaseStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "group_purchases")
public class GroupPurchase extends BaseEntity {
    private String productName;
    private Double productPrice;
    private LocalDateTime deadline;
    private String description;
    private Integer totalQuantity = 0;
    private Integer maxQuantity;
    private PurchaseStatus status = PurchaseStatus.OPEN;
    private Set<UserOrder> orders = new HashSet<>();

    public void addOrder(UserOrder order) {
        if (this.totalQuantity + order.getQuantity() > maxQuantity) {
            throw new IllegalStateException("Превышено максимальное количество товара для закупки!");
        }
        this.orders.add(order);
        this.totalQuantity += order.getQuantity();
    }

    @NotNull(message = "Product name cannot be null")
    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    @Positive(message = "Product price must be positive")
    public Double getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(Double productPrice) {
        this.productPrice = productPrice;
    }

    @NotNull(message = "Description cannot be null")
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @NotNull(message = "The deadline cannot be null")
    public LocalDateTime getDeadline() {
        return deadline;
    }

    public void setDeadline(LocalDateTime deadline) {
        this.deadline = deadline;
    }

    @PositiveOrZero(message = "The total quantity cannot be less than 0")
    public Integer getTotalQuantity() {
        return totalQuantity;
    }

    public void setTotalQuantity(Integer totalQuantity) {
        this.totalQuantity = totalQuantity;
    }

    @PositiveOrZero(message = "The max quantity cannot be less than 0")
    public Integer getMaxQuantity() {
        return maxQuantity;
    }

    public void setMaxQuantity(Integer maxQuantity) {
        this.maxQuantity = maxQuantity;
    }
    @Enumerated
    @NotNull(message = "The status cannot be null")
    public PurchaseStatus getStatus() {
        return status;
    }

    public void setStatus(PurchaseStatus status) {
        this.status = status;
    }

    @OneToMany(cascade = {CascadeType.ALL}, mappedBy = "groupPurchase")
    @NotNull(message = "User orders cannot be null")
    public Set<UserOrder> getOrders() {
        return orders;
    }

    public void setOrders(Set<UserOrder> orders) {
        this.orders = orders;
    }

    public boolean canBeClosed() {
        return this.totalQuantity >= this.maxQuantity || LocalDateTime.now().isAfter(this.deadline);
    }
}
