package com.tingeso.kartingrm.dtos;

import lombok.Data;

import java.time.LocalDate;

@Data
public class CreateReservationClientDTO {
    String firstName;
    String lastName;
    String email;
    LocalDate birthday;
}
