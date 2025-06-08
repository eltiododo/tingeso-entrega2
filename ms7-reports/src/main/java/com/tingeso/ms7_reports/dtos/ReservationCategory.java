package com.tingeso.ms7_reports.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReservationCategory {
    private Integer laps;
    private Integer minutesMax;
    private Integer cost;
    private Integer minutesTotal;

    @Override
    public String toString() {
        return String.format("%d vueltas o máx %d minutos", laps, minutesTotal);
    }
}
