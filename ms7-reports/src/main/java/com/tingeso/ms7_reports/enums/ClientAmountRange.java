package com.tingeso.ms7_reports.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.*;

@Getter
@AllArgsConstructor
public enum ClientAmountRange {
    RANGE_1TO2(1, 2),
    RANGE_3TO5(3, 5),
    RANGE_6TO10(6, 10),
    RANGE_11TO15(11, 15),
    OUT_OF_BOUNDS(16, Integer.MAX_VALUE);

    private final int lower;
    private final int upper;

    public static ClientAmountRange getClientAmountRange(int clientAmount) {
        if (1 <= clientAmount && clientAmount <= 2) return RANGE_1TO2;
        if (3 <= clientAmount && clientAmount <= 5) return RANGE_3TO5;
        if (6 <= clientAmount && clientAmount <= 10) return RANGE_6TO10;
        if (11 <= clientAmount && clientAmount <= 15) return RANGE_11TO15;
        return OUT_OF_BOUNDS;
    }

    @Override
    public String toString() {
        if (this.equals(OUT_OF_BOUNDS)) return "Rango invalido";
        return String.format("%d-%d personas", lower, upper);
    }

    public static Map<String, ClientAmountRange> clientAmountRangeMap() {
        Map<String, ClientAmountRange> clientAmountRanges = new LinkedHashMap<>();
        Arrays.stream(ClientAmountRange.values())
                .filter(range -> !range.equals(OUT_OF_BOUNDS))
                .forEach(range -> clientAmountRanges.put(range.toString(), range));
        return clientAmountRanges;
    }
}
