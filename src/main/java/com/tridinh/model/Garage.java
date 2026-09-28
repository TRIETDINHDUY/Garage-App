package com.tridinh.model;

import com.tridinh.enums.GarageStatus;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "garage")
public class Garage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long garageId;

    @Column(unique = true)
    private String garageCode;

    private String garageName;
    private String phoneNumber;
    private String email;

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "VARCHAR(20)")
    private GarageStatus status = GarageStatus.ACTIVE;

    private BigDecimal rating;
    private LocalDate updatedDate;

    /** Inverse side of GarageAddress → Garage OneToOne */
    @OneToOne(mappedBy = "garage", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private GarageAddress address;

    @OneToMany(mappedBy = "garage", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<GarageService> garageServices = new ArrayList<>();

    public Garage() {}

    public Long getGarageId() { return garageId; }
    public void setGarageId(Long garageId) { this.garageId = garageId; }

    public String getGarageCode() { return garageCode; }
    public void setGarageCode(String garageCode) { this.garageCode = garageCode; }

    public String getGarageName() { return garageName; }
    public void setGarageName(String garageName) { this.garageName = garageName; }

    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public GarageStatus getStatus() { return status; }
    public void setStatus(GarageStatus status) { this.status = status; }

    public BigDecimal getRating() { return rating; }
    public void setRating(BigDecimal rating) { this.rating = rating; }

    public LocalDate getUpdatedDate() { return updatedDate; }
    public void setUpdatedDate(LocalDate updatedDate) { this.updatedDate = updatedDate; }

    public GarageAddress getAddress() { return address; }
    public void setAddress(GarageAddress address) { this.address = address; }

    public List<GarageService> getGarageServices() { return garageServices; }
    public void setGarageServices(List<GarageService> garageServices) { this.garageServices = garageServices; }
}
