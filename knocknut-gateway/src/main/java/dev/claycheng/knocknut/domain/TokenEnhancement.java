package dev.claycheng.knocknut.domain;

import dev.claycheng.api.enums.MemberStatus;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TokenEnhancement {

  private String memberId;
  private String username;
  private String nickname;
  private String avatar;
  private String email;
  private MemberStatus status;
}
