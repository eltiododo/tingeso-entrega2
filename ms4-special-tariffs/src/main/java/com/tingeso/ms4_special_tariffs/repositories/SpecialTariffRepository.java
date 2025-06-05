package com.tingeso.ms4_special_tariffs.repositories;

import com.tingeso.ms4_special_tariffs.entities.SpecialTariff;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SpecialTariffRepository extends JpaRepository<SpecialTariff, Long> {
    SpecialTariff findByTextId(String textId);
}
