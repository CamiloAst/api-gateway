package com.example.apigateway.service;

import com.example.apigateway.config.ServiceEndpointsProperties;
import com.example.apigateway.model.ProfileRequest;
import com.example.apigateway.model.ProfileResponse;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class ProfileServiceClient {

    private final WebClient webClient;
    private final ServiceEndpointsProperties services;

    public ProfileServiceClient(WebClient.Builder builder,
                                ServiceEndpointsProperties services) {
        this.webClient = builder.build();
        this.services = services;
    }

    private String baseUrl() {
        return services.getProfile().getBaseUrl();
    }

    private String profilesPath() {
        return services.getProfile().getProfilesPath();
    }

    public Mono<ProfileResponse> getProfile(String userId) {
        String url = baseUrl() + profilesPath() + "/" + userId;

        return webClient.get()
                .uri(url)
                .retrieve()
                .bodyToMono(ProfileResponse.class);
    }

    public Mono<ProfileResponse> upsertProfile(String userId, ProfileRequest request) {
        String url = baseUrl() + profilesPath() + "/" + userId;

        return webClient.put()
                .uri(url)
                .bodyValue(request)
                .retrieve()
                .bodyToMono(ProfileResponse.class);
    }

    public Mono<Void> deleteProfile(String userId) {
        String url = baseUrl() + profilesPath() + "/" + userId;

        return webClient.delete()
                .uri(url)
                .retrieve()
                .bodyToMono(Void.class);
    }
}
