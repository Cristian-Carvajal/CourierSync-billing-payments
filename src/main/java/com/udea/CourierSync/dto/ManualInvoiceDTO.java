package com.udea.CourierSync.dto;

import lombok.Builder;
import lombok.Data;
import org.springframework.hateoas.RepresentationModel;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
public class ManualInvoiceDTO extends RepresentationModel<ManualInvoiceDTO> {
    private Long id;
    private String clientName;
    private String shipmentReferenceId;
    private LocalDate emissionDate;
    private BigDecimal amount;
    private String paymentStatus;
}