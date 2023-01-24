package net.pricefx.connector.common.operation;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.common.collect.Iterables;
import net.pricefx.connector.common.connection.PFXOperationClient;
import net.pricefx.connector.common.util.FileUploadStatus;
import net.pricefx.connector.common.util.JsonUtil;
import net.pricefx.connector.common.validation.ConnectorException;
import net.pricefx.pckg.processing.ProcessingException;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.util.Map;
import java.util.NoSuchElementException;

import static net.pricefx.connector.common.util.ConnectionUtil.createPath;
import static net.pricefx.connector.common.util.Constants.UNIQUE_KEY;
import static net.pricefx.connector.common.util.FileUploadStatus.POSTPROCESSING_DONE;
import static net.pricefx.connector.common.util.PFXConstants.FIELD_DATA;
import static net.pricefx.connector.common.util.PFXConstants.STATUS;
import static net.pricefx.connector.common.util.PFXOperation.FILE_UPLOAD_SLOT_DELETE;
import static net.pricefx.connector.common.util.PFXOperation.FILE_UPLOAD_STATUS;

public class UploadStatusChecker implements IPFXObjectExecuter {
    private final PFXOperationClient pfxClient;
    private final Map<String, String> parameters;

    public UploadStatusChecker(PFXOperationClient pfxClient, Map<String, String> parameters) {
        this.pfxClient = pfxClient;
        this.parameters = parameters;
    }

    @Override
    public JsonNode execute(JsonNode request) {
        ObjectNode returnedResponse = new ObjectNode(JsonNodeFactory.instance);
        ObjectNode result;

        String objectId = parameters.get(UNIQUE_KEY);
        validateRequest(objectId);

        try {
            result = Iterables.get(pfxClient.doAction(createPath(FILE_UPLOAD_STATUS.getOperation(), objectId)), 0);
        } catch (Exception ex) {
            throw new NoSuchElementException("Upload Slot does not exist:" + ex.getMessage());
        }


        if (result != null) {
            String data = JsonUtil.getValueAsText(result.get(FIELD_DATA));
            if (!StringUtils.isEmpty(data)) {
                try {
                    JsonNode node = new ObjectMapper().readTree(data);
                    node = JsonUtil.getFirstDataNode(node);
                    if (node.isObject()) {
                        returnedResponse = new ObjectNode(JsonNodeFactory.instance);
                        returnedResponse.setAll((ObjectNode) node);

                        FileUploadStatus status = FileUploadStatus.validValueOf(
                                JsonUtil.getValueAsText(result.get(STATUS)));
                        returnedResponse.put(STATUS, status.name());

                        returnedResponse.put("isSlotDeleted", false);
                        if (status == POSTPROCESSING_DONE) {
                            returnedResponse.put("isSlotDeleted", deleteFileUploadSlot(objectId));
                        }
                    }

                } catch (IOException e) {
                    throw new ConnectorException("unable to read json response from API");
                }
            }
        }

        if (returnedResponse.size() == 0) {
            throw new NoSuchElementException("Upload Slot does not exist");
        }

        return returnedResponse;


    }

    private void validateRequest(String objectId) {
        if (StringUtils.isEmpty(objectId)) {
            throw new ConnectorException("Upload Slot is not provided. Cannot execute Truncate");
        }
    }

    private boolean deleteFileUploadSlot(String slot) {
        ObjectNode result = null;

        try {
            result = Iterables.get(pfxClient.doAction(createPath(FILE_UPLOAD_SLOT_DELETE.getOperation(), slot)), 0);
        } catch (ProcessingException ex) {
            //not process, send empty response
        }

        return (result != null && result.size() > 0);

    }
}