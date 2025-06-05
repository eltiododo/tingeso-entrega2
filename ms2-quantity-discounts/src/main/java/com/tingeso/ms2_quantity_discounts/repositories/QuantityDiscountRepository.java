package com.tingeso.ms2_quantity_discounts.repositories;

import com.tingeso.ms2_quantity_discounts.entities.QuantityDiscount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QuantityDiscountRepository extends JpaRepository<QuantityDiscount, String> {
    QuantityDiscount findByTextId(String textId);
}
