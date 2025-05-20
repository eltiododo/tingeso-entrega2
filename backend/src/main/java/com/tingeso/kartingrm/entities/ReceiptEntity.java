package com.tingeso.kartingrm.entities;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "receipts")
public class ReceiptEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    Long id;

    @Column(nullable = false, unique = true)
    Long idReservation;

    @Column(nullable = false)
    Integer clientAmount;

    @Column(nullable = false)
    Integer costIva;

    @Column(nullable = false)
    Integer costTotal;
}
