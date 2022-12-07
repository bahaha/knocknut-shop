package dev.claycheng.knocknut.repository.factory;

import dev.claycheng.knocknut.domain.ApiRequest;
import dev.claycheng.knocknut.props.PublicUrlsProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

@EnableConfigurationProperties(value = PublicUrlsProperties.class)
@RequiredArgsConstructor
@Component
public class ApiRequestBuilder {

  private final PublicUrlsProperties publicUrlsProperties;

  public ApiRequest fromReactor(ServerWebExchange exchange) {
    var request = exchange.getRequest();
    return new ApiRequest(
        request.getURI(), request.getHeaders(), publicUrlsProperties::isPublicPath);
  }
}
