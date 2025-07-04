package com.udea.CourierSync.dto;

import lombok.Builder;
import lombok.Data;
import org.springframework.hateoas.RepresentationModel;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
public class InvoiceDTO extends RepresentationModel<InvoiceDTO> {

    private Long invoiceId;
    private ClientInfoDTO client;
    private ShipmentInfoDTO shipment;
    private BigDecimal totalAmount;
    private LocalDate emissionDate;
    private LocalDate dueDate;
    private String status;
}