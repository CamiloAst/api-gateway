package com.example.apigateway.web;

import com.example.apigateway.events.UserDeletedEvent;
import com.example.apigateway.model.FullUserRequest;
import com.example.apigateway.model.FullUserResponse;
import com.example.apigateway.service.UserAggregationService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserAggregationService userAggregationService;
    private final ApplicationEventPublisher applicationEventPublisher;

    /**
     * GET /api/users/{userId}
     * Devuelve datos completos (user + profile).
     */
    @GetMapping("/{userId}")
    public Mono<ResponseEntity<FullUserResponse>> getUser(@PathVariable String userId) {
        return userAggregationService.getFullUser(userId)
                .map(ResponseEntity::ok);
    }

    /**
     * PUT /api/users/{userId}
     * Actualiza datos completos del usuario (seguridad + perfil).
     */
    @PutMapping("/{userId}")
    public Mono<ResponseEntity<FullUserResponse>> updateUser(
            @PathVariable String userId,
            @RequestBody FullUserRequest request
    ) {
        return userAggregationService.updateFullUser(userId, request)
                .map(ResponseEntity::ok);
    }

    /**
     * DELETE /api/users/{userId}
     * Elimina usuario en auth-app y perfil en profiles-service.
     * Luego publica el evento UserDeletedEvent.
     */
    @DeleteMapping("/{userId}")
    public Mono<ResponseEntity<Void>> deleteUser(@PathVariable String userId) {
        return userAggregationService.deleteFullUser(userId)
                // Solo se ejecuta si deleteFullUser() termina bien
                .then(Mono.fromRunnable(() ->
                        applicationEventPublisher.publishEvent(
                                new UserDeletedEvent(this, userId)
                        )
                ))
                .thenReturn(ResponseEntity.noContent().build());
    }
}
