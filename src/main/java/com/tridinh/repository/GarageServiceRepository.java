package com.tridinh.repository;

import com.tridinh.model.GarageService;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GarageServiceRepository extends JpaRepository<GarageService, Long> {
    List<GarageService> findByGarage_GarageId(Long garageId);
}
