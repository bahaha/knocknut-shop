package dev.claycheng.common.exception;

import dev.claycheng.common.exception.base.ForbiddenException;

public class DataNotFoundException extends ForbiddenException {

  public DataNotFoundException(String message) {
    super(message);
  }
}
