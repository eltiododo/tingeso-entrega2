package com.tingeso.ms6_rack.services;

import com.tingeso.ms6_rack.dtos.ClientEntity;
import com.tingeso.ms6_rack.dtos.RackEventResponse;
import com.tingeso.ms6_rack.dtos.ReservationCategory;
import com.tingeso.ms6_rack.dtos.ReservationResponse;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class RackService {
    private final RestTemplate restTemplate;

    public RackService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public List<ReservationResponse> getReservations() {
        // conectar a ms5
        return restTemplate.exchange(
                "http://ms5-reservations/api/reservations-receipts/reservation/get",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<ReservationResponse>>(){})
                .getBody();
    }

    public int getReservationDurationMinutes(String category) {
        ReservationCategory resCategory = restTemplate.getForObject(
                "http://ms1-reservation-categories/api/reservation-category/get/" + category,
                ReservationCategory.class);

        // Por si acaso, retornar la duracion de TIER1 como default
        if (resCategory == null) return 30;
        return resCategory.getMinutesTotal();
    }

    public RackEventResponse convertToResponse(ReservationResponse reservation) {
        // fecha termino
        LocalDateTime bookingDate = reservation.getBookingDate();
        LocalDateTime bookingDateEnd = bookingDate.plusMinutes(
                getReservationDurationMinutes(reservation.getCategory())
        );

        // nombre cliente reservante, el primero de la lista
        ClientEntity reserveeClient = reservation.getClients().get(0);
        String reserveeClientName = reserveeClient.getFirstName() + " " + reserveeClient.getLastName();

        // poblar DTO y retornar
        return RackEventResponse.builder()
                .id(reservation.getId())
                .bookingDate(bookingDate)
                .bookingDateEnd(bookingDateEnd)
                .category(reservation.getCategory())
                .reserveeClientName(reserveeClientName)
                .build();
    }

    public List<RackEventResponse> getRackEvents() {
        return getReservations().stream()
                .map(this::convertToResponse)
                .toList();
    }
}
