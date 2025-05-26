package com.tingeso.ms2_quantity_discounts.enums;

import lombok.Getter;

@Getter
public enum QuantityDiscount {
    // Descuentos por numero de personas
    NONE(0),
    PEOPLE_3TO5(10),
    PEOPLE_6TO10(20),
    PEOPLE_11TO15(30);

    private final int percentage;

    QuantityDiscount(int percentage) {
        this.percentage = percentage;
    }
}
