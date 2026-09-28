package com.nexuscode.registro.services;

import com.nexuscode.registro.controllers.AuthController.AuthResponse;
import com.nexuscode.registro.controllers.AuthController.LoginRequest;
import com.nexuscode.registro.controllers.AuthController.RegisterRequest;
import com.nexuscode.registro.controllers.AuthController.UserResponse;
import com.nexuscode.registro.entities.Role;
import com.nexuscode.registro.entities.User;
import com.nexuscode.registro.repositories.UserRepository;
import com.nexuscode.registro.security.JwtService;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class AuthServiceImpl implements AuthService {

    private final UserRepository users;
    private final PasswordEncoder encoder;
    private final JwtService jwt;

    public AuthServiceImpl(UserRepository users, PasswordEncoder encoder, JwtService jwt) {
        this.users = users;
        this.encoder = encoder;
        this.jwt = jwt;
    }

    @Override
    public AuthResponse login(LoginRequest request) {
        User user = users.findByEmail(request.email())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Credenciales invalidas"));
        
        if (!user.isActivo() || !encoder.matches(request.password(), user.getPasswordHash())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Credenciales invalidas");
        }
        
        return new AuthResponse(jwt.createToken(user.getEmail(), user.getRol().name()), user.getId(), user.getNombre(), user.getEmail(), user.getRol().name());
    }

    @Override
    public UserResponse register(RegisterRequest request) {
        if (users.existsByEmail(request.email())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "El email ya esta registrado");
        }
        
        User newUser = new User(request.nombre(), request.email(), encoder.encode(request.password()), Role.CLIENTE, request.telefono());
        return UserResponse.from(users.save(newUser));
    }
}