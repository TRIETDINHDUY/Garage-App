package com.tridinh.dto;

import java.time.LocalDate;

/**
 * CC → App: search garages by loss location + service needed.
 */
public record GarageSearchRequest(
        String lossStreet,   // optional
        String lossCity,     // required
        String lossState,    // required
        String serviceCode,  // required - e.g. BODY_REPAIR
        LocalDate serviceCompleteDate
) {}
