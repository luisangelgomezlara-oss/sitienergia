package com.nexuscode.registro.services;

import com.nexuscode.registro.controllers.AuthController.UserResponse;
import com.nexuscode.registro.controllers.UserController.UserRequest;
import com.nexuscode.registro.controllers.UserController.UpdateUserRequest;
import java.util.List;

public interface UserService {
    List<UserResponse> obtenerTodos();
    UserResponse crearUsuario(UserRequest request);
    UserResponse actualizarUsuario(Long id, UpdateUserRequest request);
    void desactivarUsuario(Long id);
}