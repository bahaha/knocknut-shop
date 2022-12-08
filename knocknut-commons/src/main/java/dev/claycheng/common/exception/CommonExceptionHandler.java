package dev.claycheng.common.exception;

import dev.claycheng.api.CommonApiResult;
import dev.claycheng.api.CommonResult;
import dev.claycheng.common.exception.base.BadConfigurationException;
import dev.claycheng.common.exception.base.CommonApiException;
import dev.claycheng.common.exception.base.ForbiddenException;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
@RestControllerAdvice
public class CommonExceptionHandler {

  @ExceptionHandler({ForbiddenException.class})
  public ResponseEntity<CommonResult<Void>> forbidden(ForbiddenException ex) {
    return ResponseEntity.status(HttpStatus.FORBIDDEN).body(CommonResult.notFound(ex.getMessage()));
  }

  @ExceptionHandler({BadConfigurationException.class})
  public ResponseEntity<CommonResult<Void>> badConfiguration(BadConfigurationException ex) {
    return ResponseEntity.status(HttpStatus.VARIANT_ALSO_NEGOTIATES)
        .body(CommonResult.badConfiguration(ex.getMessage()));
  }

  @ExceptionHandler({CommonApiException.class})
  public ResponseEntity<CommonResult<?>> commonResult(CommonApiResult apiResult) {
    return ResponseEntity.status(apiResult.getResponseStatus())
        .body(new CommonResult<>(apiResult.getCode(), apiResult.getMessage(), null));
  }
}
