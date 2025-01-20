package com.example.jointperchasesnew.model.enums;

import com.fasterxml.jackson.annotation.JsonValue;

public enum PurchaseStatus {
    OPEN(1),
    CLOSED(2),
    COMPLETED(3);

    private int value;

    PurchaseStatus(int value){
        this.value = value;
    }

    @JsonValue
    public int getValue() {
        return value;
    }
}
