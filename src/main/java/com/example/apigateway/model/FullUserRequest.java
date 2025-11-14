package com.example.apigateway.model;

import lombok.Data;

import java.util.Map;

/**
 * Request que llega al ApiGateway cuando el cliente quiere
 * actualizar TODOS los datos del usuario (seguridad + perfil).
 *
 * El gateway separa esto en:
 *  - UpdateUserRequest  -> auth-app
 *  - ProfileRequest     -> profiles-service
 */
@Data
public class FullUserRequest {

    // ===== Parte de seguridad (auth-app) =====
    private String email;
    private String username;
    private String phoneNumber;

    // ===== Parte de perfil (profiles-service) =====
    private String nickname;
    private String homepageUrl;
    private String publicContact;
    private String address;
    private String bio;
    private String organization;
    private String country;
    private Map<String, Object> socialLinks;
}