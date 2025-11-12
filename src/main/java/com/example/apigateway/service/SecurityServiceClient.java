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
public class SecurityServiceClient {

    private final ServiceEndpointsProperties properties;
    private final WebClient webClient;

    public SecurityServiceClient(WebClient.Builder webClientBuilder, ServiceEndpointsProperties properties) {
        this.properties = properties;
        this.webClient = webClientBuilder.baseUrl(properties.getSecurity().getBaseUrl()).build();
    }

    private WebClient securityWebClient() {
        return webClient;
    }

    public Mono<ResponseEntity<String>> authenticate(Map<String, Object> payload) {
        return forward(properties.getSecurity().getLoginPath(), HttpMethod.POST, payload);
    }

    public Mono<ResponseEntity<String>> register(Map<String, Object> payload) {
        return forward(properties.getSecurity().getRegisterPath(), HttpMethod.POST, payload);
    }

    public Mono<ResponseEntity<String>> deleteUser(String userId) {
        String path = properties.getSecurity().getUsersPath() + "/" + userId;
        return forward(path, HttpMethod.DELETE, null);
    }

    public Mono<Map<String, Object>> getUser(String userId) {
        String path = properties.getSecurity().getUsersPath() + "/" + userId;
        return securityWebClient()
                .get()
                .uri(path)
                .retrieve()
                .bodyToMono(Map.class)
                .onErrorResume(WebClientResponseException.NotFound.class, ex -> Mono.empty());
    }

    public Mono<Map<String, Object>> updateUser(String userId, Map<String, Object> payload) {
        if (payload == null || payload.isEmpty()) {
            return Mono.just(Map.of());
        }
        String path = properties.getSecurity().getUsersPath() + "/" + userId;
        return securityWebClient()
                .put()
                .uri(path)
                .bodyValue(payload)
                .retrieve()
                .bodyToMono(Map.class)
                .defaultIfEmpty(Map.of());
    }

    private Mono<ResponseEntity<String>> forward(String path, HttpMethod method, Object payload) {
        WebClient.RequestBodySpec requestSpec = securityWebClient()
                .method(method)
                .uri(path);

        WebClient.RequestHeadersSpec<?> headersSpec = payload != null
                ? requestSpec.bodyValue(payload)
                : requestSpec;

        return headersSpec.exchangeToMono(clientResponse -> clientResponse.toEntity(String.class));
    }
}

