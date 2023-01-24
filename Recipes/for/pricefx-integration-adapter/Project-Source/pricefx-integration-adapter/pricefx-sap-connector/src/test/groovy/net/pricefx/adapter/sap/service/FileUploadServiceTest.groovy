package net.pricefx.adapter.sap.service

import com.apple.foundationdb.tuple.ByteArrayUtil
import com.google.common.io.ByteStreams
import net.pricefx.connector.common.connection.MockPFXOperationClient
import net.pricefx.connector.common.util.ConnectionUtil
import net.pricefx.connector.common.util.PFXConstants
import net.pricefx.connector.common.util.PFXTypeCode
import net.pricefx.connector.common.validation.RequestValidationException
import spock.lang.Specification

class FileUploadServiceTest extends Specification {
    def builder = ConnectionUtil.getPFXClientBuilder("test", "https://dummy.com", "abcdefg")
    def pfxClient = new MockPFXOperationClient(builder)


    def "upload"() {
        given:
        byte[] bytes = ByteStreams.toByteArray(FileUploadServiceTest.class.getResourceAsStream("/studio.png"))
        def input = new ByteArrayInputStream(bytes)

        when:
        def result = new FileUploadService(pfxClient, PFXTypeCode.PRODUCTIMAGE, null, "TEST").execute(null, input)

        then:
        "1000" == result.get(PFXConstants.FIELD_VALUE).textValue()

        when:
        byte[] tails = [13, 10, 45, 45]
        bytes = ByteArrayUtil.join(bytes, tails)
        input = new ByteArrayInputStream(bytes)
        result = new FileUploadService(pfxClient, PFXTypeCode.PRODUCTIMAGE, null, "TEST").execute(null, input)

        then:
        "1000" == result.get(PFXConstants.FIELD_VALUE).textValue()

        when:
        new FileUploadService(pfxClient, PFXTypeCode.PRODUCTIMAGE, null, "TEST").execute(null, null)

        then:
        thrown(RequestValidationException.class)

        when:
        new FileUploadService(pfxClient, PFXTypeCode.PRODUCT, null, "TEST").execute(null, input)

        then:
        thrown(UnsupportedOperationException.class)

        when:
        bytes = ByteStreams.toByteArray(FileUploadServiceTest.class.getResourceAsStream("/delete-product-request.json"))
        input = new ByteArrayInputStream(bytes)
        new FileUploadService(pfxClient, PFXTypeCode.PRODUCTIMAGE, null, "TEST").execute(null, input)

        then:
        thrown(RequestValidationException.class)

    }

}
