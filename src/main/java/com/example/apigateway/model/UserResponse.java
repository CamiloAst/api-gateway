package com.example.apigateway.model;

import com.example.apigateway.entity.Role;
import lombok.Data;

@Data
public class UserResponse {

    private Long id;
    private String username;
    private String email;
    private Role role;
}