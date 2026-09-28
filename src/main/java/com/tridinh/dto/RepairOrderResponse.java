package com.tridinh.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * App → CC: repair order status response.
 */
public record RepairOrderResponse(
        Long repairOrderId,
        String claimNumber,
        String claimId,
        String status,
        String lossStreet,
        String lossCity,
        String lossState,
        LocalDate completedDate,
        LocalDateTime createdDate,
        // vehicle
        Long vehicleId,
        String vehicleVin,
        String vehicleMake,
        String vehicleModel,
        Integer vehicleYear,
        // garage+service
        Long garageServiceId,
        Long garageId,
        String garageName,
        String serviceCode,
        String serviceName
) {}
