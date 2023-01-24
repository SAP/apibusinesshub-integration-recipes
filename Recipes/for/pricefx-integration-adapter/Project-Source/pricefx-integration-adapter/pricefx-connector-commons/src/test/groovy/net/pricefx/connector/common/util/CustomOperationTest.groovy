package net.pricefx.connector.common.util


import spock.lang.Specification


class CustomOperationTest extends Specification {

    def "validValueOf"() {
        when:
        def result = CustomOperation.validValueOf(CustomOperation.GENERIC_POST.name())

        then:
        CustomOperation.GENERIC_POST == result

        when:
        result = CustomOperation.validValueOf("X")

        then:
        null == result

    }

}
