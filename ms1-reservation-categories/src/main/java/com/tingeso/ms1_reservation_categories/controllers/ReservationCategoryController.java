package com.tingeso.ms1_reservation_categories.controllers;

import com.tingeso.ms1_reservation_categories.entities.ReservationCategory;
import com.tingeso.ms1_reservation_categories.services.ReservationCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/reservation-category")
public class ReservationCategoryController {
    private ReservationCategoryService reservationCategoryService;

    @Autowired
    public ReservationCategoryController(ReservationCategoryService reservationCategoryService) {
        this.reservationCategoryService = reservationCategoryService;
    }

    @GetMapping("/get")
    public ResponseEntity<Map<String, ReservationCategory>> getAll() {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(reservationCategoryService.getAll());
    }

    @GetMapping("/get/{tier}")
    public ResponseEntity<?> getByTierName(@PathVariable String tier) {
        try {
            ReservationCategory reservationCategory = reservationCategoryService.getByTierName(tier);
            return ResponseEntity.status(HttpStatus.OK)
                    .body(reservationCategory);

        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(e.getMessage());
        }
    }

}
