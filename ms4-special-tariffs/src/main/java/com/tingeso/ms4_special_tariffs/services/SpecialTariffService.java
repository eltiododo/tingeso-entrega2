package com.tingeso.ms4_special_tariffs.services;

import com.tingeso.ms4_special_tariffs.entities.SpecialTariff;
import com.tingeso.ms4_special_tariffs.repositories.SpecialTariffRepository;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Stream;

@Service
public class SpecialTariffService {
    private final SpecialTariffRepository specialTariffRepository;

    // holidays: año nuevo, dia del trabajador, fiestas patrias, navidad
    List<Integer> holidays = Stream.of("12-31", "01-01", "05-01", "09-18", "09-19", "12-25")
            .map(s -> s.concat(String.valueOf(LocalDate.now().getYear())))
            .map(s -> LocalDate.parse(s, DateTimeFormatter.ofPattern("MM-ddyyyy")))
            .map(LocalDate::getDayOfYear)
            .toList();

    public SpecialTariffService(SpecialTariffRepository specialTariffRepository) {
        this.specialTariffRepository = specialTariffRepository;
    }

    public List<SpecialTariff> getAll() {
        return specialTariffRepository.findAll();
    }

    public SpecialTariff getByTextId(String textId) {
        return specialTariffRepository.findByTextId(textId);
    }

    public SpecialTariff getFestiveValue(Integer reservationDay) {
        // Festivo
        if (holidays.contains(reservationDay)) {
            return specialTariffRepository.findByTextId("HOLIDAYS");
        }

        // Fin de semana
        int currentYear = LocalDate.now().getYear();
        DayOfWeek today = LocalDate.ofYearDay(currentYear, reservationDay).getDayOfWeek();

        if (today.equals(DayOfWeek.SATURDAY) || today.equals(DayOfWeek.SUNDAY)) {
            return specialTariffRepository.findByTextId("WEEKEND");
        }

        return new SpecialTariff(0L, "NONE", 0, false);
    }

}
