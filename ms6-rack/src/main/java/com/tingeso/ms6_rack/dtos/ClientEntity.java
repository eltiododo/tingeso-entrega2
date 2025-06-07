package com.tingeso.ms6_rack.dtos;

import lombok.Data;

import java.time.LocalDate;

@Data
public class ClientEntity {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private LocalDate birthday;
}
