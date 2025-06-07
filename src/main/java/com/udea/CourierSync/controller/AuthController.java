package com.udea.CourierSync.controller;

import com.udea.CourierSync.config.JwtService;
import com.udea.CourierSync.dto.AuthRequestDTO;
import com.udea.CourierSync.dto.AuthResponseDTO;
import com.udea.CourierSync.dto.RegisterDTO;
import com.udea.CourierSync.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.udea.CourierSync.entity.User;

import java.util.List;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authManager;
    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    // falta pedir la contraseña sin hash y hashearla
    @PostMapping("/login")
    public ResponseEntity<AuthResponseDTO> login(@RequestBody AuthRequestDTO authRequestDTO) {
        authManager.authenticate(new UsernamePasswordAuthenticationToken(
                authRequestDTO.getEmail(),
                authRequestDTO.getPassword())
        );
        UserDetails userDetails = userRepository.findByEmail(authRequestDTO.getEmail())
                .map(user -> new org.springframework.security.core.userdetails.User(
                        user.getEmail(), user.getPasswordHash(),
                        List.of(new SimpleGrantedAuthority("ROLE_" + user.getUserType()))))
                .orElseThrow();

        String token = jwtService.generateToken(userDetails);
        return ResponseEntity.ok(new AuthResponseDTO(token));
    }

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody RegisterDTO registerDTO) {
        if (userRepository.findByEmail(registerDTO.getEmail()).isPresent()) {
            return ResponseEntity.badRequest().body("This email is already in use");
        }

        String hashedPassword = passwordEncoder.encode(registerDTO.getPassword());

        User newUser = new User();
        newUser.setName(registerDTO.getName());
        newUser.setEmail(registerDTO.getEmail());
        newUser.setPasswordHash(hashedPassword);
        newUser.setUserType(registerDTO.getUserType()); // ej. "ADMIN", "USER", etc.

        userRepository.save(newUser);

        return ResponseEntity.ok("Usuario registrado exitosamente");
    }
}
