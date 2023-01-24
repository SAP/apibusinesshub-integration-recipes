package net.pricefx.connector.common.util;

import java.util.Set;

public interface IPFXExtensionType {
    int getAdditionalAttributes();

    int getAdditionalKeys();

    PFXTypeCode getTypeCode();

    String getTable();

    Set<String> getMandatoryFields();

    Set<String> getBusinessKeys();

    IPFXExtensionType withTable(String table);


}
