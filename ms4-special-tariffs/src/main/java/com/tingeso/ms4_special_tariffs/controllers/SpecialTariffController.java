package com.tingeso.ms4_special_tariffs.controllers;

import com.tingeso.ms4_special_tariffs.entities.SpecialTariff;
import com.tingeso.ms4_special_tariffs.services.SpecialTariffService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/special-tariff")
public class SpecialTariffController {
    private final SpecialTariffService specialTariffService;

    public SpecialTariffController(SpecialTariffService specialTariffService) {
        this.specialTariffService = specialTariffService;
    }

    @GetMapping("/")
    public ResponseEntity<List<SpecialTariff>> getSpecialTariffs() {
        return ResponseEntity.ok(specialTariffService.getAll());
    }

    @GetMapping("/{textId}")
    public ResponseEntity<SpecialTariff> getSpecialTariff(@PathVariable String textId) {
        return ResponseEntity.ok(specialTariffService.getByTextId(textId));
    }

    @GetMapping("/festive/{dayOfYear}")
    public ResponseEntity<SpecialTariff> getFestiveSpecialTariff(@PathVariable int dayOfYear) {
        return ResponseEntity.ok(specialTariffService.getFestiveValue(dayOfYear));
    }
}
