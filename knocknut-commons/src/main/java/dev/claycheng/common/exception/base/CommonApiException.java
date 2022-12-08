package dev.claycheng.common.exception.base;

import dev.claycheng.api.CommonApiResult;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class CommonApiException extends RuntimeException {

  private final CommonApiResult apiResult;
}
