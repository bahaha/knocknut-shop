package dev.claycheng.knocknut.members.repository.impl.repo;

import dev.claycheng.knocknut.members.repository.impl.model.KnMember;
import java.util.Optional;
import org.springframework.data.repository.CrudRepository;

public interface MemberJpaRepository extends CrudRepository<KnMember, Long> {

  Optional<KnMember> findByUsername(String username);
}
