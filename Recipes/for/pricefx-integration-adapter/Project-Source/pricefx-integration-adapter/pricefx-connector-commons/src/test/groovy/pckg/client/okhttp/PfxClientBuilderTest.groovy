package pckg.client.okhttp

import net.pricefx.connector.common.util.ConnectionUtil
import net.pricefx.pckg.client.okhttp.authenticator.AuthV1
import net.pricefx.pckg.client.okhttp.authenticator.AuthV2
import spock.lang.Specification

class PfxClientBuilderTest extends Specification {

    def "getDefaultAuthenticator"() {
        given:
        def builder = ConnectionUtil.getPFXClientBuilder("dummy", "https://www.dummy.com", "x")

        when:
        def result = builder.getDefaultAuthenticator()

        then:
        result instanceof AuthV2

        when:
        builder.appKey(null)
        result = builder.getDefaultAuthenticator()

        then:
        result instanceof AuthV1


    }

}
