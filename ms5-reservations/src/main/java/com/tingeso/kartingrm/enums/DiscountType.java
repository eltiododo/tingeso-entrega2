package com.tingeso.kartingrm.enums;

import lombok.Getter;

@Getter
public enum DiscountType {
    NONE(0),

    // Descuento por cumpleaños
    BIRTHDAY(50),

    // Descuentos por numero de personas
    PEOPLE_3TO5(10),
    PEOPLE_6TO10(20),
    PEOPLE_11TO15(30),

    // Descuentos para clientes frecuentes
    FREQUENCY_VERYHIGH(30), // 7+ visitas mensuales
    FREQUENCY_HIGH(20),     // 5-6
    FREQUENCY_REGULAR(10),  // 2-4

    // tarifa extra por dias especiales- fines de semana y festivos
    SPECIAL_TARIFF(10);

    private int percentage;
    DiscountType(int percentage) {
        this.percentage = percentage;
    }

    public static DiscountType getDiscount(int clientAmount) {
        if (3 <= clientAmount && clientAmount <= 5) return DiscountType.PEOPLE_3TO5;
        if (6 <= clientAmount && clientAmount <= 10) return DiscountType.PEOPLE_6TO10;
        if (11 <= clientAmount && clientAmount <= 15) return DiscountType.PEOPLE_11TO15;
        return DiscountType.NONE;
    }

    public static DiscountType getFrequencyDiscount(int visits) {
        if (visits >= 7) return DiscountType.FREQUENCY_VERYHIGH;
        if (5 <= visits) return DiscountType.FREQUENCY_HIGH;
        if (2 <= visits) return DiscountType.FREQUENCY_REGULAR;
        return DiscountType.NONE;
    }
}
