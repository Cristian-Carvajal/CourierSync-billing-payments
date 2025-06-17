package com.udea.CourierSync.controller;

import com.udea.CourierSync.dto.CreateManualPaymentDTO;
import com.udea.CourierSync.dto.ManualPaymentDTO;
import com.udea.CourierSync.entity.ManualPayment;
import com.udea.CourierSync.service.ManualPaymentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/manual-payments")
@CrossOrigin
@RequiredArgsConstructor
@Tag(name = "Manual Payments", description = "API para la gestión de pagos de facturas manuales")
public class ManualPaymentController {

    private final ManualPaymentService manualPaymentService;

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'FINANCE')")
    @Operation(summary = "Registra un nuevo pago para una factura manual existente")
    public ResponseEntity<ManualPaymentDTO> registerPayment(@Valid @RequestBody CreateManualPaymentDTO createDto) {
        ManualPayment newPayment = manualPaymentService.createPayment(createDto);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(newPayment.getId())
                .toUri();

        return ResponseEntity.created(location).body(toDto(newPayment));
    }

    private ManualPaymentDTO toDto(ManualPayment payment) {
        ManualPaymentDTO dto = ManualPaymentDTO.builder()
                .id(payment.getId())
                .manualInvoiceId(payment.getManualInvoice().getId())
                .amount(payment.getAmount())
                .paymentDate(payment.getPaymentDate())
                .paymentDate(payment.getPaymentDate())
                .build();
        return dto;
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'FINANCE')")
    @Operation(summary = "Obtiene una lista paginada de todos los pagos de facturas manuales")
    public ResponseEntity<PagedModel<ManualPaymentDTO>> getAllManualPayments(
            Pageable pageable,
            PagedResourcesAssembler<ManualPayment> assembler) {

        Page<ManualPayment> paymentPage = manualPaymentService.findAll(pageable);

        PagedModel<ManualPaymentDTO> pagedModel = assembler.toModel(paymentPage, this::toDto);

        return ResponseEntity.ok(pagedModel);
    }
}