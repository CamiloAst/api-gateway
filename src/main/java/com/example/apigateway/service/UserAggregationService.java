package com.example.apigateway.service;

import com.example.apigateway.web.UserUpdateRequest;
import java.util.LinkedHashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class UserAggregationService {

    private final SecurityServiceClient securityServiceClient;
    private final ProfileServiceClient profileServiceClient;

    public Mono<Map<String, Object>> getCompleteUser(String userId) {
        Mono<Map<String, Object>> securityMono = securityServiceClient.getUser(userId)
                .defaultIfEmpty(Map.of());
        Mono<Map<String, Object>> profileMono = profileServiceClient.getProfile(userId)
                .defaultIfEmpty(Map.of());

        return Mono.zip(securityMono, profileMono)
                .map(tuple -> aggregate(tuple.getT1(), tuple.getT2()));
    }

    public Mono<Map<String, Object>> updateCompleteUser(String userId, UserUpdateRequest request) {
        UserUpdateRequest safeRequest = request == null ? new UserUpdateRequest(null, null) : request;
        Mono<Map<String, Object>> securityMono = safeRequest.security() != null
                ? securityServiceClient.updateUser(userId, safeRequest.security())
                : Mono.just(Map.of());

        Mono<Map<String, Object>> profileMono = safeRequest.profile() != null
                ? profileServiceClient.updateProfile(userId, safeRequest.profile())
                : Mono.just(Map.of());

        return Mono.zip(securityMono, profileMono)
                .map(tuple -> aggregate(tuple.getT1(), tuple.getT2()));
    }

    private Map<String, Object> aggregate(Map<String, Object> securityData, Map<String, Object> profileData) {
        Map<String, Object> aggregated = new LinkedHashMap<>();
        aggregated.put("security", securityData == null ? Map.of() : securityData);
        aggregated.put("profile", profileData == null ? Map.of() : profileData);
        return aggregated;
    }
}

