package net.pricefx.connector.common.util

import com.fasterxml.jackson.databind.node.JsonNodeFactory
import com.fasterxml.jackson.databind.node.ObjectNode
import spock.lang.Specification

import static net.pricefx.connector.common.util.ConnectionUtil.createPath
import static net.pricefx.connector.common.util.PFXOperation.SAVE_QUOTE


class RequestPathFactoryTest extends Specification {

    def "buildFetchPath"(extensionType, typeCode, uniqueName, subtype, result) {
        expect:
        result == RequestPathFactory.buildFetchPath(extensionType, typeCode, uniqueName, subtype)

        where:
        extensionType                                                        | typeCode                     | uniqueName | subtype | result
        null                                                                 | PFXTypeCode.PRODUCTEXTENSION | "1234.PX3" | "PX3"   | ConnectionUtil.createPath(PFXOperation.FETCH.getOperation(), "PX3")
        new PFXConditionRecordType(4).withTable("TEST")                      | PFXTypeCode.CONDITION_RECORD | null       | null    | ConnectionUtil.createPath(PFXOperation.FETCH.getOperation(), PFXTypeCode.CONDITION_RECORD.getTypeCode() + "4")
        new PFXExtensionType(PFXTypeCode.PRODUCTEXTENSION).withTable("TEST") | PFXTypeCode.PRODUCTEXTENSION | null       | null    | ConnectionUtil.createPath(PFXOperation.PRODUCTEXTENSION_FETCH.getOperation(), "TEST")
        null                                                                 | PFXTypeCode.QUOTE            | null       | null    | PFXOperation.FETCH_QUOTES.getOperation()
        null                                                                 | PFXTypeCode.PRODUCT          | null       | null    | ConnectionUtil.createPath(PFXOperation.FETCH.getOperation(), PFXTypeCode.PRODUCT.getTypeCode())
        new PFXExtensionType(null)                                           | PFXTypeCode.PRODUCTEXTENSION | null       | null    | ConnectionUtil.createPath(PFXOperation.PRODUCTEXTENSION_FETCH.getOperation(), PFXTypeCode.PRODUCTEXTENSION.getTypeCode())
        null                                                                 | PFXTypeCode.DATAFEED         | "Product"  | null    | ConnectionUtil.createPath(PFXOperation.PA_FETCH.getOperation(), "DMF." + uniqueName)
        null                                                                 | PFXTypeCode.PRICELISTITEM    | "1234"     | null    | ConnectionUtil.createPath(PFXOperation.PRICE_LIST_ITEM_FETCH.getOperation(), "1234")
    }

    def "buildBulkLoadPath"(extensionType, typeCode, tableName, result) {

        expect:
        result == RequestPathFactory.buildBulkLoadPath(extensionType, typeCode, tableName)

        where:
        extensionType                                                                                                                  | typeCode                | tableName | result
        null                                                                                                                           | PFXTypeCode.DATAFEED    | "Product" | ConnectionUtil.createPath(PFXOperation.PA_BULK_LOAD.getOperation(), "DMF.Product")
        PFXLookupTableType.valueOf(PFXLookupTableType.LookupTableType.MATRIX.name(), PFXLookupTableType.LookupTableType.MATRIX.name()) | PFXTypeCode.LOOKUPTABLE | null      | ConnectionUtil.createPath(PFXOperation.LOOKUPTABLE_VALUES_BULK_LOAD.getOperation(), "MLTV")
        null                                                                                                                           | PFXTypeCode.PRODUCT     | null      | ConnectionUtil.createPath(PFXOperation.BULK_LOAD.getOperation(), PFXTypeCode.PRODUCT.getTypeCode())
    }

    def "buildDeletePath"(extensionType, typeCode, result) {
        expect:
        result == RequestPathFactory.buildDeletePath(extensionType, typeCode)

        where:
        extensionType                                                                                                                                    | typeCode                     | result
        new PFXExtensionType(PFXTypeCode.PRODUCTEXTENSION).withAttributes(3).withTable("TEST")                                                           | PFXTypeCode.PRODUCTEXTENSION |
                ConnectionUtil.createPath(PFXOperation.DELETE.getOperation(), "PX3", PFXOperation.BATCH.getOperation(), PFXOperation.FORCEFILTER.getOperation())
        PFXLookupTableType.valueOf(PFXLookupTableType.LookupTableType.MATRIX.name(), PFXLookupTableType.LookupTableType.MATRIX.name()).withTable("1234") |
                PFXTypeCode.LOOKUPTABLE                                                                                                                                                 | ConnectionUtil.createPath(PFXOperation.LOOKUPTABLE_DELETE.getOperation(), "1234", PFXOperation.BATCH.getOperation())
        null                                                                                                                                             | PFXTypeCode.PRODUCT          | ConnectionUtil.createPath(PFXOperation.DELETE.getOperation(), PFXTypeCode.PRODUCT.getTypeCode(), PFXOperation.BATCH.getOperation(), PFXOperation.FORCEFILTER.getOperation())
    }

    def "buildUpsertPath"(extensionType, typeCode, result) {
        expect:
        result == RequestPathFactory.buildUpsertPath(extensionType, typeCode)

        where:
        extensionType                                                                                                                                    | typeCode                     | result
        new PFXExtensionType(PFXTypeCode.PRODUCTEXTENSION).withAttributes(3).withTable("TEST")                                                           | PFXTypeCode.PRODUCTEXTENSION |
                ConnectionUtil.createPath(PFXOperation.INTEGRATE.getOperation(), "PX3")
        PFXLookupTableType.valueOf(PFXLookupTableType.LookupTableType.MATRIX.name(), PFXLookupTableType.LookupTableType.MATRIX.name()).withTable("1234") |
                PFXTypeCode.LOOKUPTABLE                                                                                                                                                 | ConnectionUtil.createPath(PFXOperation.LOOKUPTABLE_VALUES_INTEGRATE.getOperation(), "1234")
        null                                                                                                                                             | PFXTypeCode.PRODUCT          | ConnectionUtil.createPath(PFXOperation.INTEGRATE.getOperation(), PFXTypeCode.PRODUCT.getTypeCode())
    }

    def "buildCreatePath"() {
        when:
        def result = RequestPathFactory.buildCreatePath(PFXTypeCode.QUOTE)

        then:
        SAVE_QUOTE.getOperation() == result

        when:
        RequestPathFactory.buildCreatePath(PFXTypeCode.PRODUCT)

        then:
        thrown(UnsupportedOperationException.class)
    }

    def "buildUpdatePath"() {
        when:
        def request = new ObjectNode(JsonNodeFactory.instance)
        request.putArray(PFXConstants.FIELD_INPUTS).add(new ObjectNode(JsonNodeFactory.instance).put(PFXConstants.FIELD_NAME, "location").put(PFXConstants.FIELD_VALUE, "test"))

        def result = RequestPathFactory.buildUpdatePath(PFXTypeCode.QUOTE,  request)

        then:
        PFXOperation.SAVE_QUOTE.getOperation() == result

        when:
        request = new ObjectNode(JsonNodeFactory.instance)

        result = RequestPathFactory.buildUpdatePath(PFXTypeCode.QUOTE,  request)

        then:
        createPath(PFXOperation.UPDATE.getOperation(), PFXTypeCode.QUOTE.getTypeCode()) == result

        when:

        result = RequestPathFactory.buildUpdatePath(PFXTypeCode.ROLE, request)

        then:
        createPath(PFXOperation.UPDATE.getOperation(), PFXTypeCode.ROLE.getTypeCode()) == result

        when:

        RequestPathFactory.buildUpdatePath(PFXTypeCode.PRODUCT, request)

        then:
        thrown(UnsupportedOperationException.class)
    }
}