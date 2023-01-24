package net.pricefx.connector.common.util

import net.pricefx.connector.common.validation.ConnectorException
import spock.lang.Specification


class ConnectionUtilTest extends Specification {


    def "Connection"() {
        when:
        def connection = new ConnectionUtil.Connection("Ym9vbWkvbmFtZTpwdw==")

        then:
        "boomi" == connection.getPartition()
        "name" == connection.getUsername()
        "pw" == connection.getPassword()

        when:
        new ConnectionUtil.Connection("Ym9vbWkvbmFtZTo=")

        then:
        thrown(ConnectorException.class)

    }

    def "getHost"() {
        when:
        ConnectionUtil.getHost(null)

        then:
        thrown(ConnectorException.class)

        when:
        def result = ConnectionUtil.getHost("https://www.pricefx.eu/pricefx/test")

        then:
        "https://www.pricefx.eu" == result


    }

    def "createPath"() {
        when:
        def result = ConnectionUtil.createPath("fetch", "P", "123")

        then:
        "fetch/P/123" == result
    }


    def "haveMetaData"(typeCode, extensionType, result) {
        expect:
        result == ConnectionUtil.haveMetaData(typeCode, extensionType)

        where:
        typeCode                     | extensionType                                                                                           | result
        null                         | null                                                                                                    | false
        PFXTypeCode.PRODUCT          | null                                                                                                    | true
        PFXTypeCode.USER             | null                                                                                                    | false
        PFXTypeCode.PRODUCTEXTENSION | new PFXExtensionType(PFXTypeCode.PRODUCTEXTENSION).withAttributes(6)                                    | true
        PFXTypeCode.LOOKUPTABLE      | PFXLookupTableType.valueOf(PFXLookupTableType.LookupTableType.RANGE.name(), "STRING")                   | false
        PFXTypeCode.LOOKUPTABLE      | PFXLookupTableType.valueOf(PFXLookupTableType.LookupTableType.SIMPLE.name(), "STRING")                  | false
        PFXTypeCode.LOOKUPTABLE      |
                PFXLookupTableType.valueOf(PFXLookupTableType.LookupTableType.MATRIX.name(), PFXLookupTableType.LookupTableType.MATRIX.name()) | true
    }

}
