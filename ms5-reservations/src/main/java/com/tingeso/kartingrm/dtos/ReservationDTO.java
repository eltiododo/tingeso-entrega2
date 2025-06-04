package com.tingeso.kartingrm.dtos;

import com.tingeso.kartingrm.entities.ClientEntity;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class ReservationDTO {
    Long id;
    LocalDateTime bookingDate;
    String category;
    List<ClientEntity> clients;
}
