package com.tingeso.ms2_quantity_discounts.services;

import java.util.List;
import com.tingeso.ms2_quantity_discounts.entities.QuantityDiscount;
import com.tingeso.ms2_quantity_discounts.repositories.QuantityDiscountRepository;
import org.springframework.stereotype.Service;

@Service
public class QuantityDiscountService {
    private final QuantityDiscountRepository quantityDiscountRepository;

    public QuantityDiscountService(QuantityDiscountRepository quantityDiscountRepository) {
        this.quantityDiscountRepository = quantityDiscountRepository;
    }

    public List<QuantityDiscount> getAllDiscounts() {
        return quantityDiscountRepository.findAll();
    }

    public QuantityDiscount getDiscount(int clientAmount) {
        if (3 <= clientAmount && clientAmount <= 5) return quantityDiscountRepository.findByTextId("PEOPLE_3TO5");
        if (6 <= clientAmount && clientAmount <= 10) return quantityDiscountRepository.findByTextId("PEOPLE_6TO10");
        if (11 <= clientAmount && clientAmount <= 15) return quantityDiscountRepository.findByTextId("PEOPLE_11TO15");
        return quantityDiscountRepository.findByTextId("NONE");
    }
}
