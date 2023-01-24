package net.pricefx.connector.common.operation;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.common.collect.Lists;
import net.pricefx.connector.common.connection.PFXOperationClient;
import net.pricefx.connector.common.connection.RequestFactory;
import net.pricefx.connector.common.util.IPFXExtensionType;
import net.pricefx.connector.common.util.JsonUtil;
import net.pricefx.connector.common.util.PFXTypeCode;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static net.pricefx.connector.common.util.PFXConstants.*;


public class GenericMetadataFetcher implements IPFXMetadataFetcher {

    private static final String LABEL_TRANSLATIONS = "labelTranslations";
    private static final String FIELD_TYPE = "fieldType";
    private static final String MODULE = "module";

    private final PFXOperationClient pfxClient;

    private final PFXTypeCode typeCode;
    private final IPFXExtensionType extensionType;


    public GenericMetadataFetcher(PFXOperationClient pfxClient, PFXTypeCode typeCode, IPFXExtensionType extensionType) {
        this.pfxClient = pfxClient;
        this.typeCode = typeCode;
        this.extensionType = extensionType;
    }

    public PFXOperationClient getPfxClient() {
        return pfxClient;
    }


    @Override
    public List<ObjectNode> fetch(Long startRow, int pageSize, String uniqueKey) {

        ObjectNode advancedCriterion = RequestFactory.buildFetchMetadataRequest(typeCode, extensionType, uniqueKey);

        Iterable<ObjectNode> metadata = pfxClient.doFetchMetadata(typeCode, extensionType, startRow, pageSize, advancedCriterion);
        if (metadata == null || !metadata.iterator().hasNext()) {
            return Lists.newArrayList();
        } else {
            List<ObjectNode> results = new ArrayList<>();

            metadata.iterator().forEachRemaining((ObjectNode obj) -> {
                enrichMetadata(obj, typeCode);
                results.add(obj);
            });

            return results;

        }

    }

    private void enrichMetadata(ObjectNode metadata, PFXTypeCode typeCode) {

        if (typeCode == PFXTypeCode.ROLE) {

            metadata.put(FIELD_FIELDNAME, JsonUtil.getValueAsText(metadata.get(FIELD_UNIQUENAME)))
                    .put(FIELD_LABEL, JsonUtil.getValueAsText(metadata.get(FIELD_LABEL)))
                    .put(MODULE, JsonUtil.getValueAsText(metadata.get(MODULE)));
            metadata.retain(FIELD_FIELDNAME, FIELD_LABEL, MODULE);
        } else if (typeCode == PFXTypeCode.BUSINESSROLE || typeCode == PFXTypeCode.USERGROUP) {

            metadata.put(FIELD_FIELDNAME, JsonUtil.getValueAsText(metadata.get(FIELD_UNIQUENAME)))
                    .put(FIELD_LABEL, JsonUtil.getValueAsText(metadata.get(FIELD_LABEL)));
            metadata.retain(FIELD_FIELDNAME, FIELD_LABEL);
        } else {
            metadata.retain(FIELD_LABEL, FIELD_FIELDNAME, FIELD_TYPE, "requiredField", "readOnly", LABEL_TRANSLATIONS);

            String label = JsonUtil.getLabelTranslations(JsonUtil.getValueAsText(metadata.get(LABEL_TRANSLATIONS)));
            if (!StringUtils.isEmpty(label)) {
                metadata.put(LABEL_TRANSLATIONS, label);
            }

            Number fieldType = JsonUtil.getNumericValue(metadata.get(FIELD_TYPE));
            if (fieldType != null) {
                metadata.put(FIELD_TYPE, FieldType.getFieldType(fieldType.intValue()));
            }
        }

    }

    private enum FieldType {
        UNKNOWN(0), REAL(1), STRING(2), INTEGER(3), DATE(4),
        DATETIME(5), LINK(6), CALCULATION_RESULT_REFERENCE(7), ENTITY_REFERENCE(8);

        private final int id;

        FieldType(int id) {
            this.id = id;
        }

        private static String getFieldType(int id) {
            FieldType result = Stream.of(FieldType.values()).filter((FieldType fieldType) -> fieldType.id == id).findFirst().orElse(null);
            if (result != null) {
                return result.name().replace("_", " ");
            } else {
                return id + "";
            }
        }

    }
}