package net.pricefx.connector.common.util

import spock.lang.Specification


class FileUploadStatusTest extends Specification {

    def "validValueOf"() {
        when:
        def result = FileUploadStatus.validValueOf("xx")

        then:
        FileUploadStatus.NOT_FOUND == result

        when:
        result = FileUploadStatus.validValueOf("POSTPROCESSING_DONE")

        then:
        FileUploadStatus.POSTPROCESSING_DONE == result

    }

}
