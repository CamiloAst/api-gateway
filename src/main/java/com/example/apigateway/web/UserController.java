package com.example.apigateway.web;

import com.example.apigateway.events.UserDeletedEvent;
import com.example.apigateway.service.SecurityServiceClient;
import com.example.apigateway.service.UserAggregationService;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final SecurityServiceClient securityServiceClient;
    private final UserAggregationService userAggregationService;
    private final ApplicationEventPublisher applicationEventPublisher;

    @DeleteMapping("/{userId}")
    public Mono<ResponseEntity<String>> deleteUser(@PathVariable String userId) {
        return securityServiceClient.deleteUser(userId)
                .doOnSuccess(response -> {
                    if (response != null && response.getStatusCode().is2xxSuccessful()) {
                        applicationEventPublisher.publishEvent(new UserDeletedEvent(this, userId));
                    }
                });
    }

    @GetMapping("/{userId}")
    public Mono<ResponseEntity<Map<String, Object>>> getUser(@PathVariable String userId) {
        return userAggregationService.getCompleteUser(userId)
                .map(ResponseEntity::ok);
    }

    @PutMapping("/{userId}")
    public Mono<ResponseEntity<Map<String, Object>>> updateUser(
            @PathVariable String userId,
            @RequestBody(required = false) UserUpdateRequest request) {
        return userAggregationService.updateCompleteUser(userId, request == null ? new UserUpdateRequest(null, null) : request)
                .map(ResponseEntity::ok);
    }
}

