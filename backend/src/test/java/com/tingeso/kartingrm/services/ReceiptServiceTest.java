package com.tingeso.kartingrm.services;

import com.lowagie.text.DocumentException;
import com.tingeso.kartingrm.dtos.ClientDTO;
import com.tingeso.kartingrm.dtos.ClientReceiptRow;
import com.tingeso.kartingrm.entities.ClientEntity;
import com.tingeso.kartingrm.entities.ReceiptEntity;
import com.tingeso.kartingrm.entities.ReservationEntity;
import com.tingeso.kartingrm.enums.DiscountType;
import com.tingeso.kartingrm.enums.ReservationCategory;
import com.tingeso.kartingrm.repositories.ClientRepository;
import com.tingeso.kartingrm.repositories.ReceiptRepository;
import com.tingeso.kartingrm.repositories.ReservationRepository;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.javamail.JavaMailSender;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReceiptServiceTest {

    @Mock
    private ReceiptRepository receiptRepository;

    @Mock
    private ReservationRepository reservationRepository;

    @Mock
    private ClientRepository clientRepository;

    @Mock
    private JavaMailSender mailSender;

    @Mock
    private MimeMessage mimeMessage;

    @InjectMocks
    private ReceiptService receiptService;

    private ReservationEntity reservation;
    private ClientEntity client1;
    private ClientEntity client2;
    private ClientDTO clientDTO1;
    private ClientDTO clientDTO2;

    @BeforeEach
    void setUp() {
        // Initialize test reservation
        reservation = new ReservationEntity();
        reservation.setId(1L);
        reservation.setBookingDate(LocalDateTime.of(2023, 12, 25, 15, 0));
        reservation.setCategory(ReservationCategory.TIER1);
        reservation.setReserveeClientId(1L);
        reservation.setIdClients(Arrays.asList(1L, 2L));

        // Initialize test clients
        client1 = new ClientEntity();
        client1.setId(1L);
        client1.setFirstName("John");
        client1.setLastName("Doe");
        client1.setEmail("john@example.com");
        client1.setBirthDate(LocalDate.of(1990, 5, 15));

        client2 = new ClientEntity();
        client2.setId(2L);
        client2.setFirstName("Jane");
        client2.setLastName("Smith");
        client2.setEmail("jane@example.com");
        client2.setBirthDate(LocalDate.of(1985, 12, 25));

        // Initialize client DTOs
        clientDTO1 = new ClientDTO();
        clientDTO1.setFirstName("John");
        clientDTO1.setLastName("Doe");
        clientDTO1.setEmail("john@example.com");
        clientDTO1.setVisits(3);
        clientDTO1.setBirthday(client1.getBirthDate().getDayOfYear());

        clientDTO2 = new ClientDTO();
        clientDTO2.setFirstName("Jane");
        clientDTO2.setLastName("Smith");
        clientDTO2.setEmail("jane@example.com");
        clientDTO2.setVisits(5);
        clientDTO2.setBirthday(client2.getBirthDate().getDayOfYear());
    }

    @Test
    void createReceipt_shouldCreateReceiptWithCorrectCalculations() {
        // Arrange
        when(reservationRepository.findById(anyLong())).thenReturn(Optional.of(reservation));
        when(clientRepository.findById(1L)).thenReturn(Optional.of(client1));
        when(clientRepository.findById(2L)).thenReturn(Optional.of(client2));
        when(receiptRepository.findByIdReservation(anyLong())).thenReturn(null);
        when(receiptRepository.save(any(ReceiptEntity.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        ReceiptEntity result = receiptService.createReceipt(1L);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getIdReservation());
        assertEquals(2, result.getClientAmount());
        assertTrue(result.getCostTotal() > 0);
        assertTrue(result.getCostIva() > 0);
    }

    @Test
    void clientToDto_shouldCalculateVisitsCorrectly() {
        // Arrange
        LocalDateTime now = LocalDateTime.now();
        ReservationEntity currentReservation1 = new ReservationEntity();
        currentReservation1.setBookingDate(now);
        ReservationEntity currentReservation2 = new ReservationEntity();
        currentReservation2.setBookingDate(now.plusDays(1));

        when(reservationRepository.findByReserveeClientId(1L))
                .thenReturn(Arrays.asList(
                        new ReservationEntity(), // Different month
                        currentReservation1,
                        currentReservation2
                ));

        // Act
        ClientDTO result = receiptService.clientToDto(client1);

        // Assert
        assertEquals(2, result.getVisits());
    }

    @Test
    void generateClientReceiptRow_shouldApplyBirthdayDiscountWhenBirthdayMatches() {
        // Arrange
        ClientDTO birthdayClient = new ClientDTO();
        birthdayClient.setBirthday(LocalDate.of(1985, 12, 25).getDayOfYear());

        // Act
        ClientReceiptRow result = receiptService.generateClientReceiptRow(
                birthdayClient, 2, 3, reservation);

        // Assert
        assertEquals(DiscountType.BIRTHDAY, result.getIndividualDiscount());
    }

    @Test
    void generateClientReceiptRow_shouldApplyFrequencyDiscountBasedOnVisits() {
        // Arrange
        ClientDTO frequentClient = new ClientDTO();
        frequentClient.setVisits(5);
        frequentClient.setBirthday(LocalDate.of(1990, 5, 15).getDayOfYear());

        // Act
        ClientReceiptRow result = receiptService.generateClientReceiptRow(
                frequentClient, 2, 5, reservation);

        // Assert
        assertEquals(DiscountType.FREQUENCY_HIGH, result.getIndividualDiscount());
    }

    @Test
    void getTariff_shouldApplySpecialTariffOnHolidays() {
        // Arrange
        ReservationEntity holidayReservation = new ReservationEntity();
        holidayReservation.setBookingDate(LocalDateTime.of(2023, 12, 25, 15, 0));
        holidayReservation.setCategory(ReservationCategory.TIER1);

        // Act
        int result = receiptService.getTariff(2, holidayReservation);

        // Assert
        int regularTariff = ReservationCategory.TIER1.getCost();
        assertTrue(result > regularTariff);
    }

    @Test
    void generateReceiptPdf_shouldGeneratePdfWithoutErrors() throws IOException, DocumentException {
        // Arrange
        ReceiptEntity receipt = new ReceiptEntity();
        receipt.setIdReservation(1L);
        receipt.setClientAmount(2);
        receipt.setCostIva(3800);
        receipt.setCostTotal(23800);

        when(receiptRepository.findByIdReservation(anyLong())).thenReturn(receipt);
        when(reservationRepository.findById(anyLong())).thenReturn(Optional.of(reservation));
        when(clientRepository.findById(1L)).thenReturn(Optional.of(client1));
        when(clientRepository.findById(2L)).thenReturn(Optional.of(client2));

        // Act
        byte[] result = receiptService.generateReceiptPdf(1L);

        // Assert
        assertNotNull(result);
        assertTrue(result.length > 0);
    }

    @Test
    void generateReceiptPdf_shouldCreateReceiptIfNotExists() throws Exception {
        // Arrange
        when(receiptRepository.findByIdReservation(anyLong())).thenReturn(null);
        when(reservationRepository.findById(anyLong())).thenReturn(Optional.of(reservation));
        when(clientRepository.findById(anyLong())).thenReturn(Optional.of(client1));
        when(receiptRepository.save(any())).thenReturn(new ReceiptEntity());

        // Act
        byte[] result = receiptService.generateReceiptPdf(1L);

        // Assert
        verify(receiptRepository, times(1)).save(any());
        assertNotNull(result);
    }

    @Test
    void sendReceiptEmail_shouldHandleMessagingException() throws Exception {
        // Arrange
        when(mailSender.createMimeMessage()).thenReturn(mimeMessage);
        doThrow(new MessagingException("Simulated error")).when(mimeMessage).setContent(any());

        // Act & Assert
        assertThrows(RuntimeException.class, () ->
                receiptService.sendReceiptEmail(
                        "test@example.com",
                        "Subject",
                        "Body",
                        new byte[10],
                        "test.pdf"
                ));
    }

    @Test
    void sendReceiptEmailsToAllClients_shouldSendToAllClients() throws Exception {
        // Arrange
        when(reservationRepository.findById(anyLong())).thenReturn(Optional.of(reservation));
        when(clientRepository.findById(1L)).thenReturn(Optional.of(client1));
        when(clientRepository.findById(2L)).thenReturn(Optional.of(client2));
        when(mailSender.createMimeMessage()).thenReturn(mimeMessage);

        // Act
        byte[] result = receiptService.sendReceiptEmailsToAllClients(1L);

        // Assert
        verify(mailSender, times(2)).send(mimeMessage);
        assertNotNull(result);
    }

    @Test
    void getTariff_shouldApplyGroupDiscount() {
        // Arrange
        ReservationEntity regularReservation = new ReservationEntity();
        regularReservation.setBookingDate(LocalDateTime.of(2023, 6, 15, 15, 0));
        regularReservation.setCategory(ReservationCategory.TIER1);

        // Act
        int result = receiptService.getTariff(5, regularReservation);

        // Assert
        int regularTariff = ReservationCategory.TIER1.getCost();
        assertTrue(result < regularTariff);
    }
}