package com.example.jointperchasesnew.fetcher;

import com.example.jointperchasesnew.dto.OrderCancellationDto;
import com.example.jointperchasesnew.dto.UserOrderDto;
import com.example.jointperchasesnew.model.entity.UserOrder;
import com.example.jointperchasesnew.service.UserOrderService;
import com.netflix.graphql.dgs.DgsComponent;
import com.netflix.graphql.dgs.DgsMutation;
import com.netflix.graphql.dgs.InputArgument;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;

@DgsComponent
public class UserOrderMutation {
    private UserOrderService userOrderService;

    @DgsMutation
    public UserOrder addUserOrder(@InputArgument("userOrder") UserOrderDto userOrderDto) {
        return userOrderService.getUserOrderDataForAdd(userOrderDto);
    }

    @DgsMutation
    public boolean cancelOrder(@InputArgument("orderCancellation") OrderCancellationDto orderCancellationDto) {
        try {
            userOrderService.cancelOrder(orderCancellationDto);
            return true;
        } catch (EntityNotFoundException e) {
            return false;
        } catch (Exception e) {
            throw new RuntimeException("Error occurred while cancelling the order", e);
        }
    }

    @Autowired
    public void setUserOrderService(UserOrderService userOrderService) {
        this.userOrderService = userOrderService;
    }
}
