package com.example.apigateway.web;

import com.example.apigateway.model.AuthResponse;
import com.example.apigateway.model.LoginRequest;
import com.example.apigateway.model.RegisterRequest;
import com.example.apigateway.model.UserResponse;
import com.example.apigateway.service.SecurityServiceClient;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final SecurityServiceClient securityClient;

    public AuthController(SecurityServiceClient securityClient) {
        this.securityClient = securityClient;
    }

    @PostMapping("/register")
    public Mono<UserResponse> register(@RequestBody RegisterRequest request) {
        return securityClient.register(request);
    }

    @PostMapping("/login")
    public Mono<AuthResponse> login(@RequestBody LoginRequest request) {
        return securityClient.login(request);
    }
}
