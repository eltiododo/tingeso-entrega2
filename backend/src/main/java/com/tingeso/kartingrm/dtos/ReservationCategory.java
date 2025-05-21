package com.tingeso.kartingrm.dtos;

import lombok.Data;
import lombok.Getter;

@Data
@Getter
public class ReservationCategory {
    private int laps;
    private int minutesMax;
    private int cost;
    private int minutesTotal;
}
