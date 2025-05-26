package com.tingeso.ms2_quantity_discounts.controllers;

import com.tingeso.ms2_quantity_discounts.dtos.QuantityDiscountDTO;
import com.tingeso.ms2_quantity_discounts.enums.QuantityDiscount;
import com.tingeso.ms2_quantity_discounts.services.QuantityDiscountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/quantity-discount")
public class QuantityDiscountController {
    private QuantityDiscountService quantityDiscountService;

    @Autowired
    public QuantityDiscountController(QuantityDiscountService quantityDiscountService) {
        this.quantityDiscountService = quantityDiscountService;
    }

    @GetMapping("/{clientAmount}")
    public ResponseEntity<QuantityDiscountDTO> getDiscount(@PathVariable int clientAmount) {
        return ResponseEntity.ok(
                quantityDiscountService.toDto(
                        quantityDiscountService.getDiscount(clientAmount)
                )
        );
    }
}
