package com.tingeso.kartingrm.enums;

import lombok.Getter;

@Getter
public enum DiscountType {
    NONE(0),

    // Descuento por cumpleaños
    BIRTHDAY(50),

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

    public static DiscountType getFrequencyDiscount(int visits) {
        if (visits >= 7) return DiscountType.FREQUENCY_VERYHIGH;
        if (5 <= visits) return DiscountType.FREQUENCY_HIGH;
        if (2 <= visits) return DiscountType.FREQUENCY_REGULAR;
        return DiscountType.NONE;
    }
}
