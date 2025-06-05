package com.tingeso.ms2_quantity_discounts.controllers;

import com.tingeso.ms2_quantity_discounts.entities.QuantityDiscount;
import com.tingeso.ms2_quantity_discounts.services.QuantityDiscountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/quantity-discount")
public class QuantityDiscountController {
    private QuantityDiscountService quantityDiscountService;

    @Autowired
    public QuantityDiscountController(QuantityDiscountService quantityDiscountService) {
        this.quantityDiscountService = quantityDiscountService;
    }

    @GetMapping("/")
    public ResponseEntity<List<QuantityDiscount>> getAll() {
        return ResponseEntity.ok(quantityDiscountService.getAllDiscounts());
    }

    @GetMapping("/{clientAmount}")
    public ResponseEntity<QuantityDiscount> getDiscount(@PathVariable int clientAmount) {
        return ResponseEntity.ok(
                quantityDiscountService.getDiscount(clientAmount)
        );
    }
}
