package com.tingeso.kartingrm.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.util.Date;

@Entity
@Table(name = "clients")
@Data
public class ClientEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_client")
    Long id;

    @Column(nullable = false)
    String firstName;

    @Column
    String lastName;

    @Column(nullable = false, unique = true)
    String email;

    LocalDate birthDate;

}
