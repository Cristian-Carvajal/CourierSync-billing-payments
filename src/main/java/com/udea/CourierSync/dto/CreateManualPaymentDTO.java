package com.udea.CourierSync.dto;

import com.udea.CourierSync.entity.InvoiceStatus;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class CreateManualPaymentDTO {

    @NotNull(message = "El ID de la factura manual es obligatorio.")
    private Long manualInvoiceId;

    @NotNull(message = "El monto del pago es obligatorio.")
    @Positive(message = "El monto debe ser un valor positivo.")
    private BigDecimal amount;

    @NotNull(message = "La fecha del pago es obligatoria.")
    @PastOrPresent(message = "La fecha del pago no puede ser en el futuro.")
    private String paymentDate;

    @NotNull(message = "Debe especificar el nuevo estado de la factura.")
    private InvoiceStatus newStatus; // El usuario elegirá entre PENDING o PAID
}