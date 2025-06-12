package net.pricefx.connector.common.operation;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.TextNode;
import net.pricefx.connector.common.connection.PFXOperationClient;

import static net.pricefx.connector.common.util.PFXTypeCode.DATAFEED;


public class DatafeedBulkLoader extends GenericBulkLoader {


    public DatafeedBulkLoader(PFXOperationClient pfxClient, String tableName) {
        super(pfxClient, DATAFEED, null, tableName);
    }

    @Override
    public JsonNode bulkLoad(JsonNode request, boolean validate) {
        super.bulkLoad(request, false);
        return new TextNode("0"); //server isn't returning the number of records updated
    }

}




