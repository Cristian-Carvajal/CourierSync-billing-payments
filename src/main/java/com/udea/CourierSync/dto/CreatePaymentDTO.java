package com.udea.CourierSync.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class CreatePaymentDTO {

    @NotNull(message = "El ID de la factura es obligatorio.")
    private Long invoiceId;

    @NotNull(message = "El monto del pago es obligatorio.")
    @Positive(message = "El monto debe ser un valor positivo.")
    private BigDecimal amount;

    @NotBlank(message = "El método de pago no puede estar vacío.")
    private String paymentMethod;

    private LocalDate paymentDate;
}