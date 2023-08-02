package net.pricefx.adapter.sap.util


import spock.lang.Specification

class StringUtilTest extends Specification {

    def "getPropertyNameFromExpression"() {
        when:
        def result = StringUtil.getPropertyNameFromExpression('${property.abc}')

        then:
        "abc" == result

        when:
        result = StringUtil.getPropertyNameFromExpression('${header.abc}')

        then:
        "" == result

        when:
        result = StringUtil.getPropertyNameFromExpression("abc")

        then:
        "" == result

        when:
        result = StringUtil.getPropertyNameFromExpression(null)

        then:
        "" == result
        when:
        result = StringUtil.getPropertyNameFromExpression("")

        then:
        "" == result


    }

    def "getHeaderNameFromExpression"() {
        when:
        def result = StringUtil.getHeaderNameFromExpression('${property.abc}')

        then:
        "" == result

        when:
        result = StringUtil.getHeaderNameFromExpression('${header.abc}')

        then:
        "abc" == result

        when:
        result = StringUtil.getHeaderNameFromExpression("abc")

        then:
        "" == result

        when:
        result = StringUtil.getHeaderNameFromExpression(null)

        then:
        "" == result
        when:
        result = StringUtil.getHeaderNameFromExpression("")

        then:
        "" == result


    }
}
