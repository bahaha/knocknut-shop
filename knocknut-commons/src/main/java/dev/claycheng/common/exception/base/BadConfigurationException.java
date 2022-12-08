package dev.claycheng.common.exception.base;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class BadConfigurationException extends RuntimeException {

  private final String serviceName;
  private final String configDescription;

  @Override
  public String getMessage() {
    return "[" + serviceName + "] - " + configDescription;
  }
}
