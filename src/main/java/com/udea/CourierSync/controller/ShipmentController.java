package com.udea.CourierSync.controller;

import com.udea.CourierSync.dto.ClientDTO;
import com.udea.CourierSync.dto.CreateShipmentDTO;
import com.udea.CourierSync.dto.ShipmentDTO;
import com.udea.CourierSync.dto.ShipmentInfoDTO;
import com.udea.CourierSync.entity.Client;
import com.udea.CourierSync.entity.Invoice;
import com.udea.CourierSync.entity.Shipment;
import com.udea.CourierSync.service.ShipmentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@CrossOrigin
@RequestMapping("/shipments")
@RequiredArgsConstructor
@Tag(name = "Shipments", description = "API para la gestión de Envíos")
public class ShipmentController {

    private final ShipmentService shipmentService;

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'OPERATOR')")
    @Operation(summary = "Registra un nuevo envío y genera su factura automáticamente (Requiere ID de cliente y ruta, de lo contrario falla)")
    public ResponseEntity<Void> createShipment(@Valid @RequestBody CreateShipmentDTO createShipmentDTO) {
        Shipment newShipment = shipmentService.createShipmentAndInvoice(createShipmentDTO);

        Invoice generatedInvoice = newShipment.getInvoices().get(0);

        URI location = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/invoices/{id}")
                .buildAndExpand(generatedInvoice.getId())
                .toUri();

        return ResponseEntity.created(location).build();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'OPERATOR')")
    @Operation(summary = "Obtiene los detalles de un envío específico")
    public ResponseEntity<ShipmentDTO> getShipmentById(@PathVariable Long id) {
        Shipment shipment = shipmentService.findById(id);
        ShipmentDTO shipmentDTO = toDto(shipment);

        shipmentDTO.add(linkTo(methodOn(ShipmentController.class).getShipmentById(shipment.getId())).withSelfRel());

        shipmentDTO.add(linkTo(methodOn(ClientController.class).getClientById(shipment.getClient().getId())).withRel("client"));

        if (shipment.getInvoices() != null && !shipment.getInvoices().isEmpty()) {
            shipmentDTO.add(linkTo(methodOn(InvoiceController.class).getInvoiceById(shipment.getInvoices().get(0).getId())).withRel("invoice"));
        }

        return ResponseEntity.ok(shipmentDTO);
    }

    private ShipmentDTO toDto(Shipment shipment) {
        return ShipmentDTO.builder()
                .id(shipment.getId())
                .weight(shipment.getWeight())
                .priority(shipment.getPriority())
                .creationDate(shipment.getCreationDate())
                .estimatedDeliveryDate(shipment.getEstimatedDeliveryDate())
                .build();
    }
}