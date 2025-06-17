package com.udea.CourierSync.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class CreateInvoiceDTO {

    @NotBlank(message = "El nombre del cliente no puede estar vacío.")
    private String clientName;

    @NotBlank(message = "El ID de envío de referencia no puede estar vacío.")
    private String shipmentReferenceId;

    @NotNull(message = "La fecha de emisión es obligatoria.")
    private LocalDate emissionDate;

    @NotNull(message = "El monto es obligatorio.")
    @Positive(message = "El monto debe ser un valor positivo.")
    private BigDecimal amount;
}