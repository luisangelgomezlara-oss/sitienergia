package com.nexuscode.registro.controllers;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.nexuscode.registro.controllers.AuthController.UserResponse;
import com.nexuscode.registro.entities.Role;
import com.nexuscode.registro.services.UserService;

@RestController
@RequestMapping("/api/usuarios")
@PreAuthorize("hasRole('ADMIN')")
public class UserController {
    
    // Inyección de dependencias mediante la Interfaz (Principio SOLID: DIP)
    private final UserService userService;

    public UserController(UserService userService) { 
        this.userService = userService; 
    }

    @GetMapping 
    public List<UserResponse> all() { 
        return userService.obtenerTodos(); 
    }

    @PostMapping 
    @ResponseStatus(HttpStatus.CREATED)
    public UserResponse create(@Valid @RequestBody UserRequest request) {
        return userService.crearUsuario(request);
    }

    @PutMapping("/{id}") 
    public UserResponse update(@PathVariable Long id, @Valid @RequestBody UpdateUserRequest request) {
        return userService.actualizarUsuario(id, request);
    }

    @DeleteMapping("/{id}") 
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deactivate(@PathVariable Long id) { 
        userService.desactivarUsuario(id); 
    }

    // Los records (DTOs) se pueden quedar aquí por ahora para no romper tu código.
    public record UserRequest(@NotBlank String nombre, @Email @NotBlank String email, @NotBlank String password, @NotNull Role rol, String telefono) {}
    public record UpdateUserRequest(@NotBlank String nombre, @NotNull Role rol, String telefono, boolean activo) {}
}