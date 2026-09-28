package com.tridinh.service;

import com.tridinh.dto.EstimateRequest;
import com.tridinh.dto.EstimateResponse;
import com.tridinh.dto.cc.CcEstimateNotification;
import com.tridinh.enums.BillingStatus;
import com.tridinh.model.Billing;
import com.tridinh.model.RepairOrder;
import com.tridinh.repository.BillingRepository;
import com.tridinh.repository.RepairOrderRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BillingService {

    private final BillingRepository billingRepository;
    private final RepairOrderRepository repairOrderRepository;
    private final ClaimCenterClientService ccClientService;

    public BillingService(BillingRepository billingRepository,
                           RepairOrderRepository repairOrderRepository,
                           ClaimCenterClientService ccClientService) {
        this.billingRepository = billingRepository;
        this.repairOrderRepository = repairOrderRepository;
        this.ccClientService = ccClientService;
    }

    /**
     * CC → App: create billing estimate for a repair order.
     */
    @Transactional
    public EstimateResponse createEstimate(EstimateRequest req) {
        RepairOrder order = repairOrderRepository.findById(req.repairOrderId())
                .orElseThrow(() -> new RuntimeException("RepairOrder not found: " + req.repairOrderId()));

        BigDecimal labor = req.laborAmount() == null ? BigDecimal.ZERO : req.laborAmount();
        BigDecimal parts = req.partsAmount() == null ? BigDecimal.ZERO : req.partsAmount();
        BigDecimal tax   = req.taxAmount()   == null ? BigDecimal.ZERO : req.taxAmount();
        BigDecimal total = labor.add(parts).add(tax);

        Billing billing = new Billing();
        billing.setLaborAmount(labor);
        billing.setPartsAmount(parts);
        billing.setTaxAmount(tax);
        billing.setTotalAmount(total);
        billing.setStatus(BillingStatus.PENDING);
        billing.setCreatedDate(LocalDateTime.now());
        billing.setRepairOrder(order);
        billing = billingRepository.save(billing);

        // Notify CC of new estimate (outbound)
        ccClientService.notifyEstimate(new CcEstimateNotification(
                order.getClaimNumber(),
                order.getClaimId(),
                order.getRepairOrderId(),
                billing.getBillingId(),
                billing.getLaborAmount(),
                billing.getPartsAmount(),
                billing.getTaxAmount(),
                billing.getTotalAmount(),
                billing.getCreatedDate()
        ));

        return toResponse(billing, order);
    }

    @Transactional(readOnly = true)
    public List<EstimateResponse> findByClaimNumber(String claimNumber) {
        return repairOrderRepository.findByClaimNumber(claimNumber).stream()
                .filter(o -> o.getBilling() != null)
                .map(o -> toResponse(o.getBilling(), o))
                .collect(Collectors.toList());
    }

    private EstimateResponse toResponse(Billing b, RepairOrder o) {
        return new EstimateResponse(
                b.getBillingId(),
                o.getRepairOrderId(),
                o.getClaimNumber(),
                b.getLaborAmount(),
                b.getPartsAmount(),
                b.getTaxAmount(),
                b.getTotalAmount(),
                b.getStatus().name(),
                b.getCreatedDate()
        );
    }
}
