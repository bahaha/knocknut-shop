package dev.claycheng.knocknut.repository.factory;

import dev.claycheng.knocknut.api.feign.user.Member;
import dev.claycheng.knocknut.domain.AuthenticatedUser;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AuthenticatedUserMapper {

  AuthenticatedUser fromMember(Member member);
}
