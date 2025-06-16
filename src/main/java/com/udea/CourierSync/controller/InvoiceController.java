package com.udea.CourierSync.controller;

import com.udea.CourierSync.dto.ClientInfoDTO;
import com.udea.CourierSync.dto.InvoiceDTO;
import com.udea.CourierSync.dto.ShipmentInfoDTO;
import com.udea.CourierSync.entity.Invoice;
import com.udea.CourierSync.entity.InvoiceStatus;
import com.udea.CourierSync.service.InvoiceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedModel;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@CrossOrigin
@RequestMapping("/invoices")
@RequiredArgsConstructor
@Tag(name = "Invoices", description = "API para la gestión de Facturas")
@SecurityRequirement(name = "bearer-key")
public class InvoiceController {

    private final InvoiceService invoiceService;

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'FINANCE')")
    @Operation(summary = "Obtiene los detalles de una factura específica")
    public ResponseEntity<InvoiceDTO> getInvoiceById(@PathVariable Long id) {
        Invoice invoice = invoiceService.findById(id);
        InvoiceDTO invoiceDTO = toDto(invoice);

        invoiceDTO.add(linkTo(methodOn(InvoiceController.class).getInvoiceById(id)).withSelfRel());
        invoiceDTO.add(linkTo(methodOn(InvoiceController.class).downloadInvoice(id)).withRel("download"));
        invoiceDTO.add(linkTo(methodOn(ClientController.class).getClientById(invoice.getClient().getId())).withRel("client"));
        invoiceDTO.add(linkTo(methodOn(ShipmentController.class).getShipmentById(invoice.getShipment().getId())).withRel("shipment"));
        if (invoice.getPaymentStatus() == InvoiceStatus.PENDING || invoice.getPaymentStatus() == InvoiceStatus.OVERDUE) {
            invoiceDTO.add(linkTo(methodOn(PaymentController.class).registerPayment(null)).withRel("add-payment"));
        }

        return ResponseEntity.ok(invoiceDTO);
    }

    @GetMapping("/{id}/download")
    @PreAuthorize("hasAnyRole('ADMIN', 'FINANCE')")
    @Operation(summary = "Descarga la factura en formato de texto")
    public ResponseEntity<String> downloadInvoice(@PathVariable Long id) {
        Invoice invoice = invoiceService.findById(id);

        String invoiceContent = generateInvoiceText(invoice);

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=factura-" + invoice.getId() + ".txt");

        return ResponseEntity.ok()
                .headers(headers)
                .contentType(MediaType.TEXT_PLAIN)
                .body(invoiceContent);
    }


    private InvoiceDTO toDto(Invoice invoice) {
        return InvoiceDTO.builder()
                .invoiceId(invoice.getId())
                .client(ClientInfoDTO.builder()
                        .id(invoice.getClient().getId())
                        .name(invoice.getClient().getName())
                        .email(invoice.getClient().getEmail())
                        .build())
                .shipment(ShipmentInfoDTO.builder()
                        .id(invoice.getShipment().getId())
                        .weight(invoice.getShipment().getWeight())
                        .build())
                .totalAmount(invoice.getTotalAmount())
                .emissionDate(invoice.getEmissionDate())
                .dueDate(invoice.getDueDate())
                .status(invoice.getPaymentStatus().name())
                .build();
    }

    private String generateInvoiceText(Invoice invoice) {
        return new StringBuilder()
                .append("========= FACTURA COURIERSYNC =========\n")
                .append("Factura N°: ").append(invoice.getId()).append("\n")
                .append("-----------------------------------------\n")
                .append("Cliente: ").append(invoice.getClient().getName()).append("\n")
                .append("Email: ").append(invoice.getClient().getEmail()).append("\n")
                .append("-----------------------------------------\n")
                .append("Envío ID: ").append(invoice.getShipment().getId()).append("\n")
                .append("Fecha de Emisión: ").append(invoice.getEmissionDate()).append("\n")
                .append("Fecha de Vencimiento: ").append(invoice.getDueDate()).append("\n")
                .append("-----------------------------------------\n")
                .append("MONTO TOTAL: $").append(invoice.getTotalAmount()).append("\n")
                .append("Estado: ").append(invoice.getPaymentStatus().name()).append("\n")
                .append("=========================================\n")
                .toString();
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'FINANCE')")
    @Operation(summary = "Obtiene una lista paginada de todas las facturas")
    public ResponseEntity<PagedModel<InvoiceDTO>> getAllInvoices(
            Pageable pageable,
            PagedResourcesAssembler<Invoice> assembler) {
        Page<Invoice> invoicePage = invoiceService.findAll(pageable);

        PagedModel<InvoiceDTO> pagedModel = assembler.toModel(invoicePage, this::toDto);

        return ResponseEntity.ok(pagedModel);
    }

}