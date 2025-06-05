package com.tingeso.ms1_reservation_categories.services;

import com.tingeso.ms1_reservation_categories.entities.ReservationCategory;
import com.tingeso.ms1_reservation_categories.repositories.ReservationCategoryRepository;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Map;
import java.util.HashMap;

@Service
public class ReservationCategoryService {
    private ReservationCategoryRepository reservationCategoryRepository;

    public ReservationCategoryService(ReservationCategoryRepository reservationCategoryRepository) {
        this.reservationCategoryRepository = reservationCategoryRepository;
    }

    public Map<String, ReservationCategory> getAll() {
        Map<String, ReservationCategory> categories = new HashMap<>();
        reservationCategoryRepository.findAll().forEach(category -> categories.put(category.getName(), category));
        return categories;
    }

    public ReservationCategory getByTierName(String tierName) {
        return reservationCategoryRepository.findByName(tierName);
    }
}