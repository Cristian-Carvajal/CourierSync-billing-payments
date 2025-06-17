package com.udea.CourierSync.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class CreateManualPaymentDTO {

    @NotNull(message = "El ID de la factura manual es obligatorio.")
    private Long manualInvoiceId;

    @NotNull(message = "El monto del pago es obligatorio.")
    @Positive(message = "El monto debe ser un valor positivo.")
    private BigDecimal amount;

    @NotBlank(message = "La fecha del pago es obligatoria.")
    private String paymentDate;

    @NotBlank(message = "Debe especificar el nuevo estado de la factura.")
    @Pattern(regexp = "^(PENDING|PAID|OVERDUE)$", message = "El estado debe ser PENDING, PAID u OVERDUE")
    private String newStatus;
}