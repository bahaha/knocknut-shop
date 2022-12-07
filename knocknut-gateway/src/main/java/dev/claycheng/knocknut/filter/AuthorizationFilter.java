package dev.claycheng.knocknut.filter;

import dev.claycheng.knocknut.repository.factory.ApiRequestBuilder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@Slf4j
@Component
public class AuthorizationFilter implements GlobalFilter, Ordered {

  private final ApiRequestBuilder apiRequestBuilder;

  @Override
  public int getOrder() {
    return 0;
  }

  @Override
  public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
    var apiRequest = apiRequestBuilder.fromReactor(exchange);

    if (apiRequest.isPublic()) {
      return chain.filter(exchange);
    }

    return chain.filter(exchange);
  }
}
