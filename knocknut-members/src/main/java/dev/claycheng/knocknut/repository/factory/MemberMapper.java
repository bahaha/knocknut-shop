package dev.claycheng.knocknut.repository.factory;

import dev.claycheng.knocknut.domain.Member;
import dev.claycheng.knocknut.repository.impl.model.KnMember;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(
    uses = {MemberConverter.class},
    componentModel = "spring")
public interface MemberMapper {

  @Mapping(target = "status", qualifiedByName = "MemberStatusConverter")
  @Mapping(target = "gender", qualifiedByName = "MemberGenderConverter")
  Member fromDatabase(KnMember knMember);
}
