package com.nexuscode.registro.user;

import com.nexuscode.registro.auth.AuthController.UserResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/usuarios")
@PreAuthorize("hasRole('ADMIN')")
public class UserController {
    private final UserRepository users; private final PasswordEncoder encoder;
    public UserController(UserRepository users, PasswordEncoder encoder) { this.users = users; this.encoder = encoder; }
    @GetMapping public List<UserResponse> all() { return users.findAll().stream().map(UserResponse::from).toList(); }
    @PostMapping @ResponseStatus(HttpStatus.CREATED)
    public UserResponse create(@Valid @RequestBody UserRequest request) {
        if (users.existsByEmail(request.email())) throw new ResponseStatusException(HttpStatus.CONFLICT, "El email ya esta registrado");
        return UserResponse.from(users.save(new User(request.nombre(), request.email(), encoder.encode(request.password()), request.rol(), request.telefono())));
    }
    @PutMapping("/{id}") public UserResponse update(@PathVariable Long id, @Valid @RequestBody UpdateUserRequest request) {
        User user = users.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado"));
        user.setNombre(request.nombre()); user.setRol(request.rol()); user.setTelefono(request.telefono()); user.setActivo(request.activo());
        return UserResponse.from(users.save(user));
    }
    @DeleteMapping("/{id}") @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deactivate(@PathVariable Long id) { User user = users.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND)); user.setActivo(false); users.save(user); }
    public record UserRequest(@NotBlank String nombre, @Email @NotBlank String email, @NotBlank String password, @NotNull Role rol, String telefono) {}
    public record UpdateUserRequest(@NotBlank String nombre, @NotNull Role rol, String telefono, boolean activo) {}
}