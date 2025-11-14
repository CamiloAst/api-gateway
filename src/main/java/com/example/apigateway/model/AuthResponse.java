package com.example.apigateway.model;


import lombok.Data;

@Data
public class AuthResponse {
    private String token;
    public AuthResponse() {}

    public AuthResponse(String token) {
        this.token = token;
    }
    private String userId;
    private String email;
    private String message;
}
