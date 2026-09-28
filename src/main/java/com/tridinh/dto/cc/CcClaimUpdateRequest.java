package com.tridinh.dto.cc;

/**
 * Garage App to CC: notify CC of repair order status change.
 */
public record CcClaimUpdateRequest(
        String claimNumber,
        String claimId,
        Long repairOrderId,
        String repairOrderStatus,
        String vehicleVin,
        String garageName,
        String serviceCode,
        String notes
) {}
