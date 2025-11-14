package com.example.apigateway.service;

import com.example.apigateway.config.ServiceEndpointsProperties;
import com.example.apigateway.model.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class SecurityServiceClient {

    private final WebClient webClient;
    private final ServiceEndpointsProperties services;

    public SecurityServiceClient(WebClient.Builder builder,ServiceEndpointsProperties services) {
        this.webClient = builder.build();
        this.services = services;
    }

    /**
     * LOGIN
     */
    public Mono<AuthResponse> login(LoginRequest request) {

        ServiceEndpointsProperties.Service security = services.getSecurity();
        String url = security.getBaseUrl() + security.getLoginPath();
        // baseUrl = http://auth-app:8080, loginPath = /api/auth/login

        return webClient.post()
                .uri(url)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.TEXT_PLAIN)  // auth-app devuelve text/plain
                .bodyValue(request)
                .retrieve()
                .onStatus(HttpStatusCode::isError, resp ->
                        resp.bodyToMono(String.class)
                                .defaultIfEmpty("")
                                .flatMap(body ->
                                        Mono.error(new RuntimeException(
                                                "Error en login contra auth-app. " +
                                                        "Status=" + resp.statusCode() +
                                                        " Body=" + body
                                        ))
                                )
                )
                // auth-app devuelve el token como String plano
                .bodyToMono(String.class)
                .map(AuthResponse::new);  // lo envolvemos en AuthResponse { token }
    }

    /**
     * REGISTER
     */
    public Mono<UserResponse> register(RegisterRequest request) {

        ServiceEndpointsProperties.Service security = services.getSecurity();
        String url = security.getBaseUrl() + security.getRegisterPath();
        // Con tus props actuales baseUrl = http://auth-app:8080
        // y usersPath/registerPath = /api/users  → POST http://auth-app:8080/api/users

        return webClient.post()
                .uri(url)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(request)
                .retrieve()
                .onStatus(HttpStatusCode::isError, resp ->
                        resp.bodyToMono(String.class)
                                .defaultIfEmpty("")
                                .flatMap(body ->
                                        Mono.error(new RuntimeException(
                                                "Error al registrar usuario en auth-app. " +
                                                        "Status=" + resp.statusCode() +
                                                        " Body=" + body
                                        ))
                                )
                )
                .bodyToMono(UserResponse.class);
    }

    /**
     * GET USER BY ID
     */
    public Mono<UserResponse> getUserById(String userId) {

        String url = services.getSecurity().getBaseUrl()
                + services.getSecurity().getUsersPath()
                + "/" + userId;

        return webClient.get()
                .uri(url)
                .retrieve()
                .bodyToMono(UserResponse.class);
    }

    /**
     * UPDATE USER
     */
    public Mono<UserResponse> updateUser(String userId, UpdateUserRequest request) {

        String url = services.getSecurity().getBaseUrl()
                + services.getSecurity().getUsersPath()
                + "/" + userId;

        return webClient.put()
                .uri(url)
                .bodyValue(request)
                .retrieve()
                .bodyToMono(UserResponse.class);
    }

    /**
     * DELETE USER
     */
    public Mono<Void> deleteUser(String userId) {

        String url = services.getSecurity().getBaseUrl()
                + services.getSecurity().getUsersPath()
                + "/" + userId;

        return webClient.delete()
                .uri(url)
                .retrieve()
                .bodyToMono(Void.class);
    }
}
