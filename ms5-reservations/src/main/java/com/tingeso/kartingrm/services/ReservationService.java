package com.tingeso.kartingrm.services;

import com.tingeso.kartingrm.dtos.CreateReservationClientDTO;
import com.tingeso.kartingrm.dtos.ReservationDTO;
import com.tingeso.kartingrm.dtos.ReservationSummaryDTO;
import com.tingeso.kartingrm.entities.ClientEntity;
import com.tingeso.kartingrm.entities.ReservationEntity;
import com.tingeso.kartingrm.dtos.ReservationCategory;
import com.tingeso.kartingrm.repositories.ClientRepository;
import com.tingeso.kartingrm.repositories.ReservationRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.*;
import java.util.List;
import java.util.Map;

@Service
public class ReservationService {
    @Autowired
    ReservationRepository reservationRepository;

    @Autowired
    ClientRepository clientRepository;

    @Autowired
    RestTemplate restTemplate;

    public ReservationEntity createReservation(LocalDateTime bookingDate, String reservationCategory, List<ClientEntity> clients) {
        // obtener categoria desde ms1
        Map<String, ReservationCategory> resCategories = restTemplate.exchange(
                "http://ms1-reservation-categories/api/reservation-category/get",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<Map<String, ReservationCategory>>(){}
        ).getBody();
        assert resCategories != null;
        ReservationCategory category = resCategories.get(reservationCategory);
        if (category == null) {
            throw new RuntimeException("No existe la categoria " + reservationCategory);
        }

        ReservationEntity reservationEntity = new ReservationEntity();
        reservationEntity.setBookingDate(bookingDate);
        reservationEntity.setCategory(reservationCategory);

        LocalDate bookingDayMonthYear = bookingDate.toLocalDate();
        DayOfWeek bookingDay = bookingDayMonthYear.getDayOfWeek();
        LocalTime bookingTime = bookingDate.toLocalTime();
        LocalTime openTime, closeTime = LocalTime.of(22,0);

        // definir horario de apertura/cierre
        if (bookingDay.compareTo(DayOfWeek.SATURDAY) < 0)
            openTime = LocalTime.of(14, 0);
        else
            openTime = LocalTime.of(10, 0);

        // comprobar hora de reserva correcta
        if (bookingTime.isBefore(openTime) || bookingTime.isAfter(closeTime))
            throw new RuntimeException("Hora de reserva invalida");

        // quizas esto del overlap deberia ir en ms-rack, nose que otra logica pondria ahi
        // comprovar overlap de reservas antes
        List<ReservationEntity> reservationsAfter = reservationRepository
                .findByBookingDateBetween(LocalDateTime.of(bookingDayMonthYear, bookingTime),
                        LocalDateTime.of(bookingDayMonthYear,
                                bookingTime.plusMinutes(category.getMinutesTotal() - 1)));

        if (!reservationsAfter.isEmpty()) {
            throw new RuntimeException("Solapamiento de reservas (posterior)");
        }

        // comprobar overlap de reservas despues
        List<ReservationEntity> reservationsBefore = reservationRepository
                .findByBookingDateBetween(
                       LocalDateTime.of(bookingDayMonthYear,
                               bookingTime.minusMinutes(resCategories.get("TIER3").getMinutesTotal())),
                       LocalDateTime.of(bookingDayMonthYear, bookingTime))
                .stream()
                .filter(r -> {
                    int mins = resCategories.get(r.getCategory()).getMinutesTotal();
                    return r.getBookingDate().toLocalTime().plusMinutes(mins).isAfter(bookingTime);
                })
                .toList();

        if (!reservationsBefore.isEmpty()) {
            throw new RuntimeException("Solapamiento de reservas (anterior)");
        }

        // mapear clientela
        List<Long> clientIds = clients.stream()
                .map(c -> {
                    ClientEntity cl = clientRepository.findByEmail(c.getEmail());
                    if (cl == null) { // si no existe, lo crea
                        cl = new ClientEntity();
                        cl.setEmail(c.getEmail());
                        cl.setFirstName(c.getFirstName());
                        cl.setLastName(c.getLastName());
                        cl.setBirthDate(c.getBirthDate());
                        clientRepository.save(cl);
                    }
                    return cl.getId();
                })
                .toList();

        // el primero es el reservante, comprobar en el front
        reservationEntity.setReserveeClientId(clientIds.get(0));
        reservationEntity.setIdClients(clientIds);

        return reservationRepository.save(reservationEntity);
    }

    public List<ReservationDTO> getAllReservations() {
        return reservationRepository.findAll().stream()
                .map(this::toDto).toList();
    }

    public ReservationDTO getReservationById(Long id) {
        return toDto(reservationRepository.findById(id).orElse(null));
    }

    public void deleteReservation(Long id) {
        ReservationEntity reservation = reservationRepository.findById(id).orElse(null);
        assert reservation != null;

        if (reservation.getBookingDate().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("No es posible eliminar una reserva pasada");
        }
        reservationRepository.delete(reservation);
    }
    
    public ReservationDTO toDto(ReservationEntity r) {
        if (r == null) return null;
        return new ReservationDTO(
                r.getId(),
                r.getBookingDate(),
                r.getCategory(),
                r.getIdClients().stream().map(cId -> clientRepository.findById(cId).orElse(null)).toList());
    }
}
