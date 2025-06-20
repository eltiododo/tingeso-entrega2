package com.tingeso.kartingrm.repositories;

import com.tingeso.kartingrm.entities.ReservationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ReservationRepository extends JpaRepository<ReservationEntity, Long> {
    List<ReservationEntity> findByReserveeClientId(Long clientId);
    List<ReservationEntity> findByBookingDateBetween(LocalDateTime initialDate, LocalDateTime finalDate);
}
