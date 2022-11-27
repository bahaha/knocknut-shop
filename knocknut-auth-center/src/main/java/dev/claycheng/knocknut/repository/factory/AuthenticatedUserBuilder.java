package dev.claycheng.knocknut.repository.factory;

import dev.claycheng.knocknut.api.feign.user.Member;
import dev.claycheng.knocknut.domain.AuthenticatedUser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * A builder transform between {@link Member} from the api of member service and the {@link
 * AuthenticatedUser} that represent an authenticated user. Let the domain object {@link
 * AuthenticatedUser} focus on authentication and authorization. Isolate the data object from api
 * from service implementation and domain object.
 *
 * @author Clay Cheng
 */
@Component
@RequiredArgsConstructor
public class AuthenticatedUserBuilder {

  private final AuthenticatedUserMapper authenticatedUserMapper;

  public AuthenticatedUser fromMember(Member member) {
    return authenticatedUserMapper.fromMember(member);
  }
}
