package net.pricefx.connector.common.operation;


import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;
import net.pricefx.connector.common.connection.PFXOperationClient;
import net.pricefx.connector.common.util.JsonUtil;
import net.pricefx.connector.common.util.PFXTypeCode;
import net.pricefx.connector.common.util.ResponseUtil;
import org.apache.commons.collections4.CollectionUtils;

import java.util.Collections;
import java.util.List;

import static net.pricefx.connector.common.util.ConnectionUtil.createPath;
import static net.pricefx.connector.common.util.PFXConstants.FIELD_UNIQUENAME;
import static net.pricefx.connector.common.util.PFXOperation.FETCH_QUOTE;


public class QuoteFetcher extends GenericFetcher {

    private boolean fullResult;

    public QuoteFetcher(PFXOperationClient pfxClient, boolean convertValueToString) {
        super(pfxClient, PFXTypeCode.QUOTE, null, null, convertValueToString);
    }

    public QuoteFetcher withFullResult(boolean fullResult) {
        this.fullResult = fullResult;
        return this;
    }

    @Override
    public List<ObjectNode> fetch(ObjectNode advancedCriteria, List<String> sortBy, List<String> valueFields, Long startRow, int pageSize, boolean validate, boolean formatted) {

        List<ObjectNode> results = super.fetch(advancedCriteria, sortBy, valueFields, startRow, pageSize, validate, false);

        if (fullResult && !CollectionUtils.isEmpty(results)) {
            //fullresult is only supported for fetching one Quote to avoid exceeding message size
            ObjectNode quote = Iterables.get(results, 0);
            quote = fetchFullQuote(quote);
            if (quote == null) {
                return Collections.emptyList();
            } else {
                return ImmutableList.of(quote);
            }
        } else {
            return results;
        }

    }

    private ObjectNode fetchFullQuote(ObjectNode quote) {

        ObjectNode detailedQuote = Iterables.get(getPfxClient().doAction(createPath(FETCH_QUOTE.getOperation(),
                JsonUtil.getValueAsText(quote.get(FIELD_UNIQUENAME)))), 0);

        if (detailedQuote != null) {
            ResponseUtil.formatResponse(PFXTypeCode.QUOTE, detailedQuote, isConvertValueToString());
        }
        return detailedQuote;

    }

}