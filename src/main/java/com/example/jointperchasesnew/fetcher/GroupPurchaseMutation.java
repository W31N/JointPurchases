package com.example.jointperchasesnew.fetcher;

import com.example.jointperchasesnew.dto.GroupPurchaseDto;
import com.example.jointperchasesnew.model.entity.GroupPurchase;
import com.example.jointperchasesnew.service.GroupPurchaseService;
import com.netflix.graphql.dgs.DgsComponent;
import com.netflix.graphql.dgs.DgsMutation;
import com.netflix.graphql.dgs.InputArgument;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;

@DgsComponent
public class GroupPurchaseMutation {
    private GroupPurchaseService groupPurchaseService;

    @DgsMutation
    public GroupPurchase createGroupPurchase(@InputArgument(name = "input") GroupPurchaseDto groupPurchaseDto) {
        return groupPurchaseService.getGroupPurchaseDataForCreate(groupPurchaseDto);
    }

    @DgsMutation
    public boolean cancelGroupPurchase(@InputArgument("productName") String productName) {
        try {
            groupPurchaseService.cancelGroupPurchase(productName);
            return true;
        } catch (EntityNotFoundException e) {
            return false;
        } catch (Exception e) {
            throw new RuntimeException("Error occurred while cancelling the group purchase", e);
        }
    }

    @Autowired
    public void setGroupPurchaseService(GroupPurchaseService groupPurchaseService) {
        this.groupPurchaseService = groupPurchaseService;
    }
}
