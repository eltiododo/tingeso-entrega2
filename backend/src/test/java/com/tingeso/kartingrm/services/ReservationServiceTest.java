package com.tingeso.kartingrm.services;

import com.tingeso.kartingrm.dtos.ReservationDTO;
import com.tingeso.kartingrm.entities.ClientEntity;
import com.tingeso.kartingrm.entities.ReservationEntity;
import com.tingeso.kartingrm.enums.ReservationCategory;
import com.tingeso.kartingrm.repositories.ClientRepository;
import com.tingeso.kartingrm.repositories.ReservationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReservationServiceTest {
    @Mock
    private ReservationRepository reservationRepository;

    @Mock
    private ClientRepository clientRepository;

    @InjectMocks
    private ReservationService reservationService;

    private LocalDateTime validWeekdayTime;
    private LocalDateTime validWeekendTime;
    private LocalDateTime invalidEarlyTime;
    private LocalDateTime invalidLateTime;

    @BeforeEach
    void setUp() {
        // Setup test times
        validWeekdayTime = LocalDateTime.of(2023, 6, 15, 15, 0); // Thursday 3pm
        validWeekendTime = LocalDateTime.of(2023, 6, 17, 12, 0); // Saturday 12pm
        invalidEarlyTime = LocalDateTime.of(2023, 6, 15, 13, 0); // Thursday 1pm (before opening)
        invalidLateTime = LocalDateTime.of(2023, 6, 15, 22, 30); // Thursday 10:30pm (after closing)
    }

    @Test
    void createReservation_shouldCreateNewClientsWhenNotExist() {
        // Arrange
        ClientEntity newClient = new ClientEntity();
        newClient.setEmail("new@example.com");
        List<ClientEntity> clients = List.of(newClient);

        when(clientRepository.findByEmail(anyString())).thenReturn(null);
        when(clientRepository.save(any())).thenAnswer(inv -> {
            ClientEntity c = inv.getArgument(0);
            c.setId(99L);
            return c;
        });
        when(reservationRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        // Act
        ReservationEntity result = reservationService.createReservation(
                LocalDateTime.now().plusHours(1),
                "TIER1",
                clients
        );

        // Assert
        verify(clientRepository, times(1)).save(any());
        assertEquals(99L, result.getIdClients().get(0));
    }

    @Test
    void createReservation_shouldUseExistingClients() {
        // Arrange
        ClientEntity existingClient = new ClientEntity();
        existingClient.setId(55L);
        existingClient.setEmail("existing@example.com");

        when(clientRepository.findByEmail(anyString())).thenReturn(existingClient);
        when(reservationRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        // Act
        ReservationEntity result = reservationService.createReservation(
                LocalDateTime.now().plusHours(1),
                "TIER2",
                List.of(existingClient)
        );

        // Assert
        verify(clientRepository, never()).save(any());
        assertEquals(55L, result.getIdClients().get(0));
    }

    @Test
    void getAllReservations_shouldReturnDTOsWithClients() {
        // Arrange
        ReservationEntity reservation = new ReservationEntity();
        reservation.setIdClients(List.of(1L));
        when(reservationRepository.findAll()).thenReturn(List.of(reservation));
        when(clientRepository.findById(1L)).thenReturn(Optional.of(new ClientEntity()));

        // Act
        List<ReservationDTO> results = reservationService.getAllReservations();

        // Assert
        assertEquals(1, results.size());
        assertNotNull(results.get(0).getClients());
    }

    @Test
    void toDto_shouldConvertEntityWithClientsProperly() {
        // Arrange
        ReservationEntity entity = new ReservationEntity();
        entity.setIdClients(List.of(1L));
        when(clientRepository.findById(1L)).thenReturn(Optional.of(new ClientEntity()));

        // Act
        ReservationDTO dto = reservationService.toDto(entity);

        // Assert
        assertEquals(1, dto.getClients().size());
    }
}