package net.pricefx.connector.common.util

import net.pricefx.connector.common.validation.RequestValidationException
import org.apache.tika.metadata.Metadata
import org.apache.tika.mime.MediaType
import spock.lang.Specification


class XlsxDetectorTest extends Specification {
    def detector = new XlsxDetector()
    Metadata metadata = new Metadata()

    def "detect"() {

        when:
        def result = detector.detect(XlsxDetectorTest.class.getResourceAsStream("/product_dmf.xlsx"), metadata)

        then:
        MediaType.parse(FileUtil.MediaType.XLSX.getType()) == result

        when:
        result = detector.detect(XlsxDetectorTest.class.getResourceAsStream("/product_dmf.zip"), metadata)

        then:
        !result

        when:
        result = detector.detect(XlsxDetectorTest.class.getResourceAsStream("/studio.png"), metadata)

        then:
        !result

        when:
        detector.detect(null, metadata)

        then:
        thrown(RequestValidationException.class)
    }

}
