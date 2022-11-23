package dev.claycheng.knocknut.members.repository.factory;

import dev.claycheng.knocknut.members.domain.Member;
import dev.claycheng.knocknut.members.repository.impl.model.KnMember;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MemberBuilder {
  private final MemberMapper memberMapper;

  public Member fromDbEntity(KnMember knMember) {
    return memberMapper.fromDatabase(knMember);
  }
}
