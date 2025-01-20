package com.example.jointperchasesnew.repository;

import com.example.jointperchasesnew.model.entity.UserOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface UserOrderRepository extends JpaRepository<UserOrder, UUID> {

}
