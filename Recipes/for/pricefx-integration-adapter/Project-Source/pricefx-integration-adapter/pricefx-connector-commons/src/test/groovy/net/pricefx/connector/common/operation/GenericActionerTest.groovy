package net.pricefx.connector.common.operation

import net.pricefx.connector.common.connection.MockDummyPFXOperationClient
import net.pricefx.connector.common.connection.MockPFXOperationClient
import net.pricefx.connector.common.util.PFXConstants
import net.pricefx.connector.common.util.PFXOperation
import spock.lang.Specification

class GenericActionerTest extends Specification {
    def pfxClient = new MockPFXOperationClient()
    def dummyPfxClient = new MockDummyPFXOperationClient()

    def "action"() {
        when:
        def result = new GenericActioner(dummyPfxClient, PFXOperation.PRODUCT_IMAGE_DELETE.getOperation()).action()

        then:
        "false" == result.get(PFXConstants.FIELD_VALUE).textValue()

        when:
        result = new GenericActioner(pfxClient, PFXOperation.PRODUCT_IMAGE_DELETE.getOperation()).action()

        then:
        "1" == result.get(0).get(PFXConstants.FIELD_VALUE).textValue()

        when:
        result = new GenericActioner(pfxClient, PFXOperation.PRODUCT_IMAGE_EXIST.getOperation()).action()

        then:
        "true" == result.get(0).get(PFXConstants.FIELD_VALUE).textValue()
    }

}
