package com.tridinh.model;

import jakarta.persistence.*;

/**
 * GARAGE_SERVICE join entity — a specific service offered by a specific garage.
 * RepairOrder references this to know which garage+service handles the repair.
 */
@Entity
@Table(name = "garage_service")
public class GarageService {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long garageServiceId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "garage_id", nullable = false)
    private Garage garage;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "service_id", nullable = false)
    private Service service;

    /** AVAILABLE or UNAVAILABLE */
    @Column(columnDefinition = "VARCHAR(20)")
    private String status = "AVAILABLE";

    public GarageService() {}

    public Long getGarageServiceId() { return garageServiceId; }
    public void setGarageServiceId(Long garageServiceId) { this.garageServiceId = garageServiceId; }

    public Garage getGarage() { return garage; }
    public void setGarage(Garage garage) { this.garage = garage; }

    public Service getService() { return service; }
    public void setService(Service service) { this.service = service; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
