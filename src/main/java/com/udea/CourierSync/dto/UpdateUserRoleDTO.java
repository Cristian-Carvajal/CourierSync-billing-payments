package com.udea.CourierSync.dto;

import com.udea.CourierSync.entity.UserRole;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UpdateUserRoleDTO {

    @NotBlank(message = "El email del usuario es obligatorio.")
    @Email(message = "El formato del email no es válido.")
    private String email;

    @NotNull(message = "El nuevo rol es obligatorio.")
    private UserRole newRole;
}