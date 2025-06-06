package com.tingeso.kartingrm.controllers;

import com.tingeso.kartingrm.dtos.ReservationDTO;
import com.tingeso.kartingrm.entities.ReservationEntity;
import com.tingeso.kartingrm.services.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reservations-receipts/reservation")
@CrossOrigin("*")
public class ReservationController {
    @Autowired
    private ReservationService reservationService;

    @PostMapping("/create")
    public ResponseEntity<?> createReservation(@RequestBody ReservationDTO reservation) {
        try {
            return ResponseEntity.ok(reservationService.createReservation(
                    reservation.getBookingDate(),
                    reservation.getCategory(),
                    reservation.getClients()));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/get/{reservationId}")
    public ReservationDTO getReservationById(@PathVariable Long reservationId) {
        return reservationService.getReservationById(reservationId);
    }

    @GetMapping("/get")
    public List<ReservationDTO> getAllReservations() {
        return reservationService.getAllReservations();
    }

//    @GetMapping("/getCategory")
//    public List<ReservationEntity> getAllReservationsByCategory(@RequestParam String category) {
//        return reservationService.getReservationsByCategory(category);
//    }

    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteReservation(@RequestParam Long id) {
        try {
            reservationService.deleteReservation(id);
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
