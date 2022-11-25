package dev.claycheng.knocknut.api.feign.user;

import dev.claycheng.api.enums.MemberStatus;
import java.time.LocalDate;
import lombok.Data;

@Data
public class Member {
  private Long id;
  private Long memberLevelId;
  private String username;
  private String password;
  private String nickname;
  private String email;
  private String phone;
  private MemberStatus status;
  private String avatar;
  private LocalDate birthday;
}
