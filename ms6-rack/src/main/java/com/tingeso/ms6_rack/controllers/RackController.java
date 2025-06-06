package com.tingeso.ms6_rack.controllers;

import com.tingeso.ms6_rack.services.RackService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/rack")
public class RackController {
    private final RackService rackService;

    public RackController(RackService rackService) {
        this.rackService = rackService;
    }

    @GetMapping("/")
    public ResponseEntity<?> getReservations() {
        return ResponseEntity.ok(rackService.getReservations());
    }
}
