package com.tridinh.dto.cc;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Garage App to CC: notify CC of a new billing estimate.
 */
public record CcEstimateNotification(
        String claimNumber,
        String claimId,
        Long repairOrderId,
        Long billingId,
        BigDecimal laborAmount,
        BigDecimal partsAmount,
        BigDecimal taxAmount,
        BigDecimal totalAmount,
        LocalDateTime createdDate
) {}
