package net.pricefx.connector.common.operation

import com.google.common.io.ByteStreams
import net.pricefx.connector.common.connection.MockPFXOperationClient
import net.pricefx.connector.common.validation.RequestValidationException
import spock.lang.Specification

class ProductImageUploaderTest extends Specification {
    def pfxClient = new MockPFXOperationClient()

    def "upload"() {
        when:
        byte[] bytes = ByteStreams.toByteArray(ProductImageUploaderTest.class.getResourceAsStream("/studio.png"))
        new ProductImageUploader(pfxClient, "test").upload(bytes)

        then:
        noExceptionThrown()

        when:
        bytes = ByteStreams.toByteArray(ProductImageUploaderTest.class.getResourceAsStream("/product_dmf.xlsx"))
        new ProductImageUploader(pfxClient, "test").upload(bytes)

        then:
        thrown(RequestValidationException.class)

    }


}
