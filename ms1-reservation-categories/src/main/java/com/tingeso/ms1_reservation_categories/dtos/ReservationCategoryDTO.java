package com.tingeso.ms1_reservation_categories.dtos;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class ReservationCategoryDTO {
    private final String category;
    private final int laps;
    private final int minutesMax;
    private final int cost;
    private final int minutesTotal;
}
