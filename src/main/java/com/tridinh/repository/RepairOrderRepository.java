package com.tridinh.repository;

import com.tridinh.enums.RepairOrderStatus;
import com.tridinh.model.RepairOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RepairOrderRepository extends JpaRepository<RepairOrder, Long> {

    List<RepairOrder> findByClaimNumber(String claimNumber);

    @Query("SELECT ro FROM RepairOrder ro " +
           "JOIN FETCH ro.vehicle v " +
           "JOIN FETCH ro.garageService gs " +
           "JOIN FETCH gs.service " +
           "WHERE gs.garage.garageId = :garageId " +
           "ORDER BY ro.createdDate DESC")
    List<RepairOrder> findByGarageIdWithDetails(@Param("garageId") Long garageId);

    List<RepairOrder> findByGarageService_Garage_GarageIdAndStatus(Long garageId, RepairOrderStatus status);

    @Query("SELECT ro FROM RepairOrder ro " +
           "JOIN FETCH ro.vehicle v " +
           "JOIN FETCH ro.garageService gs " +
           "JOIN FETCH gs.service " +
           "WHERE ro.repairOrderId = :id")
    Optional<RepairOrder> findByIdWithDetails(@Param("id") Long id);
}
