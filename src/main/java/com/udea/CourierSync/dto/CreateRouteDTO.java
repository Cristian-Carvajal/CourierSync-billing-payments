package com.udea.CourierSync.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class CreateRouteDTO {

    @NotBlank(message = "El origen no puede estar vacío.")
    private String origin;

    @NotBlank(message = "El destino no puede estar vacío.")
    private String destination;

    @NotNull(message = "La distancia no puede ser nula.")
    @Positive(message = "La distancia debe ser un valor positivo.")
    private Double distance;

    @NotNull(message = "El tiempo estimado no puede ser nulo.")
    @Positive(message = "El tiempo estimado debe ser un valor positivo.")
    private Integer estimatedTime; // en minutos

    private String trafficLevel;
}