package com.udea.CourierSync.controller;

import com.udea.CourierSync.dto.CreateManualInvoiceDTO;
import com.udea.CourierSync.dto.ManualInvoiceDTO;
import com.udea.CourierSync.entity.ManualInvoice;
import com.udea.CourierSync.service.ManualInvoiceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Pageable;

import java.net.URI;

@RestController
@RequestMapping("/manual-invoices")
@RequiredArgsConstructor
@Tag(name = "Manual Invoices", description = "API para la gestión de facturas manuales")
public class ManualInvoiceController {

    private final ManualInvoiceService manualInvoiceService;

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'FINANCE')")
    @Operation(summary = "Crea una nueva factura de forma manual")
    public ResponseEntity<ManualInvoiceDTO> createManualInvoice(@Valid @RequestBody CreateManualInvoiceDTO createDto) {
        ManualInvoice newInvoice = manualInvoiceService.create(createDto);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(newInvoice.getId())
                .toUri();

        ManualInvoiceDTO responseDto = toDto(newInvoice);
        // Podríamos añadir un self-link si creamos un endpoint GET /manual-invoices/{id} más adelante

        return ResponseEntity.created(location).body(responseDto);
    }

    private ManualInvoiceDTO toDto(ManualInvoice invoice) {
        return ManualInvoiceDTO.builder()
                .id(invoice.getId())
                .clientName(invoice.getClientName())
                .shipmentReferenceId(invoice.getShipmentReferenceId())
                .emissionDate(invoice.getEmissionDate())
                .amount(invoice.getAmount())
                .paymentStatus(invoice.getPaymentStatus().name())
                .build();
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'FINANCE')")
    @Operation(summary = "Obtiene una lista paginada de todas las facturas manuales")
    public ResponseEntity<PagedModel<ManualInvoiceDTO>> getAllManualInvoices(
            Pageable pageable,
            PagedResourcesAssembler<ManualInvoice> assembler) {

        Page<ManualInvoice> manualInvoicePage = manualInvoiceService.findAll(pageable);

        // El assembler convierte la Page de entidades a un PagedModel de DTOs,
        // aplicando el método toDto a cada elemento y añadiendo los enlaces de paginación.
        PagedModel<ManualInvoiceDTO> pagedModel = assembler.toModel(manualInvoicePage, this::toDto);

        return ResponseEntity.ok(pagedModel);
    }
}