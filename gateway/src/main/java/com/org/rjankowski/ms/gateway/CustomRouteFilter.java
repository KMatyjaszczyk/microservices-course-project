package com.org.rjankowski.ms.gateway;

import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.GatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
public class CustomRouteFilter implements GatewayFilterFactory {
    private Long port8088count = 0L;
    private Long port8089count = 0L;

    @Override
    public Class getConfigClass() {
        return Object.class;
    }

    @Override

    public GatewayFilter apply(Object config) {
        return (exchange, chain) -> {
            return chain.filter(exchange)
                    .then(Mono.fromRunnable(
                            () -> {
                                HttpHeaders headers = exchange.getResponse().getHeaders();
                                List<String> ports = headers.get("x-port");
                                String port = ports.get(0);
                                System.out.println(port);

                                if ("8088".equals(port)) {
                                    System.out.println("Liczba strzalow do 8088: " + ++port8088count);
                                }

                                if ("8089".equals(port)) {
                                    System.out.println("Liczba strzalow do 8089: " + ++port8089count);
                                }
                            }
                    ));
        };

    }
}
