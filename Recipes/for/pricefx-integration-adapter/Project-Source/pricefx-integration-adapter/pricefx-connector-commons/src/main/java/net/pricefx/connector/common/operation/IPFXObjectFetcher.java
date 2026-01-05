package net.pricefx.connector.common.operation;


import com.fasterxml.jackson.databind.node.ObjectNode;

import java.util.List;

public interface IPFXObjectFetcher {
    int fetchCount(ObjectNode request);

    List<ObjectNode> fetch(ObjectNode advancedCriteria, List<String> sortBy, List<String> valueFields, Long startRow, int pageSize, boolean validate, boolean formatted);

    List<ObjectNode> fetch(ObjectNode advancedCriteria, List<String> sortBy, boolean validate, boolean formatted);

    List<ObjectNode> fetch(ObjectNode request, Long startRow, int pageSize, boolean validate, boolean formatted, boolean rawPost);
    List<ObjectNode> fetch(ObjectNode request, Long startRow, int pageSize, boolean validate, boolean formatted);

}
