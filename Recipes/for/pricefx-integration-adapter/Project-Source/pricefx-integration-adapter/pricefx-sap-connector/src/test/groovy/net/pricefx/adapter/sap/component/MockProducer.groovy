package net.pricefx.adapter.sap.component

import com.fasterxml.jackson.databind.node.JsonNodeFactory
import com.fasterxml.jackson.databind.node.ObjectNode
import com.sap.it.api.exception.InvalidContextException
import com.sap.it.api.securestore.exception.SecureStoreException
import net.pricefx.adapter.sap.operation.CredentialsOperation
import net.pricefx.connector.common.connection.PFXOperationClient
import net.pricefx.connector.common.util.PFXTypeCode
import org.apache.camel.Exchange

class MockProducer extends Producer {
    private PFXOperationClient pfxOperationClient

    MockProducer(org.apache.camel.Endpoint endpoint, PFXOperationClient pfxOperationClient) {
        super(endpoint)
        this.pfxOperationClient = pfxOperationClient
    }

    @Override
    PFXOperationClient createPfxClient(CredentialsOperation credentialsOperation, PFXTypeCode typeCode, boolean basicOnly) {
        return pfxOperationClient
    }

    @Override
    protected CredentialsOperation createCredentialsOperation(final Exchange exchange) throws SecureStoreException, MalformedURLException, InvalidContextException {
        return new CredentialsOperation("token", "https://app.pricefx.eu") {
            @Override
            protected void init(String securityMaterial, String host) {

                setPricefxHost(host)
            }

            @Override
            ObjectNode buildTokenRequest() {

                ObjectNode node = new ObjectNode(JsonNodeFactory.instance)
                node.put("username", "dummyUser")
                node.put("partition", "dummyPartition")
                node.put("password", "dummyPw")
                return node
            }
        }
    }
}
