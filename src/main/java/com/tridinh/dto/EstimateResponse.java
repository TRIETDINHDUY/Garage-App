package com.tridinh.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * App → CC: billing estimate response.
 */
public record EstimateResponse(
        Long billingId,
        Long repairOrderId,
        String claimNumber,
        BigDecimal laborAmount,
        BigDecimal partsAmount,
        BigDecimal taxAmount,
        BigDecimal totalAmount,
        String status,
        LocalDateTime createdDate
) {}
