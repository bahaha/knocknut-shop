package dev.claycheng.api.enums;

import dev.claycheng.base.IntEnum;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum MemberStatus implements IntEnum {

  /** */
  DISABLED(0),
  ENABLED(1),
  BANNED(2),
  ;

  private final Integer value;
}
