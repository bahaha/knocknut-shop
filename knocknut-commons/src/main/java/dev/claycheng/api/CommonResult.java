package dev.claycheng.api;

import lombok.Data;

@Data
public class CommonResult<T> {
  private final int code;
  private final String message;
  private final T data;

  public static <T> CommonResult<T> success(T data) {
    return new CommonResult<>(
        CommonApiResult.SUCCESS.getCode(), CommonApiResult.SUCCESS.getMessage(), data);
  }

  public static <T> CommonResult<T> success(T data, String message) {
    return new CommonResult<>(CommonApiResult.SUCCESS.getCode(), message, data);
  }

  public static <T> CommonResult<T> accepted() {
    return new CommonResult<>(
        CommonApiResult.ACCEPTED.getCode(), CommonApiResult.ACCEPTED.getMessage(), null);
  }

  public static <T> CommonResult<T> notFound() {
    return new CommonResult<>(
        CommonApiResult.DATA_NOT_FOUND.getCode(),
        CommonApiResult.DATA_NOT_FOUND.getMessage(),
        null);
  }
}
