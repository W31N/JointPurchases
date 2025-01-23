package com.example.jointperchasesnew.repository;

import com.example.jointperchasesnew.model.entity.GroupPurchase;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


import java.util.Optional;
import java.util.UUID;

@Repository
public interface GroupPurchaseRepository extends JpaRepository<GroupPurchase, UUID> {
    Optional<GroupPurchase> findByProductName(String productName);
}