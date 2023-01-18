package net.pricefx.connector.common.operation

import com.fasterxml.jackson.databind.node.JsonNodeFactory
import com.fasterxml.jackson.databind.node.ObjectNode
import net.pricefx.connector.common.connection.MockPFXOperationClient
import net.pricefx.connector.common.util.PFXConstants
import spock.lang.Specification

import static net.pricefx.connector.common.util.PFXConstants.FIELD_FIELDNAME
import static net.pricefx.connector.common.util.PFXConstants.FIELD_LABEL

class ICalculableObjectUpsertorTest extends Specification {
    def pfxClient = new MockPFXOperationClient()


    def "replaceAttributeExtension"() {
        given:
        def metadata =
                [new ObjectNode(JsonNodeFactory.instance).put(FIELD_LABEL, "quoteType")
                         .put(FIELD_FIELDNAME, PFXConstants.ATTRIBUTE_EXT_PREFIX + "quoteType")]

        def request = new ObjectNode(JsonNodeFactory.instance).put("quoteType", "dummy")
        def requestCopy = request.deepCopy()

        when:
        new QuoteUpdater(pfxClient, null).replaceAttributeExtension(requestCopy, metadata)

        then:
        "dummy" == requestCopy.get(PFXConstants.ATTRIBUTE_EXT_PREFIX + "quoteType").textValue()

        when:
        requestCopy = request.deepCopy()
        new QuoteUpdater(pfxClient, null).replaceAttributeExtension(requestCopy, [])

        then:
        request == requestCopy

        when:
        requestCopy = new ObjectNode(JsonNodeFactory.instance).put("quoteType", (String) null)
        new QuoteUpdater(pfxClient, null).replaceAttributeExtension(requestCopy, metadata)

        then:
        requestCopy.get(PFXConstants.ATTRIBUTE_EXT_PREFIX + "quoteType").isNull()


    }

}
