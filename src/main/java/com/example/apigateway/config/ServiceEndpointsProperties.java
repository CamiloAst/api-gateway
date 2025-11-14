package com.example.apigateway.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "services")
public class ServiceEndpointsProperties {

    private final Service security = new Service();
    private final Service profile = new Service();

    public Service getSecurity() {
        return security;
    }

    public Service getProfile() {
        return profile;
    }

    public static class Service {
        /**
         * URL base del microservicio, ej: http://auth-app:8080
         */
        private String baseUrl;

        /**
         * Rutas específicas
         */
        private String loginPath;
        private String registerPath;
        private String usersPath;
        private String profilesPath;

        // getters y setters

        public String getBaseUrl() {
            return baseUrl;
        }

        public void setBaseUrl(String baseUrl) {
            this.baseUrl = baseUrl;
        }

        public String getLoginPath() {
            return loginPath;
        }

        public void setLoginPath(String loginPath) {
            this.loginPath = loginPath;
        }

        public String getRegisterPath() {
            return registerPath;
        }

        public void setRegisterPath(String registerPath) {
            this.registerPath = registerPath;
        }

        public String getUsersPath() {
            return usersPath;
        }

        public void setUsersPath(String usersPath) {
            this.usersPath = usersPath;
        }

        public String getProfilesPath() {
            return profilesPath;
        }

        public void setProfilesPath(String profilesPath) {
            this.profilesPath = profilesPath;
        }
    }
}
