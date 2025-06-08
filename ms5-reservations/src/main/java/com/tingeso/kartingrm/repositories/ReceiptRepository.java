package com.tingeso.kartingrm.repositories;

import com.tingeso.kartingrm.entities.ReceiptEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReceiptRepository extends JpaRepository<ReceiptEntity, Long> {
    ReceiptEntity findByIdReservation(Long idReservation);
    boolean existsByIdReservation(Long idReservation);
}
