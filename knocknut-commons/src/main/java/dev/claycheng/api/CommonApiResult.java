package dev.claycheng.api;

import dev.claycheng.common.exception.base.CommonApiException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum CommonApiResult {

  /**
   *
   */
  SUCCESS(HttpStatus.OK, 200, "operation success"),
  ACCEPTED(HttpStatus.ACCEPTED, 202, "operation accepted"),
  INVALID_PAYLOAD(HttpStatus.BAD_REQUEST, 400, "invalid request payload"),
  UNAUTHORIZED(HttpStatus.UNAUTHORIZED, 40101, "token was not found or the token is expired"),
  EMPTY_AUTH_HEADER(HttpStatus.UNAUTHORIZED, 40102, "empty authorization header"),
  TOO_MANY_REQUEST(HttpStatus.TOO_MANY_REQUESTS, 429, "too many request"),
  DATA_NOT_FOUND(HttpStatus.FORBIDDEN, 403, "data not found"),
  INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, 500, "internal server error"),
  BAD_CONFIGURATION(HttpStatus.VARIANT_ALSO_NEGOTIATES, 506, "internal configuration error"),
  ;

  private final HttpStatus responseStatus;
  private final int code;
  private final String message;

  public CommonApiException toException() {
    return new CommonApiException(this);
  }

  public CommonResult<Void> toResult() {
    return new CommonResult<>(getCode(), getMessage(), null);
  }

  public <T> CommonResult<T> toResult(T data) {
    return new CommonResult<>(getCode(), getMessage(), data);
  }
}
