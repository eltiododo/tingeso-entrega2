package com.tingeso.ms1_reservation_categories.services;

import com.tingeso.ms1_reservation_categories.dtos.ReservationCategoryDTO;
import com.tingeso.ms1_reservation_categories.enums.ReservationCategory;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class ReservationCategoryService {
    public ReservationCategoryService() {}

    public List<ReservationCategoryDTO> getAll() {
        return Arrays.stream(ReservationCategory.values())
                .map(this::toDto)
                .toList();
    }

    public ReservationCategoryDTO getByTierName(String tierName) {
        return switch (tierName) {
            case "TIER1" -> toDto(ReservationCategory.TIER1);
            case "TIER2" -> toDto(ReservationCategory.TIER2);
            case "TIER3" -> toDto(ReservationCategory.TIER3);
            default -> throw new RuntimeException("No existe el tier " + tierName);
        };
    }

    public ReservationCategoryDTO toDto(ReservationCategory reservationCategory) {
        return new ReservationCategoryDTO(
                reservationCategory.name(),
                reservationCategory.getLaps(),
                reservationCategory.getMinutesMax(),
                reservationCategory.getCost(),
                reservationCategory.getMinutesTotal());
    }
}
