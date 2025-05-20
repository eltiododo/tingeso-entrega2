package com.tingeso.kartingrm.dtos;

import lombok.Data;

import java.time.LocalDate;

@Data
public class ClientDTO {
    String firstName;
    String lastName;
    String email;
    Integer visits;
    Integer birthday;
}
