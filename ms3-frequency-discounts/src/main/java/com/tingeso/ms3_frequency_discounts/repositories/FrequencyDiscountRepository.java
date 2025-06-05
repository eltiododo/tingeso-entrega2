package com.tingeso.ms3_frequency_discounts.repositories;

import com.tingeso.ms3_frequency_discounts.entities.FrequencyDiscount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FrequencyDiscountRepository extends JpaRepository<FrequencyDiscount, Long> {
    FrequencyDiscount findByTextId(String textId);
}
