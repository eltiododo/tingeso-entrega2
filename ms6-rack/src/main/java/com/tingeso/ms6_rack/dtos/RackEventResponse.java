package com.tingeso.ms6_rack.dtos;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class RackEventResponse {
    private Long id;
    private LocalDateTime bookingDate; // formato UTC!!
    private LocalDateTime bookingDateEnd;
    private String category;
    private String reserveeClientName;
}
