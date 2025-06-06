package com.tingeso.ms6_rack.dtos;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class ReservationResponse {
    Long id;
    LocalDateTime bookingDate;
    String category;
    List<Long> idClients;
    Long reserveeClientId;
}
