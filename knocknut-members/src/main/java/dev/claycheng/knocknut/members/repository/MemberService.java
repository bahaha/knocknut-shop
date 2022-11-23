package dev.claycheng.knocknut.members.repository;

import dev.claycheng.common.exception.DataNotFoundException;
import dev.claycheng.knocknut.members.domain.Member;

public interface MemberService {

  Member findByUsername(String username) throws DataNotFoundException;
}
