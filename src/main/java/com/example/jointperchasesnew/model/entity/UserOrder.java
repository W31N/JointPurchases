package com.example.jointperchasesnew.model.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "user_orders")
public class UserOrder extends BaseEntity {
    private User user;
    private GroupPurchase groupPurchase;
    private Integer quantity;
    private Double totalPrice;

    public UserOrder(User user, GroupPurchase groupPurchase, Integer quantity) {
        this.user = user;
        this.groupPurchase = groupPurchase;
        this.quantity = quantity;
        this.totalPrice = quantity * groupPurchase.getProductPrice();
    }

    public UserOrder() {

    }

    @ManyToOne
    @JoinColumn(name = "user_id")
    @NotNull(message = "The user cannot be null")
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @ManyToOne
    @JoinColumn(name = "group_purchase_id")
    @NotNull(message = "A group purchase cannot be null")
    public GroupPurchase getGroupPurchase() {
        return groupPurchase;
    }

    public void setGroupPurchase(GroupPurchase groupPurchase) {
        this.groupPurchase = groupPurchase;
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
