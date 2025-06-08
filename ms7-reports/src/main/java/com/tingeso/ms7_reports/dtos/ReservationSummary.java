package com.tingeso.ms7_reports.dtos;

import lombok.*;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReservationSummary {
    Long id;
    LocalDateTime bookingDate;
    String category;
    String reserveeClientName;
    Integer clientAmount;
    Integer costTotal;
}
