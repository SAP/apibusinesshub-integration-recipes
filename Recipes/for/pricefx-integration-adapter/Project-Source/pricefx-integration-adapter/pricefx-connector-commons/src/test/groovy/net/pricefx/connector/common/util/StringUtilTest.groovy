package net.pricefx.connector.common.util

import net.pricefx.connector.common.validation.RequestValidationException
import spock.lang.Specification


class StringUtilTest extends Specification {

    def "getIdFromTypedId"() {
        when:
        def result = StringUtil.getIdFromTypedId("1.CRCI4")

        then:
        "1" == result

        when:
        StringUtil.getIdFromTypedId("1")

        then:
        thrown(RequestValidationException.class)

        when:
        StringUtil.getIdFromTypedId(null)

        then:
        thrown(RequestValidationException.class)


    }

}
