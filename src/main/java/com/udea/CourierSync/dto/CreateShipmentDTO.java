package com.udea.CourierSync.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class CreateShipmentDTO {

    @NotNull(message = "El ID del cliente es obligatorio.")
    private Long clientId;

    @NotNull(message = "El ID de la ruta es obligatorio.")
    private Long routeId;

    @NotNull(message = "El peso es obligatorio.")
    @Positive(message = "El peso debe ser un valor positivo.")
    private Double weight;

    private String priority = "NORMAL"; // Valor por defecto
}