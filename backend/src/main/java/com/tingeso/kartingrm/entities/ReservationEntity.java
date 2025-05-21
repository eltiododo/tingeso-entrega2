package com.tingeso.kartingrm.entities;

import com.tingeso.kartingrm.dtos.ReservationCategory;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "reservations")
@Data
public class ReservationEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_reservation")
    Long id;

    @Column(nullable = false)
    LocalDateTime bookingDate;

    @Column(nullable = false)
    String category;

    @Column(nullable = false)
    @ElementCollection
    List<Long> idClients;

    // no mapeado por ahora
    @Column(nullable = false)
    Long reserveeClientId;
}
