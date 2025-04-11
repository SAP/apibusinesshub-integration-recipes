package net.pricefx.connector.common.operation;

import com.fasterxml.jackson.databind.JsonNode;
import net.pricefx.connector.common.connection.PFXOperationClient;
import net.pricefx.connector.common.util.*;

import static net.pricefx.connector.common.util.PFXTypeCode.DATAFEED;


public class DatafeedBulkLoader extends GenericBulkLoader {


    public DatafeedBulkLoader(PFXOperationClient pfxClient, String tableName) {
        super(pfxClient, DATAFEED, null, tableName);
    }

    @Override
    public String bulkLoad(JsonNode request, boolean validate) {
        super.bulkLoad(request, false);
        return "0"; //server isn't returning the number of records updated
    }

}




