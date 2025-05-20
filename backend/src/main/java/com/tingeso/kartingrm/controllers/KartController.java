package com.tingeso.kartingrm.controllers;

import com.tingeso.kartingrm.dtos.KartDTO;
import com.tingeso.kartingrm.entities.KartEntity;
import com.tingeso.kartingrm.enums.KartState;
import com.tingeso.kartingrm.services.KartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/mono/kart")
@CrossOrigin("*")
public class KartController {
    @Autowired
    KartService kartService;

    @GetMapping("/get")
    public List<KartEntity> getAllKarts() {
        return kartService.getAllKarts();
    }

    @GetMapping("/get/{id}")
    public KartEntity getKart(@PathVariable Long id) {
        return kartService.getKart(id);
    }

    @PostMapping("/update")
    public ResponseEntity<?> updateKart(@RequestBody KartEntity kart) {
        try {
            KartEntity k = kartService.updateKart(kart);
            return ResponseEntity.ok().body(k);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/create")
    public ResponseEntity<?> createKart(@RequestBody KartDTO kartDTO) {
        try {
            KartEntity k = kartService.createKart(kartDTO.getModel(),
                    KartState.valueOf(kartDTO.getState()));
            return ResponseEntity.ok().body(k);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Estado invalido");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteKart(@PathVariable Long id) {
        try {
            kartService.deleteKartById(id);
            return ResponseEntity.ok().body("Kart eliminado correctamente");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
