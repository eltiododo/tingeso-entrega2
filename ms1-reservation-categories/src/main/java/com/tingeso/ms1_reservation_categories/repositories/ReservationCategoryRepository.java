package com.tingeso.ms1_reservation_categories.repositories;

import com.tingeso.ms1_reservation_categories.entities.ReservationCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReservationCategoryRepository extends JpaRepository<ReservationCategory, Long> {
    ReservationCategory findByName(String name);
}
