package dev.claycheng.knocknut.repository.factory;

import dev.claycheng.knocknut.api.feign.user.Member;
import dev.claycheng.knocknut.domain.AuthenticatedUser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AuthenticatedUserBuilder {

  private final AuthenticatedUserMapper authenticatedUserMapper;

  public AuthenticatedUser fromMember(Member member) {
    var authenticatedUser = authenticatedUserMapper.fromMember(member);

    return authenticatedUser;
  }
}
