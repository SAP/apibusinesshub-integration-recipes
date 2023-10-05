package net.pricefx.adapter.sap.component;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.sap.it.api.exception.InvalidContextException;
import com.sap.it.api.securestore.exception.SecureStoreException;
import net.pricefx.adapter.sap.operation.*;
import net.pricefx.adapter.sap.service.*;
import net.pricefx.adapter.sap.util.StringUtil;
import net.pricefx.adapter.sap.util.SupportedOperation;
import net.pricefx.connector.common.connection.PFXOperationClient;
import net.pricefx.connector.common.operation.DataloadRunner;
import net.pricefx.connector.common.util.*;
import net.pricefx.connector.common.validation.ConnectorException;
import net.pricefx.pckg.client.okhttp.PfxClientBuilder;
import org.apache.camel.Exchange;
import org.apache.camel.impl.DefaultProducer;
import org.apache.commons.lang3.StringUtils;

import java.net.MalformedURLException;

import static net.pricefx.adapter.sap.util.Constants.*;
import static net.pricefx.connector.common.util.Constants.DEFAULT_TIMEOUT;
import static net.pricefx.connector.common.util.Constants.MAX_RECORDS;
import static net.pricefx.connector.common.util.PFXTypeCode.TOKEN;
import static net.pricefx.connector.common.validation.ConnectorException.ErrorType.CONNECTION_ERROR;


public class Producer extends DefaultProducer {

    public Producer(org.apache.camel.Endpoint endpoint) {
        super(endpoint);

    }

    private PFXTypeCode getTargetType() {
        String targetType;
        switch (SupportedOperation.valueOf(((Endpoint) getEndpoint()).getOperationType())) {
            case FETCH_COUNT:
                targetType = ((Endpoint) getEndpoint()).getFetchCountTargetType();
                break;
            case FLUSH:
                targetType = ((Endpoint) getEndpoint()).getFlushTargetType();
                break;
            case REFRESH:
                targetType = ((Endpoint) getEndpoint()).getRefreshTargetType();
                break;
            case TRUNCATE:
                targetType = ((Endpoint) getEndpoint()).getTruncateTargetType();
                break;
            case BULKLOAD:
                targetType = ((Endpoint) getEndpoint()).getBulkloadTargetType();
                break;
            case EXECUTE:
                targetType = ((Endpoint) getEndpoint()).getExecuteTargetType();
                break;
            case STATUS:
                targetType = ((Endpoint) getEndpoint()).getStatusTargetType();
                break;
            case POST:
            case PING:
                targetType = null;
                break;
            case CREATE:
                targetType = ((Endpoint) getEndpoint()).getCreateTargetType();
                break;
            case UPDATE:
                targetType = ((Endpoint) getEndpoint()).getUpdateTargetType();
                break;
            case UPSERT:
                targetType = ((Endpoint) getEndpoint()).getUpsertTargetType();
                break;
            case DELETE:
                targetType = ((Endpoint) getEndpoint()).getDeleteTargetType();
                break;
            case DELETE_BY_KEY:
                targetType = ((Endpoint) getEndpoint()).getDeleteByKeyTargetType();
                break;
            case UPLOAD:
                targetType = ((Endpoint) getEndpoint()).getUploadTargetType();
                break;
            case METADATA:
                targetType = ((Endpoint) getEndpoint()).getMetadataTargetType();
                break;
            case FETCH:
                targetType = ((Endpoint) getEndpoint()).getFetchTargetType();
                break;
            case GET:
                targetType = ((Endpoint) getEndpoint()).getGetTargetType();
                break;
            default:
                throw new UnsupportedOperationException("operation not supported: " +
                        SupportedOperation.valueOf(((Endpoint) getEndpoint()).getOperationType()));

        }
        return PFXTypeCode.validValueOf(targetType);
    }

    public void process(final Exchange exchange) throws Exception {
        String apiPath = getProperty(exchange.getProperty(API_PATH));
        String uniqueId = getProperty(exchange.getProperty(UNIQUE_ID));
        String secondaryId = getProperty(exchange.getProperty(SECONDARY_ID));
        Object input = exchange.getIn().getBody();

        CredentialsOperation credentialsOperation = createCredentialsOperation();
        String token = getProperty(exchange.getProperty(ACCESS_TOKEN));
        if (credentialsOperation.isJwt()) {
            credentialsOperation.setJwtToken(token);
        }
        PFXOperationClient pfxClient = createPfxClient(credentialsOperation);

        if (!StringUtils.isEmpty(token) && !credentialsOperation.isJwt()) {
            pfxClient.updateOAuthToken(token);
        }


        PFXTypeCode typeCode = getTargetType();

        validateTargetDate(typeCode, exchange);

        IPFXExtensionType extensionType = getPFXExtensionType(pfxClient, typeCode, exchange);

        JsonNode node;
        switch (SupportedOperation.valueOf(((Endpoint) getEndpoint()).getOperationType())) {
            case FETCH_COUNT:
                node = new FetchService(
                        pfxClient, typeCode,
                        extensionType,
                        StringUtils.isEmpty(uniqueId) ? getDynamicValue(exchange, ((Endpoint) getEndpoint()).getExtensionName()) : uniqueId).
                        fetchCount(input);
                break;
            case FLUSH:
                node = new DataloadService(pfxClient, typeCode, DataloadRunner.DataloadType.DS_FLUSH,
                        getDynamicValue(exchange, ((Endpoint) getEndpoint()).getExtensionName())).
                        execute(input);
                break;
            case REFRESH:
                String incLoadDate = getProperty(exchange.getProperty(INC_LOAD_DATE));
                node = refresh(pfxClient, typeCode, uniqueId, incLoadDate, exchange);
                break;
            case TRUNCATE:
                node = new DataloadService(pfxClient, typeCode, DataloadRunner.DataloadType.TRUNCATE,
                        getDynamicValue(exchange, ((Endpoint) getEndpoint()).getExtensionName())).execute(input);
                break;
            case BULKLOAD:
                node = new BulkLoadService(pfxClient, typeCode, extensionType,
                        getDynamicValue(exchange, ((Endpoint) getEndpoint()).getExtensionName()),
                        ((Endpoint) getEndpoint()).isValidation()).execute(input);
                break;
            case EXECUTE:
                node = new ExecuteOperation(pfxClient, ((Endpoint) getEndpoint()).getExecuteTargetType(), uniqueId, getDynamicValue(exchange, ((Endpoint) getEndpoint()).getExtensionName()), typeCode).execute(input);
                break;
            case STATUS:
                node = new StatusOperation(pfxClient, typeCode, uniqueId).get();
                break;
            case PING:
                node = new PingService(pfxClient).execute(null);
                break;
            case CREATE:
                node = new CreateService(pfxClient, typeCode).execute(input);
                break;
            case UPDATE:
                node = new UpdateService(pfxClient, typeCode, uniqueId).execute(input);
                break;
            case UPSERT:
                node = new UpsertService(pfxClient, typeCode, extensionType,
                        ((Endpoint) getEndpoint()).isSimpleResult(),
                        ((Endpoint) getEndpoint()).isShowSystemFields(),
                        ((Endpoint) getEndpoint()).isReplaceNullWithEmpty()).execute(input);
                break;
            case DELETE:
                node = new DeleteOperation(pfxClient, typeCode, uniqueId, extensionType, false).delete(input);
                break;
            case DELETE_BY_KEY:
                node = new DeleteOperation(pfxClient, typeCode, uniqueId, extensionType, true).delete(input);
                break;
            case UPLOAD:
                node = new FileUploadService(pfxClient, typeCode, getDynamicValue(exchange, ((Endpoint) getEndpoint()).getExtensionName()), uniqueId).
                        execute(input);
                break;
            case POST:
                node = new PostService(pfxClient, apiPath).execute(input);
                break;
            case METADATA:
                int pageSize = RequestUtil.getPageSize(getProperty(exchange.getProperty(PAGE_SIZE)), MAX_FETCH_RECORDS);
                long startRow = RequestUtil.getStartRow(getProperty(exchange.getProperty(PAGE_NUMBER)), pageSize);
                node = new FetchService(pfxClient, typeCode, extensionType, uniqueId).
                        fetchMetadata(startRow, pageSize);
                break;
            case FETCH:
                pageSize = RequestUtil.getPageSize(getProperty(exchange.getProperty(PAGE_SIZE)), MAX_FETCH_RECORDS);
                startRow = RequestUtil.getStartRow(getProperty(exchange.getProperty(PAGE_NUMBER)), pageSize);
                node = new FetchService(
                        pfxClient, typeCode,
                        extensionType,
                        StringUtils.isEmpty(uniqueId) ? getDynamicValue(exchange, ((Endpoint) getEndpoint()).getExtensionName()) : uniqueId).
                        fetch(startRow, pageSize, true, !((Endpoint) getEndpoint()).isShowSystemFields(), input);
                break;
            case GET:
                pageSize = RequestUtil.getPageSize(getProperty(exchange.getProperty(PAGE_SIZE)), MAX_FETCH_RECORDS);
                startRow = RequestUtil.getStartRow(getProperty(exchange.getProperty(PAGE_NUMBER)), pageSize);

                if (typeCode != TOKEN) {
                    node = new GetOperation(pfxClient, typeCode, uniqueId, secondaryId, extensionType).get(startRow, pageSize, !((Endpoint) getEndpoint()).isShowSystemFields());
                } else if (credentialsOperation.isJwt()) {
                    node = new TokenService(pfxClient).getJwt();
                } else {
                    node = new TokenService(pfxClient).get(credentialsOperation.buildTokenRequest());
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
                ((Endpoint) getEndpoint()).getPricefxHost());
    }

    public static PfxClientBuilder getPFXClientBuilder(String partitionName, String url, String token) {

        PfxClientBuilder builder = (PfxClientBuilder) new PfxClientBuilder(url, partitionName)
                .jwtCredentials(token)
                .chunkSize(MAX_RECORDS)
                .maxRetry(3)
                .timeout(DEFAULT_TIMEOUT);

        builder.insecure();
        return builder;
    }

    protected PFXOperationClient createPfxClient(CredentialsOperation credentialsOperation) {

        PFXOperationClient pfxClient;
        try {
            PfxClientBuilder builder;
            if (credentialsOperation.isJwt() && !StringUtils.isEmpty(credentialsOperation.getJwtToken())) {
                //JWT
                builder = getPFXClientBuilder(credentialsOperation.getPartition(),
                        credentialsOperation.getPricefxHost(), credentialsOperation.getJwtToken());

            } else if (credentialsOperation.isJwt() && StringUtils.isEmpty(credentialsOperation.getJwtToken())) {
                //Get JWT Token
                ObjectNode node = credentialsOperation.buildTokenRequest();
                builder = ConnectionUtil.getPFXClientBuilder(credentialsOperation.getPartition(),
                        credentialsOperation.getPricefxHost(), node.get("username").textValue(),
                        node.get("password").textValue(), null);

            } else {
                //OAuth
                builder = ConnectionUtil.getPFXClientBuilder(credentialsOperation.getPartition(),
                        credentialsOperation.getPricefxHost(),
                        credentialsOperation.getUserId());
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

    private String getDynamicValue(Exchange exchange, String expression) {
        String propertyName = StringUtil.getPropertyNameFromExpression(expression);
        if (StringUtils.isEmpty(propertyName)) {
            String headerName = StringUtil.getHeaderNameFromExpression(expression);
            if (!StringUtils.isEmpty(headerName)) {
                return getProperty(exchange.getIn().getHeader(headerName));
            }
        } else {
            return getProperty(exchange.getProperty(propertyName));
        }

        return expression;

    }

    private void validateTargetDate(PFXTypeCode typeCode, Exchange exchange) {

        if (DateUtil.getDate(getDynamicValue(exchange, ((Endpoint) getEndpoint()).getTargetDate())) == null && typeCode == PFXTypeCode.LOOKUPTABLE) {
            throw new ConnectorException("Invalid Target Date");
        }

    }

    private JsonNode refresh(PFXOperationClient pfxClient, PFXTypeCode typeCode, String uniqueId, String incLoadDate, Exchange exchange) {
        if (typeCode == TOKEN) {
            return new RefreshService(pfxClient, typeCode, uniqueId, incLoadDate).refresh();
        } else {
            return new RefreshService(pfxClient, typeCode, getDynamicValue(exchange, ((Endpoint) getEndpoint()).getExtensionName()), incLoadDate).refresh();
        }
    }

    private IPFXExtensionType getPFXExtensionType(PFXOperationClient pfxClient, PFXTypeCode typeCode, Exchange exchange) {
        if (typeCode != null && (typeCode.isExtension() || typeCode == PFXTypeCode.LOOKUPTABLE)) {
            return pfxClient.createExtensionType(typeCode, getDynamicValue(exchange, ((Endpoint) getEndpoint()).getExtensionName()),
                    getDynamicValue(exchange, ((Endpoint) getEndpoint()).getTargetDate()));
        }

        return null;
    }

}
