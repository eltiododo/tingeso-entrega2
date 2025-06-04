package com.tingeso.kartingrm.repositories;

import com.tingeso.kartingrm.entities.KartEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface KartRepository extends JpaRepository<KartEntity, Long> {}
