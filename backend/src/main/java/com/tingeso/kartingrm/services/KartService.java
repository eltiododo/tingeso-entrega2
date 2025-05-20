package com.tingeso.kartingrm.services;

import com.tingeso.kartingrm.entities.KartEntity;
import com.tingeso.kartingrm.enums.KartState;
import com.tingeso.kartingrm.repositories.KartRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class KartService {
    @Autowired
    KartRepository kartRepository;

    public KartEntity createKart(String model, KartState state) {
        KartEntity kartEntity = new KartEntity();
        kartEntity.setModel(model);
        kartEntity.setState(state);
        return kartRepository.save(kartEntity);
    }

    public List<KartEntity> getAllKarts() {
        return kartRepository.findAll();
    }

    public KartEntity getKart(Long id) {
        Optional<KartEntity> kartEntity = kartRepository.findById(id);
        return kartEntity.orElse(null);
    }

    public KartEntity updateKart(KartEntity kart) {
        return kartRepository.save(kart);
    }

    public void deleteKartById(Long kartId) {
        kartRepository.deleteById(kartId);
    }
}
