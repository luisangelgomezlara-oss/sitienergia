package com.nexuscode.registro.controllers;

import com.nexuscode.registro.services.AuthService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    
    private final AuthService authService;

    // Dependemos de la interfaz (Principios SOLID)
    public AuthController(AuthService authService) { 
        this.authService = authService; 
    }

    @PostMapping("/login")
    public AuthResponse login(@Valid @RequestBody LoginRequest request) {
        return authService.login(request);
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public UserResponse register(@Valid @RequestBody RegisterRequest request) {
        return authService.register(request);
    }

    // Los DTOs (Records) se quedan aquí para mantener la estructura
    public record LoginRequest(@Email @NotBlank String email, @NotBlank String password) {}
    public record RegisterRequest(@NotBlank String nombre, @Email @NotBlank String email, @NotBlank String password, String telefono) {}
    public record AuthResponse(String token, Long id, String nombre, String email, String rol) {}
    public record UserResponse(Long id, String nombre, String email, String rol, String telefono, boolean activo) {
        public static UserResponse from(com.nexuscode.registro.entities.User user) { 
            return new UserResponse(user.getId(), user.getNombre(), user.getEmail(), user.getRol().name(), user.getTelefono(), user.isActivo()); 
        }
    }
}