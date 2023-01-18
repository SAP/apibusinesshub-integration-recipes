package net.pricefx.connector.common.operation

import net.pricefx.connector.common.connection.MockPFXOperationClient
import net.pricefx.connector.common.util.FileUploadStatus
import net.pricefx.connector.common.util.PFXConstants
import net.pricefx.connector.common.validation.ConnectorException
import spock.lang.Specification

class UploadStatusCheckerTest extends Specification {
    def pfxClient = new MockPFXOperationClient()

    def "execute"() {
        when:
        def result = new UploadStatusChecker(pfxClient, [UNIQUE_KEY: "1234"]).execute(null)

        then:
        "Product" == result.get(PFXConstants.FIELD_NAME).textValue()
        2 == result.get("total").numberValue()
        true == result.get("isSlotDeleted").booleanValue()
        "POSTPROCESSING_DONE" == result.get("status").textValue()

        when:
        result = new UploadStatusChecker(pfxClient, [UNIQUE_KEY: FileUploadStatus.INPROGRESS.name()]).execute(null)

        then:
        "Product" == result.get(PFXConstants.FIELD_NAME).textValue()
        2 == result.get("total").numberValue()
        false == result.get("isSlotDeleted").booleanValue()
        FileUploadStatus.INPROGRESS.name() == result.get("status").textValue()

        when:
        new UploadStatusChecker(pfxClient, [UNIQUE_KEY: "NOT_EXIST"]).execute(null)

        then:
        thrown(NoSuchElementException.class)

        when:
        new UploadStatusChecker(pfxClient, [UNIQUE_KEY: "WRONG_DATA"]).execute(null)

        then:
        thrown(ConnectorException.class)

        when:
        new UploadStatusChecker(pfxClient, [:]).execute(null)

        then:
        thrown(ConnectorException.class)

    }

}
