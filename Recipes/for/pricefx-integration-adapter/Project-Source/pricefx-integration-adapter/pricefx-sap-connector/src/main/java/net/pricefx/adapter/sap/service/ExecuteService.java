package net.pricefx.adapter.sap.service;

import com.fasterxml.jackson.databind.JsonNode;
import net.pricefx.connector.common.connection.PFXOperationClient;
import net.pricefx.connector.common.operation.QuoteExecuter;
import net.pricefx.connector.common.util.CustomOperation;

import java.util.HashMap;
import java.util.Map;

import static net.pricefx.connector.common.util.Constants.UNIQUE_KEY;

public class ExecuteService extends AbstractService {
    private final CustomOperation operation;
    private final String uniqueId;


    public ExecuteService(PFXOperationClient pfxClient, CustomOperation operation, String uniqueId) {
        super(pfxClient);
        this.operation = operation;
        this.uniqueId = uniqueId;
    }

    @Override
    protected JsonNode execute(Object input) {
        Map<String, String> parameters = new HashMap<>();
        parameters.put(UNIQUE_KEY, uniqueId);

        if (operation != null && operation.isQuoteOperation()) {
            return new QuoteExecuter(getPfxClient(), parameters, operation).execute(null);
        }

        throw new UnsupportedOperationException("Execute operation not supported");
    }
}
