package com.example.apigateway.service;

import com.example.apigateway.model.*;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

@Service
public class UserAggregationService {

    private final SecurityServiceClient securityClient;
    private final ProfileServiceClient profileClient;

    public UserAggregationService(SecurityServiceClient securityClient,
                                  ProfileServiceClient profileClient) {
        this.securityClient = securityClient;
        this.profileClient = profileClient;
    }

    /**
     * Consulta de datos completos del usuario.
     * - Obtiene el usuario desde auth-app.
     * - Intenta obtener el perfil desde profiles-service.
     * - Si el perfil no existe (404), devuelve profile = null.
     */
    public Mono<FullUserResponse> getFullUser(String userId) {

        Mono<UserResponse> userMono = securityClient.getUserById(userId);

        Mono<ProfileResponse> profileMono = profileClient.getProfile(userId)
                // Si profiles-service responde 404, devolvemos Mono.empty()
                .onErrorResume(WebClientResponseException.NotFound.class, ex -> Mono.empty());

        return Mono.zip(userMono, profileMono.defaultIfEmpty(null))
                .map(tuple -> {
                    UserResponse user = tuple.getT1();
                    ProfileResponse profile = tuple.getT2();
                    return new FullUserResponse(user, profile);
                });
    }

    /**
     * Actualización de datos completos del usuario.
     * - Separa el request en:
     *      UpdateUserRequest (auth-app)
     *      ProfileRequest    (profiles-service)
     * - Llama a ambos microservicios y unifica la respuesta.
     */
    public Mono<FullUserResponse> updateFullUser(String userId, FullUserRequest request) {

        // ----- Mapeo a request de seguridad -----
        UpdateUserRequest userReq = new UpdateUserRequest();
        userReq.setEmail(request.getEmail());
        userReq.setUsername(request.getUsername());
        userReq.setPhonenumber(request.getPhoneNumber());

        // ----- Mapeo a request de perfil -----
        ProfileRequest profileReq = new ProfileRequest();
        profileReq.setNickname(request.getNickname());
        profileReq.setHomepageUrl(request.getHomepageUrl());
        profileReq.setPublicContact(request.getPublicContact());
        profileReq.setAddress(request.getAddress());
        profileReq.setBio(request.getBio());
        profileReq.setOrganization(request.getOrganization());
        profileReq.setCountry(request.getCountry());
        profileReq.setSocialLinks(request.getSocialLinks());

        Mono<UserResponse> userMono = securityClient.updateUser(userId, userReq);
        Mono<ProfileResponse> profileMono = profileClient.upsertProfile(userId, profileReq);

        return Mono.zip(userMono, profileMono)
                .map(tuple -> {
                    UserResponse user = tuple.getT1();
                    ProfileResponse profile = tuple.getT2();
                    return new FullUserResponse(user, profile);
                });
    }

    /**
     * Eliminación completa del usuario:
     * - Elimina el usuario en auth-app.
     * - Elimina el perfil en profiles-service.
     * Nota: esto normalmente se dispara desde tu UserController,
     *       además de publicar el evento user.deleted en RabbitMQ.
     */
    public Mono<Void> deleteFullUser(String userId) {
        Mono<Void> deleteUser = securityClient.deleteUser(userId);
        Mono<Void> deleteProfile = profileClient.deleteProfile(userId)
                .onErrorResume(WebClientResponseException.NotFound.class, ex -> Mono.empty());
        return Mono.when(deleteUser, deleteProfile).then();
    }
}