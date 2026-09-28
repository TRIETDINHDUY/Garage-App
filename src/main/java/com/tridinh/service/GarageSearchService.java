package com.tridinh.service;

import com.tridinh.dto.GarageSearchRequest;
import com.tridinh.dto.GarageSearchResponse;
import com.tridinh.model.Garage;
import com.tridinh.model.GarageAddress;
import com.tridinh.repository.GarageRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class GarageSearchService {

    private final GarageRepository garageRepository;

    public GarageSearchService(GarageRepository garageRepository) {
        this.garageRepository = garageRepository;
    }

    @Transactional(readOnly = true)
    public List<Garage> findAll() {
        return garageRepository.findAllWithAddress();
    }

    @Transactional(readOnly = true)
    public Garage findById(Long id) {
        return garageRepository.findByIdWithAddress(id)
                .orElseThrow(() -> new RuntimeException("Garage not found: " + id));
    }

    /**
     * CC search: find garages matching loss location and service.
     */
    @Transactional(readOnly = true)
    public List<GarageSearchResponse> searchForClaimCenter(GarageSearchRequest req) {
        List<Garage> garages;

        String street = req.lossStreet();
        if (street != null && !street.isBlank()) {
            garages = garageRepository.searchByCityStateStreetAndService(
                    req.lossCity(), req.lossState(), street, req.serviceCode());
        } else {
            garages = garageRepository.searchByCityStateAndService(
                    req.lossCity(), req.lossState(), req.serviceCode());
        }

        return garages.stream().map(this::toSearchResponse).collect(Collectors.toList());
    }

    /**
     * UI Search: flexible, all params optional.
     * name/city/state → JPQL LIKE filter in DB.
     * serviceCode     → filtered in-memory (simpler than JPQL outer join logic).
     */
    @Transactional(readOnly = true)
    public List<Garage> searchUI(String name, String city, String state, String serviceCode) {
        List<Garage> results = garageRepository.searchUI(
                blank(name), blank(city), blank(state));

        if (serviceCode != null && !serviceCode.isBlank()) {
            String code = serviceCode.trim().toUpperCase();
            results = results.stream()
                    .filter(g -> g.getGarageServices() != null &&
                            g.getGarageServices().stream()
                                    .anyMatch(gs -> "AVAILABLE".equals(gs.getStatus())
                                            && gs.getService().getServiceCode().equalsIgnoreCase(code)))
                    .collect(Collectors.toList());
        }
        return results;
    }

    /** Return null if blank so JPQL can skip the condition */
    private String blank(String s) {
        return (s == null || s.isBlank()) ? null : s.trim();
    }

    private GarageSearchResponse toSearchResponse(Garage g) {
        GarageAddress addr = g.getAddress();
        String services = g.getGarageServices() == null ? "" :
                g.getGarageServices().stream()
                        .filter(gs -> "AVAILABLE".equals(gs.getStatus()))
                        .map(gs -> gs.getService().getServiceCode())
                        .collect(Collectors.joining(", "));

        return new GarageSearchResponse(
                g.getGarageId(),
                g.getGarageCode(),
                g.getGarageName(),
                g.getPhoneNumber(),
                g.getEmail(),
                g.getStatus() == null ? "" : g.getStatus().name(),
                g.getRating() == null ? "" : g.getRating().toPlainString(),
                addr == null ? "" : addr.getStreet(),
                addr == null ? "" : addr.getCity(),
                addr == null ? "" : addr.getState(),
                addr == null ? "" : addr.getPostalCode(),
                addr == null ? "" : addr.getCountry(),
                services
        );
    }
}
