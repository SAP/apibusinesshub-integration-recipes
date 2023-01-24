package net.pricefx.connector.common.connection


import com.fasterxml.jackson.databind.node.TextNode
import net.pricefx.connector.common.util.PFXConstants
import net.pricefx.connector.common.util.PFXExtensionType
import net.pricefx.connector.common.util.PFXLookupTableType
import net.pricefx.connector.common.util.PFXTypeCode
import net.pricefx.connector.common.validation.ConnectorException
import net.pricefx.connector.common.validation.RequestValidationException
import spock.lang.Specification

import static net.pricefx.connector.common.util.PFXConstants.FIELD_TYPEDID

class IPFXConnectionTest extends Specification {
    def pfxClient = new MockPFXOperationClient()
    def pfxFailedClient = new MockFailedPFXOperationClient()

    def "fetchFormulas"() {
        when:
        def results = pfxClient.fetchFormulas()

        then:
        1 == results.size()
        results.contains("test")
    }

    def "testConnection"() {
        when:
        pfxClient.testConnection("test", false)

        then:
        noExceptionThrown()

        when:
        pfxClient.testConnection("dummy", false)

        then:
        thrown(ConnectorException.class)

        when:
        pfxFailedClient.testConnection("test", false)

        then:
        thrown(ConnectorException.class)
    }

    def "fetchFirstObject"() {
        when:
        def results = pfxClient.fetchFirstObject("fetch/F", PFXConstants.FIELD_UNIQUENAME, "test")

        then:
        "1.F" == results.get(FIELD_TYPEDID).textValue()


    }

    def "validateUserRoles"() {
        when:
        pfxClient.validateUserRoles(["ADMIN"], PFXTypeCode.ROLE)

        then:
        noExceptionThrown()

        when:
        pfxClient.validateUserRoles(["ADMIN", "DUMMY"], PFXTypeCode.ROLE)

        then:
        thrown(RequestValidationException.class)

    }

    def "getLookupTables"() {

        when:
        def results = pfxClient.getLookupTables()

        then:
        3 == results.size()
        !results?.uniqueName?.contains(new TextNode("MatrixSingleParameters"))
        results?.uniqueName?.contains(new TextNode("MatrixParameters"))

    }

    def "getDataFields"() {
        when:
        def results = pfxClient.getDataFields(PFXTypeCode.DATAFEED, "1.DMF")

        then:
        3 == results.size()
        true == results.get(1).get("key")
    }

    def "getUploadSlot"() {
        when:
        def results = pfxClient.getUploadSlot()

        then:
        "1000" == results
    }

    def "doFetchMetadata"() {

        when:
        def results = pfxClient.doFetchMetadata(PFXTypeCode.PRODUCT, null, null)

        then:
        "attribute1" == results.get(0).get("fieldName").textValue()

    }


    def "getLookupTable"() {
        when:
        def result = pfxClient.getLookupTable("MatrixParameters", "2020-01-01")

        then:
        "MatrixParameters" == result.get(PFXConstants.FIELD_UNIQUENAME).textValue()

        when:
        result = pfxClient.getLookupTable(null, "2020-01-01")

        then:
        !result

    }

    def "getLookupTableType"() {
        when:
        def result = pfxClient.getLookupTableType(null, null)

        then:
        null == result

        when:
        result = pfxClient.getLookupTableType("abc", "2020-01-01")

        then:
        null == result
    }

    def "getDataload"() {

        when:
        pfxClient.getDataLoad(null)

        then:
        thrown(ConnectorException.class)

    }

    def "createExtensionType"() {

        when:
        def result = pfxClient.createExtensionType(PFXTypeCode.LOOKUPTABLE, "MatrixParameters", "2020-01-01")

        then:
        result instanceof PFXLookupTableType
        "2" == result.getTable()

        when:
        result = pfxClient.createExtensionType(PFXTypeCode.CUSTOMEREXTENSION, "Customer_Details", "2020-01-01")

        then:
        result instanceof PFXExtensionType
        "Customer_Details" == result.getTable()

        when:
        result = pfxClient.createExtensionType(PFXTypeCode.CUSTOMEREXTENSION, "", "2020-01-01")

        then:
        null == result

        when:
        pfxClient.createExtensionType(PFXTypeCode.CUSTOMEREXTENSION, "DUMMY", "2020-01-01")

        then:
        thrown(ConnectorException.class)

        when:
        pfxFailedClient.createExtensionType(PFXTypeCode.CUSTOMEREXTENSION, "Customer_Details", "2020-01-01")

        then:
        thrown(ConnectorException.class)
    }

    def "getExtensionTables"() {

        when:
        def result = pfxClient.getExtensionTables(PFXTypeCode.CUSTOMEREXTENSION)

        then:
        result.get(PFXConstants.FIELD_VALUE).textValue().contains("Customer_Details")


    }


}
