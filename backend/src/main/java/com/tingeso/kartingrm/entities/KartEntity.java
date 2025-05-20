package com.tingeso.kartingrm.entities;

import com.tingeso.kartingrm.enums.KartState;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "karts")
@Data
public class KartEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_kart")
    Long id;

    @Column
    String model;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    KartState state;
}
