package dev.claycheng.knocknut.domain.enums;

import dev.claycheng.base.IntEnum;

public enum Gender implements IntEnum {
  /** */
  GIRL,
  BOY,
  ;

  public Integer getValue() {
    return ordinal();
  }
}
