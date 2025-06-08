package com.tingeso.ms7_reports.dtos;

import lombok.Data;
import lombok.Getter;

@Data
@Getter
public class ReservationCategory {
    private int laps;
    private int minutesMax;
    private int cost;
    private int minutesTotal;

    @Override
    public String toString() {
        return String.format("%d vueltas o máx %d minutos", laps, minutesTotal);
    }
}
