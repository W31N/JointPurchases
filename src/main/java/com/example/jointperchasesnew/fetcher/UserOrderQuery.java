package com.example.jointperchasesnew.fetcher;

import com.example.jointperchasesnew.model.entity.UserOrder;
import com.example.jointperchasesnew.service.UserOrderService;
import com.netflix.graphql.dgs.DgsComponent;
import com.netflix.graphql.dgs.DgsQuery;
import com.netflix.graphql.dgs.InputArgument;
import org.springframework.beans.factory.annotation.Autowired;

@DgsComponent
public class UserOrderQuery {
    private UserOrderService userOrderService;

    @DgsQuery
    public Iterable<UserOrder> allUserOrders() {
        return userOrderService.getAllDataUserOrders();
    }

    @DgsQuery
    public Iterable<UserOrder> userOrders(@InputArgument("username") String username) {
        return userOrderService.getDataUserOrders(username);
    }

    @DgsQuery
    public Iterable<UserOrder> groupPurchaseOrders(@InputArgument("productName") String productName) {
        return userOrderService.getDataOrdersForGroupPurchase(productName);
    }

    @Autowired
    public void setUserOrderService(UserOrderService userOrderService) {
        this.userOrderService = userOrderService;
    }
}
