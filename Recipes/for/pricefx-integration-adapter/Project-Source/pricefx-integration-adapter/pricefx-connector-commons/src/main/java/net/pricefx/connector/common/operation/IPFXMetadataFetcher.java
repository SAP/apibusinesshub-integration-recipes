package net.pricefx.connector.common.operation;


import com.fasterxml.jackson.databind.node.ObjectNode;

import java.util.List;

public interface IPFXMetadataFetcher {

    List<ObjectNode> fetch(Long startRow, int pageSize, String uniqueKey);

}
