package dev.claycheng.api;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum CommonApiResult implements ApiResult {

  /** */
  SUCCESS(200, "operation success"),
  ACCEPTED(202, "operation accepted"),
  INVALID_PAYLOAD(400, "invalid request payload"),
  UNAUTHORIZED(40101, "token was not found or the token is expired"),
  EMPTY_AUTH_HEADER(40102, "empty authorization header"),
  TOO_MANY_REQUEST(429, "too many request"),
  DATA_NOT_FOUND(403, "data not found"),
  ;

  private final int code;
  private final String message;
}
