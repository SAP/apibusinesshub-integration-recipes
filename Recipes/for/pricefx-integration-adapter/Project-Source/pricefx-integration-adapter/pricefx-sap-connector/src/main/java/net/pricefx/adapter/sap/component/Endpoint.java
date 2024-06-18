package net.pricefx.adapter.sap.component;


import org.apache.camel.Processor;
import org.apache.camel.impl.DefaultEndpoint;
import org.apache.camel.spi.UriEndpoint;
import org.apache.camel.spi.UriParam;

@UriEndpoint(scheme = "pfx-sap-general", syntax = "", title = "")
public class Endpoint extends DefaultEndpoint {

    @UriParam
    private boolean replaceNullWithEmpty;

    @UriParam
    private boolean simpleResult;

    @UriParam
    private boolean showSystemFields;

    @UriParam
    private boolean validation;

    @UriParam
    private String targetDate;

    @UriParam
    private String extensionName;

    @UriParam
    private String operationType;


    @UriParam
    private String statusTargetType;

    @UriParam
    private String metadataTargetType;

    @UriParam
    private String fetchTargetType;

    @UriParam
    private String fetchCountTargetType;

    @UriParam
    private String executeTargetType;

    @UriParam
    private String deleteTargetType;

    @UriParam
    private String updateTargetType;

    @UriParam
    private String createTargetType;

    @UriParam
    private String truncateTargetType;

    @UriParam
    private String bulkloadTargetType;

    @UriParam
    private String uploadTargetType;


    @UriParam
    private String refreshTargetType;


    @UriParam
    private String upsertTargetType;


    @UriParam
    private String getTargetType;

    @UriParam
    private String deleteByKeyTargetType;

    @UriParam
    private String flushTargetType;


    @UriParam
    private String securityMaterial;

    @UriParam
    private String pricefxHost;

    @UriParam
    private boolean retryLoginFailure;

    public Endpoint(final String endpointUri, final Component component) {
        super(endpointUri, component);
    }

    public String getSecurityMaterial() {
        return securityMaterial;
    }

    public String getPricefxHost() {
        return pricefxHost;
    }

    public void setPricefxHost(String pricefxHost) {
        this.pricefxHost = pricefxHost;
    }

    public void setSecurityMaterial(String securityMaterial) {
        this.securityMaterial = securityMaterial;
    }

    public String getOperationType() {
        return operationType;
    }

    public void setOperationType(String operationType) {
        this.operationType = operationType;
    }

    public Producer createProducer() {
        return new Producer(this);
    }

    public Consumer createConsumer(Processor processor) {
        return new Consumer(this, processor);
    }

    public boolean isSingleton() {
        return true;
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean equals(Object object) {
        return super.equals(object);
    }

    public String getExtensionName() {
        return extensionName;
    }

    public void setExtensionName(String extensionName) {
        this.extensionName = extensionName;
    }


    public String getTargetDate() {
        return targetDate;
    }

    public void setTargetDate(String targetDate) {
        this.targetDate = targetDate;
    }

    public String getMetadataTargetType() {
        return metadataTargetType;
    }

    public void setMetadataTargetType(String metadataTargetType) {
        this.metadataTargetType = metadataTargetType;
    }

    public String getStatusTargetType() {
        return statusTargetType;
    }

    public void setStatusTargetType(String statusTargetType) {
        this.statusTargetType = statusTargetType;
    }

    public String getUpdateTargetType() {
        return updateTargetType;
    }

    public void setUpdateTargetType(String updateTargetType) {
        this.updateTargetType = updateTargetType;
    }

    public String getGetTargetType() {
        return getTargetType;
    }

    public void setGetTargetType(String getTargetType) {
        this.getTargetType = getTargetType;
    }

    public String getRefreshTargetType() {
        return refreshTargetType;
    }

    public void setRefreshTargetType(String refreshTargetType) {
        this.refreshTargetType = refreshTargetType;
    }

    public String getUpsertTargetType() {
        return upsertTargetType;
    }

    public void setUpsertTargetType(String upsertTargetType) {
        this.upsertTargetType = upsertTargetType;
    }

    public String getFetchTargetType() {
        return fetchTargetType;
    }

    public void setFetchTargetType(String fetchTargetType) {
        this.fetchTargetType = fetchTargetType;
    }

    public String getDeleteTargetType() {
        return deleteTargetType;
    }

    public void setDeleteTargetType(String deleteTargetType) {
        this.deleteTargetType = deleteTargetType;
    }

    public String getDeleteByKeyTargetType() {
        return deleteByKeyTargetType;
    }

    public void setDeleteByKeyTargetType(String deleteByKeyTargetType) {
        this.deleteByKeyTargetType = deleteByKeyTargetType;
    }

    public String getCreateTargetType() {
        return createTargetType;
    }

    public void setCreateTargetType(String createTargetType) {
        this.createTargetType = createTargetType;
    }

    public String getTruncateTargetType() {
        return truncateTargetType;
    }

    public void setTruncateTargetType(String truncateTargetType) {
        this.truncateTargetType = truncateTargetType;
    }

    public String getBulkloadTargetType() {
        return bulkloadTargetType;
    }

    public void setBulkloadTargetType(String bulkloadTargetType) {
        this.bulkloadTargetType = bulkloadTargetType;
    }

    public String getUploadTargetType() {
        return uploadTargetType;
    }

    public void setUploadTargetType(String uploadTargetType) {
        this.uploadTargetType = uploadTargetType;
    }

    public String getExecuteTargetType() {
        return executeTargetType;
    }

    public void setExecuteTargetType(String executeTargetType) {
        this.executeTargetType = executeTargetType;
    }

    public boolean isValidation() {
        return validation;
    }

    public void setValidation(boolean validation) {
        this.validation = validation;
    }

    public boolean isSimpleResult() {
        return simpleResult;
    }

    public void setSimpleResult(boolean simpleResult) {
        this.simpleResult = simpleResult;
    }

    public boolean isReplaceNullWithEmpty() {
        return replaceNullWithEmpty;
    }

    public void setReplaceNullWithEmpty(boolean replaceNullWithEmpty) {
        this.replaceNullWithEmpty = replaceNullWithEmpty;
    }

    public boolean isShowSystemFields() {
        return showSystemFields;
    }

    public void setShowSystemFields(boolean showSystemFields) {
        this.showSystemFields = showSystemFields;
    }

    public String getFlushTargetType() {
        return flushTargetType;
    }

    public void setFlushTargetType(String flushTargetType) {
        this.flushTargetType = flushTargetType;
    }

    public String getFetchCountTargetType() {
        return fetchCountTargetType;
    }

    public void setFetchCountTargetType(String fetchCountTargetType) {
        this.fetchCountTargetType = fetchCountTargetType;
    }

    public boolean isRetryLoginFailure() {
        return retryLoginFailure;
    }

    public void setRetryLoginFailure(boolean retryLoginFailure) {
        this.retryLoginFailure = retryLoginFailure;
    }
}
