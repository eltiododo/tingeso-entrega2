package com.tingeso.kartingrm.repositories;

import com.tingeso.kartingrm.entities.ReservationEntity;
import com.tingeso.kartingrm.enums.ReservationCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Repository
public interface ReservationRepository extends JpaRepository<ReservationEntity, Long> {
    List<ReservationEntity> findByReserveeClientId(Long clientId);
    List<ReservationEntity> findByCategory(ReservationCategory reservationCategory);
    List<ReservationEntity> findByBookingDateBetween(LocalDateTime initialDate, LocalDateTime finalDate);
}
