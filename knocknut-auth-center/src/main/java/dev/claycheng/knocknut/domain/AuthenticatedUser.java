package dev.claycheng.knocknut.domain;

import com.google.common.collect.Lists;
import dev.claycheng.api.enums.MemberStatus;
import java.time.LocalDate;
import java.util.Collection;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Data
public class AuthenticatedUser implements UserDetails {
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

  @Override
  public boolean isEnabled() {
    return MemberStatus.ENABLED.equals(getStatus());
  }

  // TODO: with roles/authorities
  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return Lists.newArrayList();
  }

  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  @Override
  public boolean isAccountNonLocked() {
    return true;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }
}
