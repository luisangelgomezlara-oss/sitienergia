package com.nexuscode.registro.auth;

import com.nexuscode.registro.security.JwtService;
import com.nexuscode.registro.user.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final UserRepository users; private final PasswordEncoder encoder; private final JwtService jwt;
    public AuthController(UserRepository users, PasswordEncoder encoder, JwtService jwt) { this.users = users; this.encoder = encoder; this.jwt = jwt; }

    @PostMapping("/login")
    public AuthResponse login(@Valid @RequestBody LoginRequest request) {
        User user = users.findByEmail(request.email()).orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Credenciales invalidas"));
        if (!user.isActivo() || !encoder.matches(request.password(), user.getPasswordHash())) throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Credenciales invalidas");
        return new AuthResponse(jwt.createToken(user.getEmail(), user.getRol().name()), user.getId(), user.getNombre(), user.getEmail(), user.getRol().name());
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public UserResponse register(@Valid @RequestBody RegisterRequest request) {
        if (users.existsByEmail(request.email())) throw new ResponseStatusException(HttpStatus.CONFLICT, "El email ya esta registrado");
        return UserResponse.from(users.save(new User(request.nombre(), request.email(), encoder.encode(request.password()), Role.CLIENTE, request.telefono())));
    }

    public record LoginRequest(@Email @NotBlank String email, @NotBlank String password) {}
    public record RegisterRequest(@NotBlank String nombre, @Email @NotBlank String email, @NotBlank String password, String telefono) {}
    public record AuthResponse(String token, Long id, String nombre, String email, String rol) {}
    public record UserResponse(Long id, String nombre, String email, String rol, String telefono, boolean activo) {
        public static UserResponse from(User user) { return new UserResponse(user.getId(), user.getNombre(), user.getEmail(), user.getRol().name(), user.getTelefono(), user.isActivo()); }
    }
}