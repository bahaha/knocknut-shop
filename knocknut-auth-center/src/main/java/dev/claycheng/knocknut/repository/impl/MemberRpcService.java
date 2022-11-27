package dev.claycheng.knocknut.repository.impl;

import com.google.common.base.Strings;
import dev.claycheng.knocknut.api.feign.user.MemberFeignApi;
import dev.claycheng.knocknut.repository.KnocknutUserManager;
import dev.claycheng.knocknut.repository.factory.AuthenticatedUserBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberRpcService implements KnocknutUserManager {

  private final MemberFeignApi memberApi;
  private final AuthenticatedUserBuilder authenticatedUserBuilder;

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    if (Strings.isNullOrEmpty(username)) {
      throw new UsernameNotFoundException("missing required field [username]");
    }

    var result = memberApi.findByUsername(username);

    return authenticatedUserBuilder.fromMember(result.getData());
  }
}
