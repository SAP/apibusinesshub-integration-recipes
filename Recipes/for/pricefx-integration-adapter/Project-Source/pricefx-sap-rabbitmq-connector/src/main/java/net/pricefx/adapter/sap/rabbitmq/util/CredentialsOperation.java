package net.pricefx.adapter.sap.rabbitmq.util;

import com.sap.it.api.ITApiFactory;
import com.sap.it.api.exception.InvalidContextException;
import com.sap.it.api.securestore.SecureStoreService;
import com.sap.it.api.securestore.UserCredential;
import com.sap.it.api.securestore.exception.SecureStoreException;



public class CredentialsOperation {

    private final String username;
    private final char[] password;



    public CredentialsOperation(String securityMaterial) throws InvalidContextException, SecureStoreException {

        SecureStoreService secureStoreService = ITApiFactory.getService(SecureStoreService.class, null);
        UserCredential credential = secureStoreService.getUserCredential(securityMaterial);

        if (credential == null) {
            throw new ConnectorException("Security Material - " + securityMaterial + "not found.");
        }

        username = credential.getUsername();

        if (username == null) {
            throw new ConnectorException("Username must not be empty");
        }

        password = credential.getPassword();

        if (password == null) {
            throw new ConnectorException("Password must not be empty");
        }

    }

    public String getUsername() {
        return username;
    }

    public char[] getPassword() {
        return password;
    }




}
