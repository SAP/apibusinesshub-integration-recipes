package net.pricefx.connector.common.operation;


import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;
import net.pricefx.connector.common.connection.PFXOperationClient;
import net.pricefx.connector.common.util.PFXTypeCode;
import net.pricefx.connector.common.util.ResponseUtil;
import org.apache.commons.collections4.CollectionUtils;

import java.util.Collections;
import java.util.List;


public abstract class DetailObjectFetcher extends GenericFetcher {

    private boolean fullResult;

    protected DetailObjectFetcher(PFXOperationClient pfxClient, boolean convertValueToString, PFXTypeCode typeCode) {
        super(pfxClient, typeCode, null, null, convertValueToString);
    }

    public DetailObjectFetcher withFullResult(boolean fullResult) {
        this.fullResult = fullResult;
        return this;
    }

    @Override
    public List<ObjectNode> fetch(ObjectNode advancedCriteria, List<String> sortBy, List<String> valueFields, Long startRow, int pageSize, boolean validate, boolean formatted) {

        List<ObjectNode> results = super.fetch(advancedCriteria, sortBy, valueFields, startRow, pageSize, validate, false);

        if (fullResult && !CollectionUtils.isEmpty(results) && results.size() == 1) {
            //fullresult is only supported for fetching one Object to avoid exceeding message size
            ObjectNode obj = Iterables.get(results, 0);
            obj = fetchFullObject(obj);
            if (obj == null) {
                return Collections.emptyList();
            } else {
                return ImmutableList.of(obj);
            }
        } else {
            return results;
        }

    }

    protected abstract String getPath(ObjectNode obj);

    protected ObjectNode fetchFullObject(ObjectNode obj) {

        ObjectNode detailedObject = Iterables.get(getPfxClient().doAction(getPath(obj)), 0);

        if (detailedObject != null) {
            ResponseUtil.formatResponse(getTypeCode(), null, detailedObject, isConvertValueToString());
        }
        return detailedObject;

    }

}