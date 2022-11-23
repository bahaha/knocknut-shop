package dev.claycheng.knocknut.members.repository.factory;

import dev.claycheng.api.enums.MemberStatus;
import dev.claycheng.base.IntEnum;
import dev.claycheng.knocknut.members.domain.enums.Gender;
import org.mapstruct.Named;
import org.springframework.stereotype.Component;

@Component
@Named("MemberConverter")
public class MemberConverter {

  @Named("MemberStatusConverter")
  public MemberStatus statusFrom(Integer value) {
    return IntEnum.from(MemberStatus.class, value).orElse(MemberStatus.DISABLED);
  }

  @Named("MemberGenderConverter")
  public Gender genderFrom(Integer value) {
    return IntEnum.from(Gender.class, value).orElse(null);
  }
}
