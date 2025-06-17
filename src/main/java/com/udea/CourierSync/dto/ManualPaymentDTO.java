package com.udea.CourierSync.dto;

import lombok.Builder;
import lombok.Data;
import org.springframework.hateoas.RepresentationModel;

import java.math.BigDecimal;

@Data
@Builder
public class ManualPaymentDTO extends RepresentationModel<ManualPaymentDTO> {
    private Long id;
    private Long manualInvoiceId;
    private BigDecimal amount;
    private String paymentDate;
}