package dev.claycheng.knocknut.repository.factory;

import dev.claycheng.knocknut.domain.Member;
import dev.claycheng.knocknut.repository.impl.model.KnMember;
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
