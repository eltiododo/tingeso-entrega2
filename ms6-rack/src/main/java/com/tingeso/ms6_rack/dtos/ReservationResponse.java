package com.tingeso.ms6_rack.dtos;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class ReservationResponse {
    private Long id;
    private LocalDateTime bookingDate;
    private String category;
    private List<ClientEntity> clients;
}
