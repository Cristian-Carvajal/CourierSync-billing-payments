package com.udea.CourierSync.controller;

import com.udea.CourierSync.dto.CreatePaymentDTO;
import com.udea.CourierSync.dto.PaymentDTO;
import com.udea.CourierSync.entity.Payment;
import com.udea.CourierSync.service.PaymentService; // <-- CAMBIO IMPORTANTE
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
@RequestMapping("/payments")
@RequiredArgsConstructor
@Tag(name = "Payments", description = "API para la gestión de Pagos")
public class PaymentController {

    private final PaymentService paymentService; // <-- AHORA USA EL PaymentService

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

        paymentDTO.add(linkTo(methodOn(PaymentController.class).getPaymentById(id)).withSelfRel());
        paymentDTO.add(linkTo(methodOn(InvoiceController.class).getInvoiceById(payment.getInvoice().getId())).withRel("invoice"));

        return ResponseEntity.ok(paymentDTO);
    }

    private PaymentDTO toDto(Payment payment) {
        return PaymentDTO.builder()
                .paymentId(payment.getId())
                .invoiceId(payment.getInvoice().getId())
                .amount(payment.getAmount())
                .paymentMethod(payment.getPaymentMethod())
                .paymentDate(payment.getPaymentDate())
                .build();
    }
}