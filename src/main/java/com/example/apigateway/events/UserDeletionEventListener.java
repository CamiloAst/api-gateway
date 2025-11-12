package com.example.apigateway.events;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class UserDeletionEventListener {

    @EventListener
    public void onUserDeleted(UserDeletedEvent event) {
        log.info("User deletion event emitted for userId={}.", event.getUserId());
    }
}

