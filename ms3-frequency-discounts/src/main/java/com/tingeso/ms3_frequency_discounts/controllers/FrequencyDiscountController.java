package com.tingeso.ms3_frequency_discounts.controllers;

import com.tingeso.ms3_frequency_discounts.entities.FrequencyDiscount;
import com.tingeso.ms3_frequency_discounts.services.FrequencyDiscountService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/frequency-discount")
public class FrequencyDiscountController {
    private final FrequencyDiscountService frequencyDiscountService;

    public FrequencyDiscountController(FrequencyDiscountService frequencyDiscountService) {
        this.frequencyDiscountService = frequencyDiscountService;
    }

    @GetMapping("/")
    public ResponseEntity<List<FrequencyDiscount>> getAll() {
        return ResponseEntity.ok(frequencyDiscountService.getAllDiscounts());
    }

    @GetMapping("/{visits}")
    public ResponseEntity<FrequencyDiscount> getDiscount(@PathVariable int visits) {
        return ResponseEntity.ok(frequencyDiscountService.getDiscount(visits));
    }
}
