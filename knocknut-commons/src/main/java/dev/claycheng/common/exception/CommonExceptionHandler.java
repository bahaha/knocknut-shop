package dev.claycheng.common.exception;

import dev.claycheng.api.CommonResult;
import dev.claycheng.common.exception.base.ForbiddenException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class CommonExceptionHandler {

  @ExceptionHandler({ForbiddenException.class})
  public ResponseEntity<CommonResult<Void>> forbidden(ForbiddenException ex) {
    return ResponseEntity.status(HttpStatus.FORBIDDEN).body(CommonResult.notFound(ex.getMessage()));
  }
}
