package com.example.jointperchasesnew.service;

import com.example.jointperchasesnew.dto.GroupPurchaseDto;
import com.example.jointperchasesnew.model.entity.GroupPurchase;
import com.example.jointperchasesnew.representation.GroupPurchaseRepresentation;
import org.springframework.hateoas.CollectionModel;

public interface GroupPurchaseService {
    public GroupPurchaseRepresentation createGroupPurchase(GroupPurchaseDto groupPurchaseDto);
    public GroupPurchase getGroupPurchaseDataForCreate (GroupPurchaseDto groupPurchaseDto);
    public GroupPurchaseRepresentation getGroupPurchaseByProductName(String productName);
    public GroupPurchase getGroupPurchaseDataByProductName (String productName);
    public CollectionModel<GroupPurchaseRepresentation> getAllGroupPurchases();
    public Iterable<GroupPurchase> getAllDataGroupPurchases();
    public void cancelGroupPurchase(String productName);
}
