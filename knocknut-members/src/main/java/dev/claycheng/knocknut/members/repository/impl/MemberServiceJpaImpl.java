package dev.claycheng.knocknut.members.repository.impl;

import dev.claycheng.common.exception.DataNotFoundException;
import dev.claycheng.knocknut.members.domain.Member;
import dev.claycheng.knocknut.members.repository.MemberService;
import dev.claycheng.knocknut.members.repository.factory.MemberBuilder;
import dev.claycheng.knocknut.members.repository.impl.repo.MemberJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@ConditionalOnProperty(prefix = "knocknut.data-access", name = "mysql", havingValue = "jpa")
@Service
public class MemberServiceJpaImpl implements MemberService {

  private final MemberJpaRepository memberRepository;
  private final MemberBuilder memberBuilder;

  @Override
  public Member findByUsername(String username) throws DataNotFoundException {
    return memberRepository
        .findByUsername(username)
        .map(memberBuilder::fromDbEntity)
        .orElseThrow(() -> new DataNotFoundException("No member with username: " + username));
  }
}
