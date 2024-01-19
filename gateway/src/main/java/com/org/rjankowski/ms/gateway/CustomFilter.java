package com.org.rjankowski.ms.gateway;

import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
public class CustomFilter implements GlobalFilter {
    private Long customersCount = 0L;
    private Long registrationCount = 0L;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        String path = request.getPath().value();

        if ("/customers".equals(path)) {
            System.out.println("Liczba strzalow do customers: " + ++customersCount);
        }

        if ("/register".equals(path)) {
            System.out.println("Liczba strzalow do register: " + ++registrationCount);
        }

        return chain.filter(exchange);
    }
}
