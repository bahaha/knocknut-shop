package dev.claycheng.knocknut.filter;

import dev.claycheng.api.CommonApiResult;
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

    var tokenEnhancement = apiRequest.getTokenEnahcement()
        .orElseThrow(CommonApiResult.EMPTY_AUTH_HEADER::toException);

    var enhancedRequest = exchange.getRequest().mutate()
        .header("memberId", tokenEnhancement.getMemberId())
        .header("username", tokenEnhancement.getUsername())
        .header("nickname", tokenEnhancement.getNickname())
        .header("avatar", tokenEnhancement.getAvatar())
        .header("email", tokenEnhancement.getEmail())
        .header("status", tokenEnhancement.getStatus().getValue().toString())
        .build();

    var enhancedExchange = exchange.mutate().request(enhancedRequest).build();
    return chain.filter(enhancedExchange);
  }
}
