package com.tridinh.dto;

/**
 * App → CC: garage info with address returned from search.
 */
public record GarageSearchResponse(
        Long garageId,
        String garageCode,
        String garageName,
        String phoneNumber,
        String email,
        String status,
        String rating,
        // address
        String street,
        String city,
        String state,
        String postalCode,
        String country,
        // services offered (comma-separated codes)
        String availableServices
) {}
