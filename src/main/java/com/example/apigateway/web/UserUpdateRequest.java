package com.example.apigateway.web;

import java.util.Map;

public record UserUpdateRequest(Map<String, Object> security, Map<String, Object> profile) {
}

