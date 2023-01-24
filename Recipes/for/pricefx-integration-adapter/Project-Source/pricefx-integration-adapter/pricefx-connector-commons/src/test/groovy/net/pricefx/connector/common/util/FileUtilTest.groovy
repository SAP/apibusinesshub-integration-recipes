package net.pricefx.connector.common.util

import net.pricefx.connector.common.validation.RequestValidationException
import spock.lang.Specification

class FileUtilTest extends Specification {

    def "checkSupportedFileType"() {
        when:
        FileUtil.checkSupportedFileType(
                FileUtilTest.class.getResourceAsStream("/product_dmf.xlsm"), FileUtil.MediaType.XLSX)

        then:
        thrown(RequestValidationException.class)

        when:
        FileUtil.checkSupportedFileType(
                FileUtilTest.class.getResourceAsStream("/product_dmf.zip"), FileUtil.MediaType.IMAGE)

        then:
        thrown(RequestValidationException.class)

        when:
        FileUtil.checkSupportedFileType(
                FileUtilTest.class.getResourceAsStream("/studio.png"), FileUtil.MediaType.IMAGE)

        then:
        noExceptionThrown()

        when:
        FileUtil.checkSupportedFileType(
                FileUtilTest.class.getResourceAsStream("/product_dmf.xlsx"), FileUtil.MediaType.XLSX)

        then:
        noExceptionThrown()


    }


}
