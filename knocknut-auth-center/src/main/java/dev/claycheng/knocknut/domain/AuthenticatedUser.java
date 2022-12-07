package dev.claycheng.knocknut.domain;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import dev.claycheng.api.enums.MemberStatus;
import dev.claycheng.knocknut.api.feign.user.MemberFeignApi;
import java.time.LocalDate;
import java.util.Collection;
import java.util.Map;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * The authenticated user which most data comes from the member api ({@link MemberFeignApi}) focus
 * on the authentication and authorization.
 *
 * @author Clay Cheng
 */
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

  /**
   * the jwt extra payload that resource server might need, like **memberId** to identify the
   * current login member. If some required property missing from the profile, the resource could
   * take the memberId from payload to ask the member service for more information.
   *
   * @return user profile that resource server might need
   */
  public Map<String, Object> getUserProfile() {
    return ImmutableMap.<String, Object>builder()
        .put("memberId", getId())
        .put("username", getUsername())
        .put("nickname", getNickname())
        .put("email", getEmail())
        .put("status", getStatus())
        .put("avatar", getAvatar())
        .build();
  }

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
