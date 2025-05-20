package com.tingeso.kartingrm.services;

import ch.qos.logback.core.net.server.Client;
import com.lowagie.text.*;
import com.lowagie.text.Font;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import com.tingeso.kartingrm.dtos.ClientDTO;
import com.tingeso.kartingrm.dtos.ClientReceiptRow;
import com.tingeso.kartingrm.entities.ClientEntity;
import com.tingeso.kartingrm.entities.ReceiptEntity;
import com.tingeso.kartingrm.entities.ReservationEntity;
import com.tingeso.kartingrm.enums.DiscountType;
import com.tingeso.kartingrm.repositories.ClientRepository;
import com.tingeso.kartingrm.repositories.ReceiptRepository;
import com.tingeso.kartingrm.repositories.ReservationRepository;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Stream;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;


@Service
public class ReceiptService {
    @Autowired
    private ReceiptRepository receiptRepository;
    @Autowired
    private ReservationRepository reservationRepository;
    @Autowired
    private JavaMailSender mailSender;
    @Autowired
    private ClientRepository clientRepository;

    // holidays: año nuevo, dia del trabajador, fiestas patrias, navidad
    List<Integer> holidays = Stream.of("01-01", "05-01", "09-18", "09-19", "12-25")
            .map(s -> s.concat(String.valueOf(LocalDate.now().getYear())))
            .map(s -> LocalDate.parse(s, DateTimeFormatter.ofPattern("MM-ddyyyy")))
            .map(LocalDate::getDayOfYear)
            .toList();

    // Crear comprobante para una reserva
    public ReceiptEntity createReceipt(Long idReservation) {
        if (receiptRepository.findByIdReservation(idReservation) != null) {
            throw new RuntimeException("Comprobante de la reserva " + idReservation + " ya existe");
        }
        ReceiptEntity receipt = new ReceiptEntity();
        receipt.setIdReservation(idReservation);

        // Obtener datos de la reserva
        ReservationEntity reservation = reservationRepository.findById(idReservation)
                .orElseThrow(() -> new RuntimeException("Reserva no encontrada con ID: " + idReservation));

        List<ClientDTO> clients = reservation.getIdClients().stream()
                .map(c -> clientRepository.findById(c)
                        .orElseThrow(() -> new RuntimeException("Cliente no encontrado con ID: " + c)))
                .map(this::clientToDto)
                .toList();

        int clientAmount = clients.size();
        List<ClientReceiptRow> crows = clients.stream()
                .map(c -> generateClientReceiptRow(c, clientAmount, c.getVisits(), reservation))
                .toList();

        int tariff = crows.stream()
                .map(ClientReceiptRow::getFinalCost)
                .reduce(0, Integer::sum);

        int costIva = (int) (tariff * 0.19);

        receipt.setIdReservation(idReservation);
        receipt.setCostIva(costIva);
        receipt.setCostTotal(tariff + costIva);
        receipt.setClientAmount(clientAmount);
        return receiptRepository.save(receipt);
    }

    public ClientDTO clientToDto(ClientEntity client) {
        ClientDTO dto = new ClientDTO();
        dto.setFirstName(client.getFirstName());
        dto.setLastName(client.getLastName());
        dto.setEmail(client.getEmail());
        int visits = reservationRepository.findByReserveeClientId(client.getId())
                    .stream()
                    .filter(r -> r.getBookingDate().getMonth().equals(LocalDate.now().getMonth()))
                    .toList()
                    .size();
        dto.setVisits(visits);
        if (client.getBirthDate() != null) dto.setBirthday(client.getBirthDate().getDayOfYear());
        return dto;
    }

    public ClientReceiptRow generateClientReceiptRow(ClientDTO client, Integer clientAmount,
                                                     Integer clientMonthlyVisits, ReservationEntity reservation) {
        ClientReceiptRow crow = new ClientReceiptRow();
        crow.setClientName(client.getFirstName() + " " + client.getLastName());
        crow.setBaseTariff(reservation.getCategory().getCost());
        crow.setGroupDiscount(DiscountType.getDiscount(clientAmount));

        if (client.getBirthday() != null && reservation.getBookingDate().getDayOfYear() == client.getBirthday())
            crow.setIndividualDiscount(DiscountType.BIRTHDAY);
        else
            crow.setIndividualDiscount(DiscountType.getFrequencyDiscount(clientMonthlyVisits));

        crow.setFinalCost((int) (crow.getBaseTariff()
                        * (1 - (double) (crow.getGroupDiscount().getPercentage()) / 100)
                        * (1 - (double) (crow.getIndividualDiscount().getPercentage()) / 100)));
        return crow;
    }

    public byte[] generateReceiptPdf(Long idReservation) throws IOException, DocumentException {
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            Document document = new Document(PageSize.A4, 50, 50, 50, 50);
            PdfWriter.getInstance(document, outputStream);
            document.open();

            ReceiptEntity receipt = receiptRepository.findByIdReservation(idReservation);
            if (receipt == null) {
                receipt = createReceipt(idReservation);
            }

            ReservationEntity reservation = reservationRepository.findById(idReservation)
                    .orElseThrow(() -> new RuntimeException("Reserva no encontrada con id " + idReservation));

            ClientEntity reservee = clientRepository.findById(reservation.getReserveeClientId())
                    .orElseThrow(() -> new RuntimeException("Cliente "));

            // generar client rows
            List<ClientDTO> clients = reservation.getIdClients().stream()
                    .map(c -> clientRepository.findById(c)
                            .orElseThrow(() -> new RuntimeException("Cliente no encontrado con ID: " + c)))
                    .map(this::clientToDto)
                    .toList();
            List<ClientReceiptRow> crows = clients.stream()
                    .map(c -> generateClientReceiptRow(c, clients.size(), c.getVisits(), reservation))
                    .toList();

            // titulo
            Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18, Color.BLUE);
            Paragraph title = new Paragraph("Comprobante de venta", titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            title.setSpacingAfter(20f);
            document.add(title);

            // fecha de emision
            Font dateFont = FontFactory.getFont(FontFactory.HELVETICA, 12);
            Paragraph date = new Paragraph("Fecha de emisión: " +
                    LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")), dateFont);
            date.setAlignment(Element.ALIGN_CENTER);
            date.setSpacingAfter(20f);
            document.add(date);

            // detalles reserva
            Font sectionFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 14);
            Paragraph reservationTitle = new Paragraph("Detalles de la reserva", sectionFont);
            reservationTitle.setSpacingAfter(10f);
            document.add(reservationTitle);

            Font detailFont = FontFactory.getFont(FontFactory.HELVETICA, 12);
            document.add(new Paragraph("Código de reserva: " + reservation.getId(), detailFont));
            document.add(new Paragraph("Fecha y hora: " +
                    reservation.getBookingDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")), detailFont));
            document.add(new Paragraph("Categoría: " + reservation.getCategory().toString(), detailFont));
            document.add(new Paragraph("Número de personas: " + receipt.getClientAmount(), detailFont));
            document.add(new Paragraph("Reservado por: " + reservee.getFirstName() + " " + reservee.getLastName(), detailFont));
            document.add(new Paragraph(" "));

            // Payment details table
            Paragraph paymentTitle = new Paragraph("Detalles de pago", sectionFont);
            paymentTitle.setSpacingAfter(10f);
            document.add(paymentTitle);

            // Create table with 6 columns
            PdfPTable table = new PdfPTable(6);
            table.setWidthPercentage(100);
            table.setSpacingBefore(10f);
            table.setSpacingAfter(10f);

            // Table headers
            Font headerFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12);
            table.addCell(new Phrase("Cliente", headerFont));
            table.addCell(new Phrase("Tarifa Base", headerFont));
            table.addCell(new Phrase("Desc. Grupo", headerFont));
            table.addCell(new Phrase("Desc. Individual", headerFont));
            table.addCell(new Phrase("Tipo Desc.", headerFont));
            table.addCell(new Phrase("Tarifa Final", headerFont));


            // Table rows
            Font cellFont = FontFactory.getFont(FontFactory.HELVETICA, 11);
            for (ClientReceiptRow row : crows) {
                table.addCell(new Phrase(row.getClientName(), cellFont));
                table.addCell(new Phrase("$" + row.getBaseTariff(), cellFont));
                table.addCell(new Phrase(row.getGroupDiscount().getPercentage() + "%", cellFont));
                table.addCell(new Phrase(row.getIndividualDiscount().getPercentage() + "%", cellFont));

                DiscountType discountType = row.getIndividualDiscount();
                String discount;
                if (discountType.equals(DiscountType.BIRTHDAY))
                    discount = "Cumpleaños";
                else if (discountType.equals(DiscountType.NONE))
                    discount = "-";
                else
                    discount = "Frecuencia";
                table.addCell(new Phrase(discount, cellFont));

                table.addCell(new Phrase("$" + row.getFinalCost(), cellFont));
            }

            document.add(table);

            // Total amounts
            Paragraph totals = new Paragraph();
            totals.add(new Phrase("Subtotal: $" + (receipt.getCostTotal() - receipt.getCostIva()), detailFont));
            totals.add(Chunk.NEWLINE);
            totals.add(new Phrase("IVA (19%): $" + receipt.getCostIva(), detailFont));
            totals.add(Chunk.NEWLINE);
            Font totalFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12);
            totals.add(new Phrase("Total a pagar: $" + receipt.getCostTotal(), totalFont));
            totals.setSpacingBefore(10f);
            document.add(totals);

            document.close();
            return outputStream.toByteArray();
        } catch (DocumentException e) {
            throw new RuntimeException("Error generando PDF", e);
        }
    }

    int getTariff(int clientAmount, ReservationEntity reservation) {
        DiscountType discount = DiscountType.getDiscount(clientAmount);

        // Tarifa especial por dias
        int tariff = reservation.getCategory().getCost();
        LocalDate reservationDay = reservation.getBookingDate().toLocalDate();

        // si es fin de semana, o dia feriado...
        if (reservationDay.getDayOfWeek().compareTo(DayOfWeek.SATURDAY) >= 0 ||
            holidays.contains(reservationDay.getDayOfYear())) {
            tariff = tariff * (1 + DiscountType.SPECIAL_TARIFF.getPercentage() / 100);
        }

        tariff = tariff * (1 - discount.getPercentage() / 100);
        return tariff;
    }

    public void sendReceiptEmail(String toEmail, String subject, String body, byte[] attachment, String attachmentName) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setFrom("no-reply@kartingrm.com");
            helper.setTo(toEmail);
            helper.setSubject(subject);
            helper.setText(body);
            helper.addAttachment(attachmentName, () -> new ByteArrayInputStream(attachment), "application/pdf");

            mailSender.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException("Falla al enviar email", e);
        }
    }

    public byte[] sendReceiptEmailsToAllClients(Long idReservation) throws IOException {
        ReservationEntity reservation = reservationRepository.findById(idReservation)
                .orElseThrow(() -> new RuntimeException("Reserva no encontrada con ID: " + idReservation));

        byte[] pdfBytes = generateReceiptPdf(idReservation);

        List<ClientEntity> clients = reservation.getIdClients().stream()
                .map(clientId -> clientRepository.findById(clientId)
                        .orElseThrow(() -> new RuntimeException("Cliente no encontrado con id " + clientId)))
                .toList();

        // Plantilla para el email
        String subject = "KartingRM - Tu comprobante para la reserva #" + idReservation;
        String baseBody = "Este comprobante debe mostrarse el dia de la reserva."
                + "Detalles de la reserva:\n"
                + "- Fecha: %s\n"
                + "- Categoría: %s\n"
                + "- Participantes: %d\n\n"
                + "Gracias por escoger nuestro servicio!\n\n"
                + "KartingRM";

        // Enviar email a cada cliente
        for (ClientEntity client : clients) {
            String personalizedBody = "Hola " + client.getFirstName() + ",\n\n"
                    + String.format(baseBody,
                    reservation.getBookingDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")),
                    reservation.getCategory().name(),
                    clients.size());

            sendReceiptEmail(
                    client.getEmail(),
                    subject,
                    personalizedBody,
                    pdfBytes,
                    "comprobante_" + idReservation + ".pdf"
            );
        }
        return pdfBytes;
    }

}
