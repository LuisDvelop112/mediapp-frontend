package com.mediapp.citasbackend.services.interfaces;

import com.mediapp.citasbackend.dtos.AuthResponseDTO;
import com.mediapp.citasbackend.dtos.LoginRequestDTO;
import com.mediapp.citasbackend.dtos.RegisterRequestDTO;

public interface AuthService {
    
    /**
     * Registra un nuevo usuario en el sistema
     */
    AuthResponseDTO register(RegisterRequestDTO request);
    
    /**
     * Autentica un usuario y retorna los tokens JWT
     */
    AuthResponseDTO login(LoginRequestDTO request);
    
    /**
     * Refresca el token de acceso usando el refresh token
     */
    AuthResponseDTO refreshToken(String refreshToken);
}
