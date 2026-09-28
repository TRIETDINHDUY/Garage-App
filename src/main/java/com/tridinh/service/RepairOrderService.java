package com.tridinh.service;

import com.tridinh.dto.ReceiveVehicleRequest;
import com.tridinh.dto.RepairOrderResponse;
import com.tridinh.dto.cc.CcClaimUpdateRequest;
import com.tridinh.enums.RepairOrderStatus;
import com.tridinh.model.*;
import com.tridinh.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class RepairOrderService {

    private final RepairOrderRepository repairOrderRepository;
    private final VehicleRepository vehicleRepository;
    private final CustomerRepository customerRepository;
    private final GarageServiceRepository garageServiceRepository;
    private final RepairHistoryRepository repairHistoryRepository;
    private final ClaimCenterClientService ccClientService;

    public RepairOrderService(RepairOrderRepository repairOrderRepository,
                               VehicleRepository vehicleRepository,
                               CustomerRepository customerRepository,
                               GarageServiceRepository garageServiceRepository,
                               RepairHistoryRepository repairHistoryRepository,
                               ClaimCenterClientService ccClientService) {
        this.repairOrderRepository = repairOrderRepository;
        this.vehicleRepository = vehicleRepository;
        this.customerRepository = customerRepository;
        this.garageServiceRepository = garageServiceRepository;
        this.repairHistoryRepository = repairHistoryRepository;
        this.ccClientService = ccClientService;
    }

    /**
     * CC → App: receive a vehicle and create a repair order.
     */
    @Transactional
    public RepairOrderResponse receiveVehicle(ReceiveVehicleRequest req) {
        // Find or create customer
        Customer customer = customerRepository.findByPhoneNumber(req.customerPhone())
                .orElseGet(() -> {
                    Customer c = new Customer();
                    c.setCustomerName(req.customerName());
                    c.setPhoneNumber(req.customerPhone());
                    c.setEmail(req.customerEmail());
                    return customerRepository.save(c);
                });

        // Find or create vehicle
        Vehicle vehicle = vehicleRepository.findByVehicleVin(req.vehicleVin())
                .orElseGet(() -> {
                    Vehicle v = new Vehicle();
                    v.setVehicleVin(req.vehicleVin());
                    v.setVehicleMake(req.vehicleMake());
                    v.setVehicleModel(req.vehicleModel());
                    v.setVehicleYear(req.vehicleYear());
                    v.setOwner(customer);
                    return vehicleRepository.save(v);
                });

        // Load the garage service assignment
        GarageService garageService = garageServiceRepository.findById(req.garageServiceId())
                .orElseThrow(() -> new RuntimeException("GarageService not found: " + req.garageServiceId()));

        // Create repair order
        RepairOrder order = new RepairOrder();
        order.setClaimNumber(req.claimNumber());
        order.setClaimId(req.claimId());
        order.setLossStreet(req.lossStreet());
        order.setLossCity(req.lossCity());
        order.setLossState(req.lossState());
        order.setCompletedDate(req.serviceCompleteDate());
        order.setStatus(RepairOrderStatus.PENDING);
        order.setCreatedDate(LocalDateTime.now());
        order.setUpdatedDate(LocalDateTime.now());
        order.setVehicle(vehicle);
        order.setGarageService(garageService);
        order = repairOrderRepository.save(order);

        // Record initial history
        RepairHistory hist = new RepairHistory();
        hist.setOldStatus(null);
        hist.setNewStatus(RepairOrderStatus.PENDING.name());
        hist.setChangedDate(LocalDateTime.now());
        hist.setChangedBy("ClaimCenter");
        hist.setRepairOrder(order);
        repairHistoryRepository.save(hist);

        return toResponse(order);
    }

    /**
     * CC → App: update repair order status.
     */
    @Transactional
    public RepairOrderResponse updateStatus(Long repairOrderId, String newStatus, String changedBy) {
        RepairOrder order = repairOrderRepository.findByIdWithDetails(repairOrderId)
                .orElseThrow(() -> new RuntimeException("RepairOrder not found: " + repairOrderId));

        String oldStatus = order.getStatus().name();
        order.setStatus(RepairOrderStatus.valueOf(newStatus.toUpperCase()));
        order.setUpdatedDate(LocalDateTime.now());
        repairOrderRepository.save(order);

        RepairHistory hist = new RepairHistory();
        hist.setOldStatus(oldStatus);
        hist.setNewStatus(newStatus.toUpperCase());
        hist.setChangedDate(LocalDateTime.now());
        hist.setChangedBy(changedBy != null ? changedBy : "ClaimCenter");
        hist.setRepairOrder(order);
        repairHistoryRepository.save(hist);

        // Notify CC of status change (outbound)
        ccClientService.notifyStatusUpdate(new CcClaimUpdateRequest(
                order.getClaimNumber(),
                order.getClaimId(),
                order.getRepairOrderId(),
                newStatus.toUpperCase(),
                order.getVehicle() != null ? order.getVehicle().getVehicleVin() : null,
                order.getGarageService() != null && order.getGarageService().getGarage() != null
                        ? order.getGarageService().getGarage().getGarageName() : null,
                order.getGarageService() != null && order.getGarageService().getService() != null
                        ? order.getGarageService().getService().getServiceCode() : null,
                "Status updated by: " + (changedBy != null ? changedBy : "System")
        ));

        return toResponse(order);
    }

    @Transactional(readOnly = true)
    public RepairOrderResponse getStatus(Long repairOrderId) {
        RepairOrder order = repairOrderRepository.findByIdWithDetails(repairOrderId)
                .orElseThrow(() -> new RuntimeException("RepairOrder not found: " + repairOrderId));
        return toResponse(order);
    }

    @Transactional(readOnly = true)
    public List<RepairOrderResponse> findByClaimNumber(String claimNumber) {
        return repairOrderRepository.findByClaimNumber(claimNumber)
                .stream().map(this::toResponse).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<RepairOrder> findByGarage(Long garageId) {
        return repairOrderRepository.findByGarageIdWithDetails(garageId);
    }

    private RepairOrderResponse toResponse(RepairOrder o) {
        Vehicle v = o.getVehicle();
        GarageService gs = o.getGarageService();
        Garage g = gs.getGarage();
        return new RepairOrderResponse(
                o.getRepairOrderId(),
                o.getClaimNumber(),
                o.getClaimId(),
                o.getStatus().name(),
                o.getLossStreet(),
                o.getLossCity(),
                o.getLossState(),
                o.getCompletedDate(),
                o.getCreatedDate(),
                v.getVehicleId(),
                v.getVehicleVin(),
                v.getVehicleMake(),
                v.getVehicleModel(),
                v.getVehicleYear(),
                gs.getGarageServiceId(),
                g.getGarageId(),
                g.getGarageName(),
                gs.getService().getServiceCode(),
                gs.getService().getServiceName()
        );
    }
}
