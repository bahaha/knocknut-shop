package dev.claycheng.knocknut.members.domain;

import dev.claycheng.api.enums.MemberStatus;
import dev.claycheng.knocknut.members.domain.enums.Gender;
import java.time.Instant;
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
  private Gender gender;
  private LocalDate birthday;
  private Integer source;
  private Integer credit;
  private Integer totalCredit;
  private Instant createdAt;
  private Instant lastModifiedAt;
}
