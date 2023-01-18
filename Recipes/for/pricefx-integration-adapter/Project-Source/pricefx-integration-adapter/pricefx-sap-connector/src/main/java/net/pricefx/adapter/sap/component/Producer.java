package net.pricefx.adapter.sap.component;

import com.fasterxml.jackson.databind.JsonNode;
import com.sap.it.api.exception.InvalidContextException;
import com.sap.it.api.securestore.exception.SecureStoreException;
import net.pricefx.adapter.sap.operation.*;
import net.pricefx.adapter.sap.service.*;
import net.pricefx.adapter.sap.util.SupportedOperation;
import net.pricefx.connector.common.connection.PFXOperationClient;
import net.pricefx.connector.common.util.*;
import net.pricefx.connector.common.validation.ConnectorException;
import net.pricefx.pckg.client.okhttp.PfxClientBuilder;
import org.apache.camel.Exchange;
import org.apache.camel.impl.DefaultProducer;
import org.apache.commons.lang3.StringUtils;

import java.net.MalformedURLException;


import static net.pricefx.adapter.sap.util.Constants.*;
import static net.pricefx.connector.common.util.PFXTypeCode.TOKEN;
import static net.pricefx.connector.common.validation.ConnectorException.ErrorType.CONNECTION_ERROR;


public class Producer extends DefaultProducer {

    public Producer(org.apache.camel.Endpoint endpoint) {
        super(endpoint);

    }

    public void process(final Exchange exchange) throws Exception {
        PFXOperationClient pfxClient;
        CredentialsOperation credentialsOperation = createCredentialsOperation();

        String token = getProperty(exchange.getProperty(ACCESS_TOKEN));
        pfxClient = createPfxClient(credentialsOperation);

        Object input = exchange.getIn().getBody();

        String apiPath = getProperty(exchange.getProperty(API_PATH));

        String uniqueId = getProperty(exchange.getProperty(UNIQUE_ID));
        String secondaryId = getProperty(exchange.getProperty(SECONDARY_ID));
        PFXTypeCode typeCode = PFXTypeCode.validValueOf(((Endpoint) getEndpoint()).getTargetType());

        validateTargetDate(typeCode);

        IPFXExtensionType extensionType = null;
        if (typeCode != null && (typeCode.isExtension() || typeCode == PFXTypeCode.LOOKUPTABLE)) {

            if (!credentialsOperation.isJwt()){
                pfxClient.updateOAuthToken(token);
            }

            extensionType = pfxClient.createExtensionType(typeCode, ((Endpoint) getEndpoint()).getExtensionName(), ((Endpoint) getEndpoint()).getTargetDate());
        }

        JsonNode node;
        switch (SupportedOperation.valueOf(((Endpoint) getEndpoint()).getOperationType())) {
            case REFRESH:
                String incLoadDate = getProperty(exchange.getProperty(INC_LOAD_DATE));
                node = refresh(pfxClient, typeCode, uniqueId, incLoadDate, token);
                break;
            case TRUNCATE:
                node = new TruncateService(pfxClient, typeCode, ((Endpoint) getEndpoint()).getExtensionName()).execute(token, input);
                break;
            case BULKLOAD:
                node = new BulkLoadService(pfxClient, typeCode, extensionType,
                        ((Endpoint) getEndpoint()).getExtensionName(),
                        ((Endpoint) getEndpoint()).isValidation()).execute(token, input);
                break;
            case EXECUTE:
                node = new ExecuteOperation(pfxClient, ((Endpoint) getEndpoint()).getTargetType(), uniqueId, ((Endpoint) getEndpoint()).getExtensionName(), typeCode).execute(token, input);
                break;
            case STATUS:
                node = new StatusOperation(pfxClient, typeCode, uniqueId).get(token);
                break;
            case PING:
                node = new PingService(pfxClient).execute(token, null);
                break;
            case CREATE:
                node = new CreateService(pfxClient, typeCode).execute(token, input);
                break;
            case UPDATE:
                node = new UpdateService(pfxClient, typeCode, uniqueId).execute(token, input);
                break;
            case UPSERT:
                node = new UpsertService(pfxClient, typeCode, extensionType,
                        ((Endpoint) getEndpoint()).isSimpleResult(),
                        ((Endpoint) getEndpoint()).isReplaceNullWithEmpty()).execute(token, input);
                break;
            case DELETE:
                node = new DeleteOperation(pfxClient, typeCode, uniqueId, extensionType, false).delete(token, input);
                break;
            case DELETE_BY_KEY:
                node = new DeleteOperation(pfxClient, typeCode, uniqueId, extensionType, true).delete(token, input);
                break;
            case UPLOAD:
                node = new FileUploadService(pfxClient, typeCode, ((Endpoint) getEndpoint()).getExtensionName(), uniqueId).execute(token, input);
                break;
            case POST:
                node = new PostService(pfxClient, apiPath).execute(token, input);
                break;
            case METADATA:
                int pageSize = RequestUtil.getPageSize(getProperty(exchange.getProperty(PAGE_SIZE)), MAX_FETCH_RECORDS);
                long startRow = RequestUtil.getStartRow(getProperty(exchange.getProperty(PAGE_NUMBER)), pageSize);
                node = new FetchService(pfxClient, typeCode, extensionType, uniqueId).
                        fetchMetadata(token, startRow, pageSize);
                break;
            case FETCH:
                pageSize = RequestUtil.getPageSize(getProperty(exchange.getProperty(PAGE_SIZE)), MAX_FETCH_RECORDS);
                startRow = RequestUtil.getStartRow(getProperty(exchange.getProperty(PAGE_NUMBER)), pageSize);
                node = new FetchService(
                        pfxClient, typeCode,
                        extensionType,
                        StringUtils.isEmpty(uniqueId) ? ((Endpoint) getEndpoint()).getExtensionName() : uniqueId).
                        fetch(token, startRow, pageSize, true, input);
                break;
            case GET:
                pageSize = RequestUtil.getPageSize(getProperty(exchange.getProperty(PAGE_SIZE)), MAX_FETCH_RECORDS);
                startRow = RequestUtil.getStartRow(getProperty(exchange.getProperty(PAGE_NUMBER)), pageSize);
                if (typeCode == TOKEN) {
                    node = new TokenService(pfxClient).get(credentialsOperation.buildTokenRequest());
                } else {
                    node = new GetOperation(pfxClient, typeCode, uniqueId, secondaryId, extensionType).get(token, startRow, pageSize);
                }
                break;
            default:
                throw new UnsupportedOperationException("operation not supported: " +
                        SupportedOperation.valueOf(((Endpoint) getEndpoint()).getOperationType()));
        }

        exchange.getMessage().setBody(node.toString());
    }

    protected CredentialsOperation createCredentialsOperation() throws SecureStoreException, MalformedURLException, InvalidContextException {
        return new CredentialsOperation(((Endpoint) getEndpoint()).getSecurityMaterial(),
                ((Endpoint) getEndpoint()).getPricefxHost(), ((Endpoint) getEndpoint()).getPartition());
    }

    protected PFXOperationClient createPfxClient(CredentialsOperation credentialsOperation) {

        PFXOperationClient pfxClient;
        try {
            PfxClientBuilder builder;
            if (credentialsOperation.isJwt()){
                builder = ConnectionUtil.getPFXClientBuilder(credentialsOperation.getClientId(),
                        credentialsOperation.getPricefxHost(), null, null, credentialsOperation.getJwtToken());

            } else {

                builder = ConnectionUtil.getPFXClientBuilder(credentialsOperation.getPartition(),
                        credentialsOperation.getPricefxHost(),
                        credentialsOperation.getClientId());

            }
            pfxClient = (PFXOperationClient) builder.build();

        } catch (Exception ex) {
            throw new ConnectorException(CONNECTION_ERROR);
        }

        return pfxClient;

    }

    private String getProperty(Object property) {
        return (property == null) ? null : property.toString();
    }

    private void validateTargetDate(PFXTypeCode typeCode) {

        if (DateUtil.getDate(((Endpoint) getEndpoint()).getTargetDate()) == null && typeCode == PFXTypeCode.LOOKUPTABLE) {
            throw new ConnectorException("Invalid Target Date");
        }

    }

    private JsonNode refresh(PFXOperationClient pfxClient, PFXTypeCode typeCode, String uniqueId, String incLoadDate, String token) {
        if (typeCode == TOKEN) {
            return new RefreshService(pfxClient, typeCode, uniqueId, incLoadDate).refresh(token);
        } else {
            return new RefreshService(pfxClient, typeCode, ((Endpoint) getEndpoint()).getExtensionName(), incLoadDate).refresh(token);
        }
    }

}
