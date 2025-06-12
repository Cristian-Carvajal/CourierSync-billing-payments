package com.udea.CourierSync.service;

import com.udea.CourierSync.dto.CreateClientDTO;
import com.udea.CourierSync.entity.Client;
import com.udea.CourierSync.exception.BusinessException;
import com.udea.CourierSync.repository.ClientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ClientService {

    private final ClientRepository clientRepository;

    public Client createClient(CreateClientDTO dto) {
        // Validación de negocio: verificar si ya existe un cliente con el mismo email
        clientRepository.findByEmail(dto.getEmail()).ifPresent(client -> {
            throw new BusinessException("Ya existe un cliente con el email: " + dto.getEmail());
        });

        Client newClient = new Client();
        newClient.setName(dto.getName());
        newClient.setAddress(dto.getAddress());
        newClient.setPhoneNumber(dto.getPhoneNumber());
        newClient.setEmail(dto.getEmail());
        newClient.setShipmentPreferences(dto.getShipmentPreferences());

        return clientRepository.save(newClient);
    }
}