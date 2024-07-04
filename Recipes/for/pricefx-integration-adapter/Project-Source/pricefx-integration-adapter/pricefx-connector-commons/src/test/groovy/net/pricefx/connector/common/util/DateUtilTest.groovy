package net.pricefx.connector.common.util


import spock.lang.Specification

import java.text.ParseException

class DateUtilTest extends Specification {

    def "getDateTime"() {
        when:
        def result = DateUtil.getDateTime("2020-01-01T00:00:00")

        then:
        result

        when:
        result = DateUtil.getDateTime("2020-01-01TX")

        then:
        !result
    }

    def "getDate"() {
        when:
        def result = DateUtil.getDate("2020-01-01")

        then:
        result

        when:
        result = DateUtil.getDate("2020/01/01")

        then:
        !result
    }

    def "getFormattedDate"() {
        given:
        def date = DateUtil.getDateTime("2020-01-01T00:00:00")

        when:
        def result = DateUtil.getFormattedDate(date)

        then:
        "2020-01-01" == result

                when:
        result = DateUtil.getFormattedDate(null)

        then:
        "" == result
    }

    def "getFormattedDateTime"() {
        given:
        def date = DateUtil.getDateTime("2020-01-01T00:00:00")

        when:
        def result = DateUtil.getFormattedDateTime(date)

        then:
        "2020-01-01T00:00:00" == result

        when:
        result = DateUtil.getFormattedDateTime(null)

        then:
        "" == result
    }

    def "getCurrentTime"() {
        when:
        def result = DateUtil.getCurrentTime()

        then:
        DateUtil.getDateTime(result)
    }

    def "isAfter"() {
        when:
        def result = DateUtil.isAfter("2020-01-01", "2019-12-31")

        then:
        true == result

        when:
        result = DateUtil.isAfter("2019-01-01", "2019-12-31")

        then:
        false == result

        when:
        DateUtil.isAfter("xxx", "xxxx")

        then:
        thrown(ParseException.class)


    }

    def "isAfterTimestamp"() {
        when:
        def result = DateUtil.isAfterTimestamp("2020-01-01T13:00:00", "2020-01-01T12:00:00")

        then:
        true == result

        when:
        result = DateUtil.isAfter("2020-01-01T12:00:00", "2020-01-01T13:00:00")

        then:
        false == result

        when:
        DateUtil.isAfter("xxx", "xxxx")

        then:
        thrown(ParseException.class)


    }

}
