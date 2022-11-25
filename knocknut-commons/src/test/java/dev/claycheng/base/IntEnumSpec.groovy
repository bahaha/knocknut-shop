package dev.claycheng.base

import spock.lang.Specification

class IntEnumSpec extends Specification {

    enum YesOrNo implements IntEnum {
        YES, NO;

        Integer getValue() { return ordinal() }
    }

    def "should return enum instance if the value is defined in enum"() {
        when:
        def asZero = IntEnum.from(YesOrNo.class, 0)

        then:
        asZero == Optional.of(YesOrNo.YES)
    }

    def "should return empty if the value is undefined"() {
        when:
        def undefined = IntEnum.from(YesOrNo.class, 5566)
        then:
        undefined == Optional.empty()
    }

}