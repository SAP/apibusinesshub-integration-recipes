package net.pricefx.connector.common.operation

import com.fasterxml.jackson.databind.node.JsonNodeFactory
import com.fasterxml.jackson.databind.node.ObjectNode
import net.pricefx.connector.common.connection.MockPFXOperationClient
import net.pricefx.connector.common.util.JsonUtil
import net.pricefx.connector.common.util.OperatorId
import net.pricefx.connector.common.util.PFXConstants
import net.pricefx.connector.common.util.RequestUtil
import spock.lang.Specification

import static net.pricefx.connector.common.util.PFXConstants.FIELD_USER_LOGINNAME

class UserUpsertorTest extends Specification {
    def pfxClient = new MockPFXOperationClient()

    def "buildUpsertRequest"() {
        given:
        def node = new ObjectNode(JsonNodeFactory.instance).put(RequestUtil.OPERATOR, OperatorId.AND.getValue())
                .set("criteria", JsonUtil.createArrayNode(
                        new ObjectNode(JsonNodeFactory.instance).put(RequestUtil.OPERATOR, OperatorId.NOT_NULL.getValue()).put(PFXConstants.FIELD_FIELDNAME, PFXConstants.FIELD_SKU)))

        def userNode = new ObjectNode(JsonNodeFactory.instance).put("loginName", "TEST").set("productFilterCriteria", node)

        when:
        def results = new UserUpsertor(pfxClient).buildUpsertRequest(userNode)

        then:
        results.get(0).get("productFilterCriteria").isTextual()
        results.get(0).get("productFilterCriteria").textValue().contains("AdvancedCriteria")

    }

    def "upsert"() {
        given:
        def request = JsonUtil.createArrayNode(
                new ObjectNode(JsonNodeFactory.instance).put(FIELD_USER_LOGINNAME, "abc2").put("email", "abc2@pricefx.com"),
                new ObjectNode(JsonNodeFactory.instance).put(FIELD_USER_LOGINNAME, "abc1").put("email", "abc1@pricefx.com"))

        when:
        def results = new UserUpsertor(pfxClient).upsert(request, true, false, false, false, false, false)

        then:
        results.size() == 2


    }

}
