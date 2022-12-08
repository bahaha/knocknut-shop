package dev.claycheng.knocknut.repository.factory;

import dev.claycheng.knocknut.domain.ApiRequest;
import dev.claycheng.knocknut.domain.TokenParser;
import dev.claycheng.knocknut.props.PublicUrlsProps;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

@EnableConfigurationProperties(value = {PublicUrlsProps.class})
@RequiredArgsConstructor
@Component
public class ApiRequestBuilder {

  private final PublicUrlsProps publicUrlsProps;
  private final TokenParser tokenParser;

  public ApiRequest fromReactor(ServerWebExchange exchange) {
    var request = exchange.getRequest();
    return new ApiRequest(
        request.getURI(),
        request.getHeaders(),
        publicUrlsProps::isPublicPath,
        tokenParser);
  }
}
