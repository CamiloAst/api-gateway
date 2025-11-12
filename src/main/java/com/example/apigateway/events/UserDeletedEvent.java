package com.example.apigateway.events;

import org.springframework.context.ApplicationEvent;

public class UserDeletedEvent extends ApplicationEvent {

    private final String userId;

    public UserDeletedEvent(Object source, String userId) {
        super(source);
        this.userId = userId;
    }

    public String getUserId() {
        return userId;
    }
}

