package com.tingeso.ms2_quantity_discounts.services;

import com.tingeso.ms2_quantity_discounts.dtos.QuantityDiscountDTO;
import com.tingeso.ms2_quantity_discounts.enums.QuantityDiscount;
import org.springframework.stereotype.Service;

@Service
public class QuantityDiscountService {
    public QuantityDiscount getDiscount(int clientAmount) {
        if (3 <= clientAmount && clientAmount <= 5) return QuantityDiscount.PEOPLE_3TO5;
        if (6 <= clientAmount && clientAmount <= 10) return QuantityDiscount.PEOPLE_6TO10;
        if (11 <= clientAmount && clientAmount <= 15) return QuantityDiscount.PEOPLE_11TO15;
        return QuantityDiscount.NONE;
    }

    public QuantityDiscountDTO toDto(QuantityDiscount quantityDiscount) {
        return new QuantityDiscountDTO(quantityDiscount.getPercentage());
    }
}
