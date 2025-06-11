package com.udea.CourierSync.service;

import com.udea.CourierSync.dto.UpdateUserRoleDTO;
import com.udea.CourierSync.entity.User;
import com.udea.CourierSync.exception.ResourceNotFoundException;
import com.udea.CourierSync.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    // Nos aseguramos que la busqueda y el guardado se realicen, si una falla tod0 se revierte
    @Transactional
    public User updateUserRole(UpdateUserRoleDTO updateUserRoleDTO) {
        User userToUpdate = userRepository.findByEmail(updateUserRoleDTO.getEmail())
                .orElseThrow(() -> new ResourceNotFoundException("Usuario con email " + updateUserRoleDTO.getEmail() + " no encontrado"));

        // Evitamos que un admin se quite su rol de admin
        Authentication currentUser = SecurityContextHolder.getContext().getAuthentication();
        if (currentUser != null && currentUser.getName().equals(userToUpdate.getEmail())) {
            throw new IllegalArgumentException("Un administrador no puede cambiar su propio rol.");
        }

        userToUpdate.setRole(updateUserRoleDTO.getNewRole());

        return userRepository.save(userToUpdate);
    }
}