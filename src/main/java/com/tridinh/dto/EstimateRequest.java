package com.tridinh.dto;

import java.math.BigDecimal;

/**
 * CC → App: submit a billing estimate for a repair order.
 */
public record EstimateRequest(
        Long repairOrderId,
        BigDecimal laborAmount,
        BigDecimal partsAmount,
        BigDecimal taxAmount,
        String notes
) {}
