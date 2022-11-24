package dev.claycheng.knocknut.repository;

import dev.claycheng.common.exception.DataNotFoundException;
import dev.claycheng.knocknut.domain.Member;

public interface MemberService {

  Member findByUsername(String username) throws DataNotFoundException;
}
