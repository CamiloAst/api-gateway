package com.example.apigateway.model;


import com.example.apigateway.entity.Role;
import lombok.Data;

@Data
public class UpdateUserRequest {

    private String username;
    private String email;
    private String phonenumber;

    // Solo incluye los campos que auth-app permite actualizar
}