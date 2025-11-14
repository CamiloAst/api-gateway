package com.example.apigateway.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.Map;

@Data
public class ProfileRequest {

    private String nickname;

    @JsonProperty("homepage_url")
    private String homepageUrl;

    @JsonProperty("public_contact")
    private String publicContact;

    private String address;
    private String bio;
    private String organization;
    private String country;

    @JsonProperty("social_links")
    private Map<String, Object> socialLinks;  // o Map<String, String> según lo que uses
}