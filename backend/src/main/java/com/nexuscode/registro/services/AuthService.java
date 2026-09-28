package com.nexuscode.registro.services;

import com.nexuscode.registro.controllers.AuthController.LoginRequest;
import com.nexuscode.registro.controllers.AuthController.RegisterRequest;
import com.nexuscode.registro.controllers.AuthController.AuthResponse;
import com.nexuscode.registro.controllers.AuthController.UserResponse;

public interface AuthService {
    AuthResponse login(LoginRequest request);
    UserResponse register(RegisterRequest request);
}