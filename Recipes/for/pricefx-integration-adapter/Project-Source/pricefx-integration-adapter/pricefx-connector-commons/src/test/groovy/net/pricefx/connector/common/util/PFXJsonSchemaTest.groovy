package net.pricefx.connector.common.util

import spock.lang.Specification


class PFXJsonSchemaTest extends Specification {

    def "getFetchRequestSchema"() {
        when:
        def result = PFXJsonSchema.getFetchRequestSchema(PFXTypeCode.PRODUCT)

        then:
        PFXJsonSchema.FETCH_REQUEST == result

        when:
        result = PFXJsonSchema.getFetchRequestSchema(PFXTypeCode.DATAMART)

        then:
        PFXJsonSchema.SIMPLE_FETCH_REQUEST == result
    }

    def "getFetchResponseSchema"() {
        when:
        def result = PFXJsonSchema.getFetchResponseSchema(PFXTypeCode.PRODUCT, null)

        then:
        result == PFXJsonSchema.PRODUCT_FETCH_RESPONSE

        when:
        result = PFXJsonSchema.getFetchResponseSchema(PFXTypeCode.CUSTOMER, null)

        then:
        result == PFXJsonSchema.CUSTOMER_FETCH_RESPONSE

        when:
        def matrix = PFXLookupTableType.valueOf(PFXLookupTableType.LookupTableType.MATRIX.name(), PFXLookupTableType.LookupTableType.MATRIX.name())
        result = PFXJsonSchema.getFetchResponseSchema(PFXTypeCode.LOOKUPTABLE, matrix)

        then:
        result == PFXJsonSchema.PP_MATRIX_FETCH_RESPONSE

    }


}
