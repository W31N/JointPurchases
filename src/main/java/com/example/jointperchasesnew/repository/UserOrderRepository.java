package com.example.jointperchasesnew.repository;

import com.example.jointperchasesnew.model.entity.User;
import com.example.jointperchasesnew.model.entity.UserOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserOrderRepository extends JpaRepository<UserOrder, UUID> {
    Optional<UserOrder> findByUser_UsernameAndGroupPurchase_ProductName(String username, String productName);
    List<UserOrder> findByUserUsername(String username);

    List<UserOrder> findByGroupPurchaseProductName(String productName);

    @Query("SELECT DISTINCT u FROM User u JOIN UserOrder o ON u.id = o.user.id WHERE o.groupPurchase.id = :groupPurchaseId")
    List<User> findUsersByGroupPurchaseId(@Param("groupPurchaseId") UUID groupPurchaseId);

}
