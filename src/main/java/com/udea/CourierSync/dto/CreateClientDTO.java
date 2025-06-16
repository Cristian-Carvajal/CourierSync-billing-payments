package com.udea.CourierSync.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CreateClientDTO {

    @NotBlank(message = "El nombre no puede estar vacío.")
    private String name;

    private String address;

    private String phoneNumber;

    @NotBlank(message = "El email no puede estar vacío.")
    @Email(message = "Debe ser una dirección de email válida.")
    private String email;

    private String shipmentPreferences;
}