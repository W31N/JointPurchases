package com.example.jointperchasesnew.fetcher;

import com.example.jointperchasesnew.model.entity.GroupPurchase;
import com.example.jointperchasesnew.service.GroupPurchaseService;
import com.netflix.graphql.dgs.DgsComponent;
import com.netflix.graphql.dgs.DgsQuery;
import com.netflix.graphql.dgs.InputArgument;
import org.springframework.beans.factory.annotation.Autowired;

@DgsComponent
public class GroupPurchaseQuery {
    private GroupPurchaseService groupPurchaseService;

    @DgsQuery
    public GroupPurchase groupPurchase(@InputArgument(name = "productName") String productName) {
        return groupPurchaseService.getGroupPurchaseDataByProductName(productName);
    }

    @DgsQuery
    public Iterable<GroupPurchase> allGroupPurchases() {
        return groupPurchaseService.getAllDataGroupPurchases();
    }

    @Autowired
    public void setGroupPurchaseService(GroupPurchaseService groupPurchaseService) {
        this.groupPurchaseService = groupPurchaseService;
    }
}
