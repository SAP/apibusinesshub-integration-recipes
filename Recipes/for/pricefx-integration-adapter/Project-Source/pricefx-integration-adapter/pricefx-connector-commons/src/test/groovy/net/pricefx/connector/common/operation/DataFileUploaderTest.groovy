package net.pricefx.connector.common.operation

import com.google.common.io.ByteStreams
import net.pricefx.connector.common.connection.MockPFXOperationClient
import net.pricefx.connector.common.util.PFXTypeCode
import net.pricefx.connector.common.validation.RequestValidationException
import spock.lang.Specification

class DataFileUploaderTest extends Specification {
    def pfxClient = new MockPFXOperationClient()

    def "upload"() {
        when:
        byte[] bytes = ByteStreams.toByteArray(DataFileUploaderTest.class.getResourceAsStream("/product_dmf.xlsx"))
        new DataFileUploader(pfxClient, PFXTypeCode.DATAFEED, "test").upload(bytes)

        then:
        noExceptionThrown()

        when:
        bytes = ByteStreams.toByteArray(DataFileUploaderTest.class.getResourceAsStream("/product_dmf.xltx"))
        new DataFileUploader(pfxClient, PFXTypeCode.DATAFEED, "test").upload(bytes)

        then:
        thrown(RequestValidationException.class)

        when:
        bytes = ByteStreams.toByteArray(DataFileUploaderTest.class.getResourceAsStream("/product_dmf.xlsm"))
        new DataFileUploader(pfxClient, PFXTypeCode.DATAFEED, "test").upload(bytes)

        then:
        thrown(RequestValidationException.class)

        when:
        bytes = ByteStreams.toByteArray(DataFileUploaderTest.class.getResourceAsStream("/product_dmf.zip"))
        new DataFileUploader(pfxClient, PFXTypeCode.DATAFEED, "test").upload(bytes)

        then:
        thrown(RequestValidationException.class)

    }


}
