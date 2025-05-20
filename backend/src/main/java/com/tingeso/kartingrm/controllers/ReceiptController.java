package com.tingeso.kartingrm.controllers;

import com.tingeso.kartingrm.entities.ClientEntity;
import com.tingeso.kartingrm.entities.ReceiptEntity;
import com.tingeso.kartingrm.entities.ReservationEntity;
import com.tingeso.kartingrm.services.ClientService;
import com.tingeso.kartingrm.services.ReceiptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/mono/receipt")
@CrossOrigin("*")
public class ReceiptController {
    @Autowired
    ReceiptService receiptService;

    @PostMapping("/create/{idReservation}")
    public ResponseEntity<?> createReceipt(@PathVariable Long idReservation) {
        try {
            ReceiptEntity r = receiptService.createReceipt(idReservation);
            return ResponseEntity.ok(r);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error interno del servidor");
        }
    }

    @GetMapping(value = "/{idReservation}/pdf", produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<byte[]> generateReceiptPdf(@PathVariable Long idReservation) {
        try {
            byte[] pdfBytes = receiptService.generateReceiptPdf(idReservation);

            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=receipt_" + idReservation + ".pdf");

            return ResponseEntity.ok()
                    .headers(headers)
                    .contentType(MediaType.APPLICATION_PDF)
                    .body(pdfBytes);
        } catch (IOException e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @PostMapping("/{idReservation}/send")
    public ResponseEntity<?> sendReceiptEmails(@PathVariable Long idReservation) {
        try {
            byte[] pdfBytes = receiptService.sendReceiptEmailsToAllClients(idReservation);
            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=receipt_" + idReservation + ".pdf");

            return ResponseEntity.ok()
                    .headers(headers)
                    .contentType(MediaType.APPLICATION_PDF)
                    .body(pdfBytes);

        } catch (IOException e) {
            return ResponseEntity.internalServerError().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Fallo al enviar emails: " + e.getMessage());
        }
    }

}
