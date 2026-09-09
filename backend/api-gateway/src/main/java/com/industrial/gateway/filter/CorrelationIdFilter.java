package com.industrial.gateway.filter;

import java.util.UUID;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
public class CorrelationIdFilter implements GlobalFilter, Ordered {
    public static final String HEADER = "X-Correlation-Id";

    @Override
    public Mono<Void> filter(ServerWebExchange exchange,
            org.springframework.cloud.gateway.filter.GatewayFilterChain chain) {
        String id = exchange.getRequest().getHeaders().getFirst(HEADER);
        if (id == null || id.isBlank())
            id = UUID.randomUUID().toString();
        ServerWebExchange mutated = exchange.mutate().request(exchange.getRequest().mutate().header(HEADER, id).build())
                .build();
        String finalId = id;
        mutated.getResponse().beforeCommit(() -> {
            mutated.getResponse().getHeaders().set(HEADER, finalId);
            return Mono.empty();
        });
        return chain.filter(mutated);
    }

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE;
    }
}
