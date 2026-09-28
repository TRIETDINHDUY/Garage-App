package com.tridinh.repository;

import com.tridinh.model.Billing;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BillingRepository extends JpaRepository<Billing, Long> {
    Optional<Billing> findByRepairOrder_RepairOrderId(Long repairOrderId);
}
