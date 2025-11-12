package com.example.apigateway.service;

import com.example.apigateway.config.ServiceEndpointsProperties;
import java.util.Map;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

@Service
public class ProfileServiceClient {

    private final ServiceEndpointsProperties properties;
    private final WebClient webClient;

    public ProfileServiceClient(WebClient.Builder webClientBuilder, ServiceEndpointsProperties properties) {
        this.properties = properties;
        this.webClient = webClientBuilder.baseUrl(properties.getProfile().getBaseUrl()).build();
    }

    private WebClient profileWebClient() {
        return webClient;
    }

    public Mono<Map<String, Object>> getProfile(String userId) {
        String path = properties.getProfile().getProfilesPath() + "/" + userId;
        return profileWebClient()
                .get()
                .uri(path)
                .retrieve()
                .bodyToMono(Map.class)
                .onErrorResume(WebClientResponseException.NotFound.class, ex -> Mono.empty())
                .defaultIfEmpty(Map.of());
    }

    public Mono<Map<String, Object>> updateProfile(String userId, Map<String, Object> payload) {
        if (payload == null || payload.isEmpty()) {
            return Mono.just(Map.of());
        }
        String path = properties.getProfile().getProfilesPath() + "/" + userId;
        return profileWebClient()
                .put()
                .uri(path)
                .bodyValue(payload)
                .retrieve()
                .bodyToMono(Map.class)
                .defaultIfEmpty(Map.of());
    }

    public Mono<ResponseEntity<String>> forward(String relativePath, HttpMethod method, Object payload) {
        WebClient.RequestBodySpec requestSpec = profileWebClient()
                .method(method)
                .uri(relativePath);

        WebClient.RequestHeadersSpec<?> headersSpec = payload != null
                ? requestSpec.bodyValue(payload)
                : requestSpec;

        return headersSpec.exchangeToMono(clientResponse -> clientResponse.toEntity(String.class));
    }
}

