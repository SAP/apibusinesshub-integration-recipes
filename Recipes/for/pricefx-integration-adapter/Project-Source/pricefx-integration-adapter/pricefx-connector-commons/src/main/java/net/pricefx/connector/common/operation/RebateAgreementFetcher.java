package net.pricefx.connector.common.operation;


import com.fasterxml.jackson.databind.node.ObjectNode;
import net.pricefx.connector.common.connection.PFXOperationClient;
import net.pricefx.connector.common.util.JsonUtil;
import net.pricefx.connector.common.util.PFXTypeCode;

import static net.pricefx.connector.common.util.ConnectionUtil.createPath;
import static net.pricefx.connector.common.util.PFXConstants.FIELD_UNIQUENAME;
import static net.pricefx.connector.common.util.PFXOperation.FETCH_RBA;


public class RebateAgreementFetcher extends DetailObjectFetcher {

    public RebateAgreementFetcher(PFXOperationClient pfxClient, boolean convertValueToString) {
        super(pfxClient, convertValueToString, PFXTypeCode.REBATEAGREEMENT);
    }


    @Override
    protected String getPath(ObjectNode obj) {
        return createPath(FETCH_RBA.getOperation(), JsonUtil.getValueAsText(obj.get(FIELD_UNIQUENAME)));
    }
}