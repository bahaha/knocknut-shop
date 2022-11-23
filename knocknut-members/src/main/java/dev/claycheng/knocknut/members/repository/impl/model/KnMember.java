package dev.claycheng.knocknut.members.repository.impl.model;

import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDate;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

@Getter
@Setter
@ToString
@Entity(name = "kn_member")
public class KnMember implements Serializable {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private Long memberLevelId;
  private String username;
  private String password;
  private String nickname;
  private String email;
  private String phone;
  private Integer status;
  private String avatar;
  private Integer gender;
  private LocalDate birthday;
  private Integer source;
  private Integer credit;
  private Integer totalCredits;

  @CreatedDate private Instant createdAt;
  @LastModifiedDate private Instant lastModifiedAt;
}
