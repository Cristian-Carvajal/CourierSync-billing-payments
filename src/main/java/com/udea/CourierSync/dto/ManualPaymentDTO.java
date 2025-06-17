// src/main/java/com/udea/CourierSync/dto/ManualPaymentDTO.java

package com.udea.CourierSync.dto;

import lombok.Builder;
import lombok.Data;
import org.springframework.hateoas.RepresentationModel; // <-- Asegúrate de que este import exista

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
//  AÑADE ESTO --> extends RepresentationModel<ManualPaymentDTO>
public class ManualPaymentDTO extends RepresentationModel<ManualPaymentDTO> {
    private Long id;
    private Long manualInvoiceId;
    private BigDecimal amount;
    //private String paymentMethod;
    private String paymentDate;
}