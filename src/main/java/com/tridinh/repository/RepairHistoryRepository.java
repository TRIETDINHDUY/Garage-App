package com.tridinh.repository;

import com.tridinh.model.RepairHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RepairHistoryRepository extends JpaRepository<RepairHistory, Long> {
    List<RepairHistory> findByRepairOrder_RepairOrderIdOrderByChangedDateDesc(Long repairOrderId);
}
