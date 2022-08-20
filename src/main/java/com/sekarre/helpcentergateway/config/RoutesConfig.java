package com.sekarre.helpcentergateway.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class RoutesConfig {

    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("core", r -> r.path("/api/v1/issues/**", "/api/v1/comments/**", "/api/v1/users/**")
                        .filters(f-> f.circuitBreaker(config -> config
                                .setName("fallback-core")
                                .setFallbackUri("forward:/default-fallback")
                        ))
                        .uri("lb://help-center-core"))
                .route("auth", r -> r.path("/api/v1/auth/**")
                        .filters(f-> f.circuitBreaker(config -> config
                                .setName("fallback-auth")
                                .setFallbackUri("forward:/default-fallback")
                        ))
                        .uri("lb://help-center-auth"))
                .route("chat-info", r -> r.path("/api/v1/chat-info/**", "/websocket/**")
                        .filters(f-> f.circuitBreaker(config -> config
                                .setName("fallback-chat")
                                .setFallbackUri("forward:/default-fallback")
                        ))
                        .uri("lb://help-center-chat"))
                .route("notification", r -> r.path("/api/v1/event-notifications/**")
                        .filters(f-> f.circuitBreaker(config -> config
                                .setName("fallback-notification")
                                .setFallbackUri("forward:/default-fallback")
                        ))
                        .uri("lb://help-center-notification"))
                .build();
    }
}