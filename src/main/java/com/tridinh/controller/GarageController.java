package com.tridinh.controller;

import com.tridinh.model.Garage;
import com.tridinh.model.GarageService;
import com.tridinh.model.RepairOrder;
import com.tridinh.repository.GarageServiceRepository;
import com.tridinh.service.GarageSearchService;
import com.tridinh.service.RepairOrderService;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/garages")
public class GarageController {

    private final GarageSearchService garageSearchService;
    private final RepairOrderService repairOrderService;
    private final GarageServiceRepository garageServiceRepository;

    public GarageController(GarageSearchService garageSearchService,
                             RepairOrderService repairOrderService,
                             GarageServiceRepository garageServiceRepository) {
        this.garageSearchService = garageSearchService;
        this.repairOrderService = repairOrderService;
        this.garageServiceRepository = garageServiceRepository;
    }

    @Transactional(readOnly = true)
    @GetMapping("/{id}")
    public String garageDetail(@PathVariable Long id, Model model) {
        Garage garage = garageSearchService.findById(id);
        List<GarageService> services = garageServiceRepository.findByGarage_GarageId(id);
        List<RepairOrder> repairOrders = repairOrderService.findByGarage(id);

        long activeOrders = repairOrders.stream()
                .filter(o -> o.getStatus().name().equals("PENDING") || o.getStatus().name().equals("IN_PROGRESS"))
                .count();

        model.addAttribute("garage", garage);
        model.addAttribute("services", services);
        model.addAttribute("repairOrders", repairOrders);
        model.addAttribute("activeOrders", activeOrders);
        return "garage-detail";
    }
}
