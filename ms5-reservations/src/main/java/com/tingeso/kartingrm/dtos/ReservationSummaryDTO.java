package com.tingeso.kartingrm.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReservationSummaryDTO {
    Long id;
    LocalDateTime bookingDate;
    String category;
    String reserveeClientName;
    Integer clientAmount;
    Integer costTotal;
}
