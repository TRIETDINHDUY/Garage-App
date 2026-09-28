package com.tridinh.model;

import com.tridinh.enums.RepairOrderStatus;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "repair_order")
public class RepairOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long repairOrderId;

    private String claimNumber;
    private String claimId;

    /** Loss location from ClaimCenter */
    private String lossStreet;
    private String lossCity;
    private String lossState;

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "VARCHAR(20)")
    private RepairOrderStatus status = RepairOrderStatus.PENDING;

    private LocalDate completedDate;
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vehicle_id", nullable = false)
    private Vehicle vehicle;

    /** References the specific garage+service this repair order is assigned to */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "garage_service_id", nullable = false)
    private GarageService garageService;

    @OneToOne(mappedBy = "repairOrder", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Billing billing;

    @OneToMany(mappedBy = "repairOrder", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<RepairHistory> history = new ArrayList<>();

    public RepairOrder() {}

    public Long getRepairOrderId() { return repairOrderId; }
    public void setRepairOrderId(Long repairOrderId) { this.repairOrderId = repairOrderId; }

    public String getClaimNumber() { return claimNumber; }
    public void setClaimNumber(String claimNumber) { this.claimNumber = claimNumber; }

    public String getClaimId() { return claimId; }
    public void setClaimId(String claimId) { this.claimId = claimId; }

    public String getLossStreet() { return lossStreet; }
    public void setLossStreet(String lossStreet) { this.lossStreet = lossStreet; }

    public String getLossCity() { return lossCity; }
    public void setLossCity(String lossCity) { this.lossCity = lossCity; }

    public String getLossState() { return lossState; }
    public void setLossState(String lossState) { this.lossState = lossState; }

    public RepairOrderStatus getStatus() { return status; }
    public void setStatus(RepairOrderStatus status) { this.status = status; }

    public LocalDate getCompletedDate() { return completedDate; }
    public void setCompletedDate(LocalDate completedDate) { this.completedDate = completedDate; }

    public LocalDateTime getCreatedDate() { return createdDate; }
    public void setCreatedDate(LocalDateTime createdDate) { this.createdDate = createdDate; }

    public LocalDateTime getUpdatedDate() { return updatedDate; }
    public void setUpdatedDate(LocalDateTime updatedDate) { this.updatedDate = updatedDate; }

    public Vehicle getVehicle() { return vehicle; }
    public void setVehicle(Vehicle vehicle) { this.vehicle = vehicle; }

    public GarageService getGarageService() { return garageService; }
    public void setGarageService(GarageService garageService) { this.garageService = garageService; }

    public Billing getBilling() { return billing; }
    public void setBilling(Billing billing) { this.billing = billing; }

    public List<RepairHistory> getHistory() { return history; }
    public void setHistory(List<RepairHistory> history) { this.history = history; }
}
