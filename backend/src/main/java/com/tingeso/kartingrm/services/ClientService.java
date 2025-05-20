package com.tingeso.kartingrm.services;

import com.tingeso.kartingrm.dtos.ClientDTO;
import com.tingeso.kartingrm.entities.ClientEntity;
import com.tingeso.kartingrm.repositories.ClientRepository;
import com.tingeso.kartingrm.repositories.ReservationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class ClientService {
    @Autowired
    ClientRepository clientRepository;

    public ClientEntity registerClient(String firstName, String lastName, String email) {
        ClientEntity client = new ClientEntity();
        client.setFirstName(firstName);
        client.setLastName(lastName);
        client.setEmail(email);
        return clientRepository.save(client);
    }

    public ClientEntity getClientById(Long id) {
        return clientRepository.findById(id).orElse(null);
    }

}
