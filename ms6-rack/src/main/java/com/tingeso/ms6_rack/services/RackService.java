package com.tingeso.ms6_rack.services;

import com.tingeso.ms6_rack.dtos.ReservationResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class RackService {
    private final RestTemplate restTemplate;

    public RackService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public List<ReservationResponse> getReservations() {
        // conectar a ms5
        List<ReservationResponse> reservations = restTemplate.getForObject(
                "http://ms5-reservations/api/reservations-receipts/reservation/get",
                List.class);
        return reservations;
    }
}
