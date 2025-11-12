package com.example.apigateway.web;

import com.example.apigateway.service.SecurityServiceClient;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final SecurityServiceClient securityServiceClient;

    @PostMapping("/login")
    public Mono<ResponseEntity<String>> login(@RequestBody Map<String, Object> payload) {
        return securityServiceClient.authenticate(payload);
    }

    @PostMapping("/register")
    public Mono<ResponseEntity<String>> register(@RequestBody Map<String, Object> payload) {
        return securityServiceClient.register(payload);
    }
}

