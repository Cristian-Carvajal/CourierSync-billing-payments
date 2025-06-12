package com.udea.CourierSync.controller;

import com.udea.CourierSync.dto.ClientDTO;
import com.udea.CourierSync.dto.CreateClientDTO;
import com.udea.CourierSync.entity.Client;
import com.udea.CourierSync.service.ClientService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/clients")
@RequiredArgsConstructor
@Tag(name = "Clients", description = "API para la gestión de Clientes")
public class ClientController {

    private final ClientService clientService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Crea un nuevo cliente en el sistema")
    public ResponseEntity<ClientDTO> createClient(@Valid @RequestBody CreateClientDTO createClientDTO) {
        Client newClient = clientService.createClient(createClientDTO);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(newClient.getId())
                .toUri();

        ClientDTO clientDTO = ClientDTO.builder()
                .id(newClient.getId())
                .name(newClient.getName())
                .email(newClient.getEmail())
                .address(newClient.getAddress())
                .phoneNumber(newClient.getPhoneNumber())
                .shipmentPreferences(newClient.getShipmentPreferences())
                .build();

        return ResponseEntity.created(location).body(clientDTO);
    }
}