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
import org.springframework.web.bind.annotation.*;

import java.net.URI;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@CrossOrigin
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

        ClientDTO clientDTO = ClientDTO.builder()
                .id(newClient.getId())
                .name(newClient.getName())
                .email(newClient.getEmail())
                .address(newClient.getAddress())
                .phoneNumber(newClient.getPhoneNumber())
                .shipmentPreferences(newClient.getShipmentPreferences())
                .build();

        clientDTO.add(linkTo(methodOn(ClientController.class).getClientById(newClient.getId())).withSelfRel());

        URI location = URI.create(clientDTO.getLink("self").get().getHref());

        return ResponseEntity.created(location).body(clientDTO);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Obtiene un cliente por su ID")
    public ResponseEntity<ClientDTO> getClientById(@PathVariable Long id) {
        Client client = clientService.findById(id);
        ClientDTO clientDTO = toDto(client);

        clientDTO.add(linkTo(methodOn(ClientController.class).getClientById(id)).withSelfRel());

        return ResponseEntity.ok(clientDTO);
    }

    private ClientDTO toDto(Client client) {
        return ClientDTO.builder()
                .id(client.getId())
                .name(client.getName())
                .address(client.getAddress())
                .phoneNumber(client.getPhoneNumber())
                .email(client.getEmail())
                .shipmentPreferences(client.getShipmentPreferences())
                .build();
    }
}