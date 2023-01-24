package net.pricefx.adapter.sap.component


import net.pricefx.connector.common.connection.MockFailedPFXOperationClient
import net.pricefx.connector.common.connection.MockPFXOperationClient
import net.pricefx.connector.common.util.ConnectionUtil
import net.pricefx.connector.common.util.PFXTypeCode
import net.pricefx.connector.common.validation.ConnectorException
import net.pricefx.connector.common.validation.RequestValidationException
import org.apache.camel.CamelContext
import org.apache.camel.impl.DefaultCamelContext
import org.apache.camel.impl.DefaultExchange
import spock.lang.Specification

class ProducerTest extends Specification {
    def builder = ConnectionUtil.getPFXClientBuilder("test", "https://dummy.com", "abcdefg")
    def pfxClient = new MockPFXOperationClient(builder)
    def failedClient = new MockFailedPFXOperationClient(builder)

    def "process"() {
        given:
        CamelContext ctx = new DefaultCamelContext()
        def exchange = new DefaultExchange(ctx)
        def endpoint = new Endpoint("direct:test", new Component())
        endpoint.setOperationType("GET")
        endpoint.setGetTargetType(PFXTypeCode.LOOKUPTABLE.name())

        when:
        endpoint.setTargetDate("2")
        new MockProducer(endpoint, pfxClient).process(exchange)

        then:
        thrown(ConnectorException.class)

        when:
        endpoint.setTargetDate("2020-01-01")
        new MockProducer(endpoint, pfxClient).process(exchange)

        then:
        thrown(RequestValidationException.class)

        when:
        endpoint.setOperationType("xxx")
        endpoint.setTargetDate("2020-01-01")
        new MockProducer(endpoint, pfxClient).process(exchange)

        then:
        thrown(IllegalArgumentException.class)


    }

}
