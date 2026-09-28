package com.tridinh.controller.api;

import com.tridinh.dto.*;
import com.tridinh.service.BillingService;
import com.tridinh.service.GarageSearchService;
import com.tridinh.service.RepairOrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/garage")
@Tag(name = "Garage API (for ClaimCenter)", description = "REST endpoints hosted on this Garage App, called BY Guidewire ClaimCenter")
public class ClaimCenterController {

    private final GarageSearchService garageSearchService;
    private final RepairOrderService repairOrderService;
    private final BillingService billingService;

    public ClaimCenterController(GarageSearchService garageSearchService,
                                  RepairOrderService repairOrderService,
                                  BillingService billingService) {
        this.garageSearchService = garageSearchService;
        this.repairOrderService = repairOrderService;
        this.billingService = billingService;
    }

    /**
     * CC searches for available garages near the loss location that offer the needed service.
     *
     * POST /api/garage/garages/search
     * Body: { lossStreet, lossCity, lossState, serviceCode, serviceCompleteDate }
     */
    @PostMapping("/garages/search")
    public ResponseEntity<List<GarageSearchResponse>> searchGarages(
            @RequestBody GarageSearchRequest request) {
        List<GarageSearchResponse> results = garageSearchService.searchForClaimCenter(request);
        return ResponseEntity.ok(results);
    }

    /**
     * CC delivers a vehicle to a garage for repair.
     *
     * POST /api/garage/repair-orders
     * Body: { vehicleVin, ..., garageServiceId, claimNumber, lossCity, ... }
     */
    @PostMapping("/repair-orders")
    public ResponseEntity<RepairOrderResponse> receiveVehicle(
            @RequestBody ReceiveVehicleRequest request) {
        RepairOrderResponse response = repairOrderService.receiveVehicle(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * CC submits a billing estimate for a repair order.
     *
     * POST /api/garage/estimates
     */
    @PostMapping("/estimates")
    public ResponseEntity<EstimateResponse> createEstimate(
            @RequestBody EstimateRequest request) {
        EstimateResponse response = billingService.createEstimate(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // ─────────────────────────────────────────────
    // OUTBOUND: App → CC (CC polls these)
    // ─────────────────────────────────────────────

    /**
     * CC polls repair order status.
     *
     * GET /api/garage/repair-orders/{id}/status
     */
    @GetMapping("/repair-orders/{id}/status")
    public ResponseEntity<RepairOrderResponse> getRepairOrderStatus(@PathVariable Long id) {
        return ResponseEntity.ok(repairOrderService.getStatus(id));
    }

    /**
     * CC updates repair order status.
     *
     * PUT /api/garage/repair-orders/{id}/status?status=IN_PROGRESS&changedBy=Garage
     */
    @PutMapping("/repair-orders/{id}/status")
    public ResponseEntity<RepairOrderResponse> updateStatus(
            @PathVariable Long id,
            @RequestParam String status,
            @RequestParam(required = false) String changedBy) {
        return ResponseEntity.ok(repairOrderService.updateStatus(id, status, changedBy));
    }

    /**
     * CC polls all repair orders for a claim.
     *
     * GET /api/garage/repair-orders/claim/{claimNumber}
     */
    @GetMapping("/repair-orders/claim/{claimNumber}")
    public ResponseEntity<List<RepairOrderResponse>> getByClaimNumber(
            @PathVariable String claimNumber) {
        return ResponseEntity.ok(repairOrderService.findByClaimNumber(claimNumber));
    }

    /**
     * CC polls estimates for a claim number.
     *
     * GET /api/garage/estimates/claim/{claimNumber}
     */
    @GetMapping("/estimates/claim/{claimNumber}")
    public ResponseEntity<List<EstimateResponse>> getEstimatesByClaimNumber(
            @PathVariable String claimNumber) {
        return ResponseEntity.ok(billingService.findByClaimNumber(claimNumber));
    }
}
