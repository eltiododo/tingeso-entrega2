package com.tingeso.ms1_reservation_categories.enums;

import lombok.Getter;

@Getter
public enum ReservationCategory {
    TIER1(10, 10, 15000, 30),
    TIER2(15, 15, 20000, 35),
    TIER3(20, 20, 25000, 40);

    private final int laps;
    private final int minutesMax;
    private final int cost;
    private final int minutesTotal;

    ReservationCategory(int laps, int minutesMax, int cost, int minutesTotal) {
        this.laps = laps;
        this.minutesMax = minutesMax;
        this.cost = cost;
        this.minutesTotal = minutesTotal;
    }

    public String toString() {
        return String.format("%d vueltas / %d minutos máximos en pista", laps, minutesMax);
    }
}
