package dev.claycheng.knocknut.domain;

import com.google.common.base.Strings;
import dev.claycheng.api.enums.MemberStatus;
import java.util.Optional;
import java.util.stream.Stream;
import lombok.Builder;
import lombok.Data;
import org.springframework.http.server.reactive.ServerHttpRequest;

@Data
@Builder
public class TokenEnhancement {

  private String memberId;
  private String username;
  private String nickname;
  private String avatar;
  private String email;
  private MemberStatus status;

  public ServerHttpRequest.Builder attachEnhancementToHeader(
      ServerHttpRequest.Builder requestBuilder) {
    Stream.of("memberId", "username", "nickname", "avatar", "email")
        .forEach(field -> getFieldValue(field)
            .ifPresent(value -> requestBuilder.header(field, value)));

    if (status != null) {
      requestBuilder.header("status", status.getValue().toString());
    }
    return requestBuilder;
  }

  private Optional<String> getFieldValue(String fieldName) {
    try {
      var field = TokenEnhancement.class.getDeclaredField(fieldName);
      field.setAccessible(true);
      return Optional.ofNullable(String.valueOf(field.get(this)))
          .filter(value -> !Strings.isNullOrEmpty(value));
    } catch (NoSuchFieldException | IllegalAccessException e) {
      return Optional.empty();
    }
  }
}
