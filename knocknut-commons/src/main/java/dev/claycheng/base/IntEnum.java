package dev.claycheng.base;

import java.util.Arrays;
import java.util.Optional;

public interface IntEnum {

  static <E extends Enum<E> & IntEnum> Optional<E> from(Class<E> clazz, final Integer source) {
    return Arrays.stream(clazz.getEnumConstants())
        .filter(e -> e.getValue().equals(source))
        .findAny();
  }

  Integer getValue();
}
