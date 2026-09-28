package com.tridinh.dto;

import java.time.LocalDate;

/**
 * CC → App: deliver a vehicle for repair.
 * Creates a RepairOrder linked to the specified garageServiceId.
 */
public record ReceiveVehicleRequest(
        // vehicle info
        String vehicleVin,
        String vehicleMake,
        String vehicleModel,
        Integer vehicleYear,
        // customer info
        String customerName,
        String customerPhone,
        String customerEmail,
        // assignment
        Long garageServiceId,
        // claim center info
        String claimNumber,
        String claimId,
        String lossStreet,
        String lossCity,
        String lossState,
        LocalDate serviceCompleteDate
) {}
