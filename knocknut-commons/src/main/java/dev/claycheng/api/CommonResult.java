package dev.claycheng.api;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor()
@AllArgsConstructor()
@Data
public class CommonResult<T> implements ApiResult {

  private int code;
  private String message;
  private T data;

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

  public static <T> CommonResult<T> notFound(String message) {
    return new CommonResult<>(CommonApiResult.DATA_NOT_FOUND.getCode(), message, null);
  }

  public static <T> CommonResult<T> badConfiguration(String config) {
    return new CommonResult<>(CommonApiResult.BAD_CONFIGURATION.getCode(),
        "internal configuration error: " + config, null);
  }
}
