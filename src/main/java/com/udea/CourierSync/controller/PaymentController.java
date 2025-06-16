package com.udea.CourierSync.controller;

import com.udea.CourierSync.dto.CreatePaymentDTO;
import com.udea.CourierSync.dto.PaymentDTO;
import com.udea.CourierSync.entity.Payment;
import com.udea.CourierSync.service.PaymentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedModel;

import java.net.URI;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@CrossOrigin
@RequestMapping("/payments")
@RequiredArgsConstructor
@Tag(name = "Payments", description = "API para la gestión de Pagos")
@SecurityRequirement(name = "bearer-key")
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'FINANCE')")
    @Operation(summary = "Registra un nuevo pago para una factura existente")
    public ResponseEntity<PaymentDTO> registerPayment(@Valid @RequestBody CreatePaymentDTO createPaymentDTO) {
        Payment newPayment = paymentService.createPayment(createPaymentDTO);
        PaymentDTO paymentDTO = toDto(newPayment);

        paymentDTO.add(linkTo(methodOn(PaymentController.class).getPaymentById(paymentDTO.getPaymentId())).withSelfRel());
        paymentDTO.add(linkTo(methodOn(InvoiceController.class).getInvoiceById(paymentDTO.getInvoiceId())).withRel("invoice"));

        URI location = linkTo(methodOn(PaymentController.class).getPaymentById(paymentDTO.getPaymentId())).toUri();

        return ResponseEntity.created(location).body(paymentDTO);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'FINANCE')")
    @Operation(summary = "Obtiene los detalles de un pago específico")
    public ResponseEntity<PaymentDTO> getPaymentById(@PathVariable Long id) {
        Payment payment = paymentService.findById(id);
        PaymentDTO paymentDTO = toDto(payment);

        return ResponseEntity.ok(paymentDTO);
    }


    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'FINANCE')")
    @Operation(summary = "Obtiene una lista paginada de todos los pagos")
    public ResponseEntity<PagedModel<PaymentDTO>> getAllPayments(
            Pageable pageable,
            PagedResourcesAssembler<Payment> assembler) {

        Page<Payment> paymentPage = paymentService.findAll(pageable);

        PagedModel<PaymentDTO> pagedModel = assembler.toModel(paymentPage, this::toDto);

        return ResponseEntity.ok(pagedModel);
    }

    private PaymentDTO toDto(Payment payment) {
        PaymentDTO dto = PaymentDTO.builder()
                .paymentId(payment.getId())
                .invoiceId(payment.getInvoice().getId())
                .amount(payment.getAmount())
                .paymentMethod(payment.getPaymentMethod())
                .paymentDate(payment.getPaymentDate())
                .build();

        dto.add(linkTo(methodOn(PaymentController.class).getPaymentById(payment.getId())).withSelfRel());
        dto.add(linkTo(methodOn(InvoiceController.class).getInvoiceById(payment.getInvoice().getId())).withRel("invoice"));

        return dto;
    }
}