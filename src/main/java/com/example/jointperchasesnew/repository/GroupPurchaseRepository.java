package com.example.jointperchasesnew.repository;

import com.example.jointperchasesnew.model.entity.GroupPurchase;
import com.example.jointperchasesnew.model.enums.PurchaseStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface GroupPurchaseRepository extends JpaRepository<GroupPurchase, UUID> {

}