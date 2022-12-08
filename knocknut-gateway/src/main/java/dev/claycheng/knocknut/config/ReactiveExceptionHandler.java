package dev.claycheng.knocknut.config;

import dev.claycheng.api.CommonApiResult;
import dev.claycheng.common.exception.base.CommonApiException;
import java.util.Optional;
import org.springframework.boot.autoconfigure.web.ResourceProperties;
import org.springframework.boot.autoconfigure.web.reactive.error.AbstractErrorWebExceptionHandler;
import org.springframework.boot.web.reactive.error.ErrorAttributes;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerCodecConfigurer;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Configuration
public class ReactiveExceptionHandler extends AbstractErrorWebExceptionHandler {

  public ReactiveExceptionHandler(ErrorAttributes errorAttributes,
      ResourceProperties resourceProperties,
      ApplicationContext applicationContext,
      ServerCodecConfigurer configurer) {
    super(errorAttributes, resourceProperties, applicationContext);
    this.setMessageWriters(configurer.getWriters());
  }

  @Override
  protected RouterFunction<ServerResponse> getRoutingFunction(ErrorAttributes errorAttributes) {
    return RouterFunctions.route(RequestPredicates.all(), this::renderErrorResponse);
  }

  private Mono<ServerResponse> renderErrorResponse(ServerRequest request) {
    var error = getError(request);
    var apiResult = Optional.of(error)
        .filter(throwable -> throwable instanceof CommonApiException)
        .map(CommonApiException.class::cast)
        .map(CommonApiException::getApiResult)
        .orElse(CommonApiResult.INTERNAL_SERVER_ERROR);

    return ServerResponse.status(apiResult.getResponseStatus())
        .contentType(MediaType.APPLICATION_JSON)
        .body(BodyInserters.fromValue(apiResult.toResult()));
  }
}
