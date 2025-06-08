package com.udea.CourierSync.controller;

import com.udea.CourierSync.dto.CreateShipmentDTO;
import com.udea.CourierSync.entity.Invoice;
import com.udea.CourierSync.entity.Shipment;
import com.udea.CourierSync.service.ShipmentService;
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
@RequestMapping("/shipments")
@RequiredArgsConstructor
@Tag(name = "Shipments", description = "API para la gestión de Envíos")
public class ShipmentController {

    private final ShipmentService shipmentService;

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'OPERATOR')")
    @Operation(summary = "Registra un nuevo envío y genera su factura automáticamente")
    public ResponseEntity<Void> createShipment(@Valid @RequestBody CreateShipmentDTO createShipmentDTO) {
        Shipment newShipment = shipmentService.createShipmentAndInvoice(createShipmentDTO);

        Invoice generatedInvoice = newShipment.getInvoices().get(0);

        URI location = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/invoices/{id}")
                .buildAndExpand(generatedInvoice.getId())
                .toUri();

        return ResponseEntity.created(location).build();
    }
}