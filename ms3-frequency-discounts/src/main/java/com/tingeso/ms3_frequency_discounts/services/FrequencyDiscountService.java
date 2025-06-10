package com.tingeso.ms3_frequency_discounts.services;

import com.tingeso.ms3_frequency_discounts.entities.FrequencyDiscount;
import com.tingeso.ms3_frequency_discounts.repositories.FrequencyDiscountRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class FrequencyDiscountService {
    private final FrequencyDiscountRepository frequencyDiscountRepository;

    public FrequencyDiscountService(FrequencyDiscountRepository frequencyDiscountRepository) {
        this.frequencyDiscountRepository = frequencyDiscountRepository;
    }

    public List<FrequencyDiscount> getAllDiscounts() {
        return frequencyDiscountRepository.findAll();
    }

    public FrequencyDiscount getDiscount(int visits) {
        if (visits >= 7) return frequencyDiscountRepository.findByTextId("FREQUENCY_7TO28");
        if (5 <= visits) return frequencyDiscountRepository.findByTextId("FREQUENCY_5TO6");
        if (2 <= visits) return frequencyDiscountRepository.findByTextId("FREQUENCY_2TO4");
        return new FrequencyDiscount(0L, "NONE", 0);
    }
}
