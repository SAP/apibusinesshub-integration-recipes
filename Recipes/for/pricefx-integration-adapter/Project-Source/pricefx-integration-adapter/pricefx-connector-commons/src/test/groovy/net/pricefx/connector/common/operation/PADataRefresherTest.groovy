package net.pricefx.connector.common.operation

import net.pricefx.connector.common.connection.MockPFXOperationClient
import net.pricefx.connector.common.validation.ConnectorException
import spock.lang.Specification

import static net.pricefx.connector.common.util.ConnectionUtil.createPath
import static net.pricefx.connector.common.util.PFXOperation.REFRESH_DATAMART
import static net.pricefx.connector.common.util.PFXTypeCode.DATAMART

class PADataRefresherTest extends Specification {
    def pfxClient = new MockPFXOperationClient()

    def "validateRequest"() {
        when:
        new PADataRefresher(pfxClient, ["UNIQUE_KEY": "Product"]).validateRequest(null)

        then:
        noExceptionThrown()

        when:
        new PADataRefresher(pfxClient, [:]).validateRequest(null)

        then:
        thrown(ConnectorException.class)
    }

    def "buildPath"() {
        when:
        def result = new PADataRefresher(pfxClient, ["UNIQUE_KEY": "Product"]).buildPath(null)

        then:
        createPath(REFRESH_DATAMART.getOperation(), DATAMART.getTypeCode() + ".Product") == result
    }

    def "buildRequest"() {
        when:
        def result = new PADataRefresher(pfxClient, ["UNIQUE_KEY": "Product", "INCREMENTAL_LOAD_DATE": "2021-01-01"]).
                buildRequest(null)

        then:
        "2021-01-01" == result.get("incLoadDate").textValue()
        true == result.get("incremental").booleanValue()

        when:
        result = new PADataRefresher(pfxClient, ["UNIQUE_KEY": "Product"]).buildRequest(null)

        then:
        result.isEmpty()
    }


}
