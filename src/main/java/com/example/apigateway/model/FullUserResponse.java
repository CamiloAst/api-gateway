package com.example.apigateway.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Respuesta unificada que devuelve el ApiGateway.
 * Contiene:
 *  - información del usuario (auth-app)
 *  - información del perfil (profiles-service)
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FullUserResponse {

    private UserResponse user;
    private ProfileResponse profile;
}
