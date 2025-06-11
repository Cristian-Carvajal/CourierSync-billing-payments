package com.udea.CourierSync.controller;

import com.udea.CourierSync.dto.UpdateUserRoleDTO;
import com.udea.CourierSync.entity.User;
import com.udea.CourierSync.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
@Tag(name = "Admin", description = "Operaciones de administración de usuarios")
public class AdminController {

    private final UserService userService;

    @PutMapping("/users/role")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Actualiza el rol de un usuario")
    public ResponseEntity<String> updateUserRole(@Valid @RequestBody UpdateUserRoleDTO updateUserRoleDTO) {
        userService.updateUserRole(updateUserRoleDTO);
        return ResponseEntity.ok("Rol del usuario " + updateUserRoleDTO.getEmail() + " actualizado a " + updateUserRoleDTO.getNewRole() + " exitosamente.");
    }
}