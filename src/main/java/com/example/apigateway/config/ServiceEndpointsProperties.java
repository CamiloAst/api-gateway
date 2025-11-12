package com.example.apigateway.config;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "services")
public class ServiceEndpointsProperties {

    private final Security security = new Security();
    private final Profile profile = new Profile();

    @Getter
    @Setter
    public static class Security {

        /**
         * Base URL for the security service.
         */
        @NotBlank
        private String baseUrl = "http://localhost:8081";

        /**
         * Relative path for the authentication login endpoint.
         */
        @NotBlank
        private String loginPath = "/auth/login";

        /**
         * Relative path for the user registration endpoint.
         */
        @NotBlank
        private String registerPath = "/auth/register";

        /**
         * Base relative path for user resources.
         */
        @NotBlank
        private String usersPath = "/users";
    }

    @Getter
    @Setter
    public static class Profile {

        /**
         * Base URL for the profile management service.
         */
        @NotBlank
        private String baseUrl = "http://localhost:8082";

        /**
         * Base relative path for profile resources.
         */
        @NotBlank
        private String profilesPath = "/profiles";
    }
}

