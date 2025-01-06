package io.github.miris.scg.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import reactor.core.publisher.Mono;

@Configuration
public class GatewayRoutesConfig {

        @Bean
        public RouteLocator routeLocator(RouteLocatorBuilder builder) {
                return builder.routes()
                                // Route to Service
                                .route("serviceARoute", r -> r.path("/service-a/**")
                                                .filters(f -> f.stripPrefix(1)) // Remove '/service-a' from the path
                                                .uri("https://didactic-space-guide-6w6757pwg6jhrjv6-8080.app.github.dev/")) // Destination
                                // Route to Service with custom headers
                                .route("serviceBRoute", r -> r.path("/service-b/**")
                                                .filters(f -> f.addRequestHeader("X-Custom-Header", "CustomValue")
                                                                .stripPrefix(1))
                                                .uri("https://didactic-space-guide-6w6757pwg6jhrjv6-8080.app.github.dev/"))
                                .route("serviceCircuitBreaker", r -> r.path("/service-cb/**")
                                                .filters(f -> f.stripPrefix(1)
                                                                .circuitBreaker(config -> config
                                                                                .setName("serviceCircuitBreaker")
                                                                                .setFallbackUri("forward:/fallback")))
                                                .uri("https://didactic-space-guide-6w6757pwg6jhrjv6-8080.app.github.dev/"))
                                .route("headerCustomizer", r -> r.path("/header-customizer/**")
                                                .filters(f -> f.addRequestParameter("param", "value")
                                                                .stripPrefix(1))
                                                .uri("https://didactic-space-guide-6w6757pwg6jhrjv6-8080.app.github.dev/"))
                                .route("responseHedaerCustomizer", r -> r.path("/response-header-customizer/**")
                                                .filters(f -> f.addResponseHeader("X-Response-Id", "67890")
                                                                .stripPrefix(1))
                                                .uri("https://didactic-space-guide-6w6757pwg6jhrjv6-8080.app.github.dev/"))
                                .route("responseBodyCustomizer", r -> r.path("/response-body-customizer/**")
                                                .filters(f -> f.modifyResponseBody(String.class, String.class,
                                                                (exchange, s) -> {
                                                                        if (s.contains("Hello")) {
                                                                                return Mono.just(s.toUpperCase());
                                                                        }
                                                                        return Mono.just(s);
                                                                }).stripPrefix(1))
                                                .uri("https://didactic-space-guide-6w6757pwg6jhrjv6-8080.app.github.dev/"))
                                .build();
        }

}
