package com.tridinh.controller;

import com.tridinh.model.Garage;
import com.tridinh.repository.ServiceRepository;
import com.tridinh.service.GarageSearchService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class HomeController {

    private final GarageSearchService garageSearchService;
    private final ServiceRepository serviceRepository;

    public HomeController(GarageSearchService garageSearchService,
                          ServiceRepository serviceRepository) {
        this.garageSearchService = garageSearchService;
        this.serviceRepository = serviceRepository;
    }

    @GetMapping("/")
    public String homepage(Model model) {
        List<Garage> garages = garageSearchService.findAll();
        model.addAttribute("garages", garages);
        model.addAttribute("totalGarages", garages.size());
        model.addAttribute("services", serviceRepository.findAll());
        model.addAttribute("searchMode", false);
        return "index";
    }

    /**
     * UI search: GET /?city=Los+Angeles&state=CA&serviceCode=BODY_REPAIR&name=Auto
     * All params are optional — combine any you like.
     */
    @GetMapping(value = "/", params = {"search"})
    public String search(@RequestParam(required = false) String name,
                         @RequestParam(required = false) String city,
                         @RequestParam(required = false) String state,
                         @RequestParam(required = false) String serviceCode,
                         Model model) {

        List<Garage> results = garageSearchService.searchUI(name, city, state, serviceCode);
        model.addAttribute("garages", results);
        model.addAttribute("totalGarages", garageSearchService.findAll().size());
        model.addAttribute("services", serviceRepository.findAll());
        model.addAttribute("searchMode", true);
        model.addAttribute("resultCount", results.size());
        // echo back form values
        model.addAttribute("q_name",        name        != null ? name        : "");
        model.addAttribute("q_city",        city        != null ? city        : "");
        model.addAttribute("q_state",       state       != null ? state       : "");
        model.addAttribute("q_serviceCode", serviceCode != null ? serviceCode : "");
        return "index";
    }
}
