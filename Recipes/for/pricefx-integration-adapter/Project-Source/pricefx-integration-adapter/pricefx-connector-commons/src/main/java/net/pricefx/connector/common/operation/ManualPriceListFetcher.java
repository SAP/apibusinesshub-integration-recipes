package net.pricefx.connector.common.operation;

import com.fasterxml.jackson.databind.node.ObjectNode;
import net.pricefx.connector.common.connection.PFXOperationClient;
import net.pricefx.connector.common.util.ResponseUtil;

import java.util.List;

import static net.pricefx.connector.common.util.PFXTypeCode.MANUALPRICELIST;

public class ManualPriceListFetcher extends GenericFetcher {

    public ManualPriceListFetcher(PFXOperationClient pfxClient, boolean convertValueToString) {
        super(pfxClient, MANUALPRICELIST, null, null, convertValueToString);

    }

    @Override
    public List<ObjectNode> fetch(ObjectNode advancedCriteria, List<String> sortBy, List<String> valueFields, Long startRow, int pageSize, boolean validate, boolean formatted) {
        List<ObjectNode> results = super.fetch(advancedCriteria, sortBy, valueFields, startRow, pageSize, validate, false);

        if (formatted) {
            ResponseUtil.formatResponse(MANUALPRICELIST, results, false);
        }

        return results;
    }


}